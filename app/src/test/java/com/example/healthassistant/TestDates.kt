package com.example.healthassistant

import com.example.healthassistant.ui.home.experience.timeline.AxisDrawable
import org.junit.Assert.assertEquals
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
}