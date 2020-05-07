package com.piotrek.smog

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = arrayOf(ForeignKey(entity = Station::class, parentColumns = arrayOf("id"), childColumns = arrayOf("station_id"), onDelete = ForeignKey.CASCADE)),
        tableName = "station_index")
data class StationIndex (
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "station_id")
    val stationId: Int,
    val index: String?,
    val so2: String?,
    val co: String?,
    val o3: String?,
    val c6h6: String?,
    val pm10: String?,
    val pm25: String?,
    val no2: String?

)