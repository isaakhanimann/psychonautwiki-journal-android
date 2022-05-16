package com.example.healthassistant.ui.home.experience.addingestion.dose

import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.ui.main.routers.navigateToChooseColor
import com.example.healthassistant.ui.previewproviders.SubstancePreviewProvider

@Composable
fun ChooseDoseScreen(
    navController: NavController,
    viewModel: ChooseDoseViewModel = hiltViewModel()
) {
    viewModel.substance?.let {
        ChooseDoseScreenContent(substance = it, navigateToNext = {
            navController.navigateToChooseColor(
                substanceName = it.name,
                administrationRoute = AdministrationRoute.ORAL,
                units = "mg",
                isEstimate = false,
                dose = 5.0,
                experienceId = 1
            )
        })
    }
}

@Preview
@Composable
fun ChooseDoseScreenContent(
    @PreviewParameter(SubstancePreviewProvider::class) substance: Substance,
    navigateToNext: () -> Unit = {}
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Choose Dose") }) }
    ) {
        Button(onClick = navigateToNext) {
            Text("Next")
        }
    }
}