package com.example.healthassistant.ui.experiences.experience

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddReaction
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
import com.example.healthassistant.data.room.experiences.entities.Sentiment
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.ui.experiences.experience.edit.SentimentDialog
import com.example.healthassistant.ui.experiences.experience.timeline.AllTimelines
import com.example.healthassistant.ui.ingestions.DotRow
import com.example.healthassistant.ui.search.substance.roa.toReadableString
import com.example.healthassistant.ui.theme.HealthAssistantTheme

@Composable
fun ExperienceScreen(
    viewModel: OneExperienceViewModel = hiltViewModel(),
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
            addIngestion = navigateToAddIngestionSearch,
            viewModel::deleteExperience,
            navigateToEditExperienceScreen,
            navigateToIngestionScreen,
            navigateBack,
            isShowingDialog = viewModel.isShowingDeleteDialog,
            showDialog = { viewModel.isShowingDeleteDialog = true },
            dismissDialog = { viewModel.isShowingDeleteDialog = false },
            isShowingSentimentDialog = viewModel.isShowingSentimentDialog,
            showSentimentDialog = viewModel::showEditSentimentDialog,
            dismissSentimentDialog = viewModel::dismissEditSentimentDialog,
            saveSentiment = viewModel::saveSentiment,
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ExperienceScreenPreview(
    @PreviewParameter(
        OneExperienceScreenPreviewProvider::class,
        limit = 1
    ) allThatIsNeeded: OneExperienceScreenPreviewProvider.AllThatIsNeeded
) {
    HealthAssistantTheme {
        ExperienceScreen(
            experience = allThatIsNeeded.experience,
            ingestionElements = allThatIsNeeded.ingestionElements,
            cumulativeDoses = allThatIsNeeded.cumulativeDoses,
            addIngestion = {},
            deleteExperience = {},
            navigateToEditExperienceScreen = {},
            navigateToIngestionScreen = {},
            navigateBack = {},
            isShowingDialog = false,
            showDialog = {},
            dismissDialog = {},
            isShowingSentimentDialog = false,
            showSentimentDialog = {},
            dismissSentimentDialog = {},
            saveSentiment = {},
        )
    }
}

@Composable
fun ExperienceScreen(
    experience: Experience,
    ingestionElements: List<OneExperienceViewModel.IngestionElement>,
    cumulativeDoses: List<OneExperienceViewModel.CumulativeDose>,
    addIngestion: () -> Unit,
    deleteExperience: () -> Unit,
    navigateToEditExperienceScreen: () -> Unit,
    navigateToIngestionScreen: (ingestionId: Int) -> Unit,
    navigateBack: () -> Unit,
    isShowingDialog: Boolean,
    showDialog: () -> Unit,
    dismissDialog: () -> Unit,
    isShowingSentimentDialog: Boolean,
    showSentimentDialog: () -> Unit,
    dismissSentimentDialog: () -> Unit,
    saveSentiment: (sentiment: Sentiment?) -> Unit
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
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            val ingestionDurationPairs = ingestionElements.map {
                Pair(
                    first = it.ingestionWithCompanion,
                    second = it.roaDuration
                )
            }
            if (ingestionDurationPairs.isNotEmpty()) {
                AllTimelines(
                    ingestionDurationPairs = ingestionDurationPairs,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                val showOralOnsetDisclaimer =
                    ingestionDurationPairs.any { it.first.ingestion.administrationRoute == AdministrationRoute.ORAL }
                if (showOralOnsetDisclaimer) {
                    Text(
                        text = "* a full stomach can delay the onset by hours",
                        style = MaterialTheme.typography.caption
                    )
                }
                Divider(modifier = Modifier.padding(top = 10.dp))
            }
            if (ingestionElements.isNotEmpty()) {
                ingestionElements.forEach {
                    Column(horizontalAlignment = Alignment.End) {
                        if (it.dateText != null) {
                            Text(text = it.dateText)
                        }
                        IngestionRow(
                            ingestionElement = it,
                            modifier = Modifier.clickable {
                                navigateToIngestionScreen(it.ingestionWithCompanion.ingestion.id)
                            }.fillMaxWidth().padding(vertical = 5.dp)
                        )
                    }
                }
                Divider()
            }
            if (cumulativeDoses.isNotEmpty()) {
                Text(
                    text = "Cumulative Dose",
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(top = 10.dp)
                )
                cumulativeDoses.forEach {
                    CumulativeDoseRow(cumulativeDose = it, modifier = Modifier.fillMaxWidth())
                }
                Divider(modifier = Modifier.padding(top = 10.dp))
            }
            if (isShowingSentimentDialog) {
                SentimentDialog(dismiss = dismissSentimentDialog, saveSentiment = saveSentiment)
            }
            SentimentSection(
                sentiment = experience.sentiment,
                showDialog = showSentimentDialog
            )
            Divider()
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
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = experience.text, modifier = Modifier.padding(vertical = 10.dp))
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    IconButton(onClick = navigateToEditExperienceScreen) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Notes")
                    }
                }
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
fun SentimentSection(
    sentiment: Sentiment?,
    showDialog: () -> Unit
) {
    if (sentiment == null) {
        TextButton(
            onClick = showDialog,
        ) {
            Icon(
                Icons.Filled.AddReaction,
                contentDescription = "Add Sentiment",
                modifier = Modifier.size(ButtonDefaults.IconSize),
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Add Sentiment")
        }
    } else {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                sentiment.icon,
                contentDescription = sentiment.description,
                modifier = Modifier
                    .padding(vertical = 7.dp)
                    .size(40.dp),
            )
            IconButton(onClick = showDialog) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Sentiment")
            }
        }
    }
}

@Composable
fun CumulativeDoseRow(cumulativeDose: OneExperienceViewModel.CumulativeDose, modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = cumulativeDose.substanceName, style = MaterialTheme.typography.h6)
        Column {
            Text(
                text = (if (cumulativeDose.isEstimate) "~" else "") + cumulativeDose.cumulativeDose.toReadableString() + " " + cumulativeDose.units,
                style = MaterialTheme.typography.subtitle1
            )
            val doseClass = cumulativeDose.doseClass
            if (doseClass != null) {
                DotRow(doseClass = doseClass)
            }
        }
    }
}