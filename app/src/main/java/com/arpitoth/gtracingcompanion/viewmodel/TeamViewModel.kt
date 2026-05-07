package com.arpitoth.gtracingcompanion.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.arpitoth.gtracingcompanion.model.Team

class TeamViewModel : ViewModel() {
    val teams = mutableStateListOf<Team>()

    fun addTeam(team: Team) { teams.add(team) }
    fun removeTeam(team: Team) { teams.remove(team) }
    fun updateTeam(updated: Team) {
        val index = teams.indexOfFirst { it.id == updated.id }
        if (index != -1) teams[index] = updated
    }
}