package com.example.healthassistant.ui.home.experience.addingestion.interactions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.ui.main.routers.navigateToChooseRoute
import com.example.healthassistant.ui.previewproviders.SubstancePreviewProvider
import com.example.healthassistant.ui.search.substance.InteractionsView


@Composable
fun CheckInteractionsScreen(
    navController: NavController,
    viewModel: CheckInteractionsViewModel = hiltViewModel()
) {
    viewModel.substance?.let {
        CheckInteractionsScreenContent(
            substance = it,
            navigateToNext = {
                navController.navigateToChooseRoute(
                    substanceName = it.name,
                    experienceId = viewModel.experienceId
                )
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
        Column(modifier = Modifier.padding(10.dp)) {
            InteractionsView(substance = substance)
            Button(onClick = navigateToNext) {
                Text("Next")
            }
        }
    }
}