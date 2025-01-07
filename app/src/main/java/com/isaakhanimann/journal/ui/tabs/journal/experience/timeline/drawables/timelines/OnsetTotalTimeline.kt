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

package com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.timelines

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.*
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.TimelineDrawable

data class OnsetTotalTimeline(
    val onset: FullDurationRange,
    val total: FullDurationRange,
    val totalWeight: Float,
    val ingestionTimeRelativeToStartInSeconds: Float,
    override val nonNormalisedHeight: Float,
    val nonNormalisedMaxOfRoute: Float,
) : TimelineDrawable {

    override var referenceHeight = 1f

    override val endOfLineRelativeToStartInSeconds: Float =
        ingestionTimeRelativeToStartInSeconds + total.maxInSeconds

    override fun drawTimeLine(
        drawScope: DrawScope,
        canvasHeight: Float,
        pixelsPerSec: Float,
        color: Color,
        density: Density
    ) {
        val normalisedHeight = nonNormalisedHeight / referenceHeight
        val heightInPx = normalisedHeight * canvasHeight
        val top = canvasHeight - heightInPx
        val onsetWeight = 0.5f
        val startX = ingestionTimeRelativeToStartInSeconds * pixelsPerSec
        val onsetEndX =
            startX + (onset.interpolateAtValueInSeconds(onsetWeight) * pixelsPerSec)
        drawScope.drawPath(
            path = Path().apply {
                moveTo(x = startX, y = canvasHeight)
                lineTo(x = onsetEndX, y = canvasHeight)
            },
            color = color,
            style = density.normalStroke
        )
        val totalX = total.interpolateAtValueInSeconds(totalWeight) * pixelsPerSec
        val path = Path().apply {
            moveTo(x = onsetEndX, y = canvasHeight)
            endSmoothLineTo(
                smoothnessBetween0And1 = 0.5f,
                startX = onsetEndX,
                endX = startX + (totalX / 2f),
                endY = top
            )
            startSmoothLineTo(
                smoothnessBetween0And1 = 0.5f,
                startX = startX + (totalX / 2f),
                startY = top,
                endX = startX + totalX,
                endY = canvasHeight
            )
        }
        drawScope.drawPath(
            path = path,
            color = color,
            style = density.dottedStroke
        )
        path.lineTo(x = startX + totalX, y = canvasHeight + drawScope.strokeWidth / 2)
        path.lineTo(x = startX, y = canvasHeight + drawScope.strokeWidth / 2)
        path.close()
        drawScope.drawPath(
            path = path,
            color = color.copy(alpha = shapeAlpha)
        )
        drawScope.drawCircle(
            color = color,
            radius = density.ingestionDotRadius,
            center = Offset(x = ingestionTimeRelativeToStartInSeconds * pixelsPerSec, y = canvasHeight)
        )
    }
}

fun RoaDuration.toOnsetTotalTimeline(
    totalWeight: Float,
    ingestionTimeRelativeToStartInSeconds: Float,
    nonNormalisedHeight: Float,
    nonNormalisedMaxOfRoute: Float,
): OnsetTotalTimeline? {
    val fullOnset = onset?.toFullDurationRange()
    val fullTotal = total?.toFullDurationRange()
    return if (fullOnset != null && fullTotal != null) {
        OnsetTotalTimeline(
            onset = fullOnset,
            total = fullTotal,
            totalWeight = totalWeight,
            ingestionTimeRelativeToStartInSeconds = ingestionTimeRelativeToStartInSeconds,
            nonNormalisedHeight = nonNormalisedHeight,
            nonNormalisedMaxOfRoute = nonNormalisedMaxOfRoute
        )
    } else {
        null
    }
}