package com.isaakhanimann.healthassistant.ui.journal.experience.timeline

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.healthassistant.data.substances.classes.roa.RoaDuration
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant


@Preview(showBackground = true)
@Composable
fun AllTimelinesPreview(
    @PreviewParameter(
        TimelinesPreviewProvider::class,
    ) ingestionDurationPairs: List<Pair<IngestionWithCompanion, RoaDuration?>>
) {
    AllTimelines(
        ingestionDurationPairs = ingestionDurationPairs,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}


@Composable
fun AllTimelines(
    ingestionDurationPairs: List<Pair<IngestionWithCompanion, RoaDuration?>>,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 10f,
) {
    if (ingestionDurationPairs.isEmpty()) {
        Text(text = "Insufficient Data for Timeline")
    } else {
        val model: AllTimelinesModel = remember(ingestionDurationPairs) {
            AllTimelinesModel(ingestionDurationPairs)
        }
        val isDarkTheme = isSystemInDarkTheme()
        val density = LocalDensity.current
        val labelSize = MaterialTheme.typography.body1.fontSize
        val textPaint = remember(density) {
            Paint().apply {
                color =
                    if (isDarkTheme) android.graphics.Color.WHITE else android.graphics.Color.BLACK
                textAlign = Paint.Align.CENTER
                textSize = density.run { labelSize.toPx() }
            }
        }
        var currentTime by remember {
            mutableStateOf(Instant.now())
        }
        LaunchedEffect(key1 = currentTime) {
            val oneSec = 1000L
            delay(oneSec)
            currentTime = Instant.now()
        }
        Canvas(modifier = modifier) {
            val canvasWithLabelsHeight = size.height
            val labelsHeight = labelSize.toPx() + 5f
            val canvasWidth = size.width
            val pixelsPerSec = canvasWidth / model.widthInSeconds
            inset(left = 0f, top = 0f, right = 0f, bottom = labelsHeight) {
                val canvasHeightOuter = size.height
                model.ingestionDrawables.forEach { ingestionDrawable ->
                    drawIngestion(
                        ingestionDrawable = ingestionDrawable,
                        isDarkTheme = isDarkTheme,
                        pixelsPerSec = pixelsPerSec,
                        strokeWidth = strokeWidth,
                        canvasHeightOuter = canvasHeightOuter
                    )
                }
                drawCurrentTime(
                    startTime = model.startTime,
                    timelineWidthInSeconds = model.widthInSeconds,
                    currentTime = currentTime,
                    pixelsPerSec = pixelsPerSec,
                    isDarkTheme = isDarkTheme,
                    canvasHeightOuter = canvasHeightOuter
                )
            }
            drawAxis(
                axisDrawable = model.axisDrawable,
                pixelsPerSec = pixelsPerSec,
                canvasWidth = canvasWidth,
                canvasHeight = canvasWithLabelsHeight,
                textPaint = textPaint
            )
        }
    }
}

fun DrawScope.drawIngestion(
    ingestionDrawable: IngestionDrawable,
    isDarkTheme: Boolean,
    pixelsPerSec: Float,
    strokeWidth: Float,
    canvasHeightOuter: Float
) {
    val color = ingestionDrawable.color.getComposeColor(isDarkTheme)
    val startX =
        ingestionDrawable.ingestionPointDistanceFromStartInSeconds * pixelsPerSec
    val verticalInsetForLine = strokeWidth / 2
    val topInset =
        (canvasHeightOuter * (1f - ingestionDrawable.verticalHeightInPercent)) + (ingestionDrawable.insetTimes * strokeWidth)
    inset(
        left = 0f,
        top = topInset,
        right = 0f,
        bottom = 0f
    ) {
        val ingestionHeight = size.height
        inset(vertical = verticalInsetForLine) {
            val canvasHeightInner = size.height
            drawCircle(
                color = color,
                radius = 10f,
                center = Offset(x = startX, y = canvasHeightInner)
            )
        }
        ingestionDrawable.timelineDrawable?.let { timelineDrawable ->
            inset(vertical = verticalInsetForLine) {
                val canvasHeightInner = size.height
                drawPath(
                    path = timelineDrawable.getStrokePath(
                        pixelsPerSec = pixelsPerSec,
                        height = canvasHeightInner,
                        startX = startX
                    ),
                    color = color,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                        pathEffect = if (timelineDrawable.isDotted) PathEffect.dashPathEffect(
                            floatArrayOf(20f, 30f)
                        ) else null
                    )
                )
            }
            drawPath(
                path = timelineDrawable.getFillPath(
                    pixelsPerSec = pixelsPerSec,
                    height = ingestionHeight,
                    startX = startX
                ),
                color = color.copy(alpha = 0.1f)
            )
        }
    }
}

fun DrawScope.drawCurrentTime(
    startTime: Instant,
    timelineWidthInSeconds: Float,
    currentTime: Instant,
    pixelsPerSec: Float,
    isDarkTheme: Boolean,
    canvasHeightOuter: Float
) {
    val endTime = startTime.plusSeconds(timelineWidthInSeconds.toLong())
    if (startTime.isBefore(currentTime) && endTime.isAfter(currentTime)) {
        val timeStartInSec = Duration.between(startTime, currentTime).seconds
        val timeStartX = timeStartInSec * pixelsPerSec
        val color = if (isDarkTheme) Color.White else Color.Black
        drawLine(
            color = color,
            start = Offset(x = timeStartX, y = canvasHeightOuter),
            end = Offset(x = timeStartX, y = 0f),
            strokeWidth = 5f,
            cap = StrokeCap.Round
        )
    }
}

fun DrawScope.drawAxis(
    axisDrawable: AxisDrawable,
    pixelsPerSec: Float,
    canvasWidth: Float,
    canvasHeight: Float,
    textPaint: Paint
) {
    val fullHours = axisDrawable.getFullHours(
        pixelsPerSec = pixelsPerSec,
        widthInPixels = canvasWidth
    )
    drawContext.canvas.nativeCanvas.apply {
        fullHours.forEach { fullHour ->
            drawText(
                fullHour.label,
                fullHour.distanceFromStart,
                canvasHeight,
                textPaint
            )
        }
    }
}
