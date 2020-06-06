package com.piotrek.smog.api

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.widget.Toast
import com.piotrek.smog.MyApplication
import com.piotrek.smog.enity.Station
import com.piotrek.smog.enity.StationResponse
import kotlinx.coroutines.*

private const val ACTION_FETCH_API = "com.piotrek.smog.api.action.FETCH_API"
private val stationDao by lazy { MyApplication.appDatabase.stationDao() }

class ApiService : IntentService("ApiService"), CoroutineScope by MainScope() {

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_FETCH_API -> {
                fetchApi()
            }
        }
    }

    private fun fetchApi() {
        launch(Dispatchers.IO) {
            try {
                val response = ApiAdapter.apiClient.getStations()
                if (response.isSuccessful && response.body() != null) {
                    val responseData = response.body()
                    val stations = arrayListOf<Station>()
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
                    stationDao.insert(stations)
                }
            } catch (e: Exception){
                //TODO log error
            }
        }
    }

    companion object {
        @JvmStatic
        fun startActionFetchApi(context: Context) {
            val intent = Intent(context, ApiService::class.java).apply {
                action = ACTION_FETCH_API
            }
            context.startService(intent)
        }
    }
}
