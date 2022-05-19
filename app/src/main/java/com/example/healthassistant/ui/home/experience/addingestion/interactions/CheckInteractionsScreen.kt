package com.example.healthassistant.ui.home.experience.addingestion.interactions

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxHeight()
        ) {
            InteractionsView(substance = substance)
            Button(
                onClick = navigateToNext,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Next", style = MaterialTheme.typography.h3)
            }
        }
    }
}