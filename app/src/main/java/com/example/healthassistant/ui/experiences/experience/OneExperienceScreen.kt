package com.example.healthassistant.ui.experiences.experience

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
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
import com.example.healthassistant.data.room.experiences.entities.Sentiment
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.ui.experiences.experience.timeline.AllTimelines
import com.example.healthassistant.ui.ingestions.DotRow
import com.example.healthassistant.ui.ingestions.ingestion.SentimentSection
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
            isShowingSentimentMenu = viewModel.isShowingSentimentMenu,
            showSentimentMenu = viewModel::showEditSentimentMenu,
            dismissSentimentMenu = viewModel::dismissEditSentimentMenu,
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
            isShowingSentimentMenu = false,
            showSentimentMenu = {},
            dismissSentimentMenu = {},
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
    isShowingSentimentMenu: Boolean,
    showSentimentMenu: () -> Unit,
    dismissSentimentMenu: () -> Unit,
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
                .verticalScroll(rememberScrollState())
        ) {
            val horizontalPadding = 10.dp
            Spacer(modifier = Modifier.height(10.dp))
            val ingestionDurationPairs = ingestionElements.map {
                Pair(
                    first = it.ingestionWithCompanion,
                    second = it.roaDuration
                )
            }
            if (ingestionDurationPairs.isNotEmpty()) {
                Column(modifier = Modifier.padding(horizontal = horizontalPadding)) {
                    AllTimelines(
                        ingestionDurationPairs = ingestionDurationPairs,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    val showOralOnsetDisclaimer =
                        ingestionDurationPairs.any { it.first.ingestion.administrationRoute == AdministrationRoute.ORAL }
                    if (showOralOnsetDisclaimer) {
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "* a full stomach can delay the onset for hours",
                            style = MaterialTheme.typography.caption,
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
                Divider()
            }
            if (ingestionElements.isNotEmpty()) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    Spacer(modifier = Modifier.height(5.dp))
                    ingestionElements.forEach {
                        if (it.dateText != null) {
                            Text(text = it.dateText)
                        }
                        IngestionRow(
                            ingestionElement = it,
                            modifier = Modifier
                                .clickable {
                                    navigateToIngestionScreen(it.ingestionWithCompanion.ingestion.id)
                                }
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Divider()
            }
            if (cumulativeDoses.isNotEmpty()) {
                Column(
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    Text(
                        text = "Cumulative Dose",
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    cumulativeDoses.forEach {
                        CumulativeDoseRow(cumulativeDose = it, modifier = Modifier.fillMaxWidth())
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
                Divider()
            }
            SentimentSection(
                sentiment = experience.sentiment,
                isShowingEditSentiment = isShowingSentimentMenu,
                show = showSentimentMenu,
                dismiss = dismissSentimentMenu,
                saveSentiment = saveSentiment,
                modifier = Modifier.padding(start = horizontalPadding)
            )
            Divider()
            if (experience.text.isEmpty()) {
                TextButton(
                    onClick = navigateToEditExperienceScreen,
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = horizontalPadding)
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
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text(
                    "Delete Experience",
                    style = MaterialTheme.typography.caption
                )
            }
            if (isShowingDialog) {
                AlertDialog(
                    onDismissRequest = dismissDialog,
                    title = {
                        Text(text = "Delete Experience?")
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
        Column(horizontalAlignment = Alignment.End) {
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