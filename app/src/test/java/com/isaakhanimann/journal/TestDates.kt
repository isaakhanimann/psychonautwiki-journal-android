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

package com.isaakhanimann.journal

import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.AxisDrawable
import com.isaakhanimann.journal.ui.utils.getInstant
import com.isaakhanimann.journal.ui.utils.getStringOfPattern
import com.isaakhanimann.journal.ui.utils.getTimeDifferenceText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestDates {
    @Test
    fun datesBetweenAreCorrect() {
        val startTime = getInstant(2022, 6, day = 5, hourOfDay = 14, minute = 20)!!
        val endTime = getInstant(2022, 6, day = 5, hourOfDay = 20, minute = 20)!!
        val fullHours = AxisDrawable.getInstantsBetween(
            startTime = startTime,
            endTime = endTime,
            stepSizeInHours = 1
        )
        assertEquals(6, fullHours.size)
    }

    // @Test
    // fun dateDifferences() {
    //     val fromDate = Instant.now().minus(2, ChronoUnit.DAYS).plus(3, ChronoUnit.HOURS)
    //     val text = getTimeDifferenceText(fromDate, Instant.now())
    //     assertEquals("1,9 days", text)
    // }

    @Test
    fun dateRange() {
        val firstIngestionTime =
            getInstant(year = 2022, month = 9, day = 23, hourOfDay = 14, minute = 20)!!
        val lastIngestionTime =
            getInstant(year = 2022, month = 9, day = 23, hourOfDay = 23, minute = 20)!!
        val selectedDate =
            getInstant(year = 2022, month = 9, day = 21, hourOfDay = 23, minute = 20)!!
        assertFalse(
            selectedDate.minus(
                12,
                ChronoUnit.HOURS
            ) < lastIngestionTime && selectedDate.plus(12, ChronoUnit.HOURS) > firstIngestionTime
        )
    }

    @Test
    fun testTimeZone() {
        val instant = getInstant(year = 2022, month = 9, day = 23, hourOfDay = 9, minute = 20)!!
        assertEquals("09:20", instant.getStringOfPattern("HH:mm"))
    }
}