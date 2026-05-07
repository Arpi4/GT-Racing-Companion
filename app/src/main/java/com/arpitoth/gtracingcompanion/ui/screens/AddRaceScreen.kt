package com.arpitoth.gtracingcompanion.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.arpitoth.gtracingcompanion.data.GtRepository
import com.arpitoth.gtracingcompanion.ui.theme.Spacing
import kotlinx.coroutines.launch

@Composable
fun AddRaceScreen(
    navController: NavController,
    repository: GtRepository
) {
    var title by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("2026-06-01") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.medium)
    ) {
        Text("Új verseny", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(Spacing.medium))

        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Cím / esemény") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(Spacing.small))
        TextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Pálya / helyszín") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(Spacing.small))
        TextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Dátum (ÉÉÉÉ-HH-NN)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(Spacing.medium))

        Button(
            onClick = {
                val t = title.trim()
                if (t.isNotEmpty()) {
                    scope.launch {
                        repository.addRace(
                            title = t,
                            location = location.trim().ifBlank { "—" },
                            date = date.trim().ifBlank { "2099-12-31" }
                        )
                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Mentés") }
    }
}
