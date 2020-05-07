package com.piotrek.smog

import android.app.Application
import androidx.room.Room

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        appDatabase = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "stations.db")
            .build()
    }

    companion object {
        lateinit var appDatabase: AppDatabase
    }
}