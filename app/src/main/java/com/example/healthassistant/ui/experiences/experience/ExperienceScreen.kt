package com.example.healthassistant.ui.experiences.experience

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.room.experiences.entities.Experience
import com.example.healthassistant.data.room.experiences.relations.ExperienceWithIngestions
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.ui.experiences.experience.timeline.AllTimelines
import com.example.healthassistant.ui.previewproviders.ExperienceWithIngestionsPreviewProvider
import com.example.healthassistant.ui.search.substance.roa.toReadableString
import com.example.healthassistant.ui.theme.HealthAssistantTheme

@Composable
fun ExperienceScreen(
    viewModel: ExperienceViewModel = hiltViewModel(),
    navigateToAddIngestionSearch: () -> Unit,
    navigateToEditExperienceScreen: () -> Unit,
    navigateToIngestionScreen: (ingestionId: Int) -> Unit,
    navigateBack: () -> Unit,
) {
    viewModel.experienceWithIngestionsFlow.collectAsState().value?.also { experienceWithIngestions ->
        ExperienceScreen(
            experience = experienceWithIngestions.experience,
            ingestionElements = viewModel.ingestionElementsFlow.collectAsState().value,
            cumulativeDoses = viewModel.cumulativeDosesFlow.collectAsState().value,
            ingestionDurationPairs = viewModel.ingestionDurationPairsFlow.collectAsState().value,
            addIngestion = navigateToAddIngestionSearch,
            viewModel::deleteExperience,
            navigateToEditExperienceScreen,
            navigateToIngestionScreen,
            navigateBack,
            isShowingDialog = viewModel.isShowingDeleteDialog,
            showDialog = { viewModel.isShowingDeleteDialog = true },
            dismissDialog = { viewModel.isShowingDeleteDialog = false }
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
            deleteExperience = {},
            navigateToEditExperienceScreen = {},
            navigateToIngestionScreen = {},
            navigateBack = {},
            isShowingDialog = true,
            showDialog = {},
            dismissDialog = {}
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
    deleteExperience: () -> Unit,
    navigateToEditExperienceScreen: () -> Unit,
    navigateToIngestionScreen: (ingestionId: Int) -> Unit,
    navigateBack: () -> Unit,
    isShowingDialog: Boolean,
    showDialog: () -> Unit,
    dismissDialog: () -> Unit
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
            ExtendedFloatingActionButton(
                onClick = addIngestion,
                icon = {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add"
                    )
                },
                text = { Text("Add Ingestion") }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (ingestionDurationPairs.isNotEmpty()) {
                AllTimelines(
                    ingestionDurationPairs = ingestionDurationPairs,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                val showOralOnsetDisclaimer =
                    ingestionDurationPairs.any { it.first.administrationRoute == AdministrationRoute.ORAL }
                if (showOralOnsetDisclaimer) {
                    Text(
                        text = "* a full stomach can delay the onset by hours",
                        style = MaterialTheme.typography.caption
                    )
                }
                Divider(modifier = Modifier.padding(top = 10.dp))
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
                        navigateToIngestionScreen = { navigateToIngestionScreen(it.ingestion.id) },
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
            Divider(modifier = Modifier.padding(top = 10.dp))
            if (experience.text.isEmpty()) {
                TextButton(onClick = navigateToEditExperienceScreen) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Add Notes")
                }
            } else {
                Text(text = experience.text, modifier = Modifier.padding(vertical = 10.dp))
            }
            Divider()
            TextButton(
                onClick = showDialog,
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete Experience",
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                    tint = Color.Red
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Delete", color = Color.Red)
            }
            if (isShowingDialog) {
                AlertDialog(
                    onDismissRequest = dismissDialog,
                    title = {
                        Text(text = "Are you sure?")
                    },
                    text = {
                        Text("This won't delete any ingestions")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                dismissDialog()
                                deleteExperience()
                                navigateBack()
                            }
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = dismissDialog
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
            Divider()
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