package com.isaakhanimann.journal.ui.journal.experience.timeline.drawables.timelines

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.journal.ui.journal.experience.timeline.AllTimelinesModel
import com.isaakhanimann.journal.ui.journal.experience.timeline.drawables.TimelineDrawable

data class TotalTimeline(
    val total: FullDurationRange,
    val totalWeight: Float,
    val percentSmoothness: Float = 0.5f,
) : TimelineDrawable {

    override fun drawTimeLine(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color,
    ) {
        drawScope.drawPath(
            path = Path().apply {
                val totalMinX = total.minInSeconds * pixelsPerSec
                val totalX = total.interpolateAtValueInSeconds(totalWeight) * pixelsPerSec
                moveTo(startX, height)
                endSmoothLineTo(
                    smoothnessBetween0And1 = percentSmoothness,
                    startX = startX,
                    endX = startX + (totalMinX / 2),
                    endY = 0f
                )
                startSmoothLineTo(
                    smoothnessBetween0And1 = percentSmoothness,
                    startX = startX + (totalMinX / 2),
                    startY = 0f,
                    endX = startX + totalX,
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
        color: Color
    ) {
        drawScope.drawPath(
            path = Path().apply {
                // path over top
                val totalMinX = total.minInSeconds * pixelsPerSec
                val totalMaxX = total.maxInSeconds * pixelsPerSec
                moveTo(x = startX + (totalMinX / 2), y = 0f)
                startSmoothLineTo(
                    smoothnessBetween0And1 = percentSmoothness,
                    startX = startX + (totalMinX / 2),
                    startY = 0f,
                    endX = startX + totalMaxX,
                    endY = height
                )
                lineTo(x = startX + totalMaxX, y = height)
                // path bottom back
                lineTo(x = startX + totalMinX, y = height)
                endSmoothLineTo(
                    smoothnessBetween0And1 = percentSmoothness,
                    startX = startX + totalMinX,
                    endX = startX + (totalMinX / 2),
                    endY = 0f
                )
                close()
            },
            color = color.copy(alpha = AllTimelinesModel.shapeAlpha)
        )
    }

    override val widthInSeconds: Float = total.maxInSeconds
    override fun getPeakDurationRangeInSeconds(startDurationInSeconds: Float): ClosedRange<Float>? {
        return null
    }
}

fun RoaDuration.toTotalTimeline(totalWeight: Float): TotalTimeline? {
    val fullTotal = total?.toFullDurationRange()
    return if (fullTotal != null) {
        TotalTimeline(total = fullTotal, totalWeight = totalWeight)
    } else {
        null
    }
}