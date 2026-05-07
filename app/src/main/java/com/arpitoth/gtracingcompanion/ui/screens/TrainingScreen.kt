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
import com.arpitoth.gtracingcompanion.data.local.entity.TrainingEntity
import com.arpitoth.gtracingcompanion.ui.components.CustomAppBar
import com.arpitoth.gtracingcompanion.ui.theme.Spacing
import kotlinx.coroutines.launch
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.contentDescription

@Composable
fun TrainingScreen(
    navController: NavController,
    repository: GtRepository
) {
    val trainings by repository.trainings.collectAsStateWithLifecycle(initialValue = emptyList())
    var editing by remember { mutableStateOf<TrainingEntity?>(null) }
    var editDriver by remember { mutableStateOf("") }
    var editType by remember { mutableStateOf("") }
    var editDuration by remember { mutableStateOf("") }
    var editRating by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val driverFocusRequester = remember { FocusRequester() }
    val fontScale = LocalDensity.current.fontScale
    val driverMaxLines = if (fontScale > 1.2f) 2 else 1
    val titleMaxLines = if (fontScale > 1.2f) 2 else 1
    val widthDp = LocalConfiguration.current.screenWidthDp
    val useGrid = widthDp >= 600

    Scaffold(
        topBar = { CustomAppBar(title = "Edzésnapló") },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_training") }) {
                Icon(Icons.Default.Add, contentDescription = "Új edzés")
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
                    items(trainings) { item ->
                        val desc = "Edzés szerkesztése: ${item.driverName}, ${item.type}"
                        Card(
                            onClick = {
                                editing = item
                                editDriver = item.driverName
                                editType = item.type
                                editDuration = item.durationMinutes.toString()
                                editRating = item.rating.toString()
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
                                    item.driverName,
                                    maxLines = driverMaxLines,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "${item.type} · ${item.durationMinutes} p · ${item.rating}/10",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = titleMaxLines,
                                    overflow = TextOverflow.Ellipsis
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
                    items(trainings) { item ->
                        val desc = "Edzés szerkesztése: ${item.driverName}, ${item.type}"
                        Card(
                            onClick = {
                                editing = item
                                editDriver = item.driverName
                                editType = item.type
                                editDuration = item.durationMinutes.toString()
                                editRating = item.rating.toString()
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
                                    item.driverName,
                                    maxLines = driverMaxLines,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "${item.type} · ${item.durationMinutes} p · ${item.rating}/10",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = titleMaxLines,
                                    overflow = TextOverflow.Ellipsis
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
            driverFocusRequester.requestFocus()
        }
        AlertDialog(
            onDismissRequest = { editing = null },
            title = { Text("Edzés szerkesztése") },
            text = {
                Column {
                    TextField(
                        editDriver,
                        { editDriver = it },
                        modifier = Modifier.focusRequester(driverFocusRequester),
                        label = { Text("Pilóta") },
                        singleLine = true
                    )
                    TextField(editType, { editType = it }, label = { Text("Típus") }, singleLine = true)
                    TextField(editDuration, { editDuration = it }, label = { Text("Perc") }, singleLine = true)
                    TextField(editRating, { editRating = it }, label = { Text("Értékelés (0–10)") }, singleLine = true)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        repository.updateTraining(
                            current.copy(
                                driverName = editDriver.trim().ifBlank { current.driverName },
                                type = editType.trim().ifBlank { current.type },
                                durationMinutes = editDuration.toIntOrNull() ?: current.durationMinutes,
                                rating = editRating.toIntOrNull()?.coerceIn(0, 10) ?: current.rating
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
