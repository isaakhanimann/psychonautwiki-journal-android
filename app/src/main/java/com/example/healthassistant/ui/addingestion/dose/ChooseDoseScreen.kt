package com.example.healthassistant.ui.addingestion.dose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.DoseClass
import com.example.healthassistant.data.substances.RoaDose
import com.example.healthassistant.ui.search.substance.roa.dose.RoaDosePreviewProvider
import com.example.healthassistant.ui.search.substance.roa.dose.RoaDoseView
import com.example.healthassistant.ui.search.substance.roa.toReadableString

@Composable
fun ChooseDoseScreen(
    navigateToChooseTimeAndMaybeColor: (units: String?, isEstimate: Boolean, dose: Double?) -> Unit,
    navigateToDoseGuideScreen: () -> Unit,
    navigateToVolumetricDosingScreen: () -> Unit,
    viewModel: ChooseDoseViewModel = hiltViewModel()
) {
    ChooseDoseScreen(
        navigateToDoseGuideScreen = navigateToDoseGuideScreen,
        navigateToVolumetricDosingScreen = navigateToVolumetricDosingScreen,
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
                viewModel.roaDose?.units,
                viewModel.isEstimate,
                viewModel.dose
            )
        },
        useUnknownDoseAndNavigate = {
            navigateToChooseTimeAndMaybeColor(
                viewModel.roaDose?.units,
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
        convertedDoseAndUnitText = viewModel.rawDoseWithUnit
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
        convertedDoseAndUnitText = "25 mg"
    )
}

@Preview
@Composable
fun ChooseDoseScreenPreview2() {
    ChooseDoseScreen(
        navigateToDoseGuideScreen = {},
        navigateToVolumetricDosingScreen = {},
        substanceName = "Example Substance",
        roaDose = null,
        administrationRoute = AdministrationRoute.INSUFFLATED,
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
        convertedDoseAndUnitText = "25 mg"
    )
}

@Composable
fun ChooseDoseScreen(
    navigateToDoseGuideScreen: () -> Unit,
    navigateToVolumetricDosingScreen: () -> Unit,
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
    convertedDoseAndUnitText: String?
) {
    Column {
        LinearProgressIndicator(progress = 0.67f, modifier = Modifier.fillMaxWidth())
        Scaffold(
            topBar = { TopAppBar(title = { Text(text = "Choose " + administrationRoute.displayText + " Dose") }) },
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
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
            ) {
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
                            text = "There is no dosage info for ${administrationRoute.displayText} $substanceName. This is potentially extremely dangerous.",
                            style = MaterialTheme.typography.subtitle2
                        )
                    }
                }
                val text = when (currentDoseClass) {
                    DoseClass.THRESHOLD -> "threshold: ${roaDose?.threshold?.toReadableString()}${roaDose?.units ?: ""}"
                    DoseClass.LIGHT -> "light: ${roaDose?.light?.min?.toReadableString()}-${roaDose?.light?.max?.toReadableString()}${roaDose?.units ?: ""}"
                    DoseClass.COMMON -> "common: ${roaDose?.common?.min?.toReadableString()}-${roaDose?.common?.max?.toReadableString()}${roaDose?.units ?: ""}"
                    DoseClass.STRONG -> "strong: ${roaDose?.strong?.min?.toReadableString()}-${roaDose?.strong?.max?.toReadableString()}${roaDose?.units ?: ""}"
                    DoseClass.HEAVY -> "heavy: ${roaDose?.heavy?.toReadableString()}${roaDose?.units ?: ""}-.."
                    else -> ""
                }
                val isDarkTheme = isSystemInDarkTheme()
                Text(
                    text = text,
                    color = currentDoseClass?.getComposeColor(isDarkTheme)
                        ?: MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.height(10.dp))
                val focusManager = LocalFocusManager.current
                val textStyle = MaterialTheme.typography.h3
                OutlinedTextField(
                    value = doseText,
                    onValueChange = onChangeDoseText,
                    textStyle = textStyle,
                    label = { Text("Pure Dose", style = textStyle) },
                    isError = !isValidDose,
                    trailingIcon = {
                        Text(
                            text = roaDose?.units ?: "",
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
                TextButton(onClick = navigateToDoseGuideScreen) {
                    Text(text = "Dosage Guide")
                }
                if (roaDose?.shouldDefinitelyUseVolumetricDosing == true) {
                    Text(text = "To measure $substanceName dose use:", style = MaterialTheme.typography.h6)
                    TextButton(onClick = navigateToVolumetricDosingScreen) {
                        Text(text = "Volumetric Liquid Dosing")
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
}

@Preview
@Composable
fun UnknownDoseDialogPreview() {
    UnknownDoseDialog(
        useUnknownDoseAndNavigate = {},
        dismiss = {}
    )
}

@Composable
fun UnknownDoseDialog(
    useUnknownDoseAndNavigate: () -> Unit,
    dismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = dismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Warning, contentDescription = "Warning")
                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                Text(text = "Unknown Danger", style = MaterialTheme.typography.h5)
            }
        },
        text = {
            Text(
                "Administering the wrong dosage of a substance can lead to negative experiences such as extreme anxiety, uncomfortable physical side effects, hospitalization, or (in extreme cases) death.\n" +
                        "Read the dosage guide."
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    dismiss()
                    useUnknownDoseAndNavigate()
                }
            ) {
                Text("Continue")
            }
        },
        dismissButton = {
            TextButton(
                onClick = dismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun PurityCalculation(
    purityText: String,
    onPurityChange: (purity: String) -> Unit,
    isValidPurity: Boolean,
    convertedDoseAndUnitText: String?
) {
    Column(horizontalAlignment = Alignment.Start) {
        val focusManager = LocalFocusManager.current
        val textStyle = MaterialTheme.typography.h6
        OutlinedTextField(
            value = purityText,
            onValueChange = onPurityChange,
            textStyle = textStyle,
            label = { Text("Purity", style = textStyle) },
            isError = !isValidPurity,
            trailingIcon = {
                Text(
                    text = "%",
                    style = textStyle,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            },
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (!isValidPurity) {
            Text(
                text = "Purity must be between 1 and 100%",
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
            )
        }
        if (convertedDoseAndUnitText != null) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Converted Amount")
                Text(text = convertedDoseAndUnitText, style = MaterialTheme.typography.h5)
            }
        }
    }
}