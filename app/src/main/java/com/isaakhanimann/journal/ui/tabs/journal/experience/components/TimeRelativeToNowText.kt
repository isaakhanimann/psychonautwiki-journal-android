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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.isaakhanimann.journal.ui.tabs.journal.components.roundToOneDecimal
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue

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
    val isInPast = time < now.value
    val relativeTime = when {
        duration.toDays().absoluteValue > 500 -> {
            val years = roundToOneDecimal(duration.toDays().toDouble().absoluteValue / 365.0)
            if (isInPast) {
                "$years years ago"
            } else {
                "in $years years"
            }
        }

        duration.toDays().absoluteValue > 60 -> {
            val months = roundToOneDecimal(duration.toDays().toDouble().absoluteValue / 30.0)
            if (isInPast) {
                "$months months ago"
            } else {
                "in $months months"
            }
        }

        duration.toDays().absoluteValue > 20 -> {
            val weeks = roundToOneDecimal(duration.toDays().toDouble().absoluteValue / 7.0)
            if (isInPast) {
                "$weeks weeks ago"
            } else {
                "in $weeks weeks"
            }
        }

        duration.toHours().absoluteValue > 24 -> {
            val days = roundToOneDecimal(duration.toHours().toDouble().absoluteValue / 24.0)
            if (isInPast) {
                "$days days ago"
            } else {
                "in $days days"
            }
        }

        duration.toHours().absoluteValue > 0 -> {
            val hours = roundToOneDecimal(duration.toMinutes().toDouble().absoluteValue / 60.0)
            if (isInPast) {
                "$hours hours ago"
            } else {
                "in $hours hours"
            }
        }

        duration.toMinutes().absoluteValue > 0 -> {
            val minutes = duration.toMinutes().absoluteValue
            if (isInPast) {
                "$minutes minutes ago"
            } else {
                "in $minutes minutes"
            }
        }

        else -> {
            if (isInPast) {
                "just now"
            } else {
                "now"
            }
        }
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