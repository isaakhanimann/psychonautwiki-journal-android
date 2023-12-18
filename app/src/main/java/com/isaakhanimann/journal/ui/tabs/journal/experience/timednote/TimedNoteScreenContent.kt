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

package com.isaakhanimann.journal.ui.tabs.journal.experience.timednote

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.time.ColorPicker
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.CardWithTitle
import com.isaakhanimann.journal.ui.tabs.journal.experience.rating.TimePickerSection
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TimedNoteScreenContentPreview() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add timed note") },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Filled.Done,
                            contentDescription = "Done Icon"
                        )
                    }
                }
            )
        },
    ) { padding ->
        val alreadyUsedColors = listOf(AdaptiveColor.BLUE, AdaptiveColor.PINK)
        val otherColors = AdaptiveColor.values().filter { color ->
            !alreadyUsedColors.contains(color)
        }
        TimedNoteScreenContent(
            selectedTime = LocalDateTime.now(),
            onTimeChange = {},
            note = "Hello this is",
            onNoteChange = {},
            color = AdaptiveColor.PURPLE,
            onColorChange = {},
            modifier = Modifier
                .padding(padding),
            alreadyUsedColors = alreadyUsedColors,
            otherColors = otherColors,
            isPartOfTimeline = true,
            onChangeOfIsPartOfTimeline = {}
        )
    }
}

@Composable
fun TimedNoteScreenContent(
    selectedTime: LocalDateTime,
    onTimeChange: (LocalDateTime) -> Unit,
    note: String,
    onNoteChange: (String) -> Unit,
    color: AdaptiveColor,
    onColorChange: (AdaptiveColor) -> Unit,
    alreadyUsedColors: List<AdaptiveColor>,
    otherColors: List<AdaptiveColor>,
    isPartOfTimeline: Boolean,
    onChangeOfIsPartOfTimeline: (Boolean) -> Unit,
    shouldFocusTextFieldOnAppear: Boolean = false,
    modifier: Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        if (shouldFocusTextFieldOnAppear) {
            focusRequester.requestFocus()
        }
    }
    Column(
        modifier = modifier
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = note,
            onValueChange = onNoteChange,
            label = { Text(text = "Note") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )
        Spacer(modifier = Modifier.height(5.dp))
        CardWithTitle(title = "Show on timeline") {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Switch(checked = isPartOfTimeline, onCheckedChange = onChangeOfIsPartOfTimeline)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ColorPicker(
                        selectedColor = color,
                        onChangeOfColor = onColorChange,
                        alreadyUsedColors = alreadyUsedColors,
                        otherColors = otherColors
                    )
                }
            }
        }
        TimePickerSection(selectedTime = selectedTime, onTimeChange = onTimeChange)
    }
}