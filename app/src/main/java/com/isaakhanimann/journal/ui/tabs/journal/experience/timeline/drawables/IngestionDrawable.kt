/*
 * Copyright (c) 2022. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables

import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.timelines.*
import java.time.Duration
import java.time.Instant

class IngestionDrawable(
    startTimeGraph: Instant,
    val color: AdaptiveColor,
    ingestionTime: Instant,
    roaDuration: RoaDuration?,
    val height: Float = 1f,
    val peakAndOffsetWeight: Float
) {
    val ingestionPointDistanceFromStartInSeconds: Float
    val timelineDrawable: TimelineDrawable?
    var insetTimes = 0

    init {
        ingestionPointDistanceFromStartInSeconds =
            Duration.between(startTimeGraph, ingestionTime).seconds.toFloat()
        val full = roaDuration?.toFullTimeline(peakAndOffsetWeight = peakAndOffsetWeight)
        val onsetComeupPeakTotal =
            roaDuration?.toOnsetComeupPeakTotalTimeline(peakAndTotalWeight = peakAndOffsetWeight)
        val onsetComeupTotal =
            roaDuration?.toOnsetComeupTotalTimeline(totalWeight = peakAndOffsetWeight)
        val onsetTotal = roaDuration?.toOnsetTotalTimeline(totalWeight = peakAndOffsetWeight)
        val total = roaDuration?.toTotalTimeline(totalWeight = peakAndOffsetWeight)
        val onsetComeupPeak =
            roaDuration?.toOnsetComeupPeakTimeline(peakWeight = peakAndOffsetWeight)
        val onsetComeup = roaDuration?.toOnsetComeupTimeline()
        val onset = roaDuration?.toOnsetTimeline()
        timelineDrawable =
            full ?: onsetComeupPeakTotal ?: onsetComeupTotal ?: onsetTotal ?: total
                    ?: onsetComeupPeak ?: onsetComeup ?: onset
    }
}