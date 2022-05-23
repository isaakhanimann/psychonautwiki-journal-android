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
    @PreviewParameter(RoaDurationPreviewProvider::class, limit = 1) roaDuration: RoaDuration,
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
        RoaDurationFullTimeline(fullTimeline = fullTimeline, modifier = modifier)
    } else {
        val totalTimeline = remember(roaDuration) {
            roaDuration.toTotalTimeline()
        }
        if (totalTimeline != null) {
            RoaDurationTotalTimeline(totalTimeline = totalTimeline, modifier = modifier)
        } else {
            Text(text = "There can be no timeline drawn")
        }
    }
}

@Composable
fun RoaDurationFullTimeline(
    fullTimeline: FullTimeline,
    color: Color = Color.Blue,
    modifier: Modifier
) {
    val strokeWidth = 5f
    Canvas(modifier = modifier.fillMaxSize()) {
        inset(vertical = strokeWidth/2) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val pixelsPerSec = canvasWidth / fullTimeline.totalMax.inWholeSeconds
            val strokePath = Path().apply {
                moveTo(0f, canvasHeight)
                val weight = 0.5
                val onsetEndX = fullTimeline.onset.interpolateAt(weight).inWholeSeconds * pixelsPerSec
                val comeupEndX = onsetEndX + (fullTimeline.comeup.interpolateAt(weight).inWholeSeconds * pixelsPerSec)
                val peakEndX = comeupEndX + (fullTimeline.peak.interpolateAt(weight).inWholeSeconds * pixelsPerSec)
                val offsetEndX = peakEndX + (fullTimeline.offset.interpolateAt(weight).inWholeSeconds * pixelsPerSec)
                lineTo(x = onsetEndX, y = canvasHeight)
                lineTo(x = comeupEndX, y = 0f)
                lineTo(x = peakEndX, y = 0f)
                lineTo(x = offsetEndX, y = canvasHeight)
            }
            drawPath(
                path = strokePath,
                color = color,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )
//        val fillPath = Path().apply {
//            lineTo(canvasWidth, canvasHeight)
//            lineTo(0f, canvasHeight)
//            close()
//        }
//        drawPath(
//            path = fillPath,
//            brush = Brush.verticalGradient(
//                colors = listOf(
//                    color,
//                    Color.Transparent
//                ),
//                endY = canvasHeight
//            )
//        )
        }

    }
}


@Composable
fun RoaDurationTotalTimeline(
    totalTimeline: TotalTimeline,
    modifier: Modifier
) {

}

