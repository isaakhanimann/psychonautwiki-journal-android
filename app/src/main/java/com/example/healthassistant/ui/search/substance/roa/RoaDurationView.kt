package com.example.healthassistant.ui.search.substance.roa

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.ui.previewproviders.RoaDurationPreviewProvider
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Preview(showBackground = true)
@Composable
fun RoaDurationPreview(
    @PreviewParameter(RoaDurationPreviewProvider::class, limit = 1) roaDuration: RoaDuration
) {
    RoaDurationView(roaDuration = roaDuration, maxDuration = 13.toDuration(DurationUnit.HOURS))
}

@Composable
fun RoaDurationView(
    roaDuration: RoaDuration,
    maxDuration: Duration?
) {
    Column {
        val total = roaDuration.total
        val color = MaterialTheme.colors.secondary
        if ((total?.min != null) && (total.max != null)) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "total: ${total.text}",
                    textAlign = TextAlign.Center
                )
                Canvas(modifier = Modifier.fillMaxWidth()) {
                    val canvasWidth = size.width
                    val max = maxDuration ?: total.max
                    val minX = (total.min.div(max) * canvasWidth).toFloat()
                    val maxX = (total.max.div(max) * canvasWidth).toFloat()
                    val strokeWidth = 12f
                    drawLine(
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = maxX, y = 0f),
                        color = color.copy(alpha = 0.3f),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = minX, y = 0f),
                        color = color,
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        roaDuration.afterglow?.let {
            Text("after effects: ${it.text}")
        }
        val onset = roaDuration.onset
        val comeup = roaDuration.comeup
        val peak = roaDuration.peak
        val offset = roaDuration.offset
        val onsetInterpol = roaDuration.onset?.interpolateAt(0.5)
        val comeupInterpol = roaDuration.comeup?.interpolateAt(0.5)
        val peakInterpol = roaDuration.peak?.interpolateAt(0.5)
        val offsetInterpol = roaDuration.offset?.interpolateAt(0.5)
        val allDurations = listOf(onsetInterpol, comeupInterpol, peakInterpol, offsetInterpol)
        val undefinedCount = allDurations.count { it == null }
        if (maxDuration != null && undefinedCount < 4) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                val strokeWidth = 8f
                val canvasWidth = size.width
                val pixelsPerSec = canvasWidth.div(maxDuration.inWholeSeconds)
                val canvasHeightOuter = size.height
                if (onsetInterpol != null && comeupInterpol != null && peakInterpol != null && offsetInterpol != null && onset?.min != null && comeup?.min != null && peak?.min != null && offset?.min != null && onset.max != null && comeup.max != null && peak.max != null && offset.max != null) {
                    inset(vertical = strokeWidth / 2) {
                        val canvasHeight = size.height
                        val start1 = onsetInterpol.inWholeSeconds * pixelsPerSec
                        drawLine(
                            start = Offset(x = 0f, y = canvasHeight),
                            end = Offset(x = start1, y = canvasHeight),
                            color = color,
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                        val start2 = start1 + (comeupInterpol.inWholeSeconds * pixelsPerSec)
                        drawLine(
                            start = Offset(x = start1, y = canvasHeight),
                            end = Offset(x = start2, y = 0f),
                            color = color,
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                        val start3 = start2 + (peakInterpol.inWholeSeconds * pixelsPerSec)
                        drawLine(
                            start = Offset(x = start2, y = 0f),
                            end = Offset(x = start3, y = 0f),
                            color = color,
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                        val start4 = start3 + (offsetInterpol.inWholeSeconds * pixelsPerSec)
                        drawLine(
                            start = Offset(x = start3, y = 0f),
                            end = Offset(x = start4, y = canvasHeight),
                            color = color,
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    }
                    val path = Path().apply {
                        // path over top
                        val onsetStartMinX = onset.min.inWholeSeconds * pixelsPerSec
                        val comeupEndMinX =
                            onsetStartMinX + (comeup.min.inWholeSeconds * pixelsPerSec)
                        val peakEndMaxX =
                            (onset.max + comeup.max + peak.max).inWholeSeconds * pixelsPerSec
                        val offsetEndMaxX =
                            peakEndMaxX + (offset.max.inWholeSeconds * pixelsPerSec)
                        moveTo(onsetStartMinX, canvasHeightOuter)
                        lineTo(x = comeupEndMinX, y = 0f)
                        lineTo(x = peakEndMaxX, y = 0f)
                        lineTo(x = offsetEndMaxX, y = canvasHeightOuter)
                        // path bottom back
                        val onsetStartMaxX = onset.max.inWholeSeconds * pixelsPerSec
                        val comeupEndMaxX =
                            onsetStartMaxX + (comeup.max.inWholeSeconds * pixelsPerSec)
                        val peakEndMinX =
                            (onset.min + comeup.min + peak.min).inWholeSeconds * pixelsPerSec
                        val offsetEndMinX =
                            peakEndMinX + (offset.min.inWholeSeconds * pixelsPerSec)
                        lineTo(x = offsetEndMinX, y = canvasHeightOuter)
                        lineTo(x = peakEndMinX, y = 0f)
                        lineTo(x = comeupEndMaxX, y = 0f)
                        lineTo(x = onsetStartMaxX, y = canvasHeightOuter)
                        close()
                    }
                    drawPath(
                        path = path,
                        color = color.copy(alpha = 0.1f)
                    )
                } else {
                    val sumDurations = allDurations.filterNotNull().reduce { acc, duration ->
                        acc + duration
                    }
                    val restDuration = maxDuration - sumDurations
                    val dottedLineWidths = restDuration.div(undefinedCount)
                    drawLine(
                        start = Offset(x = 0f, y = canvasHeightOuter),
                        end = Offset(x = canvasWidth, y = canvasHeightOuter),
                        color = color,
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

@Composable
fun LineLow(
    modifier: Modifier = Modifier,
    color: Color,
    strokeWidth: Float
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawLine(
            start = Offset(x = 0f, y = canvasHeight),
            end = Offset(x = canvasWidth, y = canvasHeight),
            color = color,
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun LineUp(
    modifier: Modifier = Modifier,
    color: Color,
    strokeWidth: Float
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val strokePath = Path().apply {
            val control1X = canvasWidth / 10f
            val control2X = canvasWidth * 9f / 10f
            val control2Y = 0f
            moveTo(0f, canvasHeight)
            cubicTo(control1X, canvasHeight, control2X, control2Y, canvasWidth, 0f)
        }
        val fillPath = android.graphics.Path(strokePath.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(canvasWidth, canvasHeight)
                lineTo(0f, canvasHeight)
                close()
            }
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    color,
                    Color.Transparent
                ),
                endY = canvasHeight
            )
        )
        drawPath(
            path = strokePath,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )
    }
}

@Composable
fun LineHigh(
    modifier: Modifier = Modifier,
    color: Color,
    strokeWidth: Float
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val strokePath = Path().apply {
            moveTo(0f, 0f)
            lineTo(canvasWidth, 0f)
        }
        val fillPath = android.graphics.Path(strokePath.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(canvasWidth, canvasHeight)
                lineTo(0f, canvasHeight)
                close()
            }
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    color,
                    Color.Transparent
                ),
                endY = canvasHeight
            )
        )
        drawPath(
            path = strokePath,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )
    }
}

@Composable
fun LineDown(
    modifier: Modifier = Modifier,
    color: Color,
    strokeWidth: Float
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val strokePath = Path().apply {
            val control1X = canvasWidth / 10f
            val control2X = canvasWidth * 9f / 10f
            moveTo(0f, 0f)
            cubicTo(control1X, 0f, control2X, canvasHeight, canvasWidth, canvasHeight)
        }
        val fillPath = android.graphics.Path(strokePath.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(canvasWidth, canvasHeight)
                lineTo(0f, canvasHeight)
                close()
            }
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    color,
                    Color.Transparent
                ),
                endY = canvasHeight
            )
        )
        drawPath(
            path = strokePath,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )
    }
}
