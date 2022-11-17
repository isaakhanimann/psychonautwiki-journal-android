/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
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