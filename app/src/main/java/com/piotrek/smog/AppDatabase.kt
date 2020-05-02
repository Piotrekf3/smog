package com.piotrek.smog

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Station::class, StationIndex::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
}