package com.arpitoth.gtracingcompanion.data

import androidx.room.*
import com.arpitoth.gtracingcompanion.model.DriverEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DriverDao {

    @Query("SELECT * FROM drivers")
    fun getDrivers(): Flow<List<DriverEntity>>

    @Insert
    suspend fun insert(driver: DriverEntity)

    @Delete
    suspend fun delete(driver: DriverEntity)

}