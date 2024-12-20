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

package com.isaakhanimann.journal.ui.utils

import java.text.DateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun getInstant(year: Int, month: Int, day: Int, hourOfDay: Int, minute: Int): Instant? {
    val dateTime = LocalDateTime.of(year, month, day, hourOfDay, minute)
    return dateTime.atZone(ZoneId.systemDefault()).toInstant()
}

fun Instant.getStringOfPattern(pattern: String): String {
    val dateTime = LocalDateTime.ofInstant(this, ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return dateTime.format(formatter)
}

fun Instant.getDateWithWeekdayText(): String {
    return getStringOfPattern("EEE dd MMM yyyy")
}

fun Instant.getShortWeekdayText(): String {
    return getStringOfPattern("EEE")
}

fun Instant.getShortTimeWithWeekdayText(): String {
    return getShortWeekdayText() + " " + getShortTimeText()
}

fun Instant.getShortTimeText(): String {
    val timeFormat: DateFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault())
    val date = Date.from(this)
    return timeFormat.format(date)
}

fun LocalDateTime.getStringOfPattern(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return format(formatter)
}

fun LocalDateTime.getDateWithWeekdayText(): String {
    return getStringOfPattern("EEE dd MMM yyyy")
}

fun LocalDateTime.getShortTimeText(): String {
    val instant = getInstant()
    return instant.getShortTimeText()
}

fun Instant.getLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(this, ZoneId.systemDefault())
}

fun LocalDateTime.getInstant(): Instant {
    return atZone(ZoneId.systemDefault()).toInstant()
}