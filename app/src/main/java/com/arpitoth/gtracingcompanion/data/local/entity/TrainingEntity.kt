package com.arpitoth.gtracingcompanion.data.local.entity

data class TrainingEntity(
    val id: Long = 0,
    val driverName: String,
    val type: String,
    val durationMinutes: Int,
    val rating: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
