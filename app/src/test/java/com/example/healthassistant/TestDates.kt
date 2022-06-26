package com.example.healthassistant

import com.example.healthassistant.ui.experiences.experience.timeline.AxisDrawable
import com.example.healthassistant.ui.previewproviders.getDate
import com.example.healthassistant.ui.stats.StatisticsViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestDates {
    @Test
    fun datesBetweenAreCorrect() {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        cal.set(Calendar.HOUR_OF_DAY, 14)
        cal.set(Calendar.MINUTE, 20)
        val startTime = cal.time
        cal.set(Calendar.HOUR_OF_DAY, 20)
        cal.set(Calendar.MINUTE, 20)
        val endTime = cal.time
        val fullHours = AxisDrawable.getDatesBetween(startTime = startTime, endTime = endTime, stepSizeInHours = 1)
        assertEquals(6, fullHours.size)
    }

    @Test
    fun dateCreation() {
        assertNotNull(getDate(year = 2022, month = 7, day = 5, hourOfDay = 14, minute = 20))
        assertNotNull(getDate(year = 2022, month = 7, day = 5, hourOfDay = 12, minute = 30))
    }

    @Test
    fun dateDifferences() {
        val twoDaysInMs = 2*24*60*60*1000
        val threeHours = 3*60*60*1000
        val date = Date(Date().time - twoDaysInMs + threeHours)
        val text = StatisticsViewModel.getTimeTextDifferenceToNow(date)
        assertEquals("2 days", text)
    }
}