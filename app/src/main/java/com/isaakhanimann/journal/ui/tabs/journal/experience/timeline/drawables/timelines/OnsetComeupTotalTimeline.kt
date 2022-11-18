/*
 * Copyright (c) 2022. Free Software Foundation, Inc.
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

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.dottedStroke
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.TimelineDrawable
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.normalStroke
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.shapeAlpha

data class OnsetComeupTotalTimeline(
    val onset: FullDurationRange,
    val comeup: FullDurationRange,
    val total: FullDurationRange,
    val totalWeight: Float
) : TimelineDrawable {

    override fun getPeakDurationRangeInSeconds(startDurationInSeconds: Float): ClosedRange<Float>? {
        return null
    }

    override val widthInSeconds: Float =
        total.maxInSeconds

    override fun drawTimeLine(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color,
        density: Density
    ) {
        val onsetAndComeupWeight = 0.5f
        val onsetEndX =
            startX + (onset.interpolateAtValueInSeconds(onsetAndComeupWeight) * pixelsPerSec)
        val comeupEndX =
            onsetEndX + (comeup.interpolateAtValueInSeconds(onsetAndComeupWeight) * pixelsPerSec)
        drawScope.drawPath(
            path = Path().apply {
                moveTo(x = startX, y = height)
                lineTo(x = onsetEndX, y = height)
                lineTo(x = comeupEndX, y = 0f)
            },
            color = color,
            style = density.normalStroke
        )
        drawScope.drawPath(
            path = Path().apply {
                moveTo(x = comeupEndX, y = 0f)
                val offsetEndX = total.interpolateAtValueInSeconds(totalWeight) * pixelsPerSec
                startSmoothLineTo(
                    smoothnessBetween0And1 = 0.5f,
                    startX = comeupEndX,
                    startY = 0f,
                    endX = offsetEndX,
                    endY = height
                )
            },
            color = color,
            style = density.dottedStroke
        )
    }

    override fun drawTimeLineShape(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color,
        density: Density
    ) {
        drawScope.drawPath(
            path = Path().apply {
                val onsetStartMinX = startX + (onset.minInSeconds * pixelsPerSec)
                val comeupEndMinX = onsetStartMinX + (comeup.minInSeconds * pixelsPerSec)
                val offsetEndMaxX = total.maxInSeconds * pixelsPerSec
                val onsetStartMaxX = startX + (onset.maxInSeconds * pixelsPerSec)
                val comeupEndMaxX =
                    onsetStartMaxX + (comeup.maxInSeconds * pixelsPerSec)
                val offsetEndMinX =
                    total.minInSeconds * pixelsPerSec
                moveTo(onsetStartMinX, height)
                lineTo(x = comeupEndMinX, y = 0f)
                lineTo(x = comeupEndMaxX, y = 0f)
                startSmoothLineTo(
                    smoothnessBetween0And1 = 0.5f,
                    startX = comeupEndMaxX,
                    startY = 0f,
                    endX = offsetEndMaxX,
                    endY = height
                )
                lineTo(x = offsetEndMinX, y = height)
                endSmoothLineTo(
                    smoothnessBetween0And1 = 0.5f,
                    startX = offsetEndMinX,
                    endX = comeupEndMinX,
                    endY = 0f
                )
                lineTo(x = comeupEndMaxX, y = 0f)
                lineTo(x = onsetStartMaxX, y = height)
                close()
            },
            color = color.copy(alpha = shapeAlpha)
        )
    }
}

fun RoaDuration.toOnsetComeupTotalTimeline(totalWeight: Float): OnsetComeupTotalTimeline? {
    val fullOnset = onset?.toFullDurationRange()
    val fullComeup = comeup?.toFullDurationRange()
    val fullTotal = total?.toFullDurationRange()
    return if (fullOnset != null && fullComeup != null && fullTotal != null) {
        OnsetComeupTotalTimeline(
            onset = fullOnset,
            comeup = fullComeup,
            total = fullTotal,
            totalWeight = totalWeight
        )
    } else {
        null
    }
}