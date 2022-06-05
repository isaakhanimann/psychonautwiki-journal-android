package com.example.healthassistant.ui.journal.experience.addingestion.dose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
    navigateToChooseColor: (units: String?, isEstimate: Boolean, dose: Double?) -> Unit,
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
            navigateToChooseColor(
                viewModel.roaDose?.units,
                viewModel.isEstimate,
                viewModel.dose
            )
        },
        useUnknownDoseAndNavigate = {
            navigateToChooseColor(
                viewModel.roaDose?.units,
                false,
                null
            )
        },
        currentRoaRangeTextAndColor = viewModel.currentRoaRangeTextAndColor,
        purity = viewModel.purity,
        purityText = viewModel.purityText,
        onPurityChange = {
            viewModel.purity = it
        },
        rawDoseWithUnit = viewModel.rawDoseWithUnit
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
        purity = 20f,
        purityText = "20%",
        onPurityChange = {},
        rawDoseWithUnit = "20 mg"
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
    purity: Float,
    purityText: String,
    onPurityChange: (purity: Float) -> Unit,
    rawDoseWithUnit: String?
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Choose Dose") }) }
    ) {
        val focusManager = LocalFocusManager.current
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
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
                OutlinedTextField(
                    value = doseText,
                    onValueChange = onChangeDoseText,
                    textStyle = MaterialTheme.typography.h3,
                    label = { Text("Enter Dose", style = MaterialTheme.typography.h4) },
                    isError = !isValidDose,
                    trailingIcon = {
                        Text(
                            text = roaDose?.units ?: "",
                            style = MaterialTheme.typography.h4,
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
                purity = purity,
                purityText = purityText,
                onPurityChange = onPurityChange,
                rawDoseWithUnit = rawDoseWithUnit
            )
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Checkbox(checked = isEstimate, onCheckedChange = onChangeIsEstimate)
                Text("Is Estimate", style = MaterialTheme.typography.h6)
            }
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = useUnknownDoseAndNavigate,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Use Unknown Dose")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = navigateToNext,
                    enabled = isValidDose,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Next")
                }
            }
        }
    }
}

@Preview
@Composable
fun PurityPreview() {
    PurityCalculation(
        purity = 20f,
        purityText = "20%",
        onPurityChange = {},
        rawDoseWithUnit = "20 mg"
    )
}

@Composable
fun PurityCalculation(
    purity: Float,
    purityText: String,
    onPurityChange: (purity: Float) -> Unit,
    rawDoseWithUnit: String?
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Purity", style = MaterialTheme.typography.h6)
            Text(text = purityText, style = MaterialTheme.typography.h6)
        }
        Slider(
            value = purity,
            onValueChange = onPurityChange,
            valueRange = 1f..100f,
            steps = 99,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colors.secondary,
                activeTrackColor = MaterialTheme.colors.secondary
            )
        )
        if (rawDoseWithUnit != null) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Raw Amount")
                Text(text = rawDoseWithUnit, style = MaterialTheme.typography.h5)
            }
        }
    }
}