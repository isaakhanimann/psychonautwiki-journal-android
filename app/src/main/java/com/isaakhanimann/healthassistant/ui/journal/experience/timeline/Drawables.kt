package com.isaakhanimann.healthassistant.ui.journal.experience.timeline

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.isaakhanimann.healthassistant.data.substances.classes.roa.DurationRange
import com.isaakhanimann.healthassistant.data.substances.classes.roa.RoaDuration

interface TimelineDrawable {
    fun drawTimeLine(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color
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

data class FullTimeline(
    val onset: FullDurationRange,
    val comeup: FullDurationRange,
    val peak: FullDurationRange,
    val offset: FullDurationRange,
) : TimelineDrawable {

    fun getPeakDurationRangeInSeconds(startDurationInSeconds: Float): ClosedRange<Float> {
        val startRange =
            startDurationInSeconds + onset.interpolateAtValueInSeconds(0.5f) + comeup.interpolateAtValueInSeconds(
                0.5f
            )
        return startRange..(startRange + peak.interpolateAtValueInSeconds(0.5f))
    }

    override val widthInSeconds: Float =
        onset.maxInSeconds + comeup.maxInSeconds + peak.maxInSeconds + offset.maxInSeconds

    override fun drawTimeLine(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color
    ) {

        val strokeWidth = 5f
        drawScope.drawPath(
            path = Path().apply {
                val weight = 0.5f
                val onsetEndX =
                    startX + (onset.interpolateAtValueInSeconds(weight) * pixelsPerSec)
                val comeupEndX =
                    onsetEndX + (comeup.interpolateAtValueInSeconds(weight) * pixelsPerSec)
                val peakEndX =
                    comeupEndX + (peak.interpolateAtValueInSeconds(weight) * pixelsPerSec)
                val offsetEndX =
                    peakEndX + (offset.interpolateAtValueInSeconds(weight) * pixelsPerSec)
                moveTo(x = startX, y = height)
                lineTo(x = onsetEndX, y = height)
                lineTo(x = comeupEndX, y = 0f)
                lineTo(x = peakEndX, y = 0f)
                lineTo(x = offsetEndX, y = height)
            },
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
            )
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
                val onsetStartMinX = startX + (onset.minInSeconds * pixelsPerSec)
                val comeupEndMinX = onsetStartMinX + (comeup.minInSeconds * pixelsPerSec)
                val peakEndMaxX =
                    startX + ((onset.maxInSeconds + comeup.maxInSeconds + peak.maxInSeconds) * pixelsPerSec)
                val offsetEndMaxX =
                    peakEndMaxX + (offset.maxInSeconds * pixelsPerSec)
                moveTo(onsetStartMinX, height)
                lineTo(x = comeupEndMinX, y = 0f)
                lineTo(x = peakEndMaxX, y = 0f)
                lineTo(x = offsetEndMaxX, y = height)
                // path bottom back
                val onsetStartMaxX = startX + (onset.maxInSeconds * pixelsPerSec)
                val comeupEndMaxX =
                    onsetStartMaxX + (comeup.maxInSeconds * pixelsPerSec)
                val peakEndMinX =
                    startX + ((onset.minInSeconds + comeup.minInSeconds + peak.minInSeconds) * pixelsPerSec)
                val offsetEndMinX =
                    peakEndMinX + (offset.minInSeconds * pixelsPerSec)
                lineTo(x = offsetEndMinX, y = height)
                lineTo(x = peakEndMinX, y = 0f)
                lineTo(x = comeupEndMaxX, y = 0f)
                lineTo(x = onsetStartMaxX, y = height)
                close()
            },
            color = color.copy(alpha = 0.1f)
        )
    }
}

data class FullDurationRange(
    val minInSeconds: Float,
    val maxInSeconds: Float
) {
    fun interpolateAtValueInSeconds(value: Float): Float {
        val diff = maxInSeconds - minInSeconds
        return minInSeconds + diff.times(value)
    }
}

data class TotalTimeline(
    val total: FullDurationRange,
    val weight: Float = 0.5f,
    val percentSmoothness: Float = 0.5f,
) : TimelineDrawable {
    override fun drawTimeLine(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color
    ) {
        drawScope.drawPath(
            path = Path().apply {
                val totalMinX = total.minInSeconds * pixelsPerSec
                val totalX = total.interpolateAtValueInSeconds(weight) * pixelsPerSec
                moveTo(startX, height)
                endSmoothLineTo(
                    percentSmoothness = percentSmoothness,
                    startX = startX,
                    endX = startX + (totalMinX / 2),
                    endY = 0f
                )
                startSmoothLineTo(
                    percentSmoothness = percentSmoothness,
                    startX = startX + (totalMinX / 2),
                    startY = 0f,
                    endX = startX + totalX,
                    endY = height
                )
            },
            color = color,
            style = Stroke(
                width = 5f,
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 30f))
            )
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
                    percentSmoothness = percentSmoothness,
                    startX = startX + (totalMinX / 2),
                    startY = 0f,
                    endX = startX + totalMaxX,
                    endY = height
                )
                lineTo(x = startX + totalMaxX, y = height)
                // path bottom back
                lineTo(x = startX + totalMinX, y = height)
                endSmoothLineTo(
                    percentSmoothness = percentSmoothness,
                    startX = startX + totalMinX,
                    endX = startX + (totalMinX / 2),
                    endY = 0f
                )
                close()
            },
            color = color.copy(alpha = 0.1f)
        )
    }

    override val widthInSeconds: Float = total.maxInSeconds
}

fun RoaDuration.toFullTimeline(): FullTimeline? {
    val fullOnset = onset?.toFullDurationRange()
    val fullComeup = comeup?.toFullDurationRange()
    val fullPeak = peak?.toFullDurationRange()
    val fullOffset = offset?.toFullDurationRange()
    return if (fullOnset != null && fullComeup != null && fullPeak != null && fullOffset != null) {
        FullTimeline(
            onset = fullOnset,
            comeup = fullComeup,
            peak = fullPeak,
            offset = fullOffset
        )
    } else {
        null
    }
}

fun RoaDuration.toTotalTimeline(): TotalTimeline? {
    val fullTotal = total?.toFullDurationRange()
    return if (fullTotal != null) {
        TotalTimeline(total = fullTotal)
    } else {
        null
    }
}

fun DurationRange.toFullDurationRange(): FullDurationRange? {
    return if (minInSec != null && maxInSec != null) {
        FullDurationRange(minInSec, maxInSec)
    } else {
        null
    }
}

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
