package com.isaakhanimann.healthassistant.ui.journal.experience.timeline.drawables

import com.isaakhanimann.healthassistant.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.healthassistant.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.healthassistant.ui.journal.experience.timeline.drawables.timelines.*
import java.time.Duration
import java.time.Instant

class IngestionDrawable(
    startTimeGraph: Instant,
    val color: AdaptiveColor,
    ingestionTime: Instant,
    roaDuration: RoaDuration?,
    val height: Float = 1f
) {
    val ingestionPointDistanceFromStartInSeconds: Float
    val timelineDrawable: TimelineDrawable?
    var insetTimes = 0

    init {
        ingestionPointDistanceFromStartInSeconds =
            Duration.between(startTimeGraph, ingestionTime).seconds.toFloat()
        val full = roaDuration?.toFullTimeline()
        val onsetComeupPeakTotal = roaDuration?.toOnsetComeupPeakTotalTimeline()
        val onsetComeupTotal = roaDuration?.toOnsetComeupTotalTimeline()
        val onsetTotal = roaDuration?.toOnsetTotalTimeline()
        val total = roaDuration?.toTotalTimeline()
        val onsetComeupPeak = roaDuration?.toOnsetComeupPeakTimeline()
        val onsetComeup = roaDuration?.toOnsetComeupTimeline()
        val onset = roaDuration?.toOnsetTimeline()
        timelineDrawable =
            full ?: onsetComeupPeakTotal ?: onsetComeupTotal ?: onsetTotal ?: total ?: onsetComeupPeak ?: onsetComeup ?: onset
    }
}