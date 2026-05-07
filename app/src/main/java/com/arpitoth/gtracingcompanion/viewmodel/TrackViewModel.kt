package com.arpitoth.gtracingcompanion.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.arpitoth.gtracingcompanion.model.Track

class TrackViewModel : ViewModel() {
    val tracks = mutableStateListOf<Track>()

    fun addTrack(track: Track) { tracks.add(track) }
    fun removeTrack(track: Track) { tracks.remove(track) }
    fun updateTrack(updated: Track) {
        val index = tracks.indexOfFirst { it.id == updated.id }
        if (index != -1) tracks[index] = updated
    }
}