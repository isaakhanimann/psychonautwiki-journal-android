package com.isaakhanimann.healthassistant.ui.journal.experience.timeline

import androidx.compose.ui.graphics.Path

fun Path.startSmoothLineTo(
    percentSmoothness: Float,
    startX: Float,
    startY: Float,
    endX: Float,
    endY: Float
) {
    val diff = endX - startX
    val controlX = startX + (diff * percentSmoothness)
    quadraticBezierTo(controlX, startY, endX, endY)
}

fun Path.endSmoothLineTo(
    percentSmoothness: Float,
    startX: Float,
    endX: Float,
    endY: Float
) {
    val diff = endX - startX
    val controlX = endX - (diff * percentSmoothness)
    quadraticBezierTo(controlX, endY, endX, endY)
}
