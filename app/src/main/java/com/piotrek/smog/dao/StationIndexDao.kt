package com.piotrek.smog.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.piotrek.smog.enity.StationIndex

@Dao
interface StationIndexDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stations: List<StationIndex>)

    @Query("DELETE FROM station_index")
    suspend fun deleteAll()

    @Query("SELECT * FROM station_index")
    fun all(): LiveData<List<StationIndex>>
}