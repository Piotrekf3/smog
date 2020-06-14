package com.piotrek.smog.api

import android.app.*
import android.content.Intent
import android.content.Context
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import com.piotrek.smog.MyApplication
import com.piotrek.smog.R
import com.piotrek.smog.enity.Index
import com.piotrek.smog.enity.Station
import com.piotrek.smog.enity.StationResponse
import com.piotrek.smog.layout.MainActivity
import com.piotrek.smog.repository.LocationRepository
import kotlinx.coroutines.*

private val stationDao by lazy { MyApplication.appDatabase.stationDao() }

class ApiService : IntentService("ApiService"), CoroutineScope by MainScope() {

    private var mManager: NotificationManager? = null

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_FETCH_API -> {
                makeMeForeground()
                fetchApi()
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_FETCH_API -> {
                makeMeForeground()
                fetchApi()
            }
        }
        return Service.START_STICKY
    }

    private fun fetchApi() {
        createAlertChannel()
        launch(Dispatchers.IO) {
            try {
                val response = ApiAdapter.apiClient.getStations()
                if (response.isSuccessful && response.body() != null) {
                    val responseData = response.body()
                    val stations = arrayListOf<Station>()
                    coroutineScope {
                        responseData?.map {
                            async(Dispatchers.IO) {
                                val indexResponse = ApiAdapter.apiClient.getStationIndex(it.id)
                                val indexData = indexResponse.body()
                                val station = indexData?.toStation(it.toStation())
                                if (station != null) {
                                    stations.add(station)
                                }
                            }
                        }?.awaitAll()
                    }
                    stationDao.insert(stations)
                    val locationRepository = LocationRepository(this@ApiService)
                    locationRepository.getLocation()?.addOnSuccessListener { location: Location? ->
                        val stationsSorted = stations.sortedBy { location?.distanceTo(it.getLocation()) }
                        //TODO delete debug
//                        stationsSorted[0].index = Index.BAD
                        if(stationsSorted[0].index == Index.BAD || stationsSorted[0].index == Index.VERY_BAD) {
                            createAlertNotification()
                        }
                    }
                }
                stopForeground(true)
            } catch (e: Exception){
                //TODO log error
                stopForeground(true)
            }
        }
    }

    fun createAlertNotification() {
        val bm = BitmapFactory.decodeResource(
            resources,
            R.drawable.alert_foreground)

        val notBuilder = NotificationCompat.Builder(this, ALERT_CHANNEL_ID)
            .setSmallIcon(R.drawable.alert_foreground)
            .setLargeIcon(bm)
            .setContentTitle("Uwaga")
            .setContentText("Stan powietrza w pobliżu twojej lokalizacji jest poniżej przeciętnej")
            .setAutoCancel(true)

        getNotificationManager()?.notify(
            ALERT_NOTIFICATION_ID,
            notBuilder.build()
        )
    }

    private fun createAlertChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.smogChannelName)
            val description = getString(R.string.smogChannelDescription)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                ALERT_CHANNEL_ID,
                name, importance
            )
            channel.description = description
            getNotificationManager()?.createNotificationChannel(channel)
        }
    }

    fun makeMeForeground() {
        createInfoChannel()
        val mNotificationIntent = Intent(this,
            MainActivity::class.java)

        val mNotificationPendingIntent = PendingIntent.getActivity(
            this, 0,
            mNotificationIntent,
            0)

        val bm = BitmapFactory.decodeResource(
            resources,
            R.drawable.alert_foreground)

        val notBuilder = NotificationCompat.Builder(this, INFO_CHANNEL_ID)
            .setSmallIcon(R.drawable.alert_foreground)
            .setLargeIcon(bm)
            .setContentTitle("Smog")
            .setContentText("Pobieranie danych")
            .setAutoCancel(true)
            .setContentIntent(mNotificationPendingIntent)

        startForeground(INFO_NOTIFICATION_ID, notBuilder.build())
    }

    private fun createInfoChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.smogChannelName)
            val description = getString(R.string.smogChannelDescription)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                INFO_CHANNEL_ID,
                name, importance
            )
            channel.description = description
            getNotificationManager()?.createNotificationChannel(channel)
        }
    }

    private fun getNotificationManager(): NotificationManager? {
        if (mManager == null) {
            mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return mManager
    }

    companion object {
        const val ACTION_FETCH_API = "com.piotrek.smog.api.action.FETCH_API"
        const val ALERT_CHANNEL_ID = "com.example.student.android04c01.alert_chanel";
        const val ALERT_NOTIFICATION_ID = 10
        const val INFO_CHANNEL_ID = "com.example.student.android04c01.info_chanel";
        const val INFO_NOTIFICATION_ID = 11
        @JvmStatic
        fun startActionFetchApi(context: Context) {
            val intent = Intent(context, ApiService::class.java).apply {
                action = ACTION_FETCH_API
            }
            context.startService(intent)
        }
    }
}
