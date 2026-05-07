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
fun AddTeamScreen(
    navController: NavController,
    repository: GtRepository
) {
    var name by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.medium)
    ) {
        Text("Új csapat", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(Spacing.medium))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Csapat neve") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(Spacing.small))
        TextField(
            value = country,
            onValueChange = { country = it },
            label = { Text("Ország") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(Spacing.medium))

        Button(
            onClick = {
                val n = name.trim()
                if (n.isNotEmpty()) {
                    scope.launch {
                        repository.addTeam(n, country.trim())
                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Mentés") }
    }
}
