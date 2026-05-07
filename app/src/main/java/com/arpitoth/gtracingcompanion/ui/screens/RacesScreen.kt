package com.arpitoth.gtracingcompanion.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import com.arpitoth.gtracingcompanion.data.local.entity.RaceEntity
import com.arpitoth.gtracingcompanion.ui.components.CustomAppBar
import com.arpitoth.gtracingcompanion.ui.theme.Spacing
import kotlinx.coroutines.launch
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.contentDescription

@Composable
fun RacesScreen(
    navController: NavController,
    repository: GtRepository
) {
    val races by repository.races.collectAsStateWithLifecycle(initialValue = emptyList())
    var editing by remember { mutableStateOf<RaceEntity?>(null) }
    var editTitle by remember { mutableStateOf("") }
    var editDate by remember { mutableStateOf("") }
    var editLocation by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val titleFocusRequester = remember { FocusRequester() }
    val fontScale = LocalDensity.current.fontScale
    val maxLines = if (fontScale > 1.2f) 3 else 2
    val widthDp = LocalConfiguration.current.screenWidthDp
    val useGrid = widthDp >= 600

    Scaffold(
        topBar = { CustomAppBar(title = "Versenyek") },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_race") }) {
                Icon(Icons.Default.Add, contentDescription = "Új verseny")
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
                    columns = GridCells.Adaptive(minSize = 220.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = Spacing.small)
                ) {
                    items(races) { race ->
                        val desc = "Verseny szerkesztése: ${race.title}, ${race.date}"
                        Card(
                            onClick = {
                                editing = race
                                editTitle = race.title
                                editDate = race.date
                                editLocation = race.location
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
                            Column(Modifier.padding(Spacing.medium)) {
                                Text(
                                    race.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    maxLines = maxLines,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "${race.date} · ${race.location}",
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
                    contentPadding = PaddingValues(vertical = Spacing.small)
                ) {
                    items(races) { race ->
                        val desc = "Verseny szerkesztése: ${race.title}, ${race.date}"
                        Card(
                            onClick = {
                                editing = race
                                editTitle = race.title
                                editDate = race.date
                                editLocation = race.location
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
                            Column(Modifier.padding(Spacing.medium)) {
                                Text(
                                    race.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    maxLines = maxLines,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "${race.date} · ${race.location}",
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
            titleFocusRequester.requestFocus()
        }
        AlertDialog(
            onDismissRequest = { editing = null },
            title = { Text("Verseny szerkesztése") },
            text = {
                Column {
                    TextField(
                        editTitle,
                        { editTitle = it },
                        modifier = Modifier.focusRequester(titleFocusRequester),
                        label = { Text("Cím") },
                        singleLine = true
                    )
                    TextField(editDate, { editDate = it }, label = { Text("Dátum (ÉÉÉÉ-HH-NN)") }, singleLine = true)
                    TextField(editLocation, { editLocation = it }, label = { Text("Helyszín") }, singleLine = true)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        repository.updateRace(
                            current.copy(
                                title = editTitle.trim().ifBlank { current.title },
                                date = editDate.trim().ifBlank { current.date },
                                location = editLocation.trim().ifBlank { current.location }
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
