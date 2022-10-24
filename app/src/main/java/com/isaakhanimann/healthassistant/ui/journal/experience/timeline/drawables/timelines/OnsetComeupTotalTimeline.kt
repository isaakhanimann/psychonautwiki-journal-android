package com.isaakhanimann.healthassistant.ui.journal.experience.timeline.drawables.timelines

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.isaakhanimann.healthassistant.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.healthassistant.ui.journal.experience.timeline.AllTimelinesModel
import com.isaakhanimann.healthassistant.ui.journal.experience.timeline.drawables.TimelineDrawable
import com.isaakhanimann.healthassistant.ui.journal.experience.timeline.endSmoothLineTo
import com.isaakhanimann.healthassistant.ui.journal.experience.timeline.startSmoothLineTo

data class OnsetComeupTotalTimeline(
    val onset: FullDurationRange,
    val comeup: FullDurationRange,
    val total: FullDurationRange,
) : TimelineDrawable {

    override fun getPeakDurationRangeInSeconds(startDurationInSeconds: Float): ClosedRange<Float>? {
        return null
    }

    override val widthInSeconds: Float =
        total.maxInSeconds

    override fun drawTimeLine(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color,
    ) {
        val weight = 0.5f
        val onsetEndX =
            startX + (onset.interpolateAtValueInSeconds(weight) * pixelsPerSec)
        val comeupEndX =
            onsetEndX + (comeup.interpolateAtValueInSeconds(weight) * pixelsPerSec)
        drawScope.drawPath(
            path = Path().apply {
                moveTo(x = startX, y = height)
                lineTo(x = onsetEndX, y = height)
                lineTo(x = comeupEndX, y = 0f)
            },
            color = color,
            style = AllTimelinesModel.normalStroke
        )
        drawScope.drawPath(
            path = Path().apply {
                moveTo(x = comeupEndX, y = 0f)
                val offsetEndX = total.interpolateAtValueInSeconds(weight) * pixelsPerSec
                startSmoothLineTo(
                    percentSmoothness = 0.5f,
                    startX = comeupEndX,
                    startY = 0f,
                    endX = offsetEndX,
                    endY = height
                )
            },
            color = color,
            style = AllTimelinesModel.dottedStroke
        )
    }

    override fun drawTimeLineShape(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color,
    ) {
        drawScope.drawPath(
            path = Path().apply {
                val onsetStartMinX = startX + (onset.minInSeconds * pixelsPerSec)
                val comeupEndMinX = onsetStartMinX + (comeup.minInSeconds * pixelsPerSec)
                val offsetEndMaxX = total.maxInSeconds * pixelsPerSec
                val onsetStartMaxX = startX + (onset.maxInSeconds * pixelsPerSec)
                val comeupEndMaxX =
                    onsetStartMaxX + (comeup.maxInSeconds * pixelsPerSec)
                val offsetEndMinX =
                    total.minInSeconds * pixelsPerSec
                moveTo(onsetStartMinX, height)
                lineTo(x = comeupEndMinX, y = 0f)
                lineTo(x = comeupEndMaxX, y = 0f)
                startSmoothLineTo(
                    percentSmoothness = 0.5f,
                    startX = comeupEndMaxX,
                    startY = 0f,
                    endX = offsetEndMaxX,
                    endY = height
                )
                lineTo(x = offsetEndMinX, y = height)
                endSmoothLineTo(
                    percentSmoothness = 0.5f,
                    startX = offsetEndMinX,
                    endX = comeupEndMinX,
                    endY = 0f
                )
                lineTo(x = comeupEndMaxX, y = 0f)
                lineTo(x = onsetStartMaxX, y = height)
                close()
            },
            color = color.copy(alpha = AllTimelinesModel.shapeAlpha)
        )
    }
}

fun RoaDuration.toOnsetComeupTotalTimeline(): OnsetComeupTotalTimeline? {
    val fullOnset = onset?.toFullDurationRange()
    val fullComeup = comeup?.toFullDurationRange()
    val fullTotal = total?.toFullDurationRange()
    return if (fullOnset != null && fullComeup != null && fullTotal != null) {
        OnsetComeupTotalTimeline(
            onset = fullOnset,
            comeup = fullComeup,
            total = fullTotal
        )
    } else {
        null
    }
}