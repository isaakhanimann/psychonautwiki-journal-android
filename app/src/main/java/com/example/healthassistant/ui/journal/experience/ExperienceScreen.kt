package com.example.healthassistant.ui.journal.experience

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExperienceScreen(
    navigateToAddIngestionSearch: () -> Unit,
    navigateToEditExperienceScreen: () -> Unit,
    viewModel: ExperienceViewModel = hiltViewModel()
) {
    viewModel.experienceWithIngestions.collectAsState().value?.also { experienceWithIngestions ->
        ExperienceScreenContent(
            experience = experienceWithIngestions.experience,
            ingestions = experienceWithIngestions.ingestions,
            cumulativeDoses = viewModel.cumulativeDoses.collectAsState().value,
            ingestionDurationPairs = viewModel.ingestionDurationPairs.collectAsState().value,
            addIngestion = navigateToAddIngestionSearch,
            deleteIngestion = viewModel::deleteIngestion,
            navigateToEditExperienceScreen = navigateToEditExperienceScreen
        )
    }
}

@Preview
@Composable
fun ExperienceScreenContentPreview(
    @PreviewParameter(
        ExperienceWithIngestionsPreviewProvider::class,
        limit = 1
    ) expAndIng: ExperienceWithIngestions
) {
    ExperienceScreenContent(
        experience = expAndIng.experience,
        ingestions = expAndIng.ingestions,
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

@Composable
fun ExperienceScreenContent(
    experience: Experience,
    ingestions: List<Ingestion>,
    cumulativeDoses: List<ExperienceViewModel.CumulativeDose>,
    ingestionDurationPairs: List<Pair<Ingestion, RoaDuration?>>,
    addIngestion: () -> Unit,
    deleteIngestion: (Ingestion) -> Unit,
    navigateToEditExperienceScreen: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(experience.title) },
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
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingestions", style = MaterialTheme.typography.h5)
                val dateText = remember(experience.date) {
                    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                    formatter.format(experience.date) ?: ""
                }
                Text(text = dateText, style = MaterialTheme.typography.subtitle1)
            }
            if (ingestions.isEmpty()) {
                Button(onClick = addIngestion) {
                    Text(text = "Add an Ingestion")
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
            cumulativeDoses.forEach {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Cumulative ${it.substanceName} Dose:")
                    Text(text = (if (it.isEstimate) "~" else "" ) + it.cumulativeDose.toReadableString() + " " + it.units)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Timeline", style = MaterialTheme.typography.h5)
            AllTimelines(
                ingestionDurationPairs = ingestionDurationPairs,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
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
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(10.dp)) {
                        Text(text = "Notes", style = MaterialTheme.typography.h6)
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                            Text(text = experience.text)
                        }
                    }
                }
            }
        }
    }
}