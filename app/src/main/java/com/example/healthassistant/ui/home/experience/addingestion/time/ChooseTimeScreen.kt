package com.example.healthassistant.ui.home.experience.addingestion.time

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.ui.main.routers.navigateToExperience
import com.example.healthassistant.ui.previewproviders.SubstancePreviewProvider


@Composable
fun ChooseTimeScreen(
    navController: NavController,
    viewModel: ChooseTimeViewModel = hiltViewModel()
) {
    viewModel.substance?.let {
        ChooseTimeScreenContent(substance = it, navigateToNext = {
            navController.navigateToExperience(experienceId = 1)
        })
    }
}

@Preview
@Composable
fun ChooseTimeScreenContent(
    @PreviewParameter(SubstancePreviewProvider::class) substance: Substance,
    navigateToNext: () -> Unit = {}
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Choose Ingestion Time") }) }
    ) {
        Text("hello")
    }
}