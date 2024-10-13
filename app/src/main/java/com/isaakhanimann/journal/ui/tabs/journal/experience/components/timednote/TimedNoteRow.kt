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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.data.room.experiences.entities.TimedNote

@Preview(showBackground = true)
@Composable
fun TimedNoteRowPreview(@PreviewParameter(TimedNotePreviewProvider::class) timedNote: TimedNote) {
    TimedNoteRow(
        timedNote = timedNote,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Sat 7:34")
    }
}


@Composable
fun TimedNoteRow(
    timedNote: TimedNote,
    modifier: Modifier = Modifier,
    timeText: @Composable () -> Unit
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
            if (timedNote.isPartOfTimeline) {
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
        }
        Column {
            Row(horizontalArrangement = Arrangement.spacedBy(15.dp), verticalAlignment = Alignment.Bottom) {
                timeText()
                if (!timedNote.isPartOfTimeline) {
                    Text(text = "(Not in timeline)", style = MaterialTheme.typography.bodySmall)
                }
            }
            Text(text = timedNote.note, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
