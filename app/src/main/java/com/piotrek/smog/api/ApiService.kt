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
import com.piotrek.smog.repository.LocationRepository
import kotlinx.coroutines.*

private val stationDao by lazy { MyApplication.appDatabase.stationDao() }

class ApiService : IntentService("ApiService"), CoroutineScope by MainScope() {

    val SMOG_CHANNEL_ID = "com.example.student.android04c01.smog_chanel";
    val SMOG_NOTIFICATION_ID = 10
    private var mManager: NotificationManager? = null

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_FETCH_API -> {
                fetchApi()
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_FETCH_API -> {
                fetchApi()
            }
        }
        return Service.START_STICKY
    }

    private fun fetchApi() {
        createSmogChannel()
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
                            createNotification()
                        }
                    }
                }
            } catch (e: Exception){
                //TODO log error
            }
        }
    }

    fun createNotification() {
        val bm = BitmapFactory.decodeResource(
            resources,
            R.drawable.alert_foreground)

        val notBuilder = NotificationCompat.Builder(this, SMOG_CHANNEL_ID)
            .setSmallIcon(R.drawable.alert_foreground)
            .setLargeIcon(bm)
            .setContentTitle("Uwaga")
            .setContentText("Stan powietrza w pobliżu twojej lokalizacji jest poniżej przeciętnej")
            .setAutoCancel(true)

        getNotificationManager()?.notify(
            SMOG_NOTIFICATION_ID,
            notBuilder.build()
        )
    }

    private fun createSmogChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.smogChannelName)
            val description = getString(R.string.smogChannelDescription)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                SMOG_CHANNEL_ID,
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
        @JvmStatic
        fun startActionFetchApi(context: Context) {
            val intent = Intent(context, ApiService::class.java).apply {
                action = ACTION_FETCH_API
            }
            context.startService(intent)
        }
    }
}
