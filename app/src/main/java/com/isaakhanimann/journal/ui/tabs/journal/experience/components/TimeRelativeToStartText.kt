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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.isaakhanimann.journal.ui.tabs.journal.components.roundToOneDecimal
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

@Composable
fun TimeRelativeToStartText(
    time: Instant,
    startTime: Instant,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    val duration = Duration.between(startTime, time)
    val relativeTime = when {
        duration.toDays() > 3 -> "${duration.toDays()} days in"
        duration.toHours() > 24 -> "${roundToOneDecimal(duration.toHours().toDouble()/24.0)} days in"
        duration.toHours() > 1 -> "${roundToOneDecimal(duration.toMinutes().toDouble()/60.0)} hours in"
        duration.toMinutes() > 0 -> "${duration.toMinutes()} minutes in"
        else -> "Start"
    }
    Text(
        text = relativeTime,
        style = style
    )
}

@Preview(showBackground = true)
@Composable
fun TimeRelativeToStartTextPreview() {
    Column {
        TimeRelativeToStartText(time = Instant.now(), startTime = Instant.now().minus(4, ChronoUnit.HOURS))
        TimeRelativeToStartText(time = Instant.now(), startTime = Instant.now().minus(20, ChronoUnit.MINUTES))
    }
}