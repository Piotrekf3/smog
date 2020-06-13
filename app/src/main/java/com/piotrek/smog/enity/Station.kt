package com.piotrek.smog.enity

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "station")
data class Station (
    @PrimaryKey
    val id: Int,
    @ColumnInfo()
    val name: String,
    @ColumnInfo(name = "gegr_lat")
    val gegrLat: String,
    @ColumnInfo(name = "gegr_lon")
    val gegrLon: String,
    var index: String?,
    var so2: String?,
    var co: String?,
    var o3: String?,
    var c6h6: String?,
    var pm10: String?,
    var pm25: String?,
    var no2: String?
) {
    fun getLocation(): Location {
        val location = Location("")
        location.longitude = gegrLon.toDouble()
        location.latitude = gegrLat.toDouble()
        return location
    }
}