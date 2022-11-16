/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.journal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.room.experiences.relations.ExperienceWithIngestionsAndCompanions
import com.isaakhanimann.journal.ui.tabs.journal.experience.CardWithTitle
import com.isaakhanimann.journal.ui.tabs.stats.EmptyScreenDisclaimer
import com.isaakhanimann.journal.ui.theme.JournalTheme
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun JournalScreen(
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    navigateToAddIngestion: () -> Unit,
    viewModel: JournalViewModel = hiltViewModel()
) {
    val currentAndPrevious = viewModel.currentAndPreviousExperiences.collectAsState().value
    JournalScreen(
        navigateToExperiencePopNothing = navigateToExperiencePopNothing,
        navigateToAddIngestion = navigateToAddIngestion,
        isFavoriteEnabled = viewModel.isFavoriteEnabledFlow.collectAsState().value,
        onChangeIsFavorite = viewModel::onChangeFavorite,
        searchText = viewModel.searchTextFlow.collectAsState().value,
        onChangeSearchText = viewModel::search,
        isSearchEnabled = viewModel.isSearchEnabled.value,
        onChangeIsSearchEnabled = viewModel::onChangeOfIsSearchEnabled,
        currentExperience = currentAndPrevious.currentExperience,
        previousExperiences = currentAndPrevious.previousExperiences
    )
}

@Preview
@Composable
fun ExperiencesScreenPreview(
    @PreviewParameter(
        JournalScreenPreviewProvider::class,
    ) experiences: List<ExperienceWithIngestionsAndCompanions>,
) {
    JournalTheme {
        JournalScreen(
            navigateToExperiencePopNothing = {},
            navigateToAddIngestion = {},
            isFavoriteEnabled = false,
            onChangeIsFavorite = {},
            searchText = "",
            onChangeSearchText = {},
            isSearchEnabled = true,
            onChangeIsSearchEnabled = {},
            currentExperience = experiences.firstOrNull(),
            previousExperiences = experiences.drop(1)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    navigateToAddIngestion: () -> Unit,
    isFavoriteEnabled: Boolean,
    onChangeIsFavorite: (Boolean) -> Unit,
    searchText: String,
    onChangeSearchText: (String) -> Unit,
    isSearchEnabled: Boolean,
    onChangeIsSearchEnabled: (Boolean) -> Unit,
    currentExperience: ExperienceWithIngestionsAndCompanions?,
    previousExperiences: List<ExperienceWithIngestionsAndCompanions>,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Experiences") },
                actions = {
                    IconToggleButton(
                        checked = isFavoriteEnabled,
                        onCheckedChange = onChangeIsFavorite
                    ) {
                        if (isFavoriteEnabled) {
                            Icon(Icons.Filled.Star, contentDescription = "Is Favorite")
                        } else {
                            Icon(Icons.Outlined.StarOutline, contentDescription = "Is not Favorite")
                        }
                    }
                    IconToggleButton(
                        checked = isSearchEnabled,
                        onCheckedChange = onChangeIsSearchEnabled
                    ) {
                        if (isSearchEnabled) {
                            Icon(Icons.Outlined.SearchOff, contentDescription = "Search Off")
                        } else {
                            Icon(Icons.Filled.Search, contentDescription = "Search")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = navigateToAddIngestion,
                icon = {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add"
                    )
                },
                text = { Text("Ingestion") },
            )
        }
    ) { padding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                AnimatedVisibility(visible = isSearchEnabled) {
                    val focusManager = LocalFocusManager.current
                    TextField(
                        value = searchText,
                        onValueChange = onChangeSearchText,
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                            )
                        },
                        trailingIcon = {
                            if (searchText != "") {
                                IconButton(
                                    onClick = {
                                        onChangeSearchText("")
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Close",
                                    )
                                }
                            }
                        },
                        label = { Text(text = "Search by title") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        singleLine = true
                    )
                }
                AnimatedVisibility(visible = !isSearchEnabled) {
                    if (currentExperience != null) {
                        CardWithTitle(title = "Current", innerPaddingHorizontal = 0.dp) {
                            ExperienceRow(
                                currentExperience,
                                navigateToExperienceScreen = {
                                    navigateToExperiencePopNothing(currentExperience.experience.id)
                                },
                            )
                        }
                    }

                }
                if (previousExperiences.isNotEmpty()) {
                    CardWithTitle(title = "Previous", innerPaddingHorizontal = 0.dp) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            itemsIndexed(previousExperiences) { index, experienceWithIngestions ->
                                ExperienceRow(
                                    experienceWithIngestions,
                                    navigateToExperienceScreen = {
                                        navigateToExperiencePopNothing(experienceWithIngestions.experience.id)
                                    },
                                )
                                if (index < previousExperiences.size - 1) {
                                    Divider()
                                }
                            }
                        }
                    }
                }
            }
            if (currentExperience == null && previousExperiences.isEmpty()) {
                if (isSearchEnabled && searchText.isNotEmpty()) {
                    if (isFavoriteEnabled) {
                        EmptyScreenDisclaimer(
                            title = "No Results",
                            description = "No favorite experience titles match your search."
                        )
                    } else {
                        EmptyScreenDisclaimer(
                            title = "No Results",
                            description = "No experience titles match your search."
                        )
                    }
                } else {
                    if (isFavoriteEnabled) {
                        EmptyScreenDisclaimer(
                            title = "No Favorites",
                            description = "Mark experiences as favorites to find them quickly."
                        )
                    } else {
                        EmptyScreenDisclaimer(
                            title = "No Experiences Yet",
                            description = "Add your first ingestion."
                        )
                    }
                }
            }
        }
    }
}