/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.timelines

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.AllTimelinesModel
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.TimelineDrawable

data class OnsetComeupPeakTotalTimeline(
    val onset: FullDurationRange,
    val comeup: FullDurationRange,
    val peak: FullDurationRange,
    val total: FullDurationRange,
    val peakAndTotalWeight: Float,
) : TimelineDrawable {

    override fun getPeakDurationRangeInSeconds(startDurationInSeconds: Float): ClosedRange<Float> {
        val startRange =
            startDurationInSeconds + onset.interpolateAtValueInSeconds(0.5f) + comeup.interpolateAtValueInSeconds(
                0.5f
            )
        return startRange..(startRange + peak.interpolateAtValueInSeconds(peakAndTotalWeight))
    }

    override val widthInSeconds: Float =
        total.maxInSeconds

    override fun drawTimeLine(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color,
    ) {
        val onsetAndComeupWeight = 0.5f
        val onsetEndX =
            startX + (onset.interpolateAtValueInSeconds(onsetAndComeupWeight) * pixelsPerSec)
        val comeupEndX =
            onsetEndX + (comeup.interpolateAtValueInSeconds(onsetAndComeupWeight) * pixelsPerSec)
        val peakEndX =
            comeupEndX + (peak.interpolateAtValueInSeconds(peakAndTotalWeight) * pixelsPerSec)
        drawScope.drawPath(
            path = Path().apply {
                moveTo(x = startX, y = height)
                lineTo(x = onsetEndX, y = height)
                lineTo(x = comeupEndX, y = 0f)
                lineTo(x = peakEndX, y = 0f)
            },
            color = color,
            style = AllTimelinesModel.normalStroke
        )
        drawScope.drawPath(
            path = Path().apply {
                moveTo(x = peakEndX, y = 0f)
                val offsetEndX = total.interpolateAtValueInSeconds(peakAndTotalWeight) * pixelsPerSec
                lineTo(x = offsetEndX, y = height)
            },
            color = color,
            style = AllTimelinesModel.dottedStroke
        )
    }

    override fun drawTimeLineShape(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color,
    ) {
        drawScope.drawPath(
            path = Path().apply {
                // path over top
                val onsetStartMinX = startX + (onset.minInSeconds * pixelsPerSec)
                val comeupEndMinX = onsetStartMinX + (comeup.minInSeconds * pixelsPerSec)
                val peakEndMaxX =
                    startX + ((onset.maxInSeconds + comeup.maxInSeconds + peak.maxInSeconds) * pixelsPerSec)
                val offsetEndMaxX = total.maxInSeconds * pixelsPerSec
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
                    total.minInSeconds * pixelsPerSec
                lineTo(x = offsetEndMinX, y = height)
                lineTo(x = peakEndMinX, y = 0f)
                lineTo(x = comeupEndMaxX, y = 0f)
                lineTo(x = onsetStartMaxX, y = height)
                close()
            },
            color = color.copy(alpha = AllTimelinesModel.shapeAlpha)
        )
    }
}

fun RoaDuration.toOnsetComeupPeakTotalTimeline(peakAndTotalWeight: Float): OnsetComeupPeakTotalTimeline? {
    val fullOnset = onset?.toFullDurationRange()
    val fullComeup = comeup?.toFullDurationRange()
    val fullPeak = peak?.toFullDurationRange()
    val fullTotal = total?.toFullDurationRange()
    return if (fullOnset != null && fullComeup != null && fullPeak != null && fullTotal != null) {
        OnsetComeupPeakTotalTimeline(
            onset = fullOnset,
            comeup = fullComeup,
            peak = fullPeak,
            total = fullTotal,
            peakAndTotalWeight = peakAndTotalWeight
        )
    } else {
        null
    }
}