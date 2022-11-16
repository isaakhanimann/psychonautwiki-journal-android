/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.dose.custom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.dose.PurityCalculation
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.dose.UnknownDoseDialog
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun CustomChooseDose(
    navigateToChooseTimeAndMaybeColor: (units: String?, isEstimate: Boolean, dose: Double?) -> Unit,
    navigateToSaferSniffingScreen: () -> Unit,
    navigateToURL: (url: String) -> Unit,
    viewModel: CustomChooseDoseViewModel = hiltViewModel()
) {
    CustomChooseDose(
        navigateToSaferSniffingScreen = navigateToSaferSniffingScreen,
        substanceName = viewModel.substanceName,
        administrationRoute = viewModel.administrationRoute,
        doseText = viewModel.doseText,
        onChangeDoseText = viewModel::onDoseTextChange,
        isValidDose = viewModel.isValidDose,
        isEstimate = viewModel.isEstimate,
        onChangeIsEstimate = {
            viewModel.isEstimate = it
        },
        navigateToNext = {
            navigateToChooseTimeAndMaybeColor(
                viewModel.units,
                viewModel.isEstimate,
                viewModel.dose
            )
        },
        navigateToURL = navigateToURL,
        useUnknownDoseAndNavigate = {
            navigateToChooseTimeAndMaybeColor(
                viewModel.units,
                false,
                null
            )
        },
        purityText = viewModel.purityText,
        onPurityChange = {
            viewModel.purityText = it
        },
        isValidPurity = viewModel.isPurityValid,
        convertedDoseAndUnitText = viewModel.rawDoseWithUnit,
        units = viewModel.units
    )
}

@Preview
@Composable
fun CustomChooseDosePreview() {
    CustomChooseDose(
        navigateToSaferSniffingScreen = {},
        substanceName = "Example Substance",
        administrationRoute = AdministrationRoute.INSUFFLATED,
        doseText = "5",
        onChangeDoseText = {},
        isValidDose = true,
        isEstimate = false,
        onChangeIsEstimate = {},
        navigateToNext = {},
        navigateToURL = {},
        useUnknownDoseAndNavigate = {},
        purityText = "20",
        onPurityChange = {},
        isValidPurity = true,
        convertedDoseAndUnitText = "25 mg",
        units = "mg"
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomChooseDose(
    navigateToSaferSniffingScreen: () -> Unit,
    substanceName: String,
    administrationRoute: AdministrationRoute,
    doseText: String,
    onChangeDoseText: (String) -> Unit,
    isValidDose: Boolean,
    isEstimate: Boolean,
    onChangeIsEstimate: (Boolean) -> Unit,
    navigateToNext: () -> Unit,
    navigateToURL: (url: String) -> Unit,
    useUnknownDoseAndNavigate: () -> Unit,
    purityText: String,
    onPurityChange: (purity: String) -> Unit,
    isValidPurity: Boolean,
    convertedDoseAndUnitText: String?,
    units: String
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(administrationRoute.displayText + " " + substanceName + " Dosage") }) },
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
        Column(modifier = Modifier.padding(padding)) {
            LinearProgressIndicator(progress = 0.67f, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(4.dp))
            Card(modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 4.dp)) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = horizontalPadding,
                        vertical = 10.dp
                    )
                ) {
                    val focusManager = LocalFocusManager.current
                    val textStyle = MaterialTheme.typography.titleMedium
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
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onChangeIsEstimate(isEstimate.not()) }
                    ) {
                        Text("Is Estimate", style = MaterialTheme.typography.titleMedium)
                        Checkbox(checked = isEstimate, onCheckedChange = onChangeIsEstimate)
                    }
                }
            }
            AnimatedVisibility(visible = isValidDose) {
                Card(modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 4.dp)) {
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
            if (administrationRoute == AdministrationRoute.INSUFFLATED) {
                TextButton(onClick = navigateToSaferSniffingScreen) {
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
            Spacer(modifier = Modifier.weight(1f))
            var isShowingUnknownDoseDialog by remember { mutableStateOf(false) }
            TextButton(
                onClick = { isShowingUnknownDoseDialog = true },
            ) {
                Text("Use Unknown Dose")
            }
            if (isShowingUnknownDoseDialog) {
                UnknownDoseDialog(
                    useUnknownDoseAndNavigate = useUnknownDoseAndNavigate,
                    dismiss = { isShowingUnknownDoseDialog = false }
                )
            }
        }
    }
}