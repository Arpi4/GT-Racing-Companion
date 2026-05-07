package com.arpitoth.gtracingcompanion.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size

@Composable
fun LoadingSpinner() {
    CircularProgressIndicator(
        modifier = Modifier.size(48.dp)
    )
}