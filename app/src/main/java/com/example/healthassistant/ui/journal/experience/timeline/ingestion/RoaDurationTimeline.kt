package com.example.healthassistant.ui.journal.experience.timeline.ingestion

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
    strokeWidth: Float = 5f,
    modifier: Modifier
) {
    val timelineDrawable: TimelineDrawable? = remember(roaDuration) {
        val full = roaDuration.toFullTimeline()
        val total = roaDuration.toTotalTimeline()
        full ?: total
    }
    if (timelineDrawable != null) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val canvasHeightOuter = size.height
            val canvasWidth = size.width
            val pixelsPerSec = canvasWidth / timelineDrawable.width.inWholeSeconds
            inset(vertical = strokeWidth / 2) {
                val canvasHeightInner = size.height
                drawPath(
                    path = timelineDrawable.getStrokePath(
                        pixelsPerSec = pixelsPerSec,
                        height = canvasHeightInner,
                        startX = 0f
                    ),
                    color = color,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                        pathEffect = if (timelineDrawable.isDotted) PathEffect.dashPathEffect(
                            floatArrayOf(10f, 15f)
                        ) else null
                    )
                )
            }
            drawPath(
                path = timelineDrawable.getFillPath(
                    pixelsPerSec = pixelsPerSec,
                    height = canvasHeightOuter,
                    startX = 0f
                ),
                color = color.copy(alpha = 0.1f)
            )
        }
    } else {
        Text(text = "There can be no timeline drawn")
    }
}

