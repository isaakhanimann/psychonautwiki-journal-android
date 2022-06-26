package com.example.healthassistant.ui.addingestion.dose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.substances.RoaDose
import com.example.healthassistant.ui.previewproviders.RoaDosePreviewProvider
import com.example.healthassistant.ui.search.substance.roa.RoaDoseView
import com.example.healthassistant.ui.search.substance.roa.dose.DoseColor

@Composable
fun ChooseDoseScreen(
    navigateToChooseTimeAndMaybeColor: (units: String?, isEstimate: Boolean, dose: Double?) -> Unit,
    viewModel: ChooseDoseViewModel = hiltViewModel()
) {
    ChooseDoseScreen(
        roaDose = viewModel.roaDose,
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
        currentRoaRangeTextAndColor = viewModel.currentRoaRangeTextAndColor,
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
    @PreviewParameter(RoaDosePreviewProvider::class) roaDose: RoaDose?,
) {
    ChooseDoseScreen(
        roaDose = roaDose,
        doseText = "5",
        onChangeDoseText = {},
        isValidDose = true,
        isEstimate = false,
        onChangeIsEstimate = {},
        navigateToNext = {},
        useUnknownDoseAndNavigate = {},
        currentRoaRangeTextAndColor = Pair("threshold: 8mg", DoseColor.THRESH),
        purityText = "20",
        onPurityChange = {},
        isValidPurity = true,
        convertedDoseAndUnitText = "20 mg"
    )
}

@Composable
fun ChooseDoseScreen(
    roaDose: RoaDose?,
    doseText: String,
    onChangeDoseText: (String) -> Unit,
    isValidDose: Boolean,
    isEstimate: Boolean,
    onChangeIsEstimate: (Boolean) -> Unit,
    navigateToNext: () -> Unit,
    useUnknownDoseAndNavigate: () -> Unit,
    currentRoaRangeTextAndColor: Pair<String, DoseColor?>,
    purityText: String,
    onPurityChange: (purity: String) -> Unit,
    isValidPurity: Boolean,
    convertedDoseAndUnitText: String?
) {
    Column {
        LinearProgressIndicator(progress = 0.67f, modifier = Modifier.fillMaxWidth())
        Scaffold(
            topBar = { TopAppBar(title = { Text(text = "Choose Dose") }) },
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
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Column {
                    if (roaDose != null) {
                        RoaDoseView(roaDose = roaDose)
                    }
                    val isDarkTheme = isSystemInDarkTheme()
                    Text(
                        text = currentRoaRangeTextAndColor.first,
                        color = currentRoaRangeTextAndColor.second?.getComposeColor(isDarkTheme)
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
                }
                PurityCalculation(
                    purityText = purityText,
                    onPurityChange = onPurityChange,
                    convertedDoseAndUnitText = convertedDoseAndUnitText,
                    isValidPurity = isValidPurity
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Is Estimate", style = MaterialTheme.typography.h6)
                    Checkbox(checked = isEstimate, onCheckedChange = onChangeIsEstimate)
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = useUnknownDoseAndNavigate,
                ) {
                    Text("Use Unknown Dose")
                }
            }
        }
    }
}

@Preview
@Composable
fun PurityPreview() {
    PurityCalculation(
        purityText = "20",
        onPurityChange = {},
        isValidPurity = true,
        convertedDoseAndUnitText = "20 mg"
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