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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.dose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.data.substances.classes.roa.DoseClass
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDose
import com.isaakhanimann.journal.ui.DOSE_DISCLAIMER
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.dose.RoaDosePreviewProvider
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.dose.RoaDoseView
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun ChooseDoseScreen(
    navigateToChooseTimeAndMaybeColor: (units: String?, isEstimate: Boolean, dose: Double?, estimatedDoseStandardDeviation: Double?) -> Unit,
    navigateToVolumetricDosingScreenOnJournalTab: () -> Unit,
    navigateToURL: (url: String) -> Unit,
    navigateToSaferSniffingScreen: () -> Unit,
    viewModel: ChooseDoseViewModel = hiltViewModel()
) {
    ChooseDoseScreen(
        navigateToVolumetricDosingScreen = navigateToVolumetricDosingScreenOnJournalTab,
        navigateToSaferSniffingScreen = navigateToSaferSniffingScreen,
        substanceName = viewModel.substance.name,
        roaDose = viewModel.roaDose,
        administrationRoute = viewModel.administrationRoute,
        doseText = viewModel.doseText,
        doseRemark = viewModel.substance.dosageRemark,
        onChangeDoseText = viewModel::onDoseTextChange,
        estimatedDoseStandardDeviationText = viewModel.estimatedDoseStandardDeviationText,
        onChangeEstimatedDoseStandardDeviationText = viewModel::onEstimatedDoseStandardDeviationChange,
        isValidDose = viewModel.isValidDose,
        isEstimate = viewModel.isEstimate,
        onChangeIsEstimate = {
            viewModel.isEstimate = it
        },
        navigateToNext = {
            navigateToChooseTimeAndMaybeColor(
                viewModel.units,
                viewModel.isEstimate,
                viewModel.dose,
                viewModel.estimatedDoseStandardDeviation
            )
        },
        navigateToURL = navigateToURL,
        useUnknownDoseAndNavigate = {
            navigateToChooseTimeAndMaybeColor(
                viewModel.units,
                false,
                null,
                null
            )
        },
        currentDoseClass = viewModel.currentDoseClass,
        purityText = viewModel.purityText,
        onPurityChange = {
            viewModel.purityText = it
        },
        isValidPurity = viewModel.isPurityValid,
        convertedDoseAndUnitText = viewModel.rawDoseWithUnit,
        isShowingUnitsField = viewModel.roaDose?.units?.isBlank() ?: true,
        units = viewModel.units,
        onChangeOfUnits = { viewModel.units = it }
    )
}

@Preview
@Composable
fun ChooseDoseScreenPreview(
    @PreviewParameter(RoaDosePreviewProvider::class) roaDose: RoaDose,
) {
    ChooseDoseScreen(
        navigateToVolumetricDosingScreen = {},
        navigateToSaferSniffingScreen = {},
        substanceName = "Example Substance",
        roaDose = roaDose,
        administrationRoute = AdministrationRoute.INSUFFLATED,
        doseText = "5",
        onChangeDoseText = {},
        estimatedDoseStandardDeviationText = "",
        onChangeEstimatedDoseStandardDeviationText = {},
        doseRemark = "This is a dose remark",
        isValidDose = true,
        isEstimate = false,
        onChangeIsEstimate = {},
        navigateToNext = {},
        navigateToURL = {},
        useUnknownDoseAndNavigate = {},
        currentDoseClass = DoseClass.THRESHOLD,
        purityText = "20",
        onPurityChange = {},
        isValidPurity = true,
        convertedDoseAndUnitText = "25 mg",
        isShowingUnitsField = false,
        units = "mg",
        onChangeOfUnits = {}
    )
}

@Preview
@Composable
fun ChooseDoseScreenPreview2() {
    ChooseDoseScreen(
        navigateToVolumetricDosingScreen = {},
        navigateToSaferSniffingScreen = {},
        substanceName = "Example Substance",
        doseRemark = null,
        roaDose = null,
        administrationRoute = AdministrationRoute.ORAL,
        doseText = "5",
        onChangeDoseText = {},
        estimatedDoseStandardDeviationText = "",
        onChangeEstimatedDoseStandardDeviationText = {},
        isValidDose = true,
        isEstimate = false,
        onChangeIsEstimate = {},
        navigateToNext = {},
        navigateToURL = {},
        useUnknownDoseAndNavigate = {},
        currentDoseClass = null,
        purityText = "20",
        onPurityChange = {},
        isValidPurity = true,
        convertedDoseAndUnitText = "25 mg",
        isShowingUnitsField = false,
        units = "mg",
        onChangeOfUnits = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseDoseScreen(
    navigateToVolumetricDosingScreen: () -> Unit,
    navigateToSaferSniffingScreen: () -> Unit,
    navigateToURL: (url: String) -> Unit,
    substanceName: String,
    roaDose: RoaDose?,
    administrationRoute: AdministrationRoute,
    doseRemark: String?,
    doseText: String,
    onChangeDoseText: (String) -> Unit,
    estimatedDoseStandardDeviationText: String,
    onChangeEstimatedDoseStandardDeviationText: (String) -> Unit,
    isValidDose: Boolean,
    isEstimate: Boolean,
    onChangeIsEstimate: (Boolean) -> Unit,
    navigateToNext: () -> Unit,
    useUnknownDoseAndNavigate: () -> Unit,
    currentDoseClass: DoseClass?,
    purityText: String,
    onPurityChange: (purity: String) -> Unit,
    isValidPurity: Boolean,
    convertedDoseAndUnitText: String?,
    isShowingUnitsField: Boolean,
    units: String,
    onChangeOfUnits: (units: String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("$substanceName ${administrationRoute.displayText} Dose") })
        },
        floatingActionButton = {
            if (isValidDose) {
                ExtendedFloatingActionButton(
                    onClick = navigateToNext,
                    icon = {
                        Icon(
                            Icons.Filled.NavigateNext,
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
            LinearProgressIndicator(progress = 0.67f, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(4.dp))
            ElevatedCard(modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 4.dp)) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = horizontalPadding,
                        vertical = 10.dp
                    )
                ) {
                    if (!doseRemark.isNullOrBlank()) {
                        Text(text = doseRemark, style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    if (roaDose != null) {
                        RoaDoseView(roaDose = roaDose)
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Dosage Warning"
                            )
                            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                            Text(
                                text = "There is no dosage info for this administration route. Research dosages somewhere else.",
                            )
                        }
                    }
                    OptionalDosageUnitDisclaimer(substanceName)
                    Text(text = DOSE_DISCLAIMER, style = MaterialTheme.typography.bodySmall)
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
                        AnimatedVisibility(visible = currentDoseClass != null) {
                            if (currentDoseClass != null) {
                                CurrentDoseClassInfo(currentDoseClass, roaDose)
                            }
                        }
                    }
                    val focusManager = LocalFocusManager.current
                    val focusRequester = remember { FocusRequester() }
                    val textStyle = MaterialTheme.typography.titleMedium
                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }
                    OutlinedTextField(
                        value = doseText,
                        onValueChange = onChangeDoseText,
                        textStyle = textStyle,
                        label = { Text("Dose", style = textStyle) },
                        isError = !isValidDose,
                        trailingIcon = {
                            Text(
                                text = units,
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
                    if (isShowingUnitsField) {
                        OutlinedTextField(
                            value = units,
                            onValueChange = onChangeOfUnits,
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
                            OutlinedButton(onClick = { onChangeOfUnits("µg") }) {
                                Text(text = "µg")
                            }
                            OutlinedButton(onClick = { onChangeOfUnits("mg") }) {
                                Text(text = "mg")
                            }
                            OutlinedButton(onClick = { onChangeOfUnits("g") }) {
                                Text(text = "g")
                            }
                            OutlinedButton(onClick = { onChangeOfUnits("mL") }) {
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
                            value = estimatedDoseStandardDeviationText,
                            onValueChange = onChangeEstimatedDoseStandardDeviationText,
                            textStyle = textStyle,
                            label = { Text("Estimated standard deviation", style = textStyle) },
                            trailingIcon = {
                                Text(
                                    text = units,
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
            AnimatedVisibility(visible = isValidDose) {
                ElevatedCard(
                    modifier = Modifier.padding(
                        horizontal = horizontalPadding,
                        vertical = 4.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(
                            horizontal = horizontalPadding,
                            vertical = 10.dp
                        )
                    ) {
                        PurityCalculation(
                            purityText = purityText,
                            onPurityChange = onPurityChange,
                            convertedDoseAndUnitText = convertedDoseAndUnitText,
                            isValidPurity = isValidPurity
                        )
                    }
                }
            }
            var isShowingUnknownDoseDialog by remember { mutableStateOf(false) }
            TextButton(onClick = { isShowingUnknownDoseDialog = true }) {
                Text(text = "Log unknown dose")
            }
            AnimatedVisibility(visible = isShowingUnknownDoseDialog) {
                UnknownDoseDialog(
                    useUnknownDoseAndNavigate = useUnknownDoseAndNavigate,
                    dismiss = { isShowingUnknownDoseDialog = false }
                )
            }
            if (administrationRoute == AdministrationRoute.INSUFFLATED) {
                TextButton(onClick = navigateToSaferSniffingScreen) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = "Info",
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "Safer Sniffing")
                }
            } else if (administrationRoute == AdministrationRoute.RECTAL) {
                TextButton(onClick = { navigateToURL(AdministrationRoute.saferPluggingArticleURL) }) {
                    Icon(
                        Icons.Outlined.Article,
                        contentDescription = "Open Link"
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Safer Plugging")
                }
            }
            if (roaDose?.shouldUseVolumetricDosing == true) {
                TextButton(onClick = navigateToVolumetricDosingScreen) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = "Info",
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "Volumetric Liquid Dosing")
                }
            }
            if (administrationRoute == AdministrationRoute.SMOKED && substanceName != "Cannabis") {
                ElevatedCard(modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 4.dp)) {
                    ChasingTheDragonText(modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 10.dp))
                }
            }
        }
    }
}