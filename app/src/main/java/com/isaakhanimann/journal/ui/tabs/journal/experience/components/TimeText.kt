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

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.isaakhanimann.journal.ui.utils.getStringOfPattern
import java.time.Instant

@Composable
fun TimeText(
    time: Instant,
    timeDisplayOption: TimeDisplayOption,
    startTime: Instant,
    style: TextStyle
) {
    when (timeDisplayOption) {
        TimeDisplayOption.REGULAR -> {
            val timeString = time.getStringOfPattern("EEE HH:mm")
            Text(
                text = timeString,
                style = style
            )
        }
        TimeDisplayOption.RELATIVE_TO_NOW -> {
            TimeRelativeToNowText(
                time = time,
                style = style
            )
        }
        TimeDisplayOption.RELATIVE_TO_START -> {
            TimeRelativeToStartText(
                time = time,
                startTime = startTime,
                style = style
            )
        }
    }
}