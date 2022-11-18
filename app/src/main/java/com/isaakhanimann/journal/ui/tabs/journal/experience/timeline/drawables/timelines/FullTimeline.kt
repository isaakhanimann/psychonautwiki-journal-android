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

data class FullTimeline(
    val onset: FullDurationRange,
    val comeup: FullDurationRange,
    val peak: FullDurationRange,
    val offset: FullDurationRange,
    val peakAndOffsetWeight: Float
) : TimelineDrawable {

    override fun getPeakDurationRangeInSeconds(startDurationInSeconds: Float): ClosedRange<Float> {
        val startRange =
            startDurationInSeconds + onset.interpolateAtValueInSeconds(0.5f) + comeup.interpolateAtValueInSeconds(
                0.5f
            )
        return startRange..(startRange + peak.interpolateAtValueInSeconds(peakAndOffsetWeight))
    }

    override val widthInSeconds: Float =
        onset.maxInSeconds + comeup.maxInSeconds + peak.maxInSeconds + offset.maxInSeconds

    override fun drawTimeLine(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color,
        density: Density
    ) {
        drawScope.drawPath(
            path = Path().apply {
                val onsetAndComeupWeight = 0.5f
                val onsetEndX =
                    startX + (onset.interpolateAtValueInSeconds(onsetAndComeupWeight) * pixelsPerSec)
                val comeupEndX =
                    onsetEndX + (comeup.interpolateAtValueInSeconds(onsetAndComeupWeight) * pixelsPerSec)
                val peakEndX =
                    comeupEndX + (peak.interpolateAtValueInSeconds(peakAndOffsetWeight) * pixelsPerSec)
                val offsetEndX =
                    peakEndX + (offset.interpolateAtValueInSeconds(peakAndOffsetWeight) * pixelsPerSec)
                moveTo(x = startX, y = height)
                lineTo(x = onsetEndX, y = height)
                lineTo(x = comeupEndX, y = 0f)
                lineTo(x = peakEndX, y = 0f)
                lineTo(x = offsetEndX, y = height)
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
                // path over top
                val onsetStartMinX = startX + (onset.minInSeconds * pixelsPerSec)
                val comeupEndMinX = onsetStartMinX + (comeup.minInSeconds * pixelsPerSec)
                val peakEndMaxX =
                    startX + ((onset.maxInSeconds + comeup.maxInSeconds + peak.maxInSeconds) * pixelsPerSec)
                val offsetEndMaxX =
                    peakEndMaxX + (offset.maxInSeconds * pixelsPerSec)
                moveTo(onsetStartMinX, height)
                lineTo(x = comeupEndMinX, y = 0f)
                lineTo(x = peakEndMaxX, y = 0f)
                lineTo(x = offsetEndMaxX, y = height)
                // path bottom back
                val onsetStartMaxX = startX + (onset.maxInSeconds * pixelsPerSec)
                val comeupEndMaxX =
                    onsetStartMaxX + (comeup.maxInSeconds * pixelsPerSec)
                val peakEndMinX =
                    startX + ((onset.minInSeconds + comeup.minInSeconds + peak.minInSeconds) * pixelsPerSec)
                val offsetEndMinX =
                    peakEndMinX + (offset.minInSeconds * pixelsPerSec)
                lineTo(x = offsetEndMinX, y = height)
                lineTo(x = peakEndMinX, y = 0f)
                lineTo(x = comeupEndMaxX, y = 0f)
                lineTo(x = onsetStartMaxX, y = height)
                close()
            },
            color = color.copy(alpha = shapeAlpha)
        )
    }
}

fun RoaDuration.toFullTimeline(peakAndOffsetWeight: Float): FullTimeline? {
    val fullOnset = onset?.toFullDurationRange()
    val fullComeup = comeup?.toFullDurationRange()
    val fullPeak = peak?.toFullDurationRange()
    val fullOffset = offset?.toFullDurationRange()
    return if (fullOnset != null && fullComeup != null && fullPeak != null && fullOffset != null) {
        FullTimeline(
            onset = fullOnset,
            comeup = fullComeup,
            peak = fullPeak,
            offset = fullOffset,
            peakAndOffsetWeight = peakAndOffsetWeight
        )
    } else {
        null
    }
}