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

data class OnsetComeupPeakTimeline(
    val onset: FullDurationRange,
    val comeup: FullDurationRange,
    val peak: FullDurationRange,
    val peakWeight: Float
) : TimelineDrawable {

    override fun getPeakDurationRangeInSeconds(startDurationInSeconds: Float): ClosedRange<Float> {
        val startRange =
            startDurationInSeconds + onset.interpolateAtValueInSeconds(0.5f) + comeup.interpolateAtValueInSeconds(
                0.5f
            )
        return startRange..(startRange + peak.interpolateAtValueInSeconds(peakWeight))
    }

    override val widthInSeconds: Float =
        onset.maxInSeconds + comeup.maxInSeconds + peak.maxInSeconds

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
        val comeupEndX =
            onsetEndX + (comeup.interpolateAtValueInSeconds(weight) * pixelsPerSec)
        val peakEndX =
            comeupEndX + (peak.interpolateAtValueInSeconds(peakWeight) * pixelsPerSec)
        drawScope.drawPath(
            path = Path().apply {
                moveTo(x = startX, y = height)
                lineTo(x = onsetEndX, y = height)
                lineTo(x = comeupEndX, y = 0f)
                lineTo(x = peakEndX, y = 0f)
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
                val comeupEndMinX = onsetEndMinX + (comeup.minInSeconds * pixelsPerSec)
                moveTo(onsetEndMinX, height)
                lineTo(x = comeupEndMinX, y = 0f)
                val onsetEndMaxX = startX + (onset.maxInSeconds * pixelsPerSec)
                val comeupEndMaxX =
                    onsetEndMaxX + (comeup.maxInSeconds * pixelsPerSec)
                lineTo(x = comeupEndMaxX, y = 0f)
                val peakEndMaxX = comeupEndMaxX + (peak.maxInSeconds * pixelsPerSec)
                val peakEndMinX = comeupEndMinX + (peak.minInSeconds * pixelsPerSec)
                lineTo(x = peakEndMaxX, y = 0f)
                lineTo(x = peakEndMaxX, y = density.shapeWidth)
                lineTo(x = peakEndMinX, y = density.shapeWidth)
                lineTo(x = peakEndMinX, y = 0f)
                lineTo(x = comeupEndMaxX, y = 0f)
                lineTo(x = onsetEndMaxX, y = height)
                close()
            },
            color = color.copy(alpha = shapeAlpha)
        )
    }
}

fun RoaDuration.toOnsetComeupPeakTimeline(peakWeight: Float): OnsetComeupPeakTimeline? {
    val fullOnset = onset?.toFullDurationRange()
    val fullComeup = comeup?.toFullDurationRange()
    val fullPeak = peak?.toFullDurationRange()
    return if (fullOnset != null && fullComeup != null && fullPeak != null) {
        OnsetComeupPeakTimeline(
            onset = fullOnset,
            comeup = fullComeup,
            peak = fullPeak,
            peakWeight = peakWeight
        )
    } else {
        null
    }
}