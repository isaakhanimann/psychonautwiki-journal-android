package com.isaakhanimann.healthassistant.ui.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun getInstant(year: Int, month: Int, day: Int, hourOfDay: Int, minute: Int): Instant? {
    val dateTime = LocalDateTime.of(year, month, day, hourOfDay, minute)
    return dateTime.atZone(ZoneId.systemDefault()).toInstant()
}

fun Instant.getStringOfPattern(pattern: String): String {
    val dateTime = LocalDateTime.ofInstant(this, ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return dateTime.format(formatter)
}