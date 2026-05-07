package com.arpitoth.gtracingcompanion.data.local.entity

data class RaceEntity(
    val id: Long = 0,
    /** ISO-like yyyy-MM-dd for reliable sorting */
    val date: String,
    val title: String,
    val location: String,
    val isUpcoming: Boolean = true
)
