package com.isaakhanimann.healthassistant.ui.journal.experience

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Sentiment
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import com.isaakhanimann.healthassistant.ui.journal.experience.timeline.AllTimelines
import com.isaakhanimann.healthassistant.ui.search.substance.roa.toReadableString
import com.isaakhanimann.healthassistant.ui.theme.HealthAssistantTheme
import com.isaakhanimann.healthassistant.ui.utils.getStringOfPattern

@Composable
fun ExperienceScreen(
    viewModel: OneExperienceViewModel = hiltViewModel(),
    navigateToAddIngestionSearch: () -> Unit,
    navigateToEditExperienceScreen: () -> Unit,
    navigateToIngestionScreen: (ingestionId: Int) -> Unit,
    navigateBack: () -> Unit,
) {
    viewModel.oneExperienceScreenModelFlow.collectAsState().value?.also { oneExperienceScreenModel ->
        ExperienceScreen(
            oneExperienceScreenModel = oneExperienceScreenModel,
            addIngestion = navigateToAddIngestionSearch,
            deleteExperience = viewModel::deleteExperience,
            navigateToEditExperienceScreen,
            navigateToIngestionScreen,
            navigateBack,
            saveSentiment = viewModel::saveSentiment,
            saveIsFavorite = viewModel::saveIsFavorite
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ExperienceScreenPreview(
    @PreviewParameter(
        OneExperienceScreenPreviewProvider::class,
        limit = 1
    ) oneExperienceScreenModel: OneExperienceScreenModel
) {
    HealthAssistantTheme {
        ExperienceScreen(
            oneExperienceScreenModel = oneExperienceScreenModel,
            addIngestion = {},
            deleteExperience = {},
            navigateToEditExperienceScreen = {},
            navigateToIngestionScreen = {},
            navigateBack = {},
            saveSentiment = {},
            saveIsFavorite = {}
        )
    }
}

@Composable
fun ExperienceScreen(
    oneExperienceScreenModel: OneExperienceScreenModel,
    addIngestion: () -> Unit,
    deleteExperience: () -> Unit,
    navigateToEditExperienceScreen: () -> Unit,
    navigateToIngestionScreen: (ingestionId: Int) -> Unit,
    navigateBack: () -> Unit,
    saveSentiment: (sentiment: Sentiment?) -> Unit,
    saveIsFavorite: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(oneExperienceScreenModel.title)
                        IconButton(onClick = navigateToEditExperienceScreen) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit Experience",
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                },
                actions = {
                    SentimentButton(
                        sentiment = oneExperienceScreenModel.sentiment,
                        saveSentiment = saveSentiment,
                    )
                    val isFavorite = oneExperienceScreenModel.isFavorite
                    IconToggleButton(checked = isFavorite, onCheckedChange = saveIsFavorite) {
                        if (isFavorite) {
                            Icon(Icons.Filled.Star, contentDescription = "Is Favorite")
                        } else {
                            Icon(Icons.Outlined.StarOutline, contentDescription = "Is not Favorite")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (oneExperienceScreenModel.isShowingAddIngestionButton) {
                ExtendedFloatingActionButton(
                    onClick = addIngestion,
                    icon = {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add"
                        )
                    },
                    text = { Text("Ingestion") }
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            val horizontalPadding = 10.dp
            Spacer(modifier = Modifier.height(10.dp))
            val ingestionDurationPairs = oneExperienceScreenModel.ingestionElements.map {
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
                            .padding(bottom = 5.dp)
                    )
                    val showOralOnsetDisclaimer =
                        ingestionDurationPairs.any { it.first.ingestion.administrationRoute == AdministrationRoute.ORAL }
                    if (showOralOnsetDisclaimer) {
                        Text(
                            text = "* a full stomach can delay the onset for hours",
                            style = MaterialTheme.typography.caption,
                        )
                    }
                    Text(
                        text = "* heavy doses can have longer durations",
                        style = MaterialTheme.typography.caption
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                Divider()
            }
            if (oneExperienceScreenModel.ingestionElements.isNotEmpty()) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = oneExperienceScreenModel.firstIngestionTime.getStringOfPattern("EEE, dd MMM yyyy"),
                    )
                    oneExperienceScreenModel.ingestionElements.forEach {
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
            if (oneExperienceScreenModel.cumulativeDoses.isNotEmpty()) {
                Column(
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    Text(
                        text = "Cumulative Dose",
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    oneExperienceScreenModel.cumulativeDoses.forEach {
                        CumulativeDoseRow(cumulativeDose = it, modifier = Modifier.fillMaxWidth())
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
                Divider()
            }
            if (oneExperienceScreenModel.notes.isEmpty()) {
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
                    Text(
                        text = oneExperienceScreenModel.notes,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    IconButton(onClick = navigateToEditExperienceScreen) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Notes")
                    }
                }
            }
            Divider()
            var isShowingDeleteDialog by remember { mutableStateOf(false) }
            TextButton(
                onClick = { isShowingDeleteDialog = true },
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text(
                    "Delete Experience",
                    style = MaterialTheme.typography.caption
                )
            }
            if (isShowingDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { isShowingDeleteDialog = false },
                    title = {
                        Text(text = "Delete Experience?")
                    },
                    text = {
                        Text("This will also delete all its ingestions.")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                isShowingDeleteDialog = false
                                deleteExperience()
                                navigateBack()
                            }
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { isShowingDeleteDialog = false }
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
fun CumulativeDoseRow(cumulativeDose: CumulativeDose, modifier: Modifier) {
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