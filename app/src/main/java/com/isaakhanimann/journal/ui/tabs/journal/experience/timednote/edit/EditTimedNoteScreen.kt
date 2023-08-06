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

package com.isaakhanimann.journal.ui.tabs.journal.experience.timednote.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.ui.tabs.journal.experience.rating.*
import java.time.LocalDateTime

@Preview
@Composable
fun EditTimedNoteScreenPreview() {
    EditTimedNoteScreen(
        onDone = {},
        selectedTime = LocalDateTime.now(),
        onTimeChange = {},
        onDelete = {},
        note = "Hello this is",
        onNoteChange = {}
    )
}

@Composable
fun EditTimedNoteScreen(
    viewModel: EditTimedNoteViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    EditTimedNoteScreen(
        onDone = {
            viewModel.onDoneTap()
            navigateBack()
        },
        selectedTime = viewModel.localDateTimeFlow.collectAsState().value,
        onTimeChange = viewModel::onChangeTime,
        onDelete = {
            viewModel.delete()
            navigateBack()
        },
        note = viewModel.note,
        onNoteChange = viewModel::onChangeNote
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTimedNoteScreen(
    onDone: () -> Unit,
    selectedTime: LocalDateTime,
    onTimeChange: (LocalDateTime) -> Unit,
    onDelete: () -> Unit,
    note: String,
    onNoteChange: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Timed Note") },
                actions = {
                    IconButton(
                        onClick = { onDelete() },
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete Note",
                        )
                    }
                    IconButton(onClick = onDone) {
                        Icon(
                            Icons.Filled.Done,
                            contentDescription = "Done Icon"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(10.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TimePickerSection(selectedTime = selectedTime, onTimeChange = onTimeChange)
            OutlinedTextField(
                value = note,
                onValueChange = onNoteChange,
                label = { Text(text = "Note") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Default,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }
    }
}