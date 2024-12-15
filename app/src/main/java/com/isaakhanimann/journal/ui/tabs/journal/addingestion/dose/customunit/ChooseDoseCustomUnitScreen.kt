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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.dose.customunit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.room.experiences.entities.CustomUnit
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.data.substances.classes.roa.DoseClass
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDose
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.models.justUnit
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.dose.RoaDosePreviewProvider
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.dose.RoaDoseView
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun ChooseDoseCustomUnitScreen(
    navigateToChooseTimeAndMaybeColor: (
        administrationRoute: AdministrationRoute,
        units: String?,
        isEstimate: Boolean,
        dose: Double?,
        estimatedDoseStandardDeviation: Double?,
        substanceName: String,
        customUnitId: Int?
    ) -> Unit,
    viewModel: ChooseDoseCustomUnitViewModel = hiltViewModel()
) {
    viewModel.customUnit?.let { customUnitUnwrapped ->
        ChooseDoseCustomUnitScreen(
            customUnit = customUnitUnwrapped,
            roaDose = viewModel.roaDose,
            doseText = viewModel.doseText,
            doseRemark = viewModel.doseRemark,
            dose = viewModel.dose,
            onChangeDoseText = viewModel::onDoseTextChange,
            estimatedDoseDeviationText = viewModel.estimatedDoseDeviationText,
            onChangeEstimatedDoseDeviationText = viewModel::onEstimatedDoseDeviationChange,
            estimatedDoseDeviation = viewModel.estimatedDoseDeviation,
            isValidDose = viewModel.isValidDose,
            isEstimate = viewModel.isEstimate,
            onChangeIsEstimate = {
                viewModel.isEstimate = it
            },
            navigateToNext = {
                navigateToChooseTimeAndMaybeColor(
                    customUnitUnwrapped.administrationRoute,
                    customUnitUnwrapped.unit,
                    viewModel.isEstimate,
                    viewModel.dose,
                    viewModel.estimatedDoseDeviation,
                    customUnitUnwrapped.substanceName,
                    customUnitUnwrapped.id
                )
            },
            useUnknownDoseAndNavigate = {
                navigateToChooseTimeAndMaybeColor(
                    customUnitUnwrapped.administrationRoute,
                    customUnitUnwrapped.unit,
                    false,
                    null,
                    null,
                    customUnitUnwrapped.substanceName,
                    customUnitUnwrapped.id
                )
            },
            currentDoseClass = viewModel.currentDoseClass,
            customUnitCalculationText = viewModel.customUnitCalculationText,
        )
    }
}

@Preview
@Composable
fun ChooseDoseCustomUnitScreenPreview(
    @PreviewParameter(RoaDosePreviewProvider::class) roaDose: RoaDose,
) {
    ChooseDoseCustomUnitScreen(
        customUnit = CustomUnit(
            substanceName = "Example substance",
            administrationRoute = AdministrationRoute.ORAL,
            dose = 10.0,
            estimatedDoseStandardDeviation = null,
            isEstimate = false,
            isArchived = false,
            originalUnit = "mg",
            name = "Big Spoon",
            unit = "spoon",
            unitPlural = "spoons",
            note = "Note about custom unit dose"
        ),
        roaDose = roaDose,
        doseText = "5",
        dose = 5.0,
        onChangeDoseText = {},
        estimatedDoseDeviationText = "",
        onChangeEstimatedDoseDeviationText = {},
        estimatedDoseDeviation = null,
        doseRemark = "This is a dose remark",
        isValidDose = true,
        isEstimate = false,
        onChangeIsEstimate = {},
        navigateToNext = {},
        useUnknownDoseAndNavigate = {},
        currentDoseClass = DoseClass.THRESHOLD,
        customUnitCalculationText = "2 pills x 20 mg = 40 mg",
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseDoseCustomUnitScreen(
    customUnit: CustomUnit,
    roaDose: RoaDose?,
    doseRemark: String?,
    dose: Double?,
    doseText: String,
    onChangeDoseText: (String) -> Unit,
    estimatedDoseDeviationText: String,
    estimatedDoseDeviation: Double?,
    onChangeEstimatedDoseDeviationText: (String) -> Unit,
    isValidDose: Boolean,
    isEstimate: Boolean,
    onChangeIsEstimate: (Boolean) -> Unit,
    navigateToNext: () -> Unit,
    useUnknownDoseAndNavigate: () -> Unit,
    currentDoseClass: DoseClass?,
    customUnitCalculationText: String?,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("${customUnit.substanceName} (${customUnit.name})") })
        },
        floatingActionButton = {
            if (isValidDose) {
                ExtendedFloatingActionButton(
                    modifier = Modifier.imePadding(),
                    onClick = navigateToNext,
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.NavigateNext,
                            contentDescription = "Next"
                        )
                    },
                    text = { Text("Next") },
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            LinearProgressIndicator(
                progress = { 0.67f },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(4.dp))
            ElevatedCard(
                modifier = Modifier.padding(
                    horizontal = horizontalPadding,
                    vertical = 4.dp
                ).fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = horizontalPadding,
                        vertical = 10.dp
                    )
                ) {
                    if (!doseRemark.isNullOrBlank()) {
                        Text(text = doseRemark, style = MaterialTheme.typography.bodySmall)
                    }
                    if (customUnit.note.isNotBlank()) {
                        Text("${customUnit.name}: ${customUnit.note}")
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    if (roaDose != null) {
                        RoaDoseView(roaDose = roaDose)
                    }
                    Text(
                        text = customUnitCalculationText ?: "",
                        color = currentDoseClass?.getComposeColor(isSystemInDarkTheme()) ?: Color.Unspecified,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
            ElevatedCard(
                modifier = Modifier.padding(
                    horizontal = horizontalPadding,
                    vertical = 4.dp
                )
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(
                        horizontal = horizontalPadding,
                        vertical = 10.dp
                    )
                ) {
                    if (roaDose != null && customUnit.dose != null) {
                        CustomUnitRoaDoseView(roaDose, customUnit)
                    }
                    val focusManager = LocalFocusManager.current
                    val focusRequester = remember { FocusRequester() }
                    val textStyle = MaterialTheme.typography.titleMedium
                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }
                    val pluralizableUnit = customUnit.getPluralizableUnit()
                    OutlinedTextField(
                        value = doseText,
                        onValueChange = onChangeDoseText,
                        textStyle = textStyle,
                        label = { Text("Dose", style = textStyle) },
                        isError = !isValidDose,
                        trailingIcon = {
                            Text(
                                text = pluralizableUnit.justUnit(dose ?: 1.0),
                                style = textStyle,
                                modifier = Modifier.padding(horizontal = horizontalPadding)
                            )
                        },
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                        }),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Switch(checked = isEstimate, onCheckedChange = onChangeIsEstimate)
                        Text("Estimate", style = MaterialTheme.typography.titleMedium)
                    }
                    AnimatedVisibility(visible = isEstimate) {
                        OutlinedTextField(
                            value = estimatedDoseDeviationText,
                            onValueChange = onChangeEstimatedDoseDeviationText,
                            textStyle = textStyle,
                            label = { Text("Estimated standard deviation", style = textStyle) },
                            trailingIcon = {
                                Text(
                                    text = pluralizableUnit.justUnit(estimatedDoseDeviation ?: 1.0),
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
            TextButton(onClick = useUnknownDoseAndNavigate) {
                Text(text = "Log unknown dose")
            }
        }
    }
}
