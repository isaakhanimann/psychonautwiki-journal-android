package com.example.healthassistant.ui.home.experience.timeline

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.experiences.entities.Ingestion
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.ui.previewproviders.TimelinesPreviewProvider
import kotlinx.coroutines.delay
import java.util.*


@Preview(showBackground = true)
@Composable
fun AllTimelinesPreview(
    @PreviewParameter(
        TimelinesPreviewProvider::class,
        limit = 1
    ) ingestionDurationPairs: List<Pair<Ingestion, RoaDuration?>>
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
    ingestionDurationPairs: List<Pair<Ingestion, RoaDuration?>>,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 5f,
) {
    if (ingestionDurationPairs.isEmpty()) {
        Text(text = "Insufficient Data for Timeline")
    } else {
        val model: AllTimelinesModel = remember(ingestionDurationPairs) {
            AllTimelinesModel(ingestionDurationPairs)
        }
        val isDarkTheme = isSystemInDarkTheme()
        val density = LocalDensity.current
        val textPaint = remember(density) {
            Paint().apply {
                color =
                    if (isDarkTheme) android.graphics.Color.WHITE else android.graphics.Color.BLACK
                textAlign = Paint.Align.CENTER
                textSize = density.run { 40f }
            }
        }
        var currentTime by remember {
            mutableStateOf(Date())
        }
        LaunchedEffect(key1 = currentTime) {
            val oneSec = 1000L
            delay(oneSec)
            currentTime = Date()
        }
        Canvas(modifier = modifier) {
            val canvasWithLabelsHeight = size.height
            val canvasWidth = size.width
            val pixelsPerSec = canvasWidth / model.width.inWholeSeconds
            inset(left = 0f, top = 0f, right = 0f, bottom = 50f) {
                val canvasHeightOuter = size.height
                model.ingestionDrawables.forEach { ingestionDrawable ->
                    val color = ingestionDrawable.color.getComposeColor(isDarkTheme)
                    val startX =
                        ingestionDrawable.ingestionPointDistanceFromStart.inWholeSeconds * pixelsPerSec
                    val verticalInsetForLine = strokeWidth / 2
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
                                height = canvasHeightOuter,
                                startX = startX
                            ),
                            color = color.copy(alpha = 0.1f)
                        )
                    }
                }
                val endTime = Date(model.startTime.time + model.width.inWholeMilliseconds)
                if (model.startTime.before(currentTime) && endTime.after(currentTime)) {
                    val timeStartInSec = (currentTime.time - model.startTime.time) / 1000
                    val timeStartX = timeStartInSec * pixelsPerSec
                    drawLine(
                        color = if (isDarkTheme) Color.White else Color.Black,
                        start = Offset(x = timeStartX, y = canvasHeightOuter),
                        end = Offset(x = timeStartX, y = 0f),
                        strokeWidth = 5f,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(20f, 10f)
                        )
                    )
                }
            }
            drawContext.canvas.nativeCanvas.apply {
                val fullHours = model.axisDrawable.getFullHours(
                    pixelsPerSec = pixelsPerSec,
                    widthInPixels = canvasWidth
                )
                fullHours.forEach { fullHour ->
                    drawText(
                        fullHour.label,
                        fullHour.distanceFromStart,
                        canvasWithLabelsHeight,
                        textPaint
                    )
                }
            }
        }
    }
}