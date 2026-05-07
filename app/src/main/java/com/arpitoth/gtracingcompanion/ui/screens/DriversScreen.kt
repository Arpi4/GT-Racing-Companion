package com.arpitoth.gtracingcompanion.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.arpitoth.gtracingcompanion.data.GtRepository
import com.arpitoth.gtracingcompanion.data.local.entity.DriverEntity
import com.arpitoth.gtracingcompanion.ui.components.CustomAppBar
import com.arpitoth.gtracingcompanion.ui.theme.Spacing
import kotlinx.coroutines.launch
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.contentDescription

@Composable
fun DriversScreen(
    navController: NavController,
    repository: GtRepository
) {
    val drivers by repository.drivers.collectAsStateWithLifecycle(initialValue = emptyList())
    var editing by remember { mutableStateOf<DriverEntity?>(null) }
    var editName by remember { mutableStateOf("") }
    var editTeam by remember { mutableStateOf("") }
    var editAge by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val nameFocusRequester = remember { FocusRequester() }
    val fontScale = LocalDensity.current.fontScale
    val maxLines = if (fontScale > 1.2f) 3 else 2
    val widthDp = LocalConfiguration.current.screenWidthDp
    val useGrid = widthDp >= 600

    Scaffold(
        topBar = { CustomAppBar(title = "Pilóták") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_driver") },
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Új pilóta"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Spacing.medium)
        ) {
            if (useGrid) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 180.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = Spacing.small)
                ) {
                    items(drivers) { driver ->
                        val desc = buildString {
                            append("Pilóta szerkesztése: ${driver.name}")
                            if (driver.team.isNotBlank()) append(", csapat: ${driver.team}")
                        }
                        Card(
                            onClick = {
                                editing = driver
                                editName = driver.name
                                editTeam = driver.team
                                editAge = driver.age.toString()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .padding(vertical = Spacing.small)
                                .semantics(mergeDescendants = true) {
                                    role = Role.Button
                                    contentDescription = desc
                                }
                        ) {
                            Text(
                                text = driver.name + if (driver.team.isNotBlank()) " · ${driver.team}" else "",
                                modifier = Modifier.padding(Spacing.medium),
                                maxLines = maxLines,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = Spacing.small)
                ) {
                    items(drivers) { driver ->
                        val desc = buildString {
                            append("Pilóta szerkesztése: ${driver.name}")
                            if (driver.team.isNotBlank()) append(", csapat: ${driver.team}")
                        }
                        Card(
                            onClick = {
                                editing = driver
                                editName = driver.name
                                editTeam = driver.team
                                editAge = driver.age.toString()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .padding(vertical = Spacing.small)
                                .semantics(mergeDescendants = true) {
                                    role = Role.Button
                                    contentDescription = desc
                                }
                        ) {
                            Text(
                                text = driver.name + if (driver.team.isNotBlank()) " · ${driver.team}" else "",
                                modifier = Modifier.padding(Spacing.medium),
                                maxLines = maxLines,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }

    val current = editing
    if (current != null) {
        LaunchedEffect(current.id) {
            nameFocusRequester.requestFocus()
        }
        AlertDialog(
            onDismissRequest = { editing = null },
            title = { Text("Pilóta szerkesztése") },
            text = {
                Column {
                    TextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Név") },
                        singleLine = true,
                        modifier = Modifier.focusRequester(nameFocusRequester)
                    )
                    TextField(
                        value = editTeam,
                        onValueChange = { editTeam = it },
                        label = { Text("Csapat") },
                        singleLine = true
                    )
                    TextField(
                        value = editAge,
                        onValueChange = { editAge = it },
                        label = { Text("Életkor") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        val ageInt = editAge.toIntOrNull() ?: current.age
                        repository.updateDriver(
                            current.copy(
                                name = editName.trim().ifBlank { current.name },
                                team = editTeam.trim(),
                                age = ageInt
                            )
                        )
                    }
                    editing = null
                }) { Text("Mentés") }
            },
            dismissButton = {
                TextButton(onClick = { editing = null }) { Text("Mégse") }
            }
        )
    }
}
