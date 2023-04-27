/*
 * Copyright (c) 2023. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.journal.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import java.text.DecimalFormat
import java.time.Duration
import java.time.LocalDateTime

@Composable
fun RelativeDateText(
    dateTime: LocalDateTime,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    val now: MutableState<LocalDateTime> = remember { mutableStateOf(LocalDateTime.now()) }
    LaunchedEffect(key1 = "updateTime") {
        while (true) {
            delay(60000L) // update every minute
            now.value = LocalDateTime.now()
        }
    }
    val duration = Duration.between(dateTime, now.value)
    val relativeTime = when {
        duration.toDays() > 500 -> "${roundToOneDecimal(duration.toDays().toDouble()/365.0)} years ago"
        duration.toDays() > 60 -> "${roundToOneDecimal(duration.toDays().toDouble()/30.0)} months ago"
        duration.toDays() > 20 -> "${roundToOneDecimal(duration.toDays().toDouble()/7.0)} weeks ago"
        duration.toDays() > 0 -> "${duration.toDays()} days ago"
        duration.toHours() > 0 -> "${duration.toHours()} hours ago"
        duration.toMinutes() > 0 -> "${duration.toMinutes()} minutes ago"
        else -> "just now"
    }
    Text(
        text = relativeTime,
        style = style
    )
}

private fun roundToOneDecimal(num: Double): String {
    val roundedNum = "%.1f".format(num).toDouble()
    val df = DecimalFormat("#.#")
    return df.format(roundedNum)
}

@Preview(showBackground = true)
@Composable
fun RelativeDateTextPreview() {
    Column {
        val dateTime1 = remember { LocalDateTime.now().minusDays(3) }
        val dateTime2 = remember { LocalDateTime.now().minusHours(5) }
        val dateTime3 = remember { LocalDateTime.now().minusMinutes(30) }
        RelativeDateText(dateTime = dateTime1)
        RelativeDateText(dateTime = dateTime2)
        RelativeDateText(dateTime = dateTime3)
    }
}