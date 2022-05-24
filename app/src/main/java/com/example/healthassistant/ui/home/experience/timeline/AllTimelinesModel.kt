package com.example.healthassistant.ui.home.experience.timeline

import com.example.healthassistant.data.experiences.entities.Ingestion
import com.example.healthassistant.data.experiences.entities.IngestionColor
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.ui.home.experience.timeline.ingestion.TimelineDrawable
import com.example.healthassistant.ui.home.experience.timeline.ingestion.toFullTimeline
import com.example.healthassistant.ui.home.experience.timeline.ingestion.toTotalTimeline
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class AllTimelinesModel(
    ingestionDurationPairs: List<Pair<Ingestion, RoaDuration>>
) {
    val startTime: Date
    val width: Duration
    val ingestionDrawables: List<IngestionDrawable>

    init {
        startTime = ingestionDurationPairs.map { it.first.time }
            .reduce { acc, date -> if (acc.before(date)) acc else date }
        ingestionDrawables = ingestionDurationPairs.map {
            IngestionDrawable(startTime = startTime, ingestion = it.first, roaDuration = it.second)
        }
        width = ingestionDrawables.map {
            if (it.timelineDrawable != null) {
                it.timelineDrawable.width + it.ingestionPointDistanceFromStart
            } else {
                it.ingestionPointDistanceFromStart
            }
        }.maxOrNull() ?: 5.0.hours
    }
}

class IngestionDrawable(
    startTime: Date,
    ingestion: Ingestion,
    roaDuration: RoaDuration
) {
    val color: IngestionColor
    val ingestionPointDistanceFromStart: Duration
    val timelineDrawable: TimelineDrawable?

    init {
        ingestionPointDistanceFromStart =
            (ingestion.time.time - startTime.time).toDuration(DurationUnit.MILLISECONDS)
        val full = roaDuration.toFullTimeline()
        val total = roaDuration.toTotalTimeline()
        timelineDrawable = full ?: total
        color = ingestion.color
    }
}
