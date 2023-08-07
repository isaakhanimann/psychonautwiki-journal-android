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

package com.isaakhanimann.journal.ui.tabs.journal.experience.components.timednote

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.TimedNote
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.TimeDisplayOption
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.TimeText
import java.time.Instant
import java.time.temporal.ChronoUnit

@Preview(showBackground = true)
@Composable
fun TimedNoteRowPreview() {
    TimedNoteRow(
        timedNote = TimedNote(
            time = Instant.now(),
            note = "Hello my name is",
            color = AdaptiveColor.PURPLE,
            experienceId = 0
        ),
        timeDisplayOption = TimeDisplayOption.REGULAR,
        startTime = Instant.now().minus(3, ChronoUnit.HOURS),
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun TimedNoteRow(
    timedNote: TimedNote,
    timeDisplayOption: TimeDisplayOption,
    startTime: Instant,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.height(intrinsicSize = IntrinsicSize.Min)
    ) {
        val isDarkTheme = isSystemInDarkTheme()
        val strokeWidth = 3.dp
        Canvas(modifier = Modifier
            .fillMaxHeight()
            .width(strokeWidth)
            .padding(vertical = 5.dp)) {
            val strokeWidthPx = strokeWidth.toPx()
            drawLine(
                color = timedNote.color.getComposeColor(isDarkTheme),
                start = Offset(x = size.width/2, y = 0f),
                end = Offset(x = size.width/2, y = size.height),
                strokeWidth = strokeWidthPx,
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(
                    floatArrayOf(
                        strokeWidthPx,
                        strokeWidthPx * 2
                    )
                )
            )
        }
        Column {
            TimeText(
                time = timedNote.time,
                timeDisplayOption = timeDisplayOption,
                startTime = startTime,
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = timedNote.note, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
