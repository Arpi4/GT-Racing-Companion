package com.arpitoth.gtracingcompanion.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drivers")
data class DriverEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    val team: String,

    val age: Int

)