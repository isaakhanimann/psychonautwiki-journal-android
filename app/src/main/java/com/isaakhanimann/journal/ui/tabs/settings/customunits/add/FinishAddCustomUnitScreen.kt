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

package com.isaakhanimann.journal.ui.tabs.settings.customunits.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.substances.classes.roa.DoseClass
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDose
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.dose.CurrentDoseClassInfo
import com.isaakhanimann.journal.ui.tabs.journal.experience.rating.FloatingDoneButton
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.dose.RoaDosePreviewProvider
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.dose.RoaDoseView
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun FinishAddCustomUnitScreen(
    dismissAddCustomUnit: () -> Unit,
    viewModel: FinishAddCustomUnitViewModel = hiltViewModel()
) {
    FinishAddCustomUnitScreenContent(
        substanceName = viewModel.substanceName,
        roaDose = viewModel.roaDose,
        dismiss = {
            viewModel.createSaveAndDismissAfter(dismiss = dismissAddCustomUnit)
        },
        name = viewModel.name,
        onChangeOfName = viewModel::onChangeOfName,
        doseText = viewModel.doseText,
        onChangeDoseText = viewModel::onChangeOfDose,
        estimatedDoseVarianceText = viewModel.estimatedDoseVarianceText,
        onChangeEstimatedDoseVarianceText = viewModel::onChangeOfEstimatedDoseVariance,
        isEstimate = viewModel.isEstimate,
        onChangeIsEstimate = viewModel::onChangeOfIsEstimate,
        currentDoseClass = viewModel.currentDoseClass,
        isShowingUnitsField = viewModel.roaDose?.units?.isBlank() ?: true,
        unit = viewModel.unit,
        onChangeOfUnits = viewModel::onChangeOfUnit,
        originalUnit = viewModel.originalUnit,
        onChangeOfOriginalUnit = viewModel::onChangeOfOriginalUnit,
        note = viewModel.note,
        onChangeOfNote = viewModel::onChangeOfNote
    )
}

