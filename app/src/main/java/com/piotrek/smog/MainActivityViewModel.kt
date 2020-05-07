package com.piotrek.smog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {

    private val stationDao by lazy { MyApplication.appDatabase.stationDao() }

    fun getAllStations(): LiveData<List<Station>> =
        stationDao.all()
}