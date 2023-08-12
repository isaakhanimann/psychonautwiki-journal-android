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
import java.time.Duration
import java.time.Instant
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class AllTimelinesModel(
    dataForLines: List<DataForOneEffectLine>,
    dataForRatings: List<DataForOneRating>,
    timedNotes: List<DataForOneTimedNote>
) {
    val startTime: Instant
    val widthInSeconds: Float
    val groupDrawables: List<GroupDrawable>
    val axisDrawable: AxisDrawable

    data class SubstanceGroup(
        val color: AdaptiveColor,
        val roaDuration: RoaDuration?,
        val weightedLines: List<WeightedLine>
    )

    init {
        val ratingTimes = dataForRatings.map { it.time }
        val ingestionTimes = dataForLines.map { it.startTime }
        val noteTimes = timedNotes.map { it.time }
        val substanceGroups = dataForLines.groupBy { it.substanceName }.map { entry ->
            val lines = entry.value
            val color = lines.first().color
            val roaDuration = lines.first().roaDuration
            val weightedLines = lines.map { WeightedLine(it.startTime, it.horizontalWeight, it.height) }
            SubstanceGroup(color, roaDuration, weightedLines)
        }
        val allStartTimeCandidates = ratingTimes + ingestionTimes + noteTimes
        startTime = allStartTimeCandidates.reduce { acc, date -> if (acc.isBefore(date)) acc else date }
        val groupDrawables = substanceGroups.map { group ->
            GroupDrawable(
                startTimeGraph = startTime,
                color = group.color,
                roaDuration = group.roaDuration,
                weightedLines = group.weightedLines
            )
        }
        this.groupDrawables = groupDrawables
        val maxWidthIngestions: Float = groupDrawables.maxOfOrNull {
            it.endOfLineRelativeToStartInSeconds
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
        val maxCandidates = listOf(maxWidthIngestions, maxWidthRating, maxWidthNote, 2.hours.inWholeSeconds.toFloat())
        widthInSeconds = maxCandidates.max() + 10.minutes.inWholeSeconds.toFloat()
        axisDrawable = AxisDrawable(startTime, widthInSeconds)
    }
}