@Preview
@Composable
private fun FinishAddCustomUnitScreenPreview(
    @PreviewParameter(RoaDosePreviewProvider::class) roaDose: RoaDose,
    ) {
    FinishAddCustomUnitScreenContent(
        substanceName = "Example",
        roaDose = roaDose,
        dismiss = {},
        name = "Pink rocket",
        onChangeOfName = {},
        doseText = "10",
        onChangeDoseText = {},
        estimatedDoseVarianceText = "",
        onChangeEstimatedDoseVarianceText = {},
        isEstimate = true,
        onChangeIsEstimate = {},
        currentDoseClass = DoseClass.LIGHT,
        isShowingUnitsField = false,
        unit = "pill",
        onChangeOfUnits = {},
        originalUnit = "mg",
        onChangeOfOriginalUnit = {},
        note = "",
        onChangeOfNote = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FinishAddCustomUnitScreenContent(
    substanceName: String,
    roaDose: RoaDose?,
    dismiss: () -> Unit,
    name: String,
    onChangeOfName: (String) -> Unit,
    doseText: String,
    onChangeDoseText: (String) -> Unit,
    estimatedDoseVarianceText: String,
    onChangeEstimatedDoseVarianceText: (String) -> Unit,
    isEstimate: Boolean,
    onChangeIsEstimate: (Boolean) -> Unit,
    currentDoseClass: DoseClass?,
    isShowingUnitsField: Boolean,
    unit: String,
    onChangeOfUnits: (units: String) -> Unit,
    originalUnit: String,
    onChangeOfOriginalUnit: (String) -> Unit,
    note: String,
    onChangeOfNote: (String) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("$substanceName Unit") }) },
        floatingActionButton = {
            FloatingDoneButton {
                dismiss()
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            val textStyle = MaterialTheme.typography.titleMedium
            val focusRequesterName = remember { FocusRequester() }
            val focusRequesterUnit = remember { FocusRequester() }
            val focusRequesterNote = remember { FocusRequester() }
            val focusRequesterDose = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current

            LaunchedEffect(Unit) {
                focusRequesterName.requestFocus()
            }
            ElevatedCard(modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 4.dp)) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = horizontalPadding,
                        vertical = 10.dp
                    )
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = onChangeOfName,
                        textStyle = textStyle,
                        singleLine = true,
                        label = { Text(text = "Name to identify") },
                        keyboardActions = KeyboardActions(onNext = { focusRequesterUnit.requestFocus() }),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Words
                        ),
                        modifier = Modifier.fillMaxWidth().focusRequester(focusRequesterName)

                    )
                    OutlinedTextField(
                        value = unit,
                        onValueChange = onChangeOfUnits,
                        textStyle = textStyle,
                        singleLine = true,
                        label = { Text(text = "Unit in singular form") },
                        placeholder = { Text(text = "e.g. pill, spray, spoon") },
                        keyboardActions = KeyboardActions(onNext = { focusRequesterNote.requestFocus() }),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.None
                        ),
                        modifier = Modifier.fillMaxWidth().focusRequester(focusRequesterUnit)
                    )
                    OutlinedTextField(
                        value = note,
                        onValueChange = onChangeOfNote,
                        label = { Text(text = "Note") },
                        keyboardActions = KeyboardActions(onNext = { focusRequesterDose.requestFocus() }),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        modifier = Modifier.fillMaxWidth().focusRequester(focusRequesterNote)
                    )
                }
            }
            ElevatedCard(modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 4.dp)) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = horizontalPadding,
                        vertical = 10.dp
                    )
                ) {
                    if (roaDose != null) {
                        RoaDoseView(roaDose = roaDose)
                        AnimatedVisibility(visible = currentDoseClass != null) {
                            if (currentDoseClass != null) {
                                CurrentDoseClassInfo(currentDoseClass, roaDose)
                            }
                        }
                    }
                    OutlinedTextField(
                        value = doseText,
                        onValueChange = onChangeDoseText,
                        textStyle = textStyle,
                        label = { Text("Dose per ${unit}", style = textStyle) },
                        trailingIcon = {
                            Text(
                                text = roaDose?.units ?: "",
                                style = textStyle,
                                modifier = Modifier.padding(horizontal = horizontalPadding)
                            )
                        },
                        keyboardActions = KeyboardActions(onDone = {
                            focusRequesterDose.freeFocus()
                        }),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth().focusRequester(focusRequesterDose)
                    )
                    if (isShowingUnitsField) {
                        OutlinedTextField(
                            value = originalUnit,
                            onValueChange = onChangeOfOriginalUnit,
                            label = { Text("Units") },
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(onClick = { onChangeOfOriginalUnit("µg") }) {
                                Text(text = "µg")
                            }
                            OutlinedButton(onClick = { onChangeOfOriginalUnit("mg") }) {
                                Text(text = "mg")
                            }
                            OutlinedButton(onClick = { onChangeOfOriginalUnit("g") }) {
                                Text(text = "g")
                            }
                            OutlinedButton(onClick = { onChangeOfOriginalUnit("mL") }) {
                                Text(text = "mL")
                            }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onChangeIsEstimate(isEstimate.not()) }
                    ) {
                        Text("Is Estimate", style = MaterialTheme.typography.titleMedium)
                        Checkbox(checked = isEstimate, onCheckedChange = onChangeIsEstimate)
                    }
                    AnimatedVisibility(visible = isEstimate) {
                        OutlinedTextField(
                            value = estimatedDoseVarianceText,
                            onValueChange = onChangeEstimatedDoseVarianceText,
                            textStyle = textStyle,
                            label = { Text("Estimated variance per ${unit}", style = textStyle) },
                            trailingIcon = {
                                Text(
                                    text = originalUnit,
                                    style = textStyle,
                                    modifier = Modifier.padding(horizontal = horizontalPadding)
                                )
                            },
                            keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()
                            }),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}