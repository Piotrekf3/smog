package com.piotrek.smog

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StationIndexDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stations: List<StationIndex>)

    @Query("DELETE FROM station_index")
    suspend fun deleteAll()

    @Query("SELECT * FROM station_index")
    fun all(): LiveData<List<Station>>
}