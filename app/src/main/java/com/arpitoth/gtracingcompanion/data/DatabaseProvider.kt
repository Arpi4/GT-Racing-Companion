package com.arpitoth.gtracingcompanion.data

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return instance ?: synchronized(this) {
            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "gtracing_db"
            ).build()
            instance = db
            db
        }
    }
}