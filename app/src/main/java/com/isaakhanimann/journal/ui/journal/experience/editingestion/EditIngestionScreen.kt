/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.journal.experience.editingestion

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
import com.isaakhanimann.journal.ui.addingestion.time.DatePickerButton
import com.isaakhanimann.journal.ui.addingestion.time.TimePickerButton
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
        onIsEstimateChange = { viewModel.isEstimate = it },
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
            onIsEstimateChange = {},
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
    onIsEstimateChange: (Boolean) -> Unit,
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
            TopAppBar(title = { Text("Edit Ingestion") })
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
            Spacer(modifier = Modifier.height(10.dp))
            val focusManager = LocalFocusManager.current
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
                modifier = Modifier.clickable { onIsEstimateChange(isEstimate.not()) }) {
                Checkbox(checked = isEstimate, onCheckedChange = onIsEstimateChange)
                Text("Dose is an estimate")
            }
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
            }
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
            Spacer(modifier = Modifier.height(10.dp))
            var isShowingDropDownMenu by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopEnd)
            ) {
                val selectedOption = experiences.firstOrNull { it.id == selectedExperienceId }
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
            var isShowingDeleteDialog by remember { mutableStateOf(false) }
            OutlinedButton(
                onClick = { isShowingDeleteDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Ingestion")
            }
            if (isShowingDeleteDialog) {
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
        }
    }
}