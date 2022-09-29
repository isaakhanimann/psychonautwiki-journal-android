package com.isaakhanimann.healthassistant.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(0xFF0A84FF),
    primaryVariant = Color(0xFF0069cb),
    secondary = Color(0xFF0A84FF),
    onPrimary = Color.White,
    onSecondary = Color.White
)

private val LightColorPalette = lightColors(
    primary = Color(0xFF007AFF),
    primaryVariant = Color(0xFF0069cb),
    secondary = Color(0xFF007AFF),
    onPrimary = Color.White,
    onSecondary = Color.White,
    surface = Color(0xFFF2F2F7)
)

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