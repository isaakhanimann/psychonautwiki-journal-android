/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.journal.experience.timeline

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.ShulginRatingOption
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.DataForOneEffectLine
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.TimeDisplayOption
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.getDurationText
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.AxisDrawable
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.TimeRangeDrawable
import com.isaakhanimann.journal.ui.utils.getShortTimeText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.math.max


@Preview(showBackground = true)
@Composable
fun AllTimelinesPreview(
    @PreviewParameter(
        TimelinesPreviewProvider::class,
    ) dataForEffectLines: List<DataForOneEffectLine>
) {
    AllTimelines(
        dataForEffectLines = dataForEffectLines,
        dataForRatings = listOf(
            DataForOneRating(
                time = Instant.now().minus(3, ChronoUnit.HOURS),
                option = ShulginRatingOption.MINUS
            ),
            DataForOneRating(
                time = Instant.now().minus(2, ChronoUnit.HOURS),
                option = ShulginRatingOption.TWO_PLUS
            ),
            DataForOneRating(
                time = Instant.now().minus(1, ChronoUnit.HOURS),
                option = ShulginRatingOption.THREE_PLUS
            ),
            DataForOneRating(
                time = Instant.now().plus(2, ChronoUnit.HOURS),
                option = ShulginRatingOption.FOUR_PLUS
            )
        ),
        dataForTimedNotes = listOf(
            DataForOneTimedNote(
                time = Instant.now().minus(30, ChronoUnit.MINUTES),
                color = AdaptiveColor.PURPLE
            ),
            DataForOneTimedNote(
                time = Instant.now().plus(30, ChronoUnit.MINUTES),
                color = AdaptiveColor.BLUE
            ),
        ),
        isShowingCurrentTime = true,
        timeDisplayOption = TimeDisplayOption.REGULAR,
        areSubstanceHeightsIndependent = false,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}


@Composable
fun AllTimelines(
    dataForEffectLines: List<DataForOneEffectLine>,
    dataForRatings: List<DataForOneRating>,
    dataForTimedNotes: List<DataForOneTimedNote>,
    isShowingCurrentTime: Boolean,
    timeDisplayOption: TimeDisplayOption,
    areSubstanceHeightsIndependent: Boolean,
    modifier: Modifier = Modifier,
) {
    if (dataForEffectLines.isEmpty()) {
        Text(text = "Insufficient data for timeline")
    } else {
        val scope = rememberCoroutineScope()
        var timelineModel by remember { mutableStateOf<AllTimelinesModel?>(null) }

        LaunchedEffect(dataForEffectLines, dataForRatings) {
            scope.launch {
                val computedModel = withContext(Dispatchers.Default) {
                    AllTimelinesModel(
                        dataForLines = dataForEffectLines,
                        dataForRatings = dataForRatings,
                        timedNotes = dataForTimedNotes,
                        areSubstanceHeightsIndependent = areSubstanceHeightsIndependent
                    )
                }
                timelineModel = computedModel
            }
        }
        val model = timelineModel
        if (model != null) {
            val isDarkTheme = isSystemInDarkTheme()
            val density = LocalDensity.current
            val axisLabelSize = MaterialTheme.typography.labelMedium.fontSize
            val axisLabelTextPaint = remember(density) {
                Paint().apply {
                    color =
                        if (isDarkTheme) android.graphics.Color.WHITE else android.graphics.Color.BLACK
                    textAlign = Paint.Align.CENTER
                    textSize = density.run { axisLabelSize.toPx() }
                }
            }

            val dragTimeTextSize = MaterialTheme.typography.titleMedium
            val textMeasurer = rememberTextMeasurer()
            val ratingSize = MaterialTheme.typography.labelLarge.fontSize
            val ratingTextPaint = remember(density) {
                Paint().apply {
                    color =
                        if (isDarkTheme) android.graphics.Color.WHITE else android.graphics.Color.BLACK
                    textAlign = Paint.Align.CENTER
                    textSize = density.run { ratingSize.toPx() }
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
            var dragPoint by remember { mutableStateOf<Offset?>(null) }
            val verticalDistanceFromFinger = LocalDensity.current.run { 60.dp.toPx() }

            Canvas(modifier = modifier.pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { change, _ ->
                        change.consume()
                        dragPoint = change.position
                    },
                    onDragEnd = {
                        dragPoint = null
                    },
                    onDragCancel = {
                        dragPoint = null
                    }
                )
            }) {
                val canvasWithLabelsHeight = size.height
                val labelsHeight = axisLabelSize.toPx()
                val canvasWidth = size.width
                val pixelsPerSec = canvasWidth / model.widthInSeconds

                inset(left = 0f, top = 0f, right = 0f, bottom = labelsHeight + strokeWidth) {
                    val canvasHeightWithVerticalLine = size.height
                    model.groupDrawables.forEach { group ->
                        group.drawTimeLine(
                            drawScope = this,
                            canvasHeight = canvasHeightWithVerticalLine,
                            pixelsPerSec = pixelsPerSec,
                            color = group.color.getComposeColor(isDarkTheme),
                            density = density
                        )
                    }
                    dataForRatings.forEach { dataForOneRating ->
                        drawRating(
                            startTime = model.startTime,
                            ratingTime = dataForOneRating.time,
                            pixelsPerSec = pixelsPerSec,
                            canvasHeightOuter = canvasHeightWithVerticalLine,
                            rating = dataForOneRating.option,
                            textPaint = ratingTextPaint
                        )
                    }
                    dataForTimedNotes.forEach { dataForOneTimedNote ->
                        drawTimedNote(
                            startTime = model.startTime,
                            noteTime = dataForOneTimedNote.time,
                            color = dataForOneTimedNote.color,
                            pixelsPerSec = pixelsPerSec,
                            canvasHeightOuter = canvasHeightWithVerticalLine,
                            isDarkTheme = isDarkTheme
                        )
                    }
                    if (isShowingCurrentTime) {
                        drawCurrentTime(
                            startTime = model.startTime,
                            timelineWidthInSeconds = model.widthInSeconds,
                            currentTime = currentTime,
                            pixelsPerSec = pixelsPerSec,
                            isDarkTheme = isDarkTheme,
                            canvasHeightOuter = canvasHeightWithVerticalLine,
                        )
                    }
                    dragPoint?.let {
                        drawDragPointLineAndTimeLabel(
                            it,
                            canvasWidth,
                            isDarkTheme,
                            canvasHeightWithVerticalLine,
                            pixelsPerSec,
                            model,
                            verticalDistanceFromFinger,
                            textMeasurer,
                            dragTimeTextSize,
                            timeDisplayOption
                        )
                    }
                }
                drawAxis(
                    axisDrawable = model.axisDrawable,
                    pixelsPerSec = pixelsPerSec,
                    canvasWidth = canvasWidth,
                    canvasHeight = canvasWithLabelsHeight,
                    textPaint = axisLabelTextPaint
                )
            }
        } else {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}

private fun DrawScope.drawDragPointLineAndTimeLabel(
    dragPoint: Offset,
    canvasWidth: Float,
    isDarkTheme: Boolean,
    canvasHeightWithVerticalLine: Float,
    pixelsPerSec: Float,
    model: AllTimelinesModel,
    dragPointToTextVerticalDistance: Float,
    textMeasurer: TextMeasurer,
    dragTimeTextSize: TextStyle,
    timeDisplayOption: TimeDisplayOption,
) {
    val horizontallyLimitedDragPoint = Offset(
        x = dragPoint.x.coerceIn(0f, canvasWidth),
        y = dragPoint.y
    )
    val dragLineColor = if (isDarkTheme) Color.White else Color.Black
    val textColor = if (isDarkTheme) Color.Black else Color.White
    drawVerticalDragLine(dragLineColor, horizontallyLimitedDragPoint, canvasHeightWithVerticalLine)

    drawDragTimeLabelWithBackground(
        horizontallyLimitedDragPoint,
        pixelsPerSec,
        model,
        dragPointToTextVerticalDistance,
        textMeasurer,
        canvasWidth,
        canvasHeightWithVerticalLine,
        dragLineColor,
        dragTimeTextSize,
        textColor,
        timeDisplayOption
    )
}

private fun DrawScope.drawDragTimeLabelWithBackground(
    horizontallyLimitedDragPoint: Offset,
    pixelsPerSec: Float,
    model: AllTimelinesModel,
    dragPointToTextVerticalDistance: Float,
    textMeasurer: TextMeasurer,
    canvasWidth: Float,
    canvasHeightWithVerticalLine: Float,
    dragLineColor: Color,
    dragTimeTextSize: TextStyle,
    textColor: Color,
    timeDisplayOption: TimeDisplayOption,
) {
    val secondsAtDragPoint = horizontallyLimitedDragPoint.x / pixelsPerSec
    val timeAtDragPoint = model.startTime.plusSeconds(secondsAtDragPoint.toLong())
    val textHeight = max(0f, horizontallyLimitedDragPoint.y - dragPointToTextVerticalDistance)
    val timeLabel = when (timeDisplayOption) {
        TimeDisplayOption.RELATIVE_TO_NOW -> {
            val now = Instant.now()
            val isInPast = timeAtDragPoint < now
            if (isInPast) {
                getDurationText(fromInstant = timeAtDragPoint, toInstant = now) + " ago"
            } else {
                "in " + getDurationText(fromInstant = timeAtDragPoint, toInstant = now)
            }
        }

        TimeDisplayOption.RELATIVE_TO_START -> {
            getDurationText(
                fromInstant = model.startTime,
                toInstant = timeAtDragPoint
            ) + " in"
        }

        TimeDisplayOption.TIME_BETWEEN -> timeAtDragPoint.getShortTimeText()
        TimeDisplayOption.REGULAR -> timeAtDragPoint.getShortTimeText()
    }
    val measuredText =
        textMeasurer.measure(
            timeLabel,
            style = TextStyle(fontSize = 18.sp)
        )
    val textSize = measuredText.size
    val rectSize = textSize.toSize().times(1.35f)
    val rectTopLeft = Offset(
        x = (horizontallyLimitedDragPoint.x - rectSize.width / 2).coerceIn(
            0f,
            canvasWidth - rectSize.width
        ),
        y = (textHeight - rectSize.height / 2).coerceIn(
            0f,
            canvasHeightWithVerticalLine - rectSize.height
        )
    )
    drawRoundRect(
        color = dragLineColor,
        size = rectSize,
        topLeft = rectTopLeft,
        cornerRadius = CornerRadius(x = 15f, y = 15f)
    )
    drawText(
        textMeasurer,
        text = timeLabel,
        topLeft = Offset(
            x = rectTopLeft.x + (rectSize.width - textSize.width) / 2,
            y = rectTopLeft.y + (rectSize.height - textSize.height) / 2
        ),
        style = dragTimeTextSize.copy(color = textColor),
    )
}

private fun DrawScope.drawVerticalDragLine(
    dragLineColor: Color,
    horizontallyLimitedDragPoint: Offset,
    canvasHeightWithVerticalLine: Float
) {
    drawLine(
        color = dragLineColor,
        start = Offset(x = horizontallyLimitedDragPoint.x, y = canvasHeightWithVerticalLine),
        end = Offset(x = horizontallyLimitedDragPoint.x, y = 0f),
        strokeWidth = 4.dp.toPx(),
        cap = StrokeCap.Round
    )
}

fun DrawScope.drawCurrentTime(
    startTime: Instant,
    timelineWidthInSeconds: Float,
    currentTime: Instant,
    pixelsPerSec: Float,
    isDarkTheme: Boolean,
    canvasHeightOuter: Float,
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
            strokeWidth = 4.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

fun DrawScope.drawTimeRange(
    timeRangeDrawable: TimeRangeDrawable,
    canvasHeight: Float,
    pixelsPerSec: Float,
    color: Color,
    density: Density,
) {
    val startX = timeRangeDrawable.ingestionStartInSeconds * pixelsPerSec
    val endX = timeRangeDrawable.ingestionEndInSeconds * pixelsPerSec
    val minLineHeight = 12.dp.toPx()
    val horizontalLineWidth = 8.dp.toPx()
    val offset = timeRangeDrawable.intersectionCountWithPreviousRanges * horizontalLineWidth
    val horizontalLineHeight = minLineHeight / 2 + offset
    val verticalLineHeight = minLineHeight + offset
    val verticalLineTopY = canvasHeight - verticalLineHeight
    val horizontalLineY = canvasHeight - horizontalLineHeight
    val verticalLineStrokeWidth = 4.dp.toPx()
    drawLine(
        color = color,
        start = Offset(x = startX, y = verticalLineTopY),
        end = Offset(x = startX, y = canvasHeight),
        strokeWidth = verticalLineStrokeWidth,
        cap = StrokeCap.Round
    )
    drawLine(
        color = color,
        start = Offset(x = startX, y = horizontalLineY),
        end = Offset(x = endX, y = horizontalLineY),
        strokeWidth = horizontalLineWidth,
        cap = StrokeCap.Butt
    )
    drawLine(
        color = color,
        start = Offset(x = endX, y = verticalLineTopY),
        end = Offset(x = endX, y = canvasHeight),
        strokeWidth = verticalLineStrokeWidth,
        cap = StrokeCap.Round
    )
    val convolutionResultModel = timeRangeDrawable.convolutionResultModel
    if (convolutionResultModel != null) {
        val comeupStartX = convolutionResultModel.comeupStartXInSeconds * pixelsPerSec
        val peakStartX = convolutionResultModel.peakStartXInSeconds * pixelsPerSec
        val heightInPx = convolutionResultModel.height * canvasHeight
        val top = canvasHeight - heightInPx
        val offsetStartX = convolutionResultModel.offsetStartXInSeconds * pixelsPerSec
        val offsetEndX = convolutionResultModel.offsetEndXInSeconds * pixelsPerSec

        val bottom = canvasHeight + strokeWidth / 2
        val path = Path().apply {
            moveTo(x = startX, y = horizontalLineY)
            lineTo(x = comeupStartX, y = horizontalLineY)
            lineTo(x = peakStartX, y = top)
            lineTo(x = offsetStartX, y = top)
            lineTo(x = offsetEndX, y = bottom)
        }
        drawPath(
            path = path,
            color = color,
            style = density.normalStroke
        )
        path.lineTo(x = startX, y = bottom)
        path.close()
        drawPath(
            path = path,
            color = color.copy(alpha = shapeAlpha)
        )
    }
}

fun DrawScope.drawRating(
    startTime: Instant,
    ratingTime: Instant,
    pixelsPerSec: Float,
    canvasHeightOuter: Float,
    rating: ShulginRatingOption,
    textPaint: Paint
) {
    val timeStartInSec = Duration.between(startTime, ratingTime).seconds
    val timeStartX = timeStartInSec * pixelsPerSec
    val lineHeight = textPaint.textSize
    val lines = rating.verticalSign.split("\n")
    val signHeight = lines.size * lineHeight
    val verticalLineHeight = (canvasHeightOuter - 2 * lineHeight - signHeight) / 2
    drawLine(
        color = Color.Gray,
        start = Offset(x = timeStartX, y = 0f),
        end = Offset(x = timeStartX, y = verticalLineHeight),
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round
    )
    var y = verticalLineHeight + 1.5f * lineHeight
    drawContext.canvas.nativeCanvas.apply {
        for (line in lines) {
            drawText(
                line,
                timeStartX,
                y,
                textPaint
            )
            y += lineHeight
        }
    }
    drawLine(
        color = Color.Gray,
        start = Offset(x = timeStartX, y = y),
        end = Offset(x = timeStartX, y = canvasHeightOuter),
        strokeWidth = 4.dp.toPx(),
        cap = StrokeCap.Round
    )
}

fun DrawScope.drawTimedNote(
    startTime: Instant,
    noteTime: Instant,
    pixelsPerSec: Float,
    canvasHeightOuter: Float,
    color: AdaptiveColor,
    isDarkTheme: Boolean
) {
    val timeStartInSec = Duration.between(startTime, noteTime).seconds
    val timeStartX = timeStartInSec * pixelsPerSec
    val strokeWidth = 3.dp.toPx()
    drawLine(
        color = color.getComposeColor(isDarkTheme = isDarkTheme),
        start = Offset(x = timeStartX, y = 0f),
        end = Offset(x = timeStartX, y = canvasHeightOuter),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(strokeWidth, strokeWidth * 2))
    )
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
