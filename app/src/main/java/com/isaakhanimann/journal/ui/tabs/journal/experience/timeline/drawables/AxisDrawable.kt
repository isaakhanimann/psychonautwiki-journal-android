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

package com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables

import com.isaakhanimann.journal.ui.utils.getStringOfPattern
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.math.roundToLong
import kotlin.time.DurationUnit
import kotlin.time.toDuration

data class AxisDrawable(
    val startTime: Instant,
    val widthInSeconds: Float
) {
    fun getFullHours(pixelsPerSec: Float, widthInPixels: Float): List<FullHour> {
        val widthInWholeHours = widthInSeconds.toLong().toDuration(DurationUnit.SECONDS).inWholeHours
        val widthPerHour = widthInPixels / widthInWholeHours
        val minWidthPerHour = 60.0
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