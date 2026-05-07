package com.arpitoth.gtracingcompanion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arpitoth.gtracingcompanion.model.Track
import com.arpitoth.gtracingcompanion.viewmodel.TrackViewModel

@Composable
fun TrackDetailsScreen(track: Track, navController: NavController, trackVM: TrackViewModel) {
    var name by remember { mutableStateOf(track.name) }
    var location by remember { mutableStateOf(track.location) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Pálya részletek", style = MaterialTheme.typography.headlineSmall)

        TextField(value = name, onValueChange = { name = it }, label = { Text("Név") })
        TextField(value = location, onValueChange = { location = it }, label = { Text("Helyszín") })

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                trackVM.updateTrack(track.copy(name = name, location = location))
                navController.popBackStack()
            }) { Text("Mentés") }

            Button(onClick = {
                trackVM.removeTrack(track)
                navController.popBackStack()
            }) { Text("Törlés") }
        }
    }
}