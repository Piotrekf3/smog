package com.piotrek.smog.enity

class StationResponse(
    val id: Int,
    val stationName: String,
    val gegrLat: String,
    val gegrLon: String
) {

    fun toStation(): Station {
        return Station(
            id,
            stationName,
            gegrLat,
            gegrLon,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
    }

}