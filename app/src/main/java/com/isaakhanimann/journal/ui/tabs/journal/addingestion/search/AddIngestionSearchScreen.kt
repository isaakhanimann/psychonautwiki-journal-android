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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.CustomSubstance
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.SuggestionRow
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.models.SubstanceSuggestion
import com.isaakhanimann.journal.ui.tabs.search.SubstanceModel
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun AddIngestionSearchScreen(
    navigateToCheckInteractions: (substanceName: String) -> Unit,
    navigateToCheckSaferUse: (substanceName: String) -> Unit,
    navigateToChooseRoute: (substanceName: String) -> Unit,
    navigateToDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToCustomDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToChooseTime: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean) -> Unit,
    navigateToCustomSubstanceChooseRoute: (substanceName: String) -> Unit,
    navigateToAddCustomSubstanceScreen: () -> Unit,
    viewModel: AddIngestionSearchViewModel = hiltViewModel()
) {
    AddIngestionSearchScreen(
        navigateToCheckInteractions = navigateToCheckInteractions,
        navigateToCheckSaferUse = navigateToCheckSaferUse,
        navigateToChooseRoute = navigateToChooseRoute,
        navigateToCustomDose = navigateToCustomDose,
        navigateToCustomSubstanceChooseRoute = navigateToCustomSubstanceChooseRoute,
        navigateToChooseTime = navigateToChooseTime,
        navigateToDose = navigateToDose,
        navigateToAddCustomSubstanceScreen = navigateToAddCustomSubstanceScreen,
        substanceSuggestions = viewModel.filteredSuggestions.collectAsState().value,
        searchText = viewModel.searchTextFlow.collectAsState().value,
        onChangeSearchText = {
            viewModel.updateSearchText(it)
        },
        filteredSubstances = viewModel.filteredSubstancesFlow.collectAsState().value,
        filteredCustomSubstances = viewModel.filteredCustomSubstancesFlow.collectAsState().value
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddIngestionSearchScreen(
    navigateToCheckInteractions: (substanceName: String) -> Unit,
    navigateToChooseRoute: (substanceName: String) -> Unit,
    navigateToCheckSaferUse: (substanceName: String) -> Unit,
    navigateToDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToCustomDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToChooseTime: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean) -> Unit,
    navigateToCustomSubstanceChooseRoute: (substanceName: String) -> Unit,
    navigateToAddCustomSubstanceScreen: () -> Unit,
    substanceSuggestions: List<SubstanceSuggestion>,
    searchText: String,
    onChangeSearchText: (searchText: String) -> Unit,
    filteredSubstances: List<SubstanceModel>,
    filteredCustomSubstances: List<CustomSubstance>
) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    Scaffold(
        floatingActionButton = {
            if (!isFocused) {
                FloatingActionButton(onClick = { focusRequester.requestFocus() }) {
                    Icon(Icons.Default.Keyboard, contentDescription = "Keyboard")
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LinearProgressIndicator(progress = 0.17f, modifier = Modifier.fillMaxWidth())
            TextField(
                value = searchText,
                onValueChange = { value ->
                    onChangeSearchText(value)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                placeholder = { Text(text = "Search Substances") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                    )
                },
                trailingIcon = {
                    Row {
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = {
                                onChangeSearchText("")
                            }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Close",
                                )
                            }
                        }
                    }
                },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(
                    autoCorrect = false,
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Words,
                ),
                singleLine = true
            )
            LazyColumn {
                if (substanceSuggestions.isNotEmpty()) {
                    stickyHeader {
                        SectionHeader(title = "Quick Logging")
                    }
                }
                itemsIndexed(substanceSuggestions) { index, substanceRow ->
                    SuggestionRow(
                        substanceRow = substanceRow,
                        navigateToDose = navigateToDose,
                        navigateToCustomDose = navigateToCustomDose,
                        navigateToChooseTime = navigateToChooseTime
                    )
                    if (index < substanceSuggestions.size - 1) {
                        Divider()
                    }
                }
                if (filteredCustomSubstances.isNotEmpty()) {
                    stickyHeader {
                        SectionHeader(title = "Custom Substances")
                    }
                }
                itemsIndexed(filteredCustomSubstances) { index, customSubstance ->
                    SubstanceRowAddIngestion(substanceModel = SubstanceModel(
                        name = customSubstance.name,
                        commonNames = emptyList(),
                        categories = emptyList(),
                        hasSaferUse = false,
                        hasInteractions = false
                    ), onTap = {
                        navigateToCustomSubstanceChooseRoute(customSubstance.name)
                    })
                    if (index < filteredCustomSubstances.size - 1) {
                        Divider()
                    }
                }
                if (filteredSubstances.isNotEmpty()) {
                    stickyHeader {
                        SectionHeader(title = "Substances")
                    }
                }
                itemsIndexed(filteredSubstances) { index, substance ->
                    SubstanceRowAddIngestion(substanceModel = substance, onTap = {
                        if (substance.hasSaferUse) {
                            navigateToCheckSaferUse(substance.name)
                        } else if (substance.hasInteractions) {
                            navigateToCheckInteractions(substance.name)
                        } else {
                            navigateToChooseRoute(substance.name)
                        }
                    })
                    if (index < filteredSubstances.size - 1) {
                        Divider()
                    }
                }
                item {
                    TextButton(
                        onClick = navigateToAddCustomSubstanceScreen,
                        modifier = Modifier.padding(horizontal = horizontalPadding)
                    ) {
                        Icon(
                            Icons.Outlined.Add, contentDescription = "Add"
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = "Add Custom Substance")
                    }
                }
                item {
                    if (filteredSubstances.isEmpty() && filteredCustomSubstances.isEmpty()) {
                        Text("No matching substance found", modifier = Modifier.padding(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.background)) {
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = horizontalPadding, vertical = 5.dp)
                    .fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}


@Composable
fun ColorCircle(adaptiveColor: AdaptiveColor) {
    val isDarkTheme = isSystemInDarkTheme()
    Surface(
        shape = CircleShape,
        color = adaptiveColor.getComposeColor(isDarkTheme),
        modifier = Modifier.size(25.dp)
    ) {}
}