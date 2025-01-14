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

data class OnsetComeupTotalTimeline(
    val onset: FullDurationRange,
    val comeup: FullDurationRange,
    val total: FullDurationRange,
    val totalWeight: Float,
    val ingestionTimeRelativeToStartInSeconds: Float,
    override val nonNormalisedHeight: Float,
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
        val onsetAndComeupWeight = 0.5f
        val startX = ingestionTimeRelativeToStartInSeconds * pixelsPerSec
        val onsetEndX =
            startX + (onset.interpolateAtValueInSeconds(onsetAndComeupWeight) * pixelsPerSec)
        val comeupEndX =
            onsetEndX + (comeup.interpolateAtValueInSeconds(onsetAndComeupWeight) * pixelsPerSec)
        val path1 = Path().apply {
            moveTo(x = startX, y = canvasHeight)
            lineTo(x = onsetEndX, y = canvasHeight)
            lineTo(x = comeupEndX, y = top)
        }
        drawScope.drawPath(
            path = path1,
            color = color,
            style = density.normalStroke
        )
        val offsetEndX = startX + (total.interpolateAtValueInSeconds(totalWeight) * pixelsPerSec)
        val path2 = Path().apply {
            moveTo(x = comeupEndX, y = top)
            startSmoothLineTo(
                smoothnessBetween0And1 = 0.5f,
                startX = comeupEndX,
                startY = top,
                endX = offsetEndX,
                endY = canvasHeight
            )
        }
        drawScope.drawPath(
            path = path2,
            color = color,
            style = density.dottedStroke
        )
        val combinedPath = Path().apply {
            moveTo(x = startX, y = canvasHeight)
            lineTo(x = onsetEndX, y = canvasHeight)
            lineTo(x = comeupEndX, y = top)
            startSmoothLineTo(
                smoothnessBetween0And1 = 0.5f,
                startX = comeupEndX,
                startY = top,
                endX = offsetEndX,
                endY = canvasHeight
            )
            lineTo(x = offsetEndX, y = canvasHeight + drawScope.strokeWidth / 2)
            lineTo(x = startX, y = canvasHeight + drawScope.strokeWidth / 2)
            close()
        }
        drawScope.drawPath(
            path = combinedPath,
            color = color.copy(alpha = shapeAlpha)
        )
        drawScope.drawCircle(
            color = color,
            radius = density.ingestionDotRadius,
            center = Offset(
                x = ingestionTimeRelativeToStartInSeconds * pixelsPerSec,
                y = canvasHeight
            )
        )
    }
}

fun RoaDuration.toOnsetComeupTotalTimeline(
    totalWeight: Float,
    ingestionTimeRelativeToStartInSeconds: Float,
    nonNormalisedHeight: Float,
): OnsetComeupTotalTimeline? {
    val fullOnset = onset?.toFullDurationRange()
    val fullComeup = comeup?.toFullDurationRange()
    val fullTotal = total?.toFullDurationRange()
    return if (fullOnset != null && fullComeup != null && fullTotal != null) {
        OnsetComeupTotalTimeline(
            onset = fullOnset,
            comeup = fullComeup,
            total = fullTotal,
            totalWeight = totalWeight,
            ingestionTimeRelativeToStartInSeconds = ingestionTimeRelativeToStartInSeconds,
            nonNormalisedHeight = nonNormalisedHeight,
        )
    } else {
        null
    }
}