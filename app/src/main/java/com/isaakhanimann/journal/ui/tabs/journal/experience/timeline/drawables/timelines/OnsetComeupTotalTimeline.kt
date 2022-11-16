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
            style = AllTimelinesModel.normalStroke
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
            color = color.copy(alpha = AllTimelinesModel.shapeAlpha)
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