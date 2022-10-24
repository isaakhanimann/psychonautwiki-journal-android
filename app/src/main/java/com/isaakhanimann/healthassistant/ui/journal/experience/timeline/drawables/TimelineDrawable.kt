package com.isaakhanimann.healthassistant.ui.journal.experience.timeline.drawables

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

interface TimelineDrawable {
    fun drawTimeLine(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color,
        strokeWidth: Float
    )

    fun drawTimeLineShape(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color
    )

    val widthInSeconds: Float
}