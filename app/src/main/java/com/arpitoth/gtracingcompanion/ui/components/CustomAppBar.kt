package com.arpitoth.gtracingcompanion.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(title: String) {
    TopAppBar(
        title = {
            Text(
                title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.semantics { heading() }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}