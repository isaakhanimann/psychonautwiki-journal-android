package com.isaakhanimann.healthassistant.ui.journal.experience.timeline.drawables.timelines

import androidx.compose.ui.graphics.Path

fun Path.startSmoothLineTo(
    smoothnessBetween0And1: Float,
    startX: Float,
    startY: Float,
    endX: Float,
    endY: Float
) {
    val diff = endX - startX
    val controlX = startX + (diff * smoothnessBetween0And1)
    quadraticBezierTo(controlX, startY, endX, endY)
}

fun Path.endSmoothLineTo(
    smoothnessBetween0And1: Float,
    startX: Float,
    endX: Float,
    endY: Float
) {
    val diff = endX - startX
    val controlX = endX - (diff * smoothnessBetween0And1)
    quadraticBezierTo(controlX, endY, endX, endY)
}
