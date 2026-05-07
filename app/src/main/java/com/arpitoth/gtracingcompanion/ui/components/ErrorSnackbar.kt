package com.arpitoth.gtracingcompanion.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun ErrorSnackbar(message: String, onDismiss: () -> Unit) {
    Snackbar(
        action = {
            TextButton(onClick = onDismiss) { Text("Bezárás") }
        }
    ) {
        Text(message)
    }
}