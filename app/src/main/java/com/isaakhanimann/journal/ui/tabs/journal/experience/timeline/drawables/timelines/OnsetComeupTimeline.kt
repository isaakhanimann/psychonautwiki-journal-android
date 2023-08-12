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
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.TimelineDrawable
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.normalStroke

data class OnsetComeupTimeline(
    val onset: FullDurationRange,
    val comeup: FullDurationRange,
    val ingestionTimeRelativeToStartInSeconds: Float
) : TimelineDrawable {

    override val endOfLineRelativeToStartInSeconds: Float =
        ingestionTimeRelativeToStartInSeconds + onset.maxInSeconds + comeup.maxInSeconds

    override fun drawTimeLine(
        drawScope: DrawScope,
        height: Float,
        pixelsPerSec: Float,
        color: Color,
        density: Density
    ) {
        val weight = 0.5f
        val startX = ingestionTimeRelativeToStartInSeconds*pixelsPerSec
        val onsetEndX =
            startX + (onset.interpolateAtValueInSeconds(weight) * pixelsPerSec)
        val comeupEndX =
            onsetEndX + (comeup.interpolateAtValueInSeconds(weight) * pixelsPerSec)
        drawScope.drawPath(
            path = Path().apply {
                moveTo(x = startX, y = height)
                lineTo(x = onsetEndX, y = height)
                lineTo(x = comeupEndX, y = 0f)
            },
            color = color,
            style = density.normalStroke
        )
    }
}

fun RoaDuration.toOnsetComeupTimeline(ingestionTimeRelativeToStartInSeconds: Float): OnsetComeupTimeline? {
    val fullOnset = onset?.toFullDurationRange()
    val fullComeup = comeup?.toFullDurationRange()
    return if (fullOnset != null && fullComeup != null) {
        OnsetComeupTimeline(
            onset = fullOnset,
            comeup = fullComeup,
            ingestionTimeRelativeToStartInSeconds = ingestionTimeRelativeToStartInSeconds
        )
    } else {
        null
    }
}