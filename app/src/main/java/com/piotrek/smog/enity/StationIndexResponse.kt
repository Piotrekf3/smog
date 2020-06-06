package com.piotrek.smog.enity

class StationIndexResponse(
    val id: Int,
    val stIndexLevel: Index?,
    val so2IndexLevel: Index?,
    val no2IndexLevel: Index?,
    val coIndexLevel: Index?,
    val pm10IndexLevel: Index?,
    val pm25IndexLevel: Index?,
    val o3IndexLevel: Index?,
    val c6h6IndexLevel: Index?
) {

    fun toStation(station: Station): Station {
        station.index = stIndexLevel?.indexLevelName
        station.so2 = so2IndexLevel?.indexLevelName
        station.no2 = no2IndexLevel?.indexLevelName
        station.co = coIndexLevel?.indexLevelName
        station.pm10 = pm10IndexLevel?.indexLevelName
        station.pm25 = pm25IndexLevel?.indexLevelName
        station.o3 = o3IndexLevel?.indexLevelName
        station.c6h6 = c6h6IndexLevel?.indexLevelName
        return station
    }
}