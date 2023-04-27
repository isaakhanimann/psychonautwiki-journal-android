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

package com.isaakhanimann.journal.ui.tabs.journal.experience.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.isaakhanimann.journal.ui.tabs.journal.components.roundToOneDecimal
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

@Composable
fun TimeRelativeToNowText(
    time: Instant,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    val now: MutableState<Instant> = remember { mutableStateOf(Instant.now()) }
    LaunchedEffect(key1 = "updateTime") {
        while (true) {
            delay(10000L) // update every 10 seconds
            now.value = Instant.now()
        }
    }
    val duration = Duration.between(time, now.value)
    val relativeTime = when {
        duration.toDays() > 500 -> "${roundToOneDecimal(duration.toDays().toDouble()/365.0)} years ago"
        duration.toDays() > 60 -> "${roundToOneDecimal(duration.toDays().toDouble()/30.0)} months ago"
        duration.toDays() > 20 -> "${roundToOneDecimal(duration.toDays().toDouble()/7.0)} weeks ago"
        duration.toHours() > 24 -> "${roundToOneDecimal(duration.toHours().toDouble()/24.0)} days ago"
        duration.toHours() > 0 -> "${roundToOneDecimal(duration.toMinutes().toDouble()/60.0)} hours ago"
        duration.toMinutes() > 0 -> "${duration.toMinutes()} minutes ago"
        else -> "just now"
    }
    Text(
        text = relativeTime,
        style = style
    )
}

@Preview(showBackground = true)
@Composable
fun TimeRelativeToNowTextPreview() {
    Column {
        val dateTime1 = remember { Instant.now().minus(5, ChronoUnit.HOURS) }
        val dateTime2 = remember { Instant.now().minus(30, ChronoUnit.MINUTES) }
        TimeRelativeToNowText(time = dateTime1)
        TimeRelativeToNowText(time = dateTime2)
    }
}