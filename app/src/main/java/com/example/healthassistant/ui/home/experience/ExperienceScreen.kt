package com.example.healthassistant.ui.home.experience

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.healthassistant.data.experiences.entities.ExperienceWithIngestions
import com.example.healthassistant.ui.main.routers.navigateToAddIngestionSearch
import com.example.healthassistant.ui.previewproviders.ExperienceWithIngestionsPreviewProvider
import com.example.healthassistant.ui.search.substance.NavigateBackIcon

@Composable
fun ExperienceScreen(
    navController: NavHostController,
    viewModel: ExperienceViewModel = hiltViewModel()
) {
    viewModel.experienceWithIngestions?.let { exp ->
        ExperienceScreenContent(
            expAndIng = exp,
            navigateBack = navController::popBackStack,
            addIngestion = {
                navController.navigateToAddIngestionSearch(experienceId = exp.experience.id)
            }
        )
    } ?: run {
        Button(onClick = navController::popBackStack) {
            Text("There was an error. Go back.")
        }
    }
}


@Preview
@Composable
fun ExperienceScreenContent(
    @PreviewParameter(ExperienceWithIngestionsPreviewProvider::class) expAndIng: ExperienceWithIngestions,
    navigateBack: () -> Unit = {},
    addIngestion: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(expAndIng.experience.title) },
                navigationIcon = {
                    NavigateBackIcon(navigateBack)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = addIngestion) {
                Icon(Icons.Default.Add, "Add Ingestion")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Ingestions", style = MaterialTheme.typography.h5)
            expAndIng.ingestions.forEach {
                IngestionRow(ingestion = it, modifier = Modifier.padding(vertical = 3.dp))
            }
        }
    }
}