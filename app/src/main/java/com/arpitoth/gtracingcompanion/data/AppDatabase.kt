package com.arpitoth.gtracingcompanion.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arpitoth.gtracingcompanion.model.DriverEntity

@Database(
    entities = [DriverEntity::class],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun driverDao(): DriverDao

}