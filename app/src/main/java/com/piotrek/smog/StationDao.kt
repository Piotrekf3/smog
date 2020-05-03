package com.piotrek.smog

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stations: List<Station>)

    @Query("DELETE FROM station")
    suspend fun deleteAll()

    @Query("SELECT * FROM station")
    fun all(): LiveData<List<Station>>
}