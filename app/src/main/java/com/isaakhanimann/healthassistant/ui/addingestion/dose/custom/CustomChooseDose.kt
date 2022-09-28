package com.isaakhanimann.healthassistant.ui.addingestion.dose.custom

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import com.isaakhanimann.healthassistant.ui.addingestion.dose.PurityCalculation
import com.isaakhanimann.healthassistant.ui.addingestion.dose.UnknownDoseDialog

@Composable
fun CustomChooseDose(
    navigateToChooseTimeAndMaybeColor: (units: String?, isEstimate: Boolean, dose: Double?) -> Unit,
    navigateToSaferSniffingScreen: () -> Unit,
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
        useUnknownDoseAndNavigate = {},
        purityText = "20",
        onPurityChange = {},
        isValidPurity = true,
        convertedDoseAndUnitText = "25 mg",
        units = "mg"
    )
}


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
    useUnknownDoseAndNavigate: () -> Unit,
    purityText: String,
    onPurityChange: (purity: String) -> Unit,
    isValidPurity: Boolean,
    convertedDoseAndUnitText: String?,
    units: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Choose Dose") }
            )
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
    ) {
        Column {
            LinearProgressIndicator(progress = 0.67f, modifier = Modifier.fillMaxWidth())
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
            ) {
                val horizontalPadding = 10.dp
                if (administrationRoute == AdministrationRoute.INSUFFLATED) {
                    TextButton(onClick = navigateToSaferSniffingScreen) {
                        Text(text = "Safer Sniffing")
                    }
                    Divider()
                } else if (administrationRoute == AdministrationRoute.RECTAL) {
                    val uriHandler = LocalUriHandler.current
                    TextButton(onClick = { uriHandler.openUri(AdministrationRoute.saferPluggingArticleURL) }) {
                        Icon(
                            Icons.Filled.Launch,
                            contentDescription = "Open Link"
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Safer Plugging")
                    }
                    Divider()
                }
                Column(modifier = Modifier.padding(horizontal = horizontalPadding)) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = administrationRoute.displayText + " " + substanceName + " Dosage",
                        style = MaterialTheme.typography.subtitle2
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    val focusManager = LocalFocusManager.current
                    val textStyle = MaterialTheme.typography.h3
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
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                        },
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                        }),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    PurityCalculation(
                        purityText = purityText,
                        onPurityChange = onPurityChange,
                        convertedDoseAndUnitText = convertedDoseAndUnitText,
                        isValidPurity = isValidPurity
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Is Estimate", style = MaterialTheme.typography.h6)
                        Checkbox(checked = isEstimate, onCheckedChange = onChangeIsEstimate)
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
    }
}