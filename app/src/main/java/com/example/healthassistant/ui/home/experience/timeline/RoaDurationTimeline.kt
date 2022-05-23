package com.example.healthassistant.ui.home.experience.timeline

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.substances.DurationRange
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.ui.previewproviders.RoaDurationPreviewProvider
import kotlin.time.Duration

@Preview
@Composable
fun RoaDurationTimelinePreview(
    @PreviewParameter(RoaDurationPreviewProvider::class) roaDuration: RoaDuration,
) {
    RoaDurationTimeline(
        roaDuration = roaDuration,
        color = Color.Blue,
        modifier = Modifier
            .width(400.dp)
            .height(200.dp)
    )
}

data class FullTimeline(
    val onset: FullDurationRange,
    val comeup: FullDurationRange,
    val peak: FullDurationRange,
    val offset: FullDurationRange,
) {
    val totalMax
        get() = onset.max + comeup.max + peak.max + offset.max
}

data class FullDurationRange(
    val min: Duration,
    val max: Duration
) {
    fun interpolateAt(value: Double): Duration {
        val diff = max - min
        return min + diff.times(value)
    }
}

data class TotalTimeline(
    val total: FullDurationRange
)

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
    return if (min != null && max != null) {
        FullDurationRange(min, max)
    } else {
        null
    }
}

@Composable
fun RoaDurationTimeline(
    roaDuration: RoaDuration,
    color: Color,
    modifier: Modifier
) {
    val fullTimeline = remember(roaDuration) {
        roaDuration.toFullTimeline()
    }
    if (fullTimeline != null) {
        RoaDurationFullTimeline(fullTimeline = fullTimeline, color = color, modifier = modifier)
    } else {
        val totalTimeline = remember(roaDuration) {
            roaDuration.toTotalTimeline()
        }
        if (totalTimeline != null) {
            RoaDurationTotalTimeline(
                totalTimeline = totalTimeline,
                color = color,
                modifier = modifier
            )
        } else {
            Text(text = "There can be no timeline drawn")
        }
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

@Composable
fun RoaDurationFullTimeline(
    fullTimeline: FullTimeline,
    color: Color,
    strokeWidth: Float = 5f,
    percentSmoothness: Float = 0.1f,
    modifier: Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasHeightOuter = size.height
        val canvasWidth = size.width
        val pixelsPerSec = canvasWidth / fullTimeline.totalMax.inWholeSeconds
        inset(vertical = strokeWidth / 2) {
            val canvasHeightInner = size.height
            val strokePath = Path().apply {
                val weight = 0.5
                val onsetEndX =
                    fullTimeline.onset.interpolateAt(weight).inWholeSeconds * pixelsPerSec
                val comeupEndX =
                    onsetEndX + (fullTimeline.comeup.interpolateAt(weight).inWholeSeconds * pixelsPerSec)
                val peakEndX =
                    comeupEndX + (fullTimeline.peak.interpolateAt(weight).inWholeSeconds * pixelsPerSec)
                val offsetEndX =
                    peakEndX + (fullTimeline.offset.interpolateAt(weight).inWholeSeconds * pixelsPerSec)
                moveTo(0f, canvasHeightInner)
                lineTo(x = onsetEndX, y = canvasHeightInner)
                lineTo(x = comeupEndX, y = 0f)
                lineTo(x = peakEndX, y = 0f)
                lineTo(x = offsetEndX, y = canvasHeightInner)
            }
            drawPath(
                path = strokePath,
                color = color,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )
        }
        val fillPath = Path().apply {
            // path over top
            val onsetStartMinX = fullTimeline.onset.min.inWholeSeconds * pixelsPerSec
            val comeupEndMinX =
                onsetStartMinX + (fullTimeline.comeup.min.inWholeSeconds * pixelsPerSec)
            val peakEndMaxX =
                (fullTimeline.onset.max + fullTimeline.comeup.max + fullTimeline.peak.max).inWholeSeconds * pixelsPerSec
            val offsetEndMaxX =
                peakEndMaxX + (fullTimeline.offset.max.inWholeSeconds * pixelsPerSec)
            moveTo(onsetStartMinX, canvasHeightOuter)
            lineTo(x = comeupEndMinX, y = 0f)
            lineTo(x = peakEndMaxX, y = 0f)
            lineTo(x = offsetEndMaxX, y = canvasHeightOuter)
            // path bottom back
            val offsetEndMinX =
                (fullTimeline.onset.min + fullTimeline.comeup.min + fullTimeline.peak.min + fullTimeline.offset.min).inWholeSeconds * pixelsPerSec
            val peakEndMinX =
                (fullTimeline.onset.min + fullTimeline.comeup.min + fullTimeline.peak.min).inWholeSeconds * pixelsPerSec
            val comeupEndMaxX =
                (fullTimeline.onset.max + fullTimeline.comeup.max).inWholeSeconds * pixelsPerSec
            val onsetStartMaxX = fullTimeline.onset.max.inWholeSeconds * pixelsPerSec
            lineTo(x = offsetEndMinX, y = canvasHeightOuter)
            lineTo(x = peakEndMinX, y = 0f)
            lineTo(x = comeupEndMaxX, y = 0f)
            lineTo(x = onsetStartMaxX, y = canvasHeightOuter)
            close()
        }
        drawPath(
            path = fillPath,
            color = color.copy(alpha = 0.1f)
        )
    }
}


@Composable
fun RoaDurationTotalTimeline(
    totalTimeline: TotalTimeline,
    color: Color = Color.Blue,
    strokeWidth: Float = 5f,
    percentSmoothness: Float = 0.5f,
    modifier: Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasHeightOuter = size.height
        val canvasWidth = size.width
        val pixelsPerSec = canvasWidth / totalTimeline.total.max.inWholeSeconds
        val weight = 0.5
        inset(vertical = strokeWidth / 2) {
            val canvasHeightInner = size.height
            val strokePath = Path().apply {
                val totalMinX = (totalTimeline.total.min.inWholeSeconds) * pixelsPerSec
                val totalX = totalTimeline.total.interpolateAt(weight).inWholeSeconds * pixelsPerSec
                moveTo(0f, canvasHeightInner)
                endSmoothLineTo(
                    percentSmoothness = percentSmoothness,
                    startX = 0f,
                    endX = totalMinX / 2,
                    endY = 0f
                )
                startSmoothLineTo(
                    percentSmoothness = percentSmoothness,
                    startX = totalMinX / 2,
                    startY = 0f,
                    endX = totalX,
                    endY = canvasHeightInner
                )
            }
            drawPath(
                path = strokePath,
                color = color,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 15f))
                )
            )
        }
        val fillPath = Path().apply {
            // path over top
            val totalMinX = (totalTimeline.total.min.inWholeSeconds) * pixelsPerSec
            val totalMaxX = (totalTimeline.total.max.inWholeSeconds) * pixelsPerSec
            moveTo(x = totalMinX / 2, y = 0f)
            startSmoothLineTo(
                percentSmoothness = percentSmoothness,
                startX = totalMinX / 2,
                startY = 0f,
                endX = totalMaxX,
                endY = canvasHeightOuter
            )
            lineTo(x = totalMaxX, y = canvasHeightOuter)
            // path bottom back
            lineTo(x = totalMinX, y = canvasHeightOuter)
            endSmoothLineTo(percentSmoothness = percentSmoothness, startX = totalMinX,totalMinX/2, endY = 0f)
            close()
        }
        drawPath(
            path = fillPath,
            color = color.copy(alpha = 0.1f)
        )
    }
}

