/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.journal.experience.editingestion

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.time.DatePickerButton
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.time.TimePickerButton
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.CardWithTitle
import com.isaakhanimann.journal.ui.theme.JournalTheme
import com.isaakhanimann.journal.ui.theme.horizontalPadding
import com.isaakhanimann.journal.ui.utils.getStringOfPattern
import java.time.LocalDateTime


@Composable
fun EditIngestionScreen(
    viewModel: EditIngestionViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    EditIngestionScreen(
        note = viewModel.note,
        onNoteChange = { viewModel.note = it },
        isEstimate = viewModel.isEstimate,
        toggleIsEstimate = viewModel::toggleIsEstimate,
        isKnown = viewModel.isKnown,
        toggleIsKnown = viewModel::toggleIsKnown,
        dose = viewModel.dose,
        onDoseChange = { viewModel.dose = it },
        units = viewModel.units,
        onUnitsChange = { viewModel.units = it },
        experiences = viewModel.relevantExperiences.collectAsState().value,
        selectedExperienceId = viewModel.experienceId,
        onChangeId = { viewModel.experienceId = it },
        navigateBack = navigateBack,
        deleteIngestion = viewModel::deleteIngestion,
        onDone = {
            viewModel.onDoneTap()
            navigateBack()
        },
        localDateTime = viewModel.localDateTimeFlow.collectAsState().value,
        onTimeChange = viewModel::onChangeTime
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditIngestionScreenPreview() {
    JournalTheme {
        EditIngestionScreen(
            note = "This is my note",
            onNoteChange = {},
            isEstimate = false,
            toggleIsEstimate = {},
            isKnown = true,
            toggleIsKnown = {},
            dose = "5",
            onDoseChange = {},
            units = "mg",
            onUnitsChange = {},
            experiences = emptyList(),
            selectedExperienceId = 2,
            onChangeId = {},
            navigateBack = {},
            deleteIngestion = {},
            onDone = {},
            localDateTime = LocalDateTime.now(),
            onTimeChange = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditIngestionScreen(
    note: String,
    onNoteChange: (String) -> Unit,
    isEstimate: Boolean,
    toggleIsEstimate: () -> Unit,
    isKnown: Boolean,
    toggleIsKnown: () -> Unit,
    dose: String,
    onDoseChange: (String) -> Unit,
    units: String,
    onUnitsChange: (String) -> Unit,
    experiences: List<ExperienceOption>,
    selectedExperienceId: Int,
    onChangeId: (Int) -> Unit,
    navigateBack: () -> Unit,
    deleteIngestion: () -> Unit,
    onDone: () -> Unit,
    localDateTime: LocalDateTime,
    onTimeChange: (LocalDateTime) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Ingestion") },
                actions = {
                    var isShowingDeleteDialog by remember { mutableStateOf(false) }
                    AnimatedVisibility(visible = isShowingDeleteDialog) {
                        AlertDialog(
                            onDismissRequest = { isShowingDeleteDialog = false },
                            title = {
                                Text(text = "Delete Ingestion?")
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        isShowingDeleteDialog = false
                                        deleteIngestion()
                                        navigateBack()
                                    }
                                ) {
                                    Text("Delete")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { isShowingDeleteDialog = false }
                                ) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }
                    IconButton(
                        onClick = { isShowingDeleteDialog = true },
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete Ingestion",
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onDone,
                icon = {
                    Icon(
                        Icons.Filled.Done,
                        contentDescription = "Done Icon"
                    )
                },
                text = { Text("Done") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = horizontalPadding)
        ) {
            Spacer(modifier = Modifier.height(3.dp))
            val focusManager = LocalFocusManager.current
            CardWithTitle(title = "Dose") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(onClick = toggleIsKnown)
                        .fillMaxWidth()
                ) {
                    Checkbox(checked = isKnown, onCheckedChange = { toggleIsKnown() })
                    Text("Dose is known")
                }
                AnimatedVisibility(visible = isKnown) {
                    Column {
                        OutlinedTextField(
                            value = units,
                            onValueChange = onUnitsChange,
                            label = { Text(text = "Units") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()
                            }),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = dose,
                            onValueChange = onDoseChange,
                            label = { Text(text = "Dose") },
                            trailingIcon = { Text(text = units) },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()
                            }),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable(onClick = toggleIsEstimate)
                        ) {
                            Checkbox(checked = isEstimate, onCheckedChange = { toggleIsEstimate() })
                            Text("Dose is an estimate")
                        }
                    }
                }
            }
            CardWithTitle(title = "Notes") {
                OutlinedTextField(
                    value = note,
                    onValueChange = onNoteChange,
                    label = { Text(text = "Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                    }),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    singleLine = true
                )
            }
            CardWithTitle(title = "Time") {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    DatePickerButton(
                        localDateTime = localDateTime,
                        onChange = onTimeChange,
                        dateString = localDateTime.getStringOfPattern("EEE dd MMM yyyy"),
                        modifier = Modifier.fillMaxWidth()
                    )
                    TimePickerButton(
                        localDateTime = localDateTime,
                        onChange = onTimeChange,
                        timeString = localDateTime.getStringOfPattern("HH:mm"),
                        modifier = Modifier.fillMaxWidth()
                    )
                    var isShowingDropDownMenu by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .wrapContentSize(Alignment.TopEnd)
                    ) {
                        val selectedOption =
                            experiences.firstOrNull { it.id == selectedExperienceId }
                        OutlinedButton(
                            onClick = { isShowingDropDownMenu = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = if (selectedOption?.title != null) "Part of " + selectedOption.title else "Part of Unknown Experience")
                        }
                        DropdownMenu(
                            expanded = isShowingDropDownMenu,
                            onDismissRequest = { isShowingDropDownMenu = false }
                        ) {
                            experiences.forEach { experience ->
                                DropdownMenuItem(
                                    text = { Text(experience.title) },
                                    onClick = {
                                        onChangeId(experience.id)
                                        isShowingDropDownMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}