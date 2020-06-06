package com.piotrek.smog.api

import com.piotrek.smog.enity.StationIndexResponse
import com.piotrek.smog.enity.StationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiClient {
    @GET("station/findAll")
    suspend fun getStations(): Response<List<StationResponse>>
    @GET("aqindex/getIndex/{stationId}")
    suspend fun getStationIndex(@Path("stationId") id: Int): Response<StationIndexResponse>
}