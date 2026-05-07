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
fun AddTrainingScreen(
    navController: NavController,
    repository: GtRepository
) {
    var driver by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("7") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.medium)
    ) {
        Text("Új edzés", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(Spacing.medium))

        TextField(
            value = driver,
            onValueChange = { driver = it },
            label = { Text("Pilóta neve") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(Spacing.small))
        TextField(
            value = type,
            onValueChange = { type = it },
            label = { Text("Típus (pl. szimulátor)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(Spacing.small))
        TextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Időtartam (perc)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(Spacing.small))
        TextField(
            value = rating,
            onValueChange = { rating = it },
            label = { Text("Értékelés (0–10)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(Spacing.medium))

        Button(
            onClick = {
                val t = type.trim()
                if (t.isNotEmpty()) {
                    scope.launch {
                        repository.addTraining(
                            driverName = driver.trim(),
                            type = t,
                            durationMinutes = duration.toIntOrNull() ?: 0,
                            rating = rating.toIntOrNull()?.coerceIn(0, 10) ?: 0
                        )
                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Mentés") }
    }
}
