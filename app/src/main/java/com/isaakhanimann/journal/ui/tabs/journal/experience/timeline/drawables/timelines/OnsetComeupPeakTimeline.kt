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

data class OnsetComeupPeakTimeline(
    val onset: FullDurationRange,
    val comeup: FullDurationRange,
    val peak: FullDurationRange,
    val peakWeight: Float
) : TimelineDrawable {

    override fun getPeakDurationRangeInSeconds(startDurationInSeconds: Float): ClosedRange<Float> {
        val startRange =
            startDurationInSeconds + onset.interpolateAtValueInSeconds(0.5f) + comeup.interpolateAtValueInSeconds(0.5f)
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
            style = AllTimelinesModel.normalStroke
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
                lineTo(x = peakEndMaxX, y = AllTimelinesModel.shapeWidth)
                lineTo(x = peakEndMinX, y = AllTimelinesModel.shapeWidth)
                lineTo(x = peakEndMinX, y = 0f)
                lineTo(x = comeupEndMaxX, y = 0f)
                lineTo(x = onsetEndMaxX, y = height)
                close()
            },
            color = color.copy(alpha = AllTimelinesModel.shapeAlpha)
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