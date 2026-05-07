package com.arpitoth.gtracingcompanion.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.arpitoth.gtracingcompanion.model.Race

class RaceViewModel : ViewModel() {

    val races = mutableStateListOf(
        Race(1, "Monaco", "28/05/2026"),
        Race(2, "Silverstone", "05/07/2026"),
        Race(3, "Monza", "10/09/2026")
    )

    fun addRace(race: Race) { races.add(race) }
    fun removeRace(race: Race) { races.remove(race) }
    fun updateRace(updatedRace: Race) {
        val index = races.indexOfFirst { it.id == updatedRace.id }
        if (index != -1) races[index] = updatedRace
    }
}