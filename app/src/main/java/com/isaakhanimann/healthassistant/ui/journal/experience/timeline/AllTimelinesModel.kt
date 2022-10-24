package com.isaakhanimann.healthassistant.ui.journal.experience.timeline

import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.healthassistant.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.healthassistant.ui.journal.experience.timeline.ingestion.FullTimeline
import com.isaakhanimann.healthassistant.ui.journal.experience.timeline.ingestion.TimelineDrawable
import com.isaakhanimann.healthassistant.ui.journal.experience.timeline.ingestion.toFullTimeline
import com.isaakhanimann.healthassistant.ui.journal.experience.timeline.ingestion.toTotalTimeline
import com.isaakhanimann.healthassistant.ui.utils.getStringOfPattern
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.math.roundToLong
import kotlin.time.Duration.Companion.hours
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class AllTimelinesModel(
    ingestionDurationPairs: List<Pair<IngestionWithCompanion, RoaDuration?>>
) {
    val startTime: Instant
    val widthInSeconds: Float
    val ingestionDrawables: List<IngestionDrawable>
    val axisDrawable: AxisDrawable

    init {
        startTime = ingestionDurationPairs.map { it.first.ingestion.time }
            .reduce { acc, date -> if (acc.isBefore(date)) acc else date }
        val ingestionDrawablesWithoutInsets = ingestionDurationPairs.map { pair ->
            val verticalHeightInPercent = getVerticalHeightInPercent(
                ingestion = pair.first.ingestion,
                allIngestions = ingestionDurationPairs.map { it.first.ingestion })
            IngestionDrawable(
                startTime = startTime,
                ingestionWithCompanion = pair.first,
                roaDuration = pair.second,
                verticalHeightInPercent = verticalHeightInPercent
            )
        }
        ingestionDrawables = updateInsets(ingestionDrawablesWithoutInsets)
        val max = ingestionDrawables.maxOfOrNull {
            if (it.timelineDrawable != null) {
                it.timelineDrawable.widthInSeconds + it.ingestionPointDistanceFromStartInSeconds
            } else {
                it.ingestionPointDistanceFromStartInSeconds
            }
        }
        widthInSeconds = if (max == null || max == 0f) {
            5.hours.inWholeSeconds.toFloat()
        } else {
            max
        }
        axisDrawable = AxisDrawable(startTime, widthInSeconds)
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
                    .filter { it.verticalHeightInPercent == ingestionDrawable.verticalHeightInPercent }
                    .mapNotNull {
                        val full = it.timelineDrawable as? FullTimeline ?: return@mapNotNull null
                        return@mapNotNull full.getPeakDurationRangeInSeconds(startDurationInSeconds = it.ingestionPointDistanceFromStartInSeconds)
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

data class AxisDrawable(
    val startTime: Instant,
    val widthInSeconds: Float
) {
    fun getFullHours(pixelsPerSec: Float, widthInPixels: Float): List<FullHour> {
        val widthInWholeHours = widthInSeconds.toLong().toDuration(DurationUnit.SECONDS).inWholeHours
        val widthPerHour = widthInPixels / widthInWholeHours
        val minWidthPerHour = 70.0
        var stepSize = (minWidthPerHour / widthPerHour).roundToLong()
        if (stepSize == 0.toLong()) {
            stepSize = 1
        }
        val dates = getInstantsBetween(
            startTime = startTime,
            endTime = startTime.plusSeconds(widthInSeconds.toLong()),
            stepSizeInHours = stepSize
        )
        return dates.map {
            val distanceInSec = Duration.between(startTime, it).seconds
            FullHour(
                distanceFromStart = distanceInSec * pixelsPerSec,
                label = it.getStringOfPattern("HH")
            )
        }
    }

    companion object {
        fun getInstantsBetween(startTime: Instant, endTime: Instant, stepSizeInHours: Long): List<Instant> {
            val firstDate = startTime.nearestFullHourInTheFuture()
            val fullHours: MutableList<Instant> = mutableListOf()
            var checkTime = firstDate
            while (checkTime.isBefore(endTime)) {
                fullHours.add(checkTime)
                checkTime = checkTime.plus(stepSizeInHours, ChronoUnit.HOURS)
            }
            return fullHours.toList()
        }
    }
}

fun Instant.nearestFullHourInTheFuture(): Instant {
    val oneHourInFuture = this.plus(1, ChronoUnit.HOURS)
    val dateTime = oneHourInFuture.atZone(ZoneId.systemDefault())
    val seconds = dateTime.second
    val minutes = dateTime.minute
    val newDateTime = dateTime.minusMinutes(minutes.toLong()).minusSeconds(seconds.toLong())
    return newDateTime.toInstant()
}


data class FullHour(
    val distanceFromStart: Float,
    val label: String
)

class IngestionDrawable(
    startTime: Instant,
    ingestionWithCompanion: IngestionWithCompanion,
    roaDuration: RoaDuration?,
    val verticalHeightInPercent: Float = 1f
) {
    val color: SubstanceColor
    val ingestionPointDistanceFromStartInSeconds: Float
    val timelineDrawable: TimelineDrawable?
    var insetTimes = 0

    init {
        ingestionPointDistanceFromStartInSeconds = Duration.between(startTime, ingestionWithCompanion.ingestion.time).seconds.toFloat()
        val full = roaDuration?.toFullTimeline()
        val total = roaDuration?.toTotalTimeline()
        timelineDrawable = full ?: total
        color = ingestionWithCompanion.substanceCompanion!!.color
    }
}
