package com.example.healthassistant.ui.home.experience

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.experiences.entities.Experience
import com.example.healthassistant.data.experiences.entities.ExperienceWithIngestions
import com.example.healthassistant.data.experiences.entities.Ingestion
import com.example.healthassistant.ui.previewproviders.ExperienceWithIngestionsPreviewProvider

@Composable
fun ExperienceScreen(
    navigateToAddIngestionSearch: (experienceId: Int) -> Unit,
    viewModel: ExperienceViewModel = hiltViewModel()
) {
    viewModel.experienceWithIngestions.collectAsState().value?.also { expWithIngs ->
        ExperienceScreenContent(
            experience = expWithIngs.experience,
            ingestions = expWithIngs.ingestions,
            addIngestion = {
                navigateToAddIngestionSearch(expWithIngs.experience.id)
            },
            deleteIngestion = viewModel::deleteIngestion
        )
    }
}

@Preview
@Composable
fun ExperienceScreenContentPreview(
    @PreviewParameter(ExperienceWithIngestionsPreviewProvider::class) expAndIng: ExperienceWithIngestions
) {
    ExperienceScreenContent(
        experience = expAndIng.experience,
        ingestions = expAndIng.ingestions,
        addIngestion = {},
        deleteIngestion = {}
    )
}

@Composable
fun ExperienceScreenContent(
    experience: Experience,
    ingestions: List<Ingestion>,
    addIngestion: () -> Unit,
    deleteIngestion: (Ingestion) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(experience.title) }
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
            if (ingestions.isEmpty()) {
                Button(onClick = addIngestion) {
                    Text(text = "Add an Ingestion", style = MaterialTheme.typography.subtitle1)
                }
            }
            ingestions.forEach {
                IngestionRow(
                    ingestion = it,
                    deleteIngestion = {
                        deleteIngestion(it)
                    },
                    modifier = Modifier.padding(vertical = 3.dp)
                )
            }
        }
    }
}