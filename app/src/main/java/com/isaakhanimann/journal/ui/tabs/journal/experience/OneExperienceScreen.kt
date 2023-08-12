/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.journal.experience

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.FULL_STOMACH_DISCLAIMER
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.*
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.ingestion.IngestionRow
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.rating.RatingRow
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.timednote.TimedNoteRow
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.AllTimelines
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.DataForOneRating
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.DataForOneTimedNote
import com.isaakhanimann.journal.ui.theme.JournalTheme
import com.isaakhanimann.journal.ui.theme.horizontalPadding
import com.isaakhanimann.journal.ui.utils.getStringOfPattern
import java.time.Instant
import androidx.compose.material3.ElevatedCard as Card

@Composable
fun OneExperienceScreen(
    viewModel: OneExperienceViewModel = hiltViewModel(),
    navigateToAddIngestionSearch: () -> Unit,
    navigateToEditExperienceScreen: () -> Unit,
    navigateToExplainTimeline: () -> Unit,
    navigateToURL: (url: String) -> Unit,
    navigateToIngestionScreen: (ingestionId: Int) -> Unit,
    navigateToAddRatingScreen: () -> Unit,
    navigateToAddTimedNoteScreen: () -> Unit,
    navigateToEditRatingScreen: (ratingId: Int) -> Unit,
    navigateToEditTimedNoteScreen: (timedNoteId: Int) -> Unit,
    navigateBack: () -> Unit,
) {
    val ingestionsWithCompanions = viewModel.ingestionsWithCompanionsFlow.collectAsState().value
    val experience = viewModel.experienceFlow.collectAsState().value
    val isFavorite = viewModel.isFavoriteFlow.collectAsState().value
    val oneExperienceScreenModel = OneExperienceScreenModel(
        isFavorite = isFavorite,
        title = experience?.title ?: "",
        firstIngestionTime = ingestionsWithCompanions.firstOrNull()?.ingestion?.time
            ?: experience?.sortDate ?: Instant.now(),
        notes = experience?.text ?: "",
        locationName = experience?.location?.name ?: "",
        isShowingAddIngestionButton = viewModel.isShowingAddIngestionButtonFlow.collectAsState().value,
        ingestionElements = viewModel.ingestionElementsFlow.collectAsState().value,
        cumulativeDoses = viewModel.cumulativeDosesFlow.collectAsState().value,
        interactions = viewModel.interactionsFlow.collectAsState().value,
        interactionExplanations = viewModel.interactionExplanationsFlow.collectAsState().value,
        ratings = viewModel.ratingsFlow.collectAsState().value,
        timedNotes = viewModel.timedNotesFlow.collectAsState().value
    )
    OneExperienceScreen(
        oneExperienceScreenModel = oneExperienceScreenModel,
        addIngestion = navigateToAddIngestionSearch,
        deleteExperience = viewModel::deleteExperience,
        navigateToEditExperienceScreen = navigateToEditExperienceScreen,
        navigateToExplainTimeline = navigateToExplainTimeline,
        navigateToIngestionScreen = navigateToIngestionScreen,
        navigateToAddRatingScreen = navigateToAddRatingScreen,
        navigateToAddTimedNoteScreen = navigateToAddTimedNoteScreen,
        navigateBack = navigateBack,
        saveIsFavorite = viewModel::saveIsFavorite,
        navigateToURL = navigateToURL,
        navigateToEditRatingScreen = navigateToEditRatingScreen,
        navigateToEditTimedNoteScreen = navigateToEditTimedNoteScreen,
        timeDisplayOption = viewModel.timeDisplayOption.value,
        onChangeTimeDisplayOption = { viewModel.timeDisplayOption.value = it }
    )
}

