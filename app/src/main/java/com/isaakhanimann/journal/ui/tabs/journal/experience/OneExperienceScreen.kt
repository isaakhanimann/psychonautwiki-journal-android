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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
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
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.*
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.ingestion.IngestionRow
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.rating.RatingRow
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.AllTimelinesNew
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.DataForOneRating
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
    navigateToEditRatingScreen: (ratingId: Int) -> Unit,
    navigateBack: () -> Unit,
) {
    val ingestionsWithCompanions = viewModel.ingestionsWithCompanionsFlow.collectAsState().value
    val experience = viewModel.experienceFlow.collectAsState().value
    val oneExperienceScreenModel = OneExperienceScreenModel(
        isFavorite = experience?.isFavorite ?: false,
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
        ratings = viewModel.ratingsFlow.collectAsState().value
    )
    OneExperienceScreen(
        oneExperienceScreenModel = oneExperienceScreenModel,
        addIngestion = navigateToAddIngestionSearch,
        deleteExperience = viewModel::deleteExperience,
        navigateToEditExperienceScreen = navigateToEditExperienceScreen,
        navigateToExplainTimeline = navigateToExplainTimeline,
        navigateToIngestionScreen = navigateToIngestionScreen,
        navigateToAddRatingScreen = navigateToAddRatingScreen,
        navigateBack = navigateBack,
        saveIsFavorite = viewModel::saveIsFavorite,
        navigateToURL = navigateToURL,
        navigateToEditRatingScreen = navigateToEditRatingScreen
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
            navigateBack = {},
            saveIsFavorite = {},
            navigateToURL = {},
            navigateToEditRatingScreen = {}
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
    navigateBack: () -> Unit,
    saveIsFavorite: (Boolean) -> Unit,
    navigateToEditRatingScreen: (ratingId: Int) -> Unit
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
                    CardTitle(title = "Effect Timeline")
                    Column(
                        modifier = Modifier
                            .padding(
                                horizontal = horizontalPadding,
                            )
                            .padding(bottom = 10.dp)
                    ) {
                        AllTimelinesNew(
                            dataForEffectLines = effectTimelines,
                            dataForRatings = oneExperienceScreenModel.ratings.map { DataForOneRating(time = it.time, option = it.option) },
                            isShowingCurrentTime = true,
                            navigateToExplainTimeline = navigateToExplainTimeline,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
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
            Card(modifier = Modifier.padding(vertical = verticalCardPadding)) {
                CardTitle(title = "Shulgin Rating")
                val ratings = oneExperienceScreenModel.ratings
                if (ratings.isNotEmpty()) {
                    Divider()
                }
                ratings.forEach { rating ->
                    RatingRow(
                        rating = rating,
                        modifier = Modifier
                            .clickable {
                                navigateToEditRatingScreen(rating.id)
                            }
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = horizontalPadding)
                    )
                    Divider()
                }
                TextButton(
                    onClick = navigateToAddRatingScreen,
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Add Rating")
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