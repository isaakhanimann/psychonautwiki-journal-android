package com.isaakhanimann.journal.ui.journal.experience.timeline.drawables.timelines

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.journal.ui.journal.experience.timeline.AllTimelinesModel
import com.isaakhanimann.journal.ui.journal.experience.timeline.drawables.TimelineDrawable

data class OnsetTotalTimeline(
    val onset: FullDurationRange,
    val total: FullDurationRange,
    val totalWeight: Float
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
        val onsetWeight = 0.5f
        val onsetEndX =
            startX + (onset.interpolateAtValueInSeconds(onsetWeight) * pixelsPerSec)
        drawScope.drawPath(
            path = Path().apply {
                moveTo(x = startX, y = height)
                lineTo(x = onsetEndX, y = height)
            },
            color = color,
            style = AllTimelinesModel.normalStroke
        )
        drawScope.drawPath(
            path = Path().apply {
                moveTo(x = onsetEndX, y = height)
                val totalX = total.interpolateAtValueInSeconds(totalWeight) * pixelsPerSec
                endSmoothLineTo(
                    smoothnessBetween0And1 = 0.5f,
                    startX = onsetEndX,
                    endX = totalX / 2f,
                    endY = 0f
                )
                startSmoothLineTo(
                    smoothnessBetween0And1 = 0.5f,
                    startX = totalX / 2f,
                    startY = 0f,
                    endX = totalX,
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
                val onsetEndMinX = startX + (onset.minInSeconds * pixelsPerSec)
                val onsetEndMaxX = startX + (onset.maxInSeconds * pixelsPerSec)
                val totalX = total.interpolateAtValueInSeconds(totalWeight) * pixelsPerSec
                val totalMinX =
                    total.minInSeconds * pixelsPerSec
                val totalMaxX =
                    total.maxInSeconds * pixelsPerSec
                moveTo(onsetEndMinX, height)
                endSmoothLineTo(
                    smoothnessBetween0And1 = 0.5f,
                    startX = onsetEndMinX,
                    endX = totalX/2,
                    endY = 0f
                )
                startSmoothLineTo(
                    smoothnessBetween0And1 = 0.5f,
                    startX = totalX/2,
                    startY = 0f,
                    endX = totalMaxX,
                    endY = height
                )
                lineTo(x = totalMinX, y = height)
                endSmoothLineTo(
                    smoothnessBetween0And1 = 0.5f,
                    startX = totalMinX,
                    endX = totalX/2,
                    endY = 0f
                )
                startSmoothLineTo(
                    smoothnessBetween0And1 = 0.5f,
                    startX = totalX/2,
                    startY = 0f,
                    endX = onsetEndMaxX,
                    endY = height
                )
                close()
            },
            color = color.copy(alpha = AllTimelinesModel.shapeAlpha)
        )
    }
}

fun RoaDuration.toOnsetTotalTimeline(totalWeight: Float): OnsetTotalTimeline? {
    val fullOnset = onset?.toFullDurationRange()
    val fullTotal = total?.toFullDurationRange()
    return if (fullOnset != null && fullTotal != null) {
        OnsetTotalTimeline(
            onset = fullOnset,
            total = fullTotal,
            totalWeight = totalWeight
        )
    } else {
        null
    }
}