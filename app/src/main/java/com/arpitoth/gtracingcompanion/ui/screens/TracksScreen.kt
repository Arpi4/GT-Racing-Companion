package com.arpitoth.gtracingcompanion.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.arpitoth.gtracingcompanion.data.GtRepository
import com.arpitoth.gtracingcompanion.data.local.entity.TrackEntity
import com.arpitoth.gtracingcompanion.ui.components.CustomAppBar
import com.arpitoth.gtracingcompanion.ui.theme.Spacing
import kotlinx.coroutines.launch
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.contentDescription

@Composable
fun TracksScreen(
    navController: NavController,
    repository: GtRepository
) {
    val tracks by repository.tracks.collectAsStateWithLifecycle(initialValue = emptyList())
    var editing by remember { mutableStateOf<TrackEntity?>(null) }
    var editName by remember { mutableStateOf("") }
    var editLength by remember { mutableStateOf("") }
    var editLocation by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val nameFocusRequester = remember { FocusRequester() }
    val fontScale = LocalDensity.current.fontScale
    val maxNameLines = if (fontScale > 1.2f) 2 else 1
    val widthDp = LocalConfiguration.current.screenWidthDp
    val useGrid = widthDp >= 600

    Scaffold(
        topBar = { CustomAppBar(title = "Pályák") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_track") },
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Új pálya")
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
                    columns = GridCells.Adaptive(minSize = 240.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = Spacing.extraSmall)
                ) {
                    items(tracks) { track ->
                        val desc = "Pálya szerkesztése: ${track.name}"
                        Card(
                            onClick = {
                                editing = track
                                editName = track.name
                                editLength = track.lengthKm
                                editLocation = track.location
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .padding(vertical = Spacing.extraSmall)
                                .semantics(mergeDescendants = true) {
                                    role = Role.Button
                                    contentDescription = desc
                                }
                        ) {
                            Column(Modifier.padding(Spacing.medium)) {
                                Text(track.name, maxLines = maxNameLines, overflow = TextOverflow.Ellipsis)
                                Text(
                                    "${track.lengthKm} km ${if (track.location.isNotBlank()) "· ${track.location}" else ""}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = Spacing.extraSmall)
                ) {
                    items(tracks) { track ->
                        val desc = "Pálya szerkesztése: ${track.name}"
                        Card(
                            onClick = {
                                editing = track
                                editName = track.name
                                editLength = track.lengthKm
                                editLocation = track.location
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .padding(vertical = Spacing.extraSmall)
                                .semantics(mergeDescendants = true) {
                                    role = Role.Button
                                    contentDescription = desc
                                }
                        ) {
                            Column(Modifier.padding(Spacing.medium)) {
                                Text(track.name, maxLines = maxNameLines, overflow = TextOverflow.Ellipsis)
                                Text(
                                    "${track.lengthKm} km ${if (track.location.isNotBlank()) "· ${track.location}" else ""}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
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
            title = { Text("Pálya szerkesztése") },
            text = {
                Column {
                    TextField(
                        editName,
                        { editName = it },
                        modifier = Modifier.focusRequester(nameFocusRequester),
                        label = { Text("Név") },
                        singleLine = true
                    )
                    TextField(editLength, { editLength = it }, label = { Text("Hossz (km)") }, singleLine = true)
                    TextField(editLocation, { editLocation = it }, label = { Text("Ország / hely") }, singleLine = true)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        repository.updateTrack(
                            current.copy(
                                name = editName.trim().ifBlank { current.name },
                                lengthKm = editLength.trim().ifBlank { current.lengthKm },
                                location = editLocation.trim()
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
