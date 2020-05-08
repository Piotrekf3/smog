package com.piotrek.smog.api

import com.piotrek.smog.enity.Station
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiClient {
    @GET("station/findAll")
    suspend fun getStations(): Response<List<Station>>

    object ApiAdapter {
        val apiClient: ApiClient = Retrofit.Builder()
            .baseUrl("http://api.gios.gov.pl/pjp-api/rest/")
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiClient::class.java)
    }
}