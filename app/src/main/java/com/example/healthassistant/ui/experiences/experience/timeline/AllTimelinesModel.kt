package com.example.healthassistant.ui.experiences.experience.timeline

import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.room.experiences.entities.IngestionColor
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.ui.experiences.experience.timeline.ingestion.FullTimeline
import com.example.healthassistant.ui.experiences.experience.timeline.ingestion.TimelineDrawable
import com.example.healthassistant.ui.experiences.experience.timeline.ingestion.toFullTimeline
import com.example.healthassistant.ui.experiences.experience.timeline.ingestion.toTotalTimeline
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
        val ingestionDrawablesWithoutInsets = ingestionDurationPairs.map { pair ->
            val verticalHeightInPercent = getVerticalHeightInPercent(
                ingestion = pair.first,
                allIngestions = ingestionDurationPairs.map { it.first })
            IngestionDrawable(
                startTime = startTime,
                ingestion = pair.first,
                roaDuration = pair.second,
                verticalHeightInPercent = verticalHeightInPercent
            )
        }
        ingestionDrawables = updateInsets(ingestionDrawablesWithoutInsets)
        width = ingestionDrawables.map {
            if (it.timelineDrawable != null) {
                it.timelineDrawable.width + it.ingestionPointDistanceFromStart
            } else {
                it.ingestionPointDistanceFromStart
            }
        }.maxOrNull() ?: 5.0.hours
        axisDrawable = AxisDrawable(startTime, width)
    }

    companion object {
        fun getVerticalHeightInPercent(
            ingestion: Ingestion,
            allIngestions: List<Ingestion>
        ): Float {
            val max = allIngestions
                .filter { it.substanceName == ingestion.substanceName }
                .mapNotNull { it.dose }
                .maxOrNull()
            return ingestion.dose.let { doseSnap ->
                if (max == null || doseSnap == null) {
                    1f
                } else {
                    doseSnap.div(max).toFloat()
                }
            }
        }

        fun updateInsets(ingestionDrawables: List<IngestionDrawable>): List<IngestionDrawable> {
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
            val otherFullTimelinePeakRangesWithSameHeight: List<ClosedRange<Duration>> =
                otherDrawables
                    .filter { it.verticalHeightInPercent == ingestionDrawable.verticalHeightInPercent }
                    .mapNotNull {
                        val full = it.timelineDrawable as? FullTimeline ?: return@mapNotNull null
                        return@mapNotNull full.getPeakDurationRange(startDuration = it.ingestionPointDistanceFromStart)
                    }
            val currentRange =
                currentFullTimeline.getPeakDurationRange(startDuration = ingestionDrawable.ingestionPointDistanceFromStart)
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

data class AxisDrawable(
    val startTime: Date,
    val width: Duration
) {
    fun getFullHours(pixelsPerSec: Float, widthInPixels: Float): List<FullHour> {
        val widthPerHour = widthInPixels / width.inWholeHours
        val minWidthPerHour = 70.0
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
    roaDuration: RoaDuration?,
    val verticalHeightInPercent: Float = 1f
) {
    val color: IngestionColor
    val ingestionPointDistanceFromStart: Duration
    val timelineDrawable: TimelineDrawable?
    var insetTimes = 0

    init {
        ingestionPointDistanceFromStart =
            (ingestion.time.time - startTime.time).toDuration(DurationUnit.MILLISECONDS)
        val full = roaDuration?.toFullTimeline()
        val total = roaDuration?.toTotalTimeline()
        timelineDrawable = full ?: total
        color = ingestion.color
    }
}
