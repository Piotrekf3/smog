package com.piotrek.smog

import androidx.room.Database
import androidx.room.RoomDatabase
import com.piotrek.smog.dao.StationDao
import com.piotrek.smog.enity.Station

@Database(entities = arrayOf(Station::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun stationDao(): StationDao
}