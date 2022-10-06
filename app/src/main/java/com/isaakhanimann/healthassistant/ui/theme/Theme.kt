package com.isaakhanimann.healthassistant.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkColorPalette = darkColors(
    primary = Color(0xFF0A84FF),
    secondary = Color(0xFF0A84FF),
    background = Color(0xFF00060D),
    surface = Color(0xFF001933),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onSurface = Color.White,
)

private val LightColorPalette = lightColors(
    primary = Color(0xFF007AFF),
    secondary = Color(0xFF007AFF),
    background = Color(0xFFF7FBFF),
    surface = Color(0xFFE6F2FF),
    onPrimary = Color.White,
    onSecondary = Color.White,
)

val horizontalPadding = 10.dp

@Composable
fun HealthAssistantTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}