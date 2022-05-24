package com.example.healthassistant.ui.home.experience.timeline

import com.example.healthassistant.data.experiences.entities.Ingestion
import com.example.healthassistant.data.experiences.entities.IngestionColor
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.ui.home.experience.timeline.ingestion.TimelineDrawable
import com.example.healthassistant.ui.home.experience.timeline.ingestion.toFullTimeline
import com.example.healthassistant.ui.home.experience.timeline.ingestion.toTotalTimeline
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class AllTimelinesModel(
    ingestionDurationPairs: List<Pair<Ingestion, RoaDuration?>>
) {
    val startTime: Date
    val width: Duration
    val ingestionDrawables: List<IngestionDrawable>
    val axisDrawable: AxisDrawable

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
        axisDrawable = AxisDrawable(startTime, width)
    }
}

data class AxisDrawable(
    val startTime: Date,
    val width: Duration
) {
    fun getFullHours(pixelsPerSec: Float, widthInPixels: Float): List<FullHour> {
        val widthPerHour = widthInPixels / width.inWholeHours
        val minWidthPerHour = 40.0
        var stepSize = (minWidthPerHour / widthPerHour).roundToInt()
        if (stepSize == 0) {
            stepSize = 1
        }
        val dates = getDatesBetween(
            startTime = startTime,
            endTime = Date(startTime.time + width.inWholeMilliseconds),
            stepSizeInHours = stepSize
        )
        val formatter = SimpleDateFormat("HH", Locale.getDefault())
        return dates.map {
            val distanceInSec = (it.time - startTime.time) / 1000
            FullHour(
                distanceFromStart = distanceInSec * pixelsPerSec,
                label = formatter.format(it) ?: "??"
            )
        }
    }

    companion object {
        fun getDatesBetween(startTime: Date, endTime: Date, stepSizeInHours: Int): List<Date> {
            val firstDate = startTime.nearestFullHourInTheFuture()
            val stepInMilliseconds = stepSizeInHours * 60 * 60 * 1000
            val fullHours: MutableList<Date> = mutableListOf()
            var checkTime = firstDate
            while (checkTime.before(endTime)) {
                fullHours.add(checkTime)
                checkTime = Date(checkTime.time + stepInMilliseconds)
            }
            return fullHours.toList()
        }
    }
}

fun Date.nearestFullHourInTheFuture(): Date {
    val cal = Calendar.getInstance(TimeZone.getDefault())
    cal.time = this
    cal.add(Calendar.HOUR_OF_DAY, 1)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    return cal.time
}


data class FullHour(
    val distanceFromStart: Float,
    val label: String
)

class IngestionDrawable(
    startTime: Date,
    ingestion: Ingestion,
    roaDuration: RoaDuration?
) {
    val color: IngestionColor
    val ingestionPointDistanceFromStart: Duration
    val timelineDrawable: TimelineDrawable?

    init {
        ingestionPointDistanceFromStart =
            (ingestion.time.time - startTime.time).toDuration(DurationUnit.MILLISECONDS)
        val full = roaDuration?.toFullTimeline()
        val total = roaDuration?.toTotalTimeline()
        timelineDrawable = full ?: total
        color = ingestion.color
    }
}