@Preview
@Composable
fun ExperienceScreenPreview(
    @PreviewParameter(
        OneExperienceScreenPreviewProvider::class,
        limit = 1
    ) oneExperienceScreenModel: OneExperienceScreenModel
) {
    JournalTheme {
        OneExperienceScreen(
            oneExperienceScreenModel = oneExperienceScreenModel,
            addIngestion = {},
            deleteExperience = {},
            navigateToEditExperienceScreen = {},
            navigateToExplainTimeline = {},
            navigateToIngestionScreen = {},
            navigateToAddRatingScreen = {},
            navigateToAddTimedNoteScreen = {},
            navigateBack = {},
            saveIsFavorite = {},
            navigateToURL = {},
            navigateToEditRatingScreen = {},
            navigateToEditTimedNoteScreen = {},
            timeDisplayOption = TimeDisplayOption.RELATIVE_TO_START,
            onChangeTimeDisplayOption = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneExperienceScreen(
    oneExperienceScreenModel: OneExperienceScreenModel,
    addIngestion: () -> Unit,
    deleteExperience: () -> Unit,
    navigateToEditExperienceScreen: () -> Unit,
    navigateToURL: (url: String) -> Unit,
    navigateToExplainTimeline: () -> Unit,
    navigateToIngestionScreen: (ingestionId: Int) -> Unit,
    navigateToAddRatingScreen: () -> Unit,
    navigateToAddTimedNoteScreen: () -> Unit,
    navigateBack: () -> Unit,
    saveIsFavorite: (Boolean) -> Unit,
    navigateToEditRatingScreen: (ratingId: Int) -> Unit,
    navigateToEditTimedNoteScreen: (timedNoteId: Int) -> Unit,
    timeDisplayOption: TimeDisplayOption,
    onChangeTimeDisplayOption: (TimeDisplayOption) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(oneExperienceScreenModel.title) },
                actions = {
                    var isShowingDeleteDialog by remember { mutableStateOf(false) }
                    IconButton(
                        onClick = { isShowingDeleteDialog = true },
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete Experience",
                        )
                    }
                    AnimatedVisibility(visible = isShowingDeleteDialog) {
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
                    IconButton(onClick = navigateToEditExperienceScreen) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Experience",
                        )
                    }
                    val isFavorite = oneExperienceScreenModel.isFavorite
                    IconToggleButton(checked = isFavorite, onCheckedChange = saveIsFavorite) {
                        if (isFavorite) {
                            Icon(Icons.Filled.Star, contentDescription = "Is Favorite")
                        } else {
                            Icon(Icons.Outlined.StarOutline, contentDescription = "Is not Favorite")
                        }
                    }
                    var isExpanded by remember { mutableStateOf(false) }
                    IconButton(onClick = { isExpanded = true }) {
                        if (timeDisplayOption == TimeDisplayOption.REGULAR) {
                            Icon(Icons.Outlined.Timer, contentDescription = "Time Regular")
                        } else {
                            Icon(Icons.Filled.Timer, contentDescription = "Regular Time")
                        }
                    }
                    DropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }
                    ) {
                        TimeDisplayOption.values().forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.text) },
                                onClick = {
                                    onChangeTimeDisplayOption(option)
                                    isExpanded = false
                                },
                                leadingIcon = {
                                    if (option == timeDisplayOption) {
                                        Icon(
                                            Icons.Filled.Check,
                                            contentDescription = "Check",
                                            modifier = Modifier.size(ButtonDefaults.IconSize)
                                        )
                                    }
                                }
                            )
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
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = horizontalPadding)
        ) {
            val elements = oneExperienceScreenModel.ingestionElements
            val effectTimelines = remember(elements) {
                elements.map { oneElement ->
                    val horizontalWeight = if (oneElement.numDots == null) {
                        0.5f
                    } else if (oneElement.numDots > 4) {
                        1f
                    } else {
                        oneElement.numDots.toFloat() / 4f
                    }
                    return@map DataForOneEffectLine(
                        substanceName = oneElement.ingestionWithCompanion.ingestion.substanceName,
                        roaDuration = oneElement.roaDuration,
                        height = getHeightBetween0And1(
                            ingestion = oneElement.ingestionWithCompanion.ingestion,
                            allIngestions = elements.map { it.ingestionWithCompanion.ingestion }
                        ),
                        horizontalWeight = horizontalWeight,
                        color = oneElement.ingestionWithCompanion.substanceCompanion?.color
                            ?: AdaptiveColor.RED,
                        startTime = oneElement.ingestionWithCompanion.ingestion.time
                    )
                }
            }
            val verticalCardPadding = 4.dp
            if (effectTimelines.isNotEmpty()) {
                Card(modifier = Modifier.padding(vertical = verticalCardPadding)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CardTitle(title = "Effect Timeline")
                        IconButton(onClick = navigateToExplainTimeline) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "Timeline Disclaimer"
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .padding(horizontal = horizontalPadding)
                            .padding(bottom = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        AllTimelines(
                            dataForEffectLines = effectTimelines,
                            dataForRatings = oneExperienceScreenModel.ratings.mapNotNull {
                                val ratingTime = it.time
                                return@mapNotNull if (ratingTime == null) {
                                    null
                                } else {
                                    DataForOneRating(
                                        time = ratingTime,
                                        option = it.option
                                    )
                                }
                            },
                            dataForTimedNotes = oneExperienceScreenModel.timedNotes.map {
                                DataForOneTimedNote(time = it.time, color = it.color)
                            },
                            isShowingCurrentTime = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                        if (oneExperienceScreenModel.ingestionElements.any { it.ingestionWithCompanion.ingestion.administrationRoute == AdministrationRoute.ORAL }) {
                            Text(
                                text = FULL_STOMACH_DISCLAIMER,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
            if (oneExperienceScreenModel.ingestionElements.isNotEmpty()) {
                Card(modifier = Modifier.padding(vertical = verticalCardPadding)) {
                    CardTitle(
                        title = oneExperienceScreenModel.firstIngestionTime.getStringOfPattern(
                            "EEE, dd MMM yyyy"
                        )
                    )
                    if (oneExperienceScreenModel.ingestionElements.isNotEmpty()) {
                        Divider()
                    }
                    oneExperienceScreenModel.ingestionElements.forEachIndexed { index, ingestionElement ->
                        IngestionRow(
                            ingestionElement = ingestionElement,
                            timeDisplayOption = timeDisplayOption,
                            startTime = oneExperienceScreenModel.firstIngestionTime,
                            modifier = Modifier
                                .clickable {
                                    navigateToIngestionScreen(ingestionElement.ingestionWithCompanion.ingestion.id)
                                }
                                .fillMaxWidth()
                                .padding(vertical = 5.dp, horizontal = horizontalPadding)
                        )
                        if (index < oneExperienceScreenModel.ingestionElements.size - 1) {
                            Divider()
                        }
                    }
                }
            }
            val cumulativeDoses = oneExperienceScreenModel.cumulativeDoses
            if (cumulativeDoses.isNotEmpty()) {
                Card(modifier = Modifier.padding(vertical = verticalCardPadding)) {
                    CardTitle(title = "Cumulative Dose")
                    cumulativeDoses.forEachIndexed { index, cumulativeDose ->
                        CumulativeDoseRow(
                            cumulativeDose = cumulativeDose, modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp, horizontal = horizontalPadding)
                        )
                        if (index < cumulativeDoses.size - 1) {
                            Divider()
                        }
                    }
                }
            }
            Card(modifier = Modifier.padding(vertical = verticalCardPadding)) {
                if (oneExperienceScreenModel.timedNotes.isEmpty()) {
                    AddTimedNoteButton(navigateToAddTimedNoteScreen)
                } else {
                    CardTitle(title = "Timed Notes")
                    if (oneExperienceScreenModel.timedNotes.isNotEmpty()) {
                        Divider()
                    }
                    oneExperienceScreenModel.timedNotes.forEach { timedNote ->
                        TimedNoteRow(
                            timedNote = timedNote,
                            timeDisplayOption = timeDisplayOption,
                            startTime = oneExperienceScreenModel.firstIngestionTime,
                            modifier = Modifier
                                .clickable {
                                    navigateToEditTimedNoteScreen(timedNote.id)
                                }
                                .fillMaxWidth()
                                .padding(vertical = 5.dp, horizontal = horizontalPadding)
                        )
                        Divider()
                    }
                    AddTimedNoteButton(navigateToAddTimedNoteScreen)
                }
            }
            Card(modifier = Modifier.padding(vertical = verticalCardPadding)) {
                if (oneExperienceScreenModel.ratings.isEmpty()) {
                    AddShulginRatingButton(navigateToAddRatingScreen)
                } else {
                    CardTitle(title = "Shulgin Ratings")
                    Divider()
                    val ratingsWithTime =
                        oneExperienceScreenModel.ratings.filter { it.time != null }
                    ratingsWithTime.forEach { rating ->
                        RatingRow(
                            rating = rating,
                            timeDisplayOption = timeDisplayOption,
                            startTime = oneExperienceScreenModel.firstIngestionTime,
                            modifier = Modifier
                                .clickable {
                                    navigateToEditRatingScreen(rating.id)
                                }
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = horizontalPadding)
                        )
                        Divider()
                    }
                    val overallRating =
                        oneExperienceScreenModel.ratings.firstOrNull { it.time == null }
                    if (overallRating != null) {
                        RatingRow(
                            rating = overallRating,
                            timeDisplayOption = timeDisplayOption,
                            startTime = oneExperienceScreenModel.firstIngestionTime,
                            modifier = Modifier
                                .clickable {
                                    navigateToEditRatingScreen(overallRating.id)
                                }
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = horizontalPadding)
                        )
                        Divider()
                    }
                    AddShulginRatingButton(navigateToAddRatingScreen)
                }
            }
            Card(modifier = Modifier.padding(vertical = verticalCardPadding)) {
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
                    CardTitle(title = "Notes")
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = horizontalPadding)
                    ) {
                        Column(modifier = Modifier.padding(vertical = 10.dp)) {
                            Text(text = oneExperienceScreenModel.notes)
                            if (oneExperienceScreenModel.locationName.isNotBlank()) {
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(text = "Location: ${oneExperienceScreenModel.locationName}")
                            }
                        }
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        IconButton(onClick = navigateToEditExperienceScreen) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Notes"
                            )
                        }
                    }
                }
            }
            val interactions = oneExperienceScreenModel.interactions
            AnimatedVisibility(visible = interactions.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .padding(vertical = verticalCardPadding)
                ) {
                    CardTitle(title = "Interactions")
                    interactions.forEachIndexed { index, interaction ->
                        InteractionRow(interaction = interaction)
                        if (index < interactions.size - 1) {
                            Divider()
                        }
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Explanations",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = horizontalPadding)
                    )
                    FlowRow(
                        mainAxisSpacing = 5.dp,
                        crossAxisAlignment = FlowCrossAxisAlignment.Center,
                        modifier = Modifier.padding(horizontal = horizontalPadding)
                    ) {
                        oneExperienceScreenModel.interactionExplanations.forEach {
                            SuggestionChip(
                                onClick = {
                                    navigateToURL(it.url)
                                },
                                label = { Text(it.name) }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
fun AddShulginRatingButton(navigateToAddRatingScreen: () -> Unit) {
    TextButton(
        onClick = navigateToAddRatingScreen,
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = "Add",
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("Add Shulgin Rating")
    }
}

@Composable
fun AddTimedNoteButton(navigateToAddTimedNoteScreen: () -> Unit) {
    TextButton(
        onClick = navigateToAddTimedNoteScreen,
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = "Add",
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("Add Timed Note")
    }
}