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

package com.isaakhanimann.journal.ui.tabs.journal.experience.timeline

import com.isaakhanimann.journal.ui.tabs.journal.experience.components.DataForOneEffectLine
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.AxisDrawable
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.IngestionDrawable
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.timelines.FullTimeline
import java.time.Duration
import java.time.Instant
import kotlin.math.max
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class AllTimelinesModel(
    dataForLines: List<DataForOneEffectLine>,
    dataForRatings: List<DataForOneRating>
) {
    val startTime: Instant
    val widthInSeconds: Float
    val ingestionDrawables: List<IngestionDrawable>
    val axisDrawable: AxisDrawable

    init {
        val ratingTimes = dataForRatings.map { it.time }
        val ingestionTimes = dataForLines.map { it.startTime }
        val allStartTimeCandidates = ratingTimes + ingestionTimes
        startTime = allStartTimeCandidates.reduce { acc, date -> if (acc.isBefore(date)) acc else date }
        val ingestionDrawablesWithoutInsets = dataForLines.map { dataForOneLine ->
            IngestionDrawable(
                startTimeGraph = startTime,
                color = dataForOneLine.color,
                ingestionTime = dataForOneLine.startTime,
                roaDuration = dataForOneLine.roaDuration,
                height = dataForOneLine.height,
                peakAndOffsetWeight = dataForOneLine.horizontalWeight
            )
        }
        ingestionDrawables = updateInsets(ingestionDrawablesWithoutInsets)
        val maxWidthIngestions: Float = ingestionDrawables.maxOfOrNull {
            if (it.timelineDrawable != null) {
                it.timelineDrawable.widthInSeconds + it.ingestionPointDistanceFromStartInSeconds
            } else {
                it.ingestionPointDistanceFromStartInSeconds
            }
        } ?: 0f
        val latestRating = ratingTimes.maxOrNull()
        val maxWidthRating: Float = if (latestRating != null) {
            Duration.between(startTime, latestRating).seconds.toFloat()
        } else {
            0f
        }
        widthInSeconds = max(maxWidthIngestions, max(maxWidthRating, 4.hours.inWholeSeconds.toFloat())) + 10.minutes.inWholeSeconds.toFloat()
        axisDrawable = AxisDrawable(startTime, widthInSeconds)
    }

    companion object {
        private fun updateInsets(ingestionDrawables: List<IngestionDrawable>): List<IngestionDrawable> {
            val results = mutableListOf<IngestionDrawable>()
            for (i in ingestionDrawables.indices) {
                val currentDrawable = ingestionDrawables[i]
                val otherDrawables = ingestionDrawables.take(i)
                val insetTimes = getInsetTimes(
                    ingestionDrawable = currentDrawable,
                    otherDrawables = otherDrawables
                )
                currentDrawable.insetTimes = insetTimes
                results.add(currentDrawable)
            }
            return results
        }

        private fun getInsetTimes(
            ingestionDrawable: IngestionDrawable,
            otherDrawables: List<IngestionDrawable>
        ): Int {
            val currentFullTimeline =
                ingestionDrawable.timelineDrawable as? FullTimeline ?: return 0
            val otherFullTimelinePeakRangesWithSameHeight: List<ClosedRange<Float>> =
                otherDrawables
                    .filter { it.height == ingestionDrawable.height }
                    .mapNotNull {
                        return@mapNotNull it.timelineDrawable?.getPeakDurationRangeInSeconds(
                            startDurationInSeconds = it.ingestionPointDistanceFromStartInSeconds
                        )
                    }
            val currentRange =
                currentFullTimeline.getPeakDurationRangeInSeconds(startDurationInSeconds = ingestionDrawable.ingestionPointDistanceFromStartInSeconds)
            var insetTimes = 0
            for (otherRange in otherFullTimelinePeakRangesWithSameHeight) {
                val isOverlap =
                    currentRange.start <= otherRange.endInclusive && otherRange.start <= currentRange.endInclusive
                if (isOverlap) insetTimes++
            }
            return insetTimes
        }
    }
}
