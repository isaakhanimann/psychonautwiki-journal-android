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

import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.DataForOneEffectLine
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.AxisDrawable
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.GroupDrawable
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.TimeRangeDrawable
import java.time.Duration
import java.time.Instant
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class AllTimelinesModel(
    dataForLines: List<DataForOneEffectLine>,
    dataForRatings: List<DataForOneRating>,
    timedNotes: List<DataForOneTimedNote>,
    areSubstanceHeightsIndependent: Boolean,
) {
    val startTime: Instant
    val widthInSeconds: Float
    val groupDrawables: List<GroupDrawable>
    val timeRangeDrawables: List<TimeRangeDrawable>
    val axisDrawable: AxisDrawable

    data class RoaGroup(
        val color: AdaptiveColor,
        val roaDuration: RoaDuration?,
        val weightedLines: List<WeightedLine>
    )

    init {
        val ratingTimes = dataForRatings.map { it.time }
        val ingestionTimes = dataForLines.map { it.startTime }
        val noteTimes = timedNotes.map { it.time }
        val allStartTimeCandidates = ratingTimes + ingestionTimes + noteTimes
        startTime = allStartTimeCandidates.reduce { acc, date -> if (acc.isBefore(date)) acc else date }
        val roaGroups = dataForLines.filter { it.endTime == null }.groupBy { it.substanceName }.flatMap { substanceGroup ->
            val linesPerSubstance = substanceGroup.value
            return@flatMap linesPerSubstance.groupBy { it.route }.map { routeGroup ->
                val linesPerRoute = routeGroup.value
                return@map RoaGroup(
                    color = linesPerRoute.first().color,
                    roaDuration = linesPerRoute.first().roaDuration,
                    weightedLines = linesPerRoute.map { WeightedLine(it.startTime, it.horizontalWeight, it.height) })
            }
        }
        val groupDrawables = roaGroups.map { group ->
            GroupDrawable(
                startTimeGraph = startTime,
                color = group.color,
                roaDuration = group.roaDuration,
                weightedLines = group.weightedLines,
                areSubstanceHeightsIndependent = areSubstanceHeightsIndependent
            )
        }
        val overallMaxHeight = groupDrawables.maxOfOrNull { it.nonNormalisedHeight } ?: 1f
        groupDrawables.forEach { it.setOverallHeight(overallMaxHeight) }
        this.groupDrawables = groupDrawables
        val maxWidthOfPointInTimeIngestions: Float = groupDrawables.maxOfOrNull {
            it.endOfLineRelativeToStartInSeconds
        } ?: 0f

        val intermediateRanges = dataForLines.mapNotNull {
            if (it.endTime != null) {
                val startInSeconds = Duration.between(startTime, it.startTime).seconds
                val endInSeconds = Duration.between(startTime, it.endTime).seconds
                return@mapNotNull TimeRangeDrawable.IntermediateRepresentation(
                    color = it.color,
                    startInSeconds = startInSeconds,
                    endInSeconds = endInSeconds,
                )
            } else {
                return@mapNotNull null
            }
        }.sortedBy { it.startInSeconds }
        timeRangeDrawables = intermediateRanges.mapIndexed { index, currentRange ->
            val previousRanges = intermediateRanges.subList(0, index)
            val intersectionCount = previousRanges.count {
                return@count it.startInSeconds <= currentRange.endInSeconds && currentRange.startInSeconds <= it.endInSeconds
            }
            TimeRangeDrawable(
                color = currentRange.color,
                startInSeconds = currentRange.startInSeconds,
                endInSeconds = currentRange.endInSeconds,
                intersectionCountWithPreviousRanges = intersectionCount
            )
        }

        val maxWidthOfTimeRangeIngestions = timeRangeDrawables.maxOfOrNull {
            it.endInSeconds.toFloat()
        } ?: 0f

        val latestRating = ratingTimes.maxOrNull()
        val maxWidthRating: Float = if (latestRating != null) {
            Duration.between(startTime, latestRating).seconds.toFloat()
        } else {
            0f
        }
        val latestNote = noteTimes.maxOrNull()
        val maxWidthNote: Float = if (latestNote != null) {
            Duration.between(startTime, latestNote).seconds.toFloat()
        } else {
            0f
        }
        val maxCandidates = listOf(maxWidthOfPointInTimeIngestions, maxWidthOfTimeRangeIngestions, maxWidthRating, maxWidthNote, 2.hours.inWholeSeconds.toFloat())
        widthInSeconds = maxCandidates.max() + 10.minutes.inWholeSeconds.toFloat()
        axisDrawable = AxisDrawable(startTime, widthInSeconds)
    }
}
