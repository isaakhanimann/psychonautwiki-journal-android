package com.example.healthassistant.ui.home.experience.addingestion.dose

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
import androidx.navigation.NavController
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.RoaDose
import com.example.healthassistant.ui.main.routers.navigateToChooseColor
import com.example.healthassistant.ui.previewproviders.RoaDosePreviewProvider
import com.example.healthassistant.ui.search.substance.roa.RoaDoseView
import com.example.healthassistant.ui.search.substance.roa.dose.DoseColor

@Composable
fun ChooseDoseScreen(
    navController: NavController,
    viewModel: ChooseDoseViewModel = hiltViewModel()
) {
    viewModel.substance?.let { sub ->
        ChooseDoseScreenContent(
            roaDose = viewModel.roaDose,
            doseText = viewModel.doseText,
            onChangeDoseText = viewModel::onDoseTextChange,
            isValidDose = viewModel.isValidDose,
            isEstimate = viewModel.isEstimate,
            onChangeIsEstimate = {
                viewModel.isEstimate = it
            },
            navigateToNext = {
                navController.navigateToChooseColor(
                    substanceName = sub.name,
                    administrationRoute = AdministrationRoute.ORAL,
                    units = viewModel.roaDose?.units ?: "",
                    isEstimate = viewModel.isEstimate,
                    dose = viewModel.dose,
                    experienceId = viewModel.experienceId
                )
            },
            useUnknownDoseAndNavigate = {
                navController.navigateToChooseColor(
                    substanceName = sub.name,
                    administrationRoute = AdministrationRoute.ORAL,
                    units = viewModel.roaDose?.units ?: "",
                    isEstimate = false,
                    dose = null,
                    experienceId = viewModel.experienceId
                )
            },
            currentRoaRangeTextAndColor = viewModel.currentRoaRangeTextAndColor
        )
    }
}

@Preview
@Composable
fun ChooseDoseScreenContent(
    @PreviewParameter(RoaDosePreviewProvider::class) roaDose: RoaDose?,
    doseText: String = "5",
    onChangeDoseText: (String) -> Unit = {},
    isValidDose: Boolean = true,
    isEstimate: Boolean = false,
    onChangeIsEstimate: (Boolean) -> Unit = {},
    navigateToNext: () -> Unit = {},
    useUnknownDoseAndNavigate: () -> Unit = {},
    currentRoaRangeTextAndColor: Pair<String, DoseColor?> = Pair("threshold: 8mg", DoseColor.THRESH)
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
                        if (isValidDose) {
                            navigateToNext()
                        }
                    }),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isEstimate, onCheckedChange = onChangeIsEstimate)
                    Text("Is Estimate")
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                if (isValidDose) {
                    Button(
                        onClick = navigateToNext,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text("Next", style = MaterialTheme.typography.h3)
                    }
                }
                Button(
                    onClick = useUnknownDoseAndNavigate,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text("Use Unknown Dose", style = MaterialTheme.typography.h6)
                }
            }
        }
    }
}