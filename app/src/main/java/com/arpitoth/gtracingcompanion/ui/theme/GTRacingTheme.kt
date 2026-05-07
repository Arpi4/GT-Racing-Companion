package com.arpitoth.gtracingcompanion.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun GTRacingTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    val configuration = LocalConfiguration.current
    val widthDp = configuration.screenWidthDp
    val typeScale = if (widthDp >= 720) 1.1f else 1f
    val typography = remember(typeScale) { appTypography(typeScale) }
    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        content = content
    )
}