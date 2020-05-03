package com.piotrek.smog

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
        @ColumnInfo(name = "gegr_long")
        val gegrLong: String
)