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

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.dottedStroke
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.TimelineDrawable

data class TotalTimeline(
    val total: FullDurationRange,
    val totalWeight: Float,
    val percentSmoothness: Float = 0.5f,
    val ingestionTimeRelativeToStartInSeconds: Float
) : TimelineDrawable {

    override val endOfLineRelativeToStartInSeconds: Float = ingestionTimeRelativeToStartInSeconds + total.maxInSeconds

    override fun drawTimeLine(
        drawScope: DrawScope,
        height: Float,
        pixelsPerSec: Float,
        color: Color,
        density: Density
    ) {
        drawScope.drawPath(
            path = Path().apply {
                val totalMinX = total.minInSeconds * pixelsPerSec
                val totalX = total.interpolateAtValueInSeconds(totalWeight) * pixelsPerSec
                val startX = ingestionTimeRelativeToStartInSeconds*pixelsPerSec
                moveTo(startX, height)
                endSmoothLineTo(
                    smoothnessBetween0And1 = percentSmoothness,
                    startX = startX,
                    endX = startX + (totalMinX / 2),
                    endY = 0f
                )
                startSmoothLineTo(
                    smoothnessBetween0And1 = percentSmoothness,
                    startX = startX + (totalMinX / 2),
                    startY = 0f,
                    endX = startX + totalX,
                    endY = height
                )
            },
            color = color,
            style = density.dottedStroke
        )
    }
}

fun RoaDuration.toTotalTimeline(totalWeight: Float, ingestionTimeRelativeToStartInSeconds: Float): TotalTimeline? {
    val fullTotal = total?.toFullDurationRange()
    return if (fullTotal != null) {
        TotalTimeline(total = fullTotal, totalWeight = totalWeight, ingestionTimeRelativeToStartInSeconds = ingestionTimeRelativeToStartInSeconds)
    } else {
        null
    }
}