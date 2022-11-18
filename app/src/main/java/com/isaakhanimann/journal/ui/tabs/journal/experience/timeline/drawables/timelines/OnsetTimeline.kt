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
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.TimelineDrawable
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.normalStroke
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.shapeAlpha
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.shapeWidth


data class OnsetTimeline(
    val onset: FullDurationRange,
) : TimelineDrawable {

    override fun getPeakDurationRangeInSeconds(startDurationInSeconds: Float): ClosedRange<Float>? {
        return null
    }

    override val widthInSeconds: Float =
        onset.maxInSeconds

    override fun drawTimeLine(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color,
        density: Density
    ) {
        val weight = 0.5f
        val onsetEndX =
            startX + (onset.interpolateAtValueInSeconds(weight) * pixelsPerSec)
        drawScope.drawPath(
            path = Path().apply {
                moveTo(x = startX, y = height)
                lineTo(x = onsetEndX, y = height)
            },
            color = color,
            style = density.normalStroke
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
                val onsetEndMinX = startX + (onset.minInSeconds * pixelsPerSec)
                val onsetEndMaxX = startX + (onset.maxInSeconds * pixelsPerSec)
                moveTo(onsetEndMinX, height)
                lineTo(x = onsetEndMaxX, y = height)
                lineTo(x = onsetEndMaxX, y = height - density.shapeWidth)
                lineTo(x = onsetEndMinX, y = height - density.shapeWidth)
                close()
            },
            color = color.copy(alpha = shapeAlpha)
        )
    }
}

fun RoaDuration.toOnsetTimeline(): OnsetTimeline? {
    val fullOnset = onset?.toFullDurationRange()
    return if (fullOnset != null) {
        OnsetTimeline(
            onset = fullOnset,
        )
    } else {
        null
    }
}