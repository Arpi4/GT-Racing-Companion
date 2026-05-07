package com.arpitoth.gtracingcompanion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arpitoth.gtracingcompanion.model.Race
import com.arpitoth.gtracingcompanion.viewmodel.RaceViewModel

@Composable
fun RaceDetailsScreen(race: Race, navController: NavController, raceVM: RaceViewModel) {
    var location by remember { mutableStateOf(race.location) }
    var date by remember { mutableStateOf(race.date) }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Verseny részletek", style = MaterialTheme.typography.headlineSmall)

        TextField(value = location, onValueChange = { location = it }, label = { Text("Helyszín") })
        TextField(value = date, onValueChange = { date = it }, label = { Text("Dátum") })

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                raceVM.updateRace(race.copy(location = location, date = date))
                navController.popBackStack()
            }) { Text("Mentés") }

            Button(onClick = {
                raceVM.removeRace(race)
                navController.popBackStack()
            }) { Text("Törlés") }
        }
    }
}