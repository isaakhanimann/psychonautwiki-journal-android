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

package com.isaakhanimann.journal.ui.tabs.settings.customunits.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.data.substances.classes.roa.DoseClass
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDose
import com.isaakhanimann.journal.ui.tabs.journal.experience.rating.FloatingDoneButton
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.dose.RoaDosePreviewProvider
import com.isaakhanimann.journal.ui.tabs.settings.customunits.add.EditCustomUnitSections

@Composable
fun EditCustomUnitScreen(
    navigateBack: () -> Unit,
    viewModel: EditCustomUnitViewModel = hiltViewModel()
) {
    EditCustomUnitScreenContent(
        substanceName = viewModel.substanceName,
        administrationRoute = viewModel.administrationRoute,
        roaDose = viewModel.roaDose,
        dismiss = {
            viewModel.updateAndDismissAfter(dismiss = navigateBack)
        },
        name = viewModel.name,
        onChangeOfName = viewModel::onChangeOfName,
        doseText = viewModel.doseText,
        onChangeDoseText = viewModel::onChangeOfDose,
        estimatedDoseStandardDeviationText = viewModel.estimatedDoseDeviationText,
        onChangeEstimatedDoseStandardDeviationText = viewModel::onChangeOfEstimatedDoseDeviation,
        isEstimate = viewModel.isEstimate,
        onChangeIsEstimate = viewModel::onChangeOfIsEstimate,
        currentDoseClass = viewModel.currentDoseClass,
        isShowingUnitsField = viewModel.roaDose?.units?.isBlank() ?: true,
        unit = viewModel.unit,
        onChangeOfUnits = viewModel::onChangeOfUnit,
        originalUnit = viewModel.originalUnit,
        onChangeOfOriginalUnit = viewModel::onChangeOfOriginalUnit,
        note = viewModel.note,
        onChangeOfNote = viewModel::onChangeOfNote,
        isArchived = viewModel.isArchived,
        onChangeOfIsArchived = viewModel::onChangeOfIsArchived,
        onDelete = viewModel::deleteCustomUnit
    )
}

@Preview
@Composable
private fun EditCustomUnitScreenPreview(
    @PreviewParameter(RoaDosePreviewProvider::class) roaDose: RoaDose,
) {
    EditCustomUnitScreenContent(
        substanceName = "Example",
        administrationRoute = AdministrationRoute.ORAL,
        roaDose = roaDose,
        dismiss = {},
        name = "Pink rocket",
        onChangeOfName = {},
        doseText = "10",
        onChangeDoseText = {},
        estimatedDoseStandardDeviationText = "",
        onChangeEstimatedDoseStandardDeviationText = {},
        isEstimate = true,
        onChangeIsEstimate = {},
        currentDoseClass = DoseClass.LIGHT,
        isShowingUnitsField = false,
        unit = "pill",
        onChangeOfUnits = {},
        originalUnit = "mg",
        onChangeOfOriginalUnit = {},
        note = "",
        onChangeOfNote = {},
        isArchived = false,
        onChangeOfIsArchived = {},
        onDelete = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditCustomUnitScreenContent(
    substanceName: String,
    administrationRoute: AdministrationRoute,
    roaDose: RoaDose?,
    dismiss: () -> Unit,
    name: String,
    onChangeOfName: (String) -> Unit,
    doseText: String,
    onChangeDoseText: (String) -> Unit,
    estimatedDoseStandardDeviationText: String,
    onChangeEstimatedDoseStandardDeviationText: (String) -> Unit,
    isEstimate: Boolean,
    onChangeIsEstimate: (Boolean) -> Unit,
    currentDoseClass: DoseClass?,
    isShowingUnitsField: Boolean,
    unit: String,
    onChangeOfUnits: (units: String) -> Unit,
    originalUnit: String,
    onChangeOfOriginalUnit: (String) -> Unit,
    note: String,
    onChangeOfNote: (String) -> Unit,
    isArchived: Boolean,
    onChangeOfIsArchived: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$substanceName unit") },
                actions = {
                    var isShowingDeleteDialog by remember { mutableStateOf(false) }
                    IconButton(onClick = { isShowingDeleteDialog = true }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete custom unit"
                        )
                    }
                    AnimatedVisibility(visible = isShowingDeleteDialog) {
                        AlertDialog(
                            onDismissRequest = { isShowingDeleteDialog = false },
                            title = {
                                Text(text = "Delete custom unit?")
                            },
                            text = {
                                Text("This will affect all ingestions that are using it. Consider archiving it instead.")
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        isShowingDeleteDialog = false
                                        onDelete()
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
            )
        },
        floatingActionButton = {
            FloatingDoneButton(
                onDone = dismiss,
                modifier = Modifier.imePadding(),
            )
        }
    ) { padding ->
        EditCustomUnitSections(
            substanceName = substanceName,
            administrationRoute = administrationRoute,
            padding = padding,
            roaDose = roaDose,
            name = name,
            onChangeOfName = onChangeOfName,
            doseText = doseText,
            onChangeDoseText = onChangeDoseText,
            estimatedDoseStandardDeviationText = estimatedDoseStandardDeviationText,
            onChangeEstimatedDoseStandardDeviationText = onChangeEstimatedDoseStandardDeviationText,
            isEstimate = isEstimate,
            onChangeIsEstimate = onChangeIsEstimate,
            currentDoseClass = currentDoseClass,
            isShowingUnitsField = isShowingUnitsField,
            unit = unit,
            onChangeOfUnits = onChangeOfUnits,
            originalUnit = originalUnit,
            onChangeOfOriginalUnit = onChangeOfOriginalUnit,
            note = note,
            onChangeOfNote = onChangeOfNote,
            isArchived = isArchived,
            onChangeOfIsArchived = onChangeOfIsArchived
        )
    }
}