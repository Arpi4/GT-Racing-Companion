package com.arpitoth.gtracingcompanion.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.arpitoth.gtracingcompanion.model.TrainingSession

class TrainingViewModel : ViewModel() {

    val sessions = mutableStateListOf(
        TrainingSession(1, "Hamilton", "FP1 Silverstone"),
        TrainingSession(2, "Verstappen", "FP2 Monza"),
        TrainingSession(3, "Leclerc", "Practice Spa")
    )

    fun addSession(session: TrainingSession) { sessions.add(session) }
    fun removeSession(session: TrainingSession) { sessions.remove(session) }
    fun updateSession(index: Int, session: TrainingSession) { sessions[index] = session }
}