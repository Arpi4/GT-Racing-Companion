package com.arpitoth.gtracingcompanion.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.arpitoth.gtracingcompanion.model.*

class DriverViewModel : ViewModel() {
    val drivers = mutableStateListOf<Driver>()

    fun addDriver(driver: Driver) { drivers.add(driver) }
    fun removeDriver(driver: Driver) { drivers.remove(driver) }
    fun updateDriver(updated: Driver) {
        val index = drivers.indexOfFirst { it.id == updated.id }
        if (index != -1) drivers[index] = updated
    }
}