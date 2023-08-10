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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.ShulginRatingOption
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.DataForOneEffectLine
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.AxisDrawable
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.GroupDrawable
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit


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
        navigateToExplainTimeline = {},
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
    navigateToExplainTimeline: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (dataForEffectLines.isEmpty()) {
        Text(text = "Insufficient Data for Timeline")
    } else {
        val model: AllTimelinesModel = remember(dataForEffectLines, dataForRatings) {
            AllTimelinesModel(dataForEffectLines, dataForRatings)
        }
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
        Box(contentAlignment = Alignment.TopEnd) {
            Canvas(modifier = modifier) {
                val canvasWithLabelsHeight = size.height
                val labelsHeight = axisLabelSize.toPx()
                val canvasWidth = size.width
                val pixelsPerSec = canvasWidth / model.widthInSeconds
                inset(left = 0f, top = 0f, right = 0f, bottom = labelsHeight) {
                    val canvasHeightWithVerticalLine = size.height
                    model.groupDrawables.forEach { group ->
                        drawGroup(
                            groupDrawable = group,
                            isDarkTheme = isDarkTheme,
                            pixelsPerSec = pixelsPerSec,
                            canvasHeightOuter = canvasHeightWithVerticalLine,
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
                }
                drawAxis(
                    axisDrawable = model.axisDrawable,
                    pixelsPerSec = pixelsPerSec,
                    canvasWidth = canvasWidth,
                    canvasHeight = canvasWithLabelsHeight,
                    textPaint = axisLabelTextPaint
                )
            }
            IconButton(onClick = navigateToExplainTimeline) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Timeline Disclaimer"
                )
            }
        }
    }
}

fun DrawScope.drawGroup(
    groupDrawable: GroupDrawable,
    isDarkTheme: Boolean,
    pixelsPerSec: Float,
    canvasHeightOuter: Float,
    density: Density
) {
    val color = groupDrawable.color.getComposeColor(isDarkTheme)
    for (drawable in groupDrawable.timelineDrawables) {
        drawable.drawTimeLine(
            drawScope = this,
            height = canvasHeightOuter,
            startX = 0f,
            pixelsPerSec = pixelsPerSec,
            color = color,
            density = density
        )
    }
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
        strokeWidth = 4.dp.toPx(),
        cap = StrokeCap.Round
    )
    var y = verticalLineHeight + 1.5f*lineHeight
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
