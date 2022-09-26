package com.isaakhanimann.healthassistant.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(0xFF96a4fe),
    primaryVariant = Color(0xFF001FB5),
    secondary = Color(0xFF5b29ff),
    onPrimary = Color.Black,
    onSecondary = Color.White
)

private val LightColorPalette = lightColors(
    primary = Color(0xFF003bf0),
    primaryVariant = Color(0xFF0001c0),
    secondary = Color(0xFF5b29ff),
    onPrimary = Color.White,
    onSecondary = Color.White
)

val yellow = Color(0xFFffc629)

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