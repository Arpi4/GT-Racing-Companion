package com.arpitoth.gtracingcompanion.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

fun appTypography(scale: Float = 1f): Typography {
    val s = scale.coerceIn(0.9f, 1.25f)
    return Typography(
        headlineLarge = TextStyle(
            fontSize = (32f * s).sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = (0.5f * s).sp
        ),
        headlineMedium = TextStyle(
            fontSize = (26f * s).sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = (0.4f * s).sp
        ),
        headlineSmall = TextStyle(
            fontSize = (22f * s).sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = (0.3f * s).sp
        ),
        titleLarge = TextStyle(
            fontSize = (20f * s).sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.SansSerif
        ),
        titleMedium = TextStyle(
            fontSize = (16f * s).sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = (0.8f * s).sp
        ),
        bodyLarge = TextStyle(fontSize = (16f * s).sp, fontFamily = FontFamily.SansSerif),
        bodyMedium = TextStyle(fontSize = (14f * s).sp, fontFamily = FontFamily.SansSerif),
        labelLarge = TextStyle(
            fontSize = (12f * s).sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = (1f * s).sp
        ),
        labelMedium = TextStyle(fontSize = (11f * s).sp, fontFamily = FontFamily.SansSerif)
    )
}