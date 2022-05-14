package com.example.healthassistant.ui.home.experience.addingestion.interactions

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.ui.main.routers.navigateToChooseRoute
import com.example.healthassistant.ui.previewproviders.SubstancePreviewProvider


@Composable
fun CheckInteractionsScreen(
    navController: NavController,
    viewModel: CheckInteractionsViewModel = hiltViewModel()
) {
    viewModel.substance?.let {
        CheckInteractionsScreenContent(
            substance = it,
            navigateToNext = {
                navController.navigateToChooseRoute(substanceName = it.name, experienceId = 1)
            }
        )
    }
}

@Preview
@Composable
fun CheckInteractionsScreenContent(
    @PreviewParameter(SubstancePreviewProvider::class) substance: Substance,
    navigateToNext: () -> Unit = {}
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Check Interactions") }) }
    ) {
        Column {
            Button(onClick = navigateToNext) {
                Text("Next")
            }
            Text(text = "List all interactions of ${substance.name} here including the loaded once")
        }
    }
}