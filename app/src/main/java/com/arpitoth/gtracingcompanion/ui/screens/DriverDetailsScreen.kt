package com.arpitoth.gtracingcompanion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arpitoth.gtracingcompanion.model.Driver
import com.arpitoth.gtracingcompanion.viewmodel.DriverViewModel

@Composable
fun DriverDetailsScreen(driver: Driver, navController: NavController, driverVM: DriverViewModel) {
    var name by remember { mutableStateOf(driver.name) }
    var team by remember { mutableStateOf(driver.team) }
    var age by remember { mutableStateOf(driver.age.toString()) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Pilóta részletek", style = MaterialTheme.typography.headlineSmall)

        TextField(value = name, onValueChange = { name = it }, label = { Text("Név") })
        TextField(value = team, onValueChange = { team = it }, label = { Text("Csapat") })
        TextField(value = age, onValueChange = { age = it }, label = { Text("Életkor") })

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                driverVM.updateDriver(driver.copy(name = name, team = team, age = age.toIntOrNull() ?: driver.age))
                navController.popBackStack()
            }) { Text("Mentés") }

            Button(onClick = {
                driverVM.removeDriver(driver)
                navController.popBackStack()
            }) { Text("Törlés") }
        }
    }
}