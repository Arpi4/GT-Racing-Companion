package com.arpitoth.gtracingcompanion.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun ConfirmationDialog(
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Igen") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Mégse") }
        },
        text = { Text(message) }
    )
}