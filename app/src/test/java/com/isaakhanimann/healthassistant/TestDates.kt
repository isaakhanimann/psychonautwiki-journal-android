package com.isaakhanimann.healthassistant

import com.isaakhanimann.healthassistant.ui.journal.experience.timeline.drawables.AxisDrawable
import com.isaakhanimann.healthassistant.ui.utils.getInstant
import com.isaakhanimann.healthassistant.ui.utils.getStringOfPattern
import com.isaakhanimann.healthassistant.ui.utils.getTimeDifferenceText
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

    @Test
    fun dateDifferences() {
        val fromDate = Instant.now().minus(2, ChronoUnit.DAYS).plus(3, ChronoUnit.HOURS)
        val text = getTimeDifferenceText(fromDate, Instant.now())
        assertEquals("1,9 days", text)
    }

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