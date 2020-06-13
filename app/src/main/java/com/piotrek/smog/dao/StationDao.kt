package com.piotrek.smog.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.piotrek.smog.enity.Station

@Dao
interface StationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stations: List<Station>)

    @Query("DELETE FROM station")
    suspend fun deleteAll()

    @Query("SELECT * FROM STATION WHERE ID = :id")
    suspend fun get(id:Int): Station

    @Query("SELECT * FROM station")
    fun all(): LiveData<List<Station>>
}