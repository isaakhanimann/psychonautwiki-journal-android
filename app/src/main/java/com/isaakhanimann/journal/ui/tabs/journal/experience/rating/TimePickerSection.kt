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

package com.isaakhanimann.journal.ui.tabs.journal.experience.rating

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.time.DatePickerButton
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.time.TimePickerButton
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.CardWithTitle
import com.isaakhanimann.journal.ui.utils.getStringOfPattern
import java.time.LocalDateTime

@Preview
@Composable
fun TimePickerSectionPreview() {
    TimePickerSection(selectedTime = LocalDateTime.now(), onTimeChange = {})
}

@Composable
fun TimePickerSection(
    selectedTime: LocalDateTime,
    onTimeChange: (LocalDateTime) -> Unit,
) {
    CardWithTitle(title = "Time") {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DatePickerButton(
                localDateTime = selectedTime,
                onChange = onTimeChange,
                dateString = selectedTime.getStringOfPattern("EEE dd MMM yyyy"),
                modifier = Modifier.fillMaxWidth()
            )
            TimePickerButton(
                localDateTime = selectedTime,
                onChange = onTimeChange,
                timeString = selectedTime.getStringOfPattern("HH:mm"),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}