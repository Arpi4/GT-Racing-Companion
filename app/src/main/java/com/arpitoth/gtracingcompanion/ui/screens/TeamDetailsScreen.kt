package com.arpitoth.gtracingcompanion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arpitoth.gtracingcompanion.model.Team
import com.arpitoth.gtracingcompanion.viewmodel.TeamViewModel

@Composable
fun TeamDetailsScreen(team: Team, navController: NavController, teamVM: TeamViewModel) {
    var name by remember { mutableStateOf(team.name) }
    var base by remember { mutableStateOf(team.base) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Csapat részletek", style = MaterialTheme.typography.headlineSmall)

        TextField(value = name, onValueChange = { name = it }, label = { Text("Név") })
        TextField(value = base, onValueChange = { base = it }, label = { Text("Bázis") })

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                teamVM.updateTeam(team.copy(name = name, base = base))
                navController.popBackStack()
            }) { Text("Mentés") }

            Button(onClick = {
                teamVM.removeTeam(team)
                navController.popBackStack()
            }) { Text("Törlés") }
        }
    }
}