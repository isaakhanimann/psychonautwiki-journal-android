package com.example.healthassistant.ui.home.experience.timeline.ingestion

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.ui.previewproviders.RoaDurationPreviewProvider

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

@Composable
fun RoaDurationFullTimeline(
    fullTimeline: FullTimeline,
    color: Color,
    strokeWidth: Float = 5f,
    modifier: Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasHeightOuter = size.height
        val canvasWidth = size.width
        val pixelsPerSec = canvasWidth / fullTimeline.totalMax.inWholeSeconds
        inset(vertical = strokeWidth / 2) {
            val canvasHeightInner = size.height
            drawPath(
                path = fullTimeline.getStrokePath(
                    pixelsPerSec = pixelsPerSec,
                    height = canvasHeightInner
                ),
                color = color,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )
        }
        drawPath(
            path = fullTimeline.getFillPath(
                pixelsPerSec = pixelsPerSec,
                height = canvasHeightOuter
            ),
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
        inset(vertical = strokeWidth / 2) {
            val canvasHeightInner = size.height
            drawPath(
                path = totalTimeline.getStrokePath(pixelsPerSec, canvasHeightInner),
                color = color,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 15f))
                )
            )
        }
        drawPath(
            path = totalTimeline.getFillPath(pixelsPerSec, canvasHeightOuter),
            color = color.copy(alpha = 0.1f)
        )
    }
}

