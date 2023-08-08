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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.ui.tabs.journal.experience.rating.*
import com.isaakhanimann.journal.ui.tabs.journal.experience.timednote.TimedNoteScreenContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTimedNoteScreen(
    viewModel: EditTimedNoteViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Timed Note") },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.delete()
                            navigateBack()
                        },
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete Note",
                        )
                    }
                    IconButton(onClick = {
                        viewModel.onDoneTap()
                        navigateBack()
                    }) {
                        Icon(
                            Icons.Filled.Done,
                            contentDescription = "Done Icon"
                        )
                    }
                }
            )
        }
    ) { padding ->
        TimedNoteScreenContent(
            selectedTime = viewModel.localDateTimeFlow.collectAsState().value,
            onTimeChange = viewModel::onChangeTime,
            note = viewModel.note,
            onNoteChange = viewModel::onChangeNote,
            color = viewModel.color,
            onColorChange = viewModel::onChangeColor,
            modifier = Modifier.padding(padding),
            alreadyUsedColors = viewModel.alreadyUsedColorsFlow.collectAsState().value,
            otherColors = viewModel.otherColorsFlow.collectAsState().value
        )
    }
}