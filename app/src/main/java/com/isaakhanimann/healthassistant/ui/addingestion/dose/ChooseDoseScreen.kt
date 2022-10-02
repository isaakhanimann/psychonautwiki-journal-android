package com.isaakhanimann.healthassistant.ui.addingestion.dose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import com.isaakhanimann.healthassistant.data.substances.classes.roa.DoseClass
import com.isaakhanimann.healthassistant.data.substances.classes.roa.RoaDose
import com.isaakhanimann.healthassistant.ui.search.substance.roa.dose.RoaDosePreviewProvider
import com.isaakhanimann.healthassistant.ui.search.substance.roa.dose.RoaDoseView
import com.isaakhanimann.healthassistant.ui.theme.horizontalPadding
import com.isaakhanimann.healthassistant.ui.utils.JournalTopAppBar

@Composable
fun ChooseDoseScreen(
    navigateToChooseTimeAndMaybeColor: (units: String?, isEstimate: Boolean, dose: Double?) -> Unit,
    navigateToDoseGuideScreen: () -> Unit,
    navigateToVolumetricDosingScreen: () -> Unit,
    navigateToSaferSniffingScreen: () -> Unit,
    viewModel: ChooseDoseViewModel = hiltViewModel()
) {
    ChooseDoseScreen(
        navigateToDoseGuideScreen = navigateToDoseGuideScreen,
        navigateToVolumetricDosingScreen = navigateToVolumetricDosingScreen,
        navigateToSaferSniffingScreen = navigateToSaferSniffingScreen,
        substanceName = viewModel.substance.name,
        roaDose = viewModel.roaDose,
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
        currentDoseClass = viewModel.currentDoseClass,
        purityText = viewModel.purityText,
        onPurityChange = {
            viewModel.purityText = it
        },
        isValidPurity = viewModel.isPurityValid,
        convertedDoseAndUnitText = viewModel.rawDoseWithUnit,
        isShowingUnitsField = viewModel.roaDose?.units?.isBlank() ?: true,
        units = viewModel.units,
        onChangeOfUnits = {viewModel.units = it}
    )
}

@Preview
@Composable
fun ChooseDoseScreenPreview(
    @PreviewParameter(RoaDosePreviewProvider::class) roaDose: RoaDose,
) {
    ChooseDoseScreen(
        navigateToDoseGuideScreen = {},
        navigateToVolumetricDosingScreen = {},
        navigateToSaferSniffingScreen = {},
        substanceName = "Example Substance",
        roaDose = roaDose,
        administrationRoute = AdministrationRoute.INSUFFLATED,
        doseText = "5",
        onChangeDoseText = {},
        isValidDose = true,
        isEstimate = false,
        onChangeIsEstimate = {},
        navigateToNext = {},
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
        navigateToDoseGuideScreen = {},
        navigateToVolumetricDosingScreen = {},
        navigateToSaferSniffingScreen = {},
        substanceName = "Example Substance",
        roaDose = null,
        administrationRoute = AdministrationRoute.ORAL,
        doseText = "5",
        onChangeDoseText = {},
        isValidDose = true,
        isEstimate = false,
        onChangeIsEstimate = {},
        navigateToNext = {},
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

@Composable
fun ChooseDoseScreen(
    navigateToDoseGuideScreen: () -> Unit,
    navigateToVolumetricDosingScreen: () -> Unit,
    navigateToSaferSniffingScreen: () -> Unit,
    substanceName: String,
    roaDose: RoaDose?,
    administrationRoute: AdministrationRoute,
    doseText: String,
    onChangeDoseText: (String) -> Unit,
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
            JournalTopAppBar(title = "$substanceName ${administrationRoute.displayText} Dose")
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
                if (administrationRoute == AdministrationRoute.INSUFFLATED) {
                    TextButton(onClick = navigateToSaferSniffingScreen) {
                        Text(text = "Safer Sniffing")
                    }
                    Divider()
                } else if (administrationRoute == AdministrationRoute.RECTAL) {
                    val uriHandler = LocalUriHandler.current
                    TextButton(onClick = { uriHandler.openUri(AdministrationRoute.saferPluggingArticleURL) }) {
                        Icon(
                            Icons.Default.OpenInBrowser,
                            contentDescription = "Open Link"
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Safer Plugging")
                    }
                    Divider()
                }
                if (roaDose?.shouldDefinitelyUseVolumetricDosing == true) {
                    TextButton(onClick = navigateToVolumetricDosingScreen) {
                        Text(text = "Use Volumetric Liquid Dosing")
                    }
                    Divider()
                }
                Column(modifier = Modifier.padding(horizontal = horizontalPadding)) {
                    Spacer(modifier = Modifier.height(10.dp))
                    if (roaDose != null) {
                        CurrentDoseClassInfo(currentDoseClass, roaDose)
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    if (roaDose != null) {
                        RoaDoseView(
                            roaDose = roaDose,
                            navigateToDosageExplanationScreen = navigateToDoseGuideScreen
                        )
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
                    Spacer(modifier = Modifier.height(10.dp))
                    val focusManager = LocalFocusManager.current
                    val textStyle = MaterialTheme.typography.h6
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
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(onClick = { onChangeOfUnits("µg") }) {
                                Text(text = "µg")
                            }
                            OutlinedButton(onClick = { onChangeOfUnits("mg") }) {
                                Text(text = "mg")
                            }
                            OutlinedButton(onClick = { onChangeOfUnits("g") }) {
                                Text(text = "g")
                            }
                            OutlinedButton(onClick = { onChangeOfUnits("ml") }) {
                                Text(text = "ml")
                            }
                        }
                    }
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
                        modifier = Modifier.clickable {
                            onChangeIsEstimate(isEstimate.not())
                        }
                    ) {
                        Checkbox(checked = isEstimate, onCheckedChange = onChangeIsEstimate)
                        Text("Dose is an estimate")
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