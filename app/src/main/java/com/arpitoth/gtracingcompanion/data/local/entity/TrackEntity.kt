package com.arpitoth.gtracingcompanion.data.local.entity

data class TrackEntity(
    val id: Long = 0,
    val name: String,
    val lengthKm: String,
    val location: String = ""
)
