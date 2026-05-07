package com.example.gtracing.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

val LightPrimary = Color(0xFF6200EE)
val LightSecondary = Color(0xFF03DAC6)
val LightBackground = Color(0xFFFFFFFF)

val DarkPrimary = Color(0xFFBB86FC)
val DarkSecondary = Color(0xFF03DAC6)
val DarkBackground = Color(0xFF121212)

object Spacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
}

object Shadows {
    val small = 2.dp
    val medium = 4.dp
    val large = 8.dp
}

val AppTypography = Typography(
    headlineLarge = TextStyle(fontSize = 32.sp),
    headlineMedium = TextStyle(fontSize = 24.sp),
    bodyLarge = TextStyle(fontSize = 16.sp),
    bodyMedium = TextStyle(fontSize = 14.sp),
    labelSmall = TextStyle(fontSize = 12.sp)
)

@Composable
fun GTTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = DarkPrimary,
            secondary = DarkSecondary,
            background = DarkBackground
        )
    } else {
        lightColorScheme(
            primary = LightPrimary,
            secondary = LightSecondary,
            background = LightBackground
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}