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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.isaakhanimann.journal.ui.tabs.journal.components.roundToOneDecimal
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue

fun getDurationText(fromInstant: Instant, toInstant: Instant): String {
    val duration = Duration.between(fromInstant, toInstant)
    return when {
        duration.toDays().absoluteValue > 500 -> {
            val years = roundToOneDecimal(duration.toDays().toDouble().absoluteValue / 365.0)
            "$years years"
        }

        duration.toDays().absoluteValue > 60 -> {
            val months = roundToOneDecimal(duration.toDays().toDouble().absoluteValue / 30.0)
            "$months months"
        }

        duration.toDays().absoluteValue > 20 -> {
            val weeks = roundToOneDecimal(duration.toDays().toDouble().absoluteValue / 7.0)
            "$weeks weeks"
        }

        duration.toDays().absoluteValue > 4 -> {
            val days = duration.toDays().absoluteValue
            "$days days"
        }

        duration.toHours().absoluteValue > 40 -> {
            val days = roundToOneDecimal(duration.toHours().toDouble().absoluteValue / 24.0)
            "$days days"
        }

        duration.toHours().absoluteValue > 1 -> {
            val hours = roundToOneDecimal(duration.toMinutes().toDouble().absoluteValue / 60.0)
            "$hours hr"
        }

        duration.toMinutes().absoluteValue > 0 -> {
            val minutes = duration.toMinutes().absoluteValue
            "$minutes min"
        }

        else -> {
            val seconds = (duration.toMillis().toDouble().absoluteValue / 1000.0).toInt()
            "$seconds s"
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimeRelativeToStartTextPreview() {
    Column {

        Text(
            text = getDurationText(
                fromInstant = Instant.now().minus(4, ChronoUnit.HOURS),
                toInstant = Instant.now()
            )
        )
        Text(
            text = getDurationText(
                fromInstant = Instant.now().minus(20, ChronoUnit.MINUTES),
                toInstant = Instant.now()
            )
        )
    }
}