package com.example.healthassistant

import com.example.healthassistant.ui.home.experience.timeline.AxisDrawable
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun fullHoursAreCorrect() {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        cal.add(Calendar.HOUR_OF_DAY, 14)
        cal.set(Calendar.MINUTE, 20)
        val todayTime = cal.time
        val axisDrawable = AxisDrawable(
            startTime = todayTime,
            width = 6.toDuration(DurationUnit.HOURS)
        )
        val fullHours = axisDrawable.getFullHours(pixelsPerSec = 5f, widthInPixels = 2000f)
        assertEquals(6, fullHours.size)
        assertEquals(fullHours[0].label, "15")
        assertEquals(fullHours[1].label, "16")
        assertEquals(fullHours[2].label, "17")
        assertEquals(fullHours[3].label, "18")
        assertEquals(fullHours[4].label, "19")
        assertEquals(fullHours[5].label, "20")
    }
}