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
    primaryVariant = Color(0xFF0069cb),
    secondary = Color(0xFF0A84FF),
    onPrimary = Color.White,
    onSecondary = Color.White,
    surface = Color(0xFF2C2C2E),
    onSurface = Color.White
)

private val LightColorPalette = lightColors(
    primary = Color(0xFF007AFF),
    primaryVariant = Color(0xFF0069cb),
    secondary = Color(0xFF007AFF),
    onPrimary = Color.White,
    onSecondary = Color.White,
    surface = Color(0xFFF2F2F7)
)

fun lightGray(isDarkTheme: Boolean): Color {
    return if (isDarkTheme) {
        Color(0xff3A3A3C)
    } else {
        Color(0xffE5E5EA)
    }
}

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