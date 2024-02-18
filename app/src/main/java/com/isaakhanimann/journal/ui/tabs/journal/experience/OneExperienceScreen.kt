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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExposurePlus2
import androidx.compose.material.icons.outlined.NoteAdd
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.FULL_STOMACH_DISCLAIMER
import com.isaakhanimann.journal.ui.ME
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.CardTitle
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.CumulativeDoseRow
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.ExperienceEffectTimelines
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.InteractionRow
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.SavedTimeDisplayOption
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.TimeDisplayOption
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.ingestion.IngestionRow
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.rating.RatingRow
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.timednote.TimedNoteRow
import com.isaakhanimann.journal.ui.tabs.journal.experience.models.OneExperienceScreenModel
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.DataForOneRating
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.DataForOneTimedNote
import com.isaakhanimann.journal.ui.theme.JournalTheme
import com.isaakhanimann.journal.ui.theme.horizontalPadding
import com.isaakhanimann.journal.ui.utils.getStringOfPattern
import java.time.Instant

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
    navigateToTimelineScreen: (consumerName: String) -> Unit,
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
        isCurrentExperience = viewModel.isCurrentExperienceFlow.collectAsState().value,
        ingestionElements = viewModel.ingestionElementsFlow.collectAsState().value,
        cumulativeDoses = viewModel.cumulativeDosesFlow.collectAsState().value,
        interactions = viewModel.interactionsFlow.collectAsState().value,
        interactionExplanations = viewModel.interactionExplanationsFlow.collectAsState().value,
        ratings = viewModel.ratingsFlow.collectAsState().value,
        timedNotes = viewModel.timedNotesFlow.collectAsState().value,
        consumersWithIngestions = viewModel.consumersWithIngestionsFlow.collectAsState().value
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
        savedTimeDisplayOption = viewModel.savedTimeDisplayOption.collectAsState().value,
        timeDisplayOption = viewModel.timeDisplayOptionFlow.collectAsState().value,
        onChangeTimeDisplayOption = viewModel::saveTimeDisplayOption,
        navigateToTimelineScreen = navigateToTimelineScreen
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
            savedTimeDisplayOption = SavedTimeDisplayOption.RELATIVE_TO_START,
            timeDisplayOption = TimeDisplayOption.RELATIVE_TO_START,
            onChangeTimeDisplayOption = {},
            navigateToTimelineScreen = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
    savedTimeDisplayOption: SavedTimeDisplayOption,
    timeDisplayOption: TimeDisplayOption,
    onChangeTimeDisplayOption: (SavedTimeDisplayOption) -> Unit,
    navigateToTimelineScreen: (consumerName: String) -> Unit,
    ) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(oneExperienceScreenModel.title) },
                actions = {
                    var areTimeOptionsExpanded by remember { mutableStateOf(false) }
                    IconButton(onClick = { areTimeOptionsExpanded = true }) {
                        Icon(Icons.Outlined.Timer, contentDescription = "Time Display Option")
                    }
                    DropdownMenu(
                        expanded = areTimeOptionsExpanded,
                        onDismissRequest = { areTimeOptionsExpanded = false }
                    ) {
                        SavedTimeDisplayOption.values().forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.text) },
                                onClick = {
                                    onChangeTimeDisplayOption(option)
                                    areTimeOptionsExpanded = false
                                },
                                leadingIcon = {
                                    if (option == savedTimeDisplayOption) {
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
                    var areEditOptionsExpanded by remember { mutableStateOf(false) }
                    IconButton(onClick = { areEditOptionsExpanded = true }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Options",
                        )
                    }
                    var isShowingDeleteDialog by remember { mutableStateOf(false) }
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
                    DropdownMenu(
                        expanded = areEditOptionsExpanded,
                        onDismissRequest = { areEditOptionsExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit Title/Notes/Location") },
                            onClick = {
                                navigateToEditExperienceScreen()
                                areEditOptionsExpanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Edit,
                                    contentDescription = "Edit Experience",
                                    modifier = Modifier.size(ButtonDefaults.IconSize)
                                )
                            }
                        )
                        val isFavorite = oneExperienceScreenModel.isFavorite
                        if (isFavorite) {
                            DropdownMenuItem(
                                text = { Text("Unmark Favorite") },
                                onClick = {
                                    saveIsFavorite(false)
                                    areEditOptionsExpanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Star,
                                        contentDescription = "Unmark Favorite",
                                        modifier = Modifier.size(ButtonDefaults.IconSize)
                                    )
                                }
                            )
                        } else {
                            DropdownMenuItem(
                                text = { Text("Mark Favorite") },
                                onClick = {
                                    saveIsFavorite(true)
                                    areEditOptionsExpanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.StarOutline,
                                        contentDescription = "Mark Favorite",
                                        modifier = Modifier.size(ButtonDefaults.IconSize)
                                    )
                                }
                            )
                        }
                        DropdownMenuItem(
                            text = { Text("Delete Experience") },
                            onClick = {
                                isShowingDeleteDialog = true
                                areEditOptionsExpanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Delete,
                                    contentDescription = "Delete Experience",
                                    modifier = Modifier.size(ButtonDefaults.IconSize)
                                )
                            }
                        )
                    }

                    var areAddOptionsExpanded by remember { mutableStateOf(false) }
                    IconButton(onClick = { areAddOptionsExpanded = true }) {
                        Icon(
                            Icons.Outlined.Add,
                            contentDescription = "Add Options",
                        )
                    }
                    DropdownMenu(
                        expanded = areAddOptionsExpanded,
                        onDismissRequest = { areAddOptionsExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Add Timed Note") },
                            onClick = {
                                navigateToAddTimedNoteScreen()
                                areAddOptionsExpanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.NoteAdd,
                                    contentDescription = "Add timed note",
                                    modifier = Modifier.size(ButtonDefaults.IconSize)
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Add Shulgin Rating") },
                            onClick = {
                                navigateToAddRatingScreen()
                                areAddOptionsExpanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.ExposurePlus2,
                                    contentDescription = "Add Shulgin rating",
                                    modifier = Modifier.size(ButtonDefaults.IconSize)
                                )
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (oneExperienceScreenModel.isCurrentExperience) {
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
            val verticalCardPadding = 4.dp
            if (oneExperienceScreenModel.ingestionElements.isNotEmpty()) {
                ElevatedCard(modifier = Modifier.padding(vertical = verticalCardPadding)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CardTitle(title = "Effect Timeline")
                        TextButton(onClick = navigateToExplainTimeline) {
                            Text(text = "Limitations")
                        }
                    }
                    Column(
                        modifier = Modifier
                            .padding(horizontal = horizontalPadding)
                            .padding(bottom = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        ExperienceEffectTimelines(
                            ingestionElements = oneExperienceScreenModel.ingestionElements,
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
                            dataForTimedNotes = oneExperienceScreenModel.timedNotes.filter { it.isPartOfTimeline }
                                .map {
                                    DataForOneTimedNote(time = it.time, color = it.color)
                                },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clickable {
                                    navigateToTimelineScreen(ME)
                                }
                        )
                        if (oneExperienceScreenModel.ingestionElements.any { it.ingestionWithCompanionAndCustomUnit.ingestion.administrationRoute == AdministrationRoute.ORAL }) {
                            Text(
                                text = FULL_STOMACH_DISCLAIMER,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
            if (oneExperienceScreenModel.ingestionElements.isNotEmpty()) {
                ElevatedCard(modifier = Modifier.padding(vertical = verticalCardPadding)) {
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
                                    navigateToIngestionScreen(ingestionElement.ingestionWithCompanionAndCustomUnit.ingestion.id)
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
                ElevatedCard(modifier = Modifier.padding(vertical = verticalCardPadding)) {
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
            val timedNotes = oneExperienceScreenModel.timedNotes
            if (timedNotes.isNotEmpty()) {
                ElevatedCard(modifier = Modifier.padding(vertical = verticalCardPadding)) {
                    CardTitle(title = "Timed Notes")
                    if (timedNotes.isNotEmpty()) {
                        Divider()
                    }
                    timedNotes.forEachIndexed { index, timedNote ->
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
                        if (index < timedNotes.size-1) {
                            Divider()
                        }
                    }
                }
            }
            if (oneExperienceScreenModel.ratings.isNotEmpty()) {
                ElevatedCard(modifier = Modifier.padding(vertical = verticalCardPadding)) {
                    CardTitle(title = "Shulgin Ratings")
                    Divider()
                    val ratingsWithTime =
                        oneExperienceScreenModel.ratings.filter { it.time != null }
                    ratingsWithTime.forEachIndexed { index, rating ->
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
                        if (index < ratingsWithTime.size-1) {
                            Divider()
                        }
                    }
                    val overallRating =
                        oneExperienceScreenModel.ratings.firstOrNull { it.time == null }
                    if (overallRating != null) {
                        if (ratingsWithTime.isNotEmpty()) {
                            Divider()
                        }
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
                    }
                }
            }
            val notes = oneExperienceScreenModel.notes
            if (notes.isNotBlank()) {
                ElevatedCard(modifier = Modifier
                    .padding(vertical = verticalCardPadding)
                    .fillMaxWidth()
                    .clickable { navigateToEditExperienceScreen() }) {
                    CardTitle(title = "Notes")
                    Column(modifier = Modifier
                        .padding(horizontal = horizontalPadding)
                        .padding(bottom = 10.dp)
                    ) {
                        Text(text = oneExperienceScreenModel.notes)
                        if (oneExperienceScreenModel.locationName.isNotBlank()) {
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(text = "Location: ${oneExperienceScreenModel.locationName}")
                        }
                    }
                }
            }
            oneExperienceScreenModel.consumersWithIngestions.forEach { consumerWithIngestions ->
                ElevatedCard(modifier = Modifier.padding(vertical = verticalCardPadding)) {
                    CardTitle(title = consumerWithIngestions.consumerName)
                    Column(
                        modifier = Modifier
                            .padding(horizontal = horizontalPadding)
                            .padding(bottom = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        ExperienceEffectTimelines(
                            ingestionElements = consumerWithIngestions.ingestionElements,
                            dataForRatings = emptyList(),
                            dataForTimedNotes = emptyList(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clickable {
                                    navigateToTimelineScreen(consumerWithIngestions.consumerName)
                                }
                        )
                    }
                    Divider()
                    consumerWithIngestions.ingestionElements.forEachIndexed { index, ingestionElement ->
                        IngestionRow(
                            ingestionElement = ingestionElement,
                            timeDisplayOption = timeDisplayOption,
                            startTime = oneExperienceScreenModel.firstIngestionTime,
                            modifier = Modifier
                                .clickable {
                                    navigateToIngestionScreen(ingestionElement.ingestionWithCompanionAndCustomUnit.ingestion.id)
                                }
                                .fillMaxWidth()
                                .padding(vertical = 5.dp, horizontal = horizontalPadding)
                        )
                        if (index < consumerWithIngestions.ingestionElements.size - 1) {
                            Divider()
                        }
                    }
                }
            }
            val interactions = oneExperienceScreenModel.interactions
            AnimatedVisibility(visible = interactions.isNotEmpty()) {
                ElevatedCard(
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
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
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