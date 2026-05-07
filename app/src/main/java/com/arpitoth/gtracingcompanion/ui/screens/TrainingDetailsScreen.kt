package com.arpitoth.gtracingcompanion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arpitoth.gtracingcompanion.model.TrainingSession
import com.arpitoth.gtracingcompanion.viewmodel.TrainingViewModel

@Composable
fun TrainingDetailsScreen(
    session: TrainingSession,
    navController: NavController,
    trainingViewModel: TrainingViewModel
) {
    var driverName by remember { mutableStateOf(session.driver) }
    var description by remember { mutableStateOf(session.description) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Edzés részletek", style = MaterialTheme.typography.headlineSmall)

        TextField(
            value = driverName,
            onValueChange = { driverName = it },
            label = { Text("Pilóta") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Leírás") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            val index = trainingViewModel.sessions.indexOf(session)
            if (index >= 0) {
                trainingViewModel.updateSession(index, session.copy(driver = driverName, description = description))
            }
        }) {
            Text("Mentés")
        }

        Button(onClick = {
            trainingViewModel.removeSession(session)
            navController.popBackStack()
        }) {
            Text("Törlés")
        }
    }
}