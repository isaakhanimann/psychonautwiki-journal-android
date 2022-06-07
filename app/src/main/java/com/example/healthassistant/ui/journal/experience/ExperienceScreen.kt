package com.example.healthassistant.ui.journal.experience

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.room.experiences.entities.Experience
import com.example.healthassistant.data.room.experiences.entities.ExperienceWithIngestions
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.ui.journal.experience.timeline.AllTimelines
import com.example.healthassistant.ui.previewproviders.ExperienceWithIngestionsPreviewProvider
import com.example.healthassistant.ui.search.substance.roa.toReadableString
import com.example.healthassistant.ui.theme.HealthAssistantTheme

@Composable
fun ExperienceScreen(
    navigateToAddIngestionSearch: () -> Unit,
    navigateToEditExperienceScreen: () -> Unit,
    viewModel: ExperienceViewModel = hiltViewModel()
) {
    viewModel.experienceWithIngestions.collectAsState().value?.also { experienceWithIngestions ->
        ExperienceScreen(
            experience = experienceWithIngestions.experience,
            ingestionElements = viewModel.ingestionElements.collectAsState().value,
            cumulativeDoses = viewModel.cumulativeDoses.collectAsState().value,
            ingestionDurationPairs = viewModel.ingestionDurationPairs.collectAsState().value,
            addIngestion = navigateToAddIngestionSearch,
            deleteIngestion = viewModel::deleteIngestion,
            navigateToEditExperienceScreen = navigateToEditExperienceScreen
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ExperienceScreenContentPreview(
    @PreviewParameter(
        ExperienceWithIngestionsPreviewProvider::class,
        limit = 1
    ) expAndIng: ExperienceWithIngestions
) {
    HealthAssistantTheme {
        ExperienceScreen(
            experience = expAndIng.experience,
            ingestionElements = expAndIng.ingestions.map {
                ExperienceViewModel.IngestionElement(
                    dateText = null,
                    ingestion = it
                )
            },
            cumulativeDoses = listOf(
                ExperienceViewModel.CumulativeDose(
                    substanceName = "Cocaine",
                    cumulativeDose = 50.0,
                    units = "mg",
                    isEstimate = false
                )
            ),
            ingestionDurationPairs = listOf(),
            addIngestion = {},
            deleteIngestion = {},
            navigateToEditExperienceScreen = {}
        )
    }
}

@Composable
fun ExperienceScreen(
    experience: Experience,
    ingestionElements: List<ExperienceViewModel.IngestionElement>,
    cumulativeDoses: List<ExperienceViewModel.CumulativeDose>,
    ingestionDurationPairs: List<Pair<Ingestion, RoaDuration?>>,
    addIngestion: () -> Unit,
    deleteIngestion: (Ingestion) -> Unit,
    navigateToEditExperienceScreen: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(experience.title)
                },
                actions = {
                    IconButton(onClick = navigateToEditExperienceScreen) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Experience")
                    }
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
            val spacingBetweenSections = 20.dp
            if (ingestionDurationPairs.isNotEmpty()) {
                AllTimelines(
                    ingestionDurationPairs = ingestionDurationPairs,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Divider(modifier = Modifier.padding(vertical = spacingBetweenSections))
            }
            Text("Ingestions", style = MaterialTheme.typography.subtitle1)
            if (ingestionElements.isEmpty()) {
                Button(onClick = addIngestion) {
                    Text(text = "Add an Ingestion")
                }
            }
            ingestionElements.forEach {
                Column(horizontalAlignment = Alignment.End) {
                    if (it.dateText != null) {
                        Text(text = it.dateText)
                    }
                    IngestionRow(
                        ingestion = it.ingestion,
                        deleteIngestion = {
                            deleteIngestion(it.ingestion)
                        },
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }
            }
            if (cumulativeDoses.isNotEmpty()) {
                Text(
                    text = "Cumulative Dose",
                    style = MaterialTheme.typography.subtitle1
                )
                cumulativeDoses.forEach {
                    CumulativeDoseRow(cumulativeDose = it)
                }
            }
            Divider(modifier = Modifier.padding(vertical = spacingBetweenSections))
            if (experience.text.isEmpty()) {
                Button(onClick = navigateToEditExperienceScreen) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Add Notes")
                }
            } else {
                Text(text = experience.text)
            }
        }
    }
}

@Composable
fun CumulativeDoseRow(cumulativeDose: ExperienceViewModel.CumulativeDose) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = cumulativeDose.substanceName, style = MaterialTheme.typography.h6)
            Text(
                text = (if (cumulativeDose.isEstimate) "~" else "") + cumulativeDose.cumulativeDose.toReadableString() + " " + cumulativeDose.units,
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}