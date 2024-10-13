/*
 * Copyright (c) 2024. Isaak Hanimann.
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

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.isaakhanimann.journal.ui.utils.getStringOfPattern
import java.time.Instant

@Composable
fun TimeOrDurationText(
    time: Instant,
    index: Int,
    timeDisplayOption: TimeDisplayOption,
    allTimesSortedMap: List<Instant>
) {
   val isFirstIngestion = index == 0
    when (timeDisplayOption) {
        TimeDisplayOption.REGULAR -> {
            val timeString = time.getStringOfPattern("EEE HH:mm")
            Text(
                text = timeString,
                style = MaterialTheme.typography.titleSmall
            )
        }

        TimeDisplayOption.RELATIVE_TO_NOW -> {
            TimeRelativeToNowText(
                time = time,
                style = MaterialTheme.typography.titleSmall
            )
        }

        TimeDisplayOption.TIME_BETWEEN -> {
            if (isFirstIngestion) {
                val timeString = time.getStringOfPattern("EEE HH:mm")
                Text(
                    text = timeString,
                    style = MaterialTheme.typography.titleSmall
                )
            } else {
                val previousTime =
                    allTimesSortedMap[index - 1]
                Text(
                    text = getDurationText(
                        fromInstant = previousTime,
                        toInstant = time
                    ) + " later",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }

        TimeDisplayOption.RELATIVE_TO_START -> {
            if (isFirstIngestion) {
                val timeString = time.getStringOfPattern("EEE HH:mm")
                Text(
                    text = timeString,
                    style = MaterialTheme.typography.titleSmall
                )
            } else {
                Text(
                    text = getDurationText(
                        fromInstant = allTimesSortedMap.firstOrNull()
                            ?: Instant.now(),
                        toInstant = time
                    ) + " in",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}