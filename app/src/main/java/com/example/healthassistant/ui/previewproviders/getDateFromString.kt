package com.example.healthassistant.ui.previewproviders

import java.util.*

fun getDate(year: Int, month: Int, day: Int, hourOfDay: Int, minute: Int): Date? {
    val cal = Calendar.getInstance(TimeZone.getDefault())
    cal.set(Calendar.YEAR, year)
    cal.set(Calendar.MONTH, month)
    cal.set(Calendar.DAY_OF_MONTH, day)
    cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
    cal.set(Calendar.MINUTE, minute)
    return cal.time
}