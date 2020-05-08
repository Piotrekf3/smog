package com.piotrek.smog

import androidx.room.Database
import androidx.room.RoomDatabase
import com.piotrek.smog.dao.StationDao
import com.piotrek.smog.dao.StationIndexDao
import com.piotrek.smog.enity.Station
import com.piotrek.smog.enity.StationIndex

@Database(entities = arrayOf(Station::class, StationIndex::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun stationDao(): StationDao
    abstract fun stationIndexDao(): StationIndexDao
}