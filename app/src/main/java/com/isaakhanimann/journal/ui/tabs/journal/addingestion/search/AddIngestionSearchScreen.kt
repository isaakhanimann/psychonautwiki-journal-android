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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.CustomSubstance
import com.isaakhanimann.journal.data.room.experiences.entities.CustomUnit
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.SuggestionRow
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.models.Suggestion
import com.isaakhanimann.journal.ui.tabs.search.SubstanceModel
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun AddIngestionSearchScreen(
    navigateToCheckInteractions: (substanceName: String) -> Unit,
    navigateToCheckSaferUse: (substanceName: String) -> Unit,
    navigateToChooseRoute: (substanceName: String) -> Unit,
    navigateToDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToChooseCustomSubstanceDose: (customSubstanceName: String, route: AdministrationRoute) -> Unit,
    navigateToChooseTime: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean, estimatedDoseStandardDeviation: Double?, customUnitId: Int?) -> Unit,
    navigateToCustomSubstanceChooseRoute: (customSubstanceName: String) -> Unit,
    navigateToCustomUnitChooseDose: (customUnitId: Int) -> Unit,
    navigateToAddCustomSubstanceScreen: (searchText: String) -> Unit,
    viewModel: AddIngestionSearchViewModel = hiltViewModel()
) {
    val searchText = viewModel.searchTextFlow.collectAsState().value
    AddIngestionSearchScreen(
        navigateToCheckInteractions = navigateToCheckInteractions,
        navigateToCheckSaferUse = navigateToCheckSaferUse,
        navigateToChooseRoute = navigateToChooseRoute,
        navigateToCustomDose = navigateToChooseCustomSubstanceDose,
        navigateToCustomSubstanceChooseRoute = navigateToCustomSubstanceChooseRoute,
        navigateToChooseTime = navigateToChooseTime,
        navigateToDose = navigateToDose,
        navigateToAddCustomSubstanceScreen = {
            navigateToAddCustomSubstanceScreen(searchText)
        },
        navigateToCustomUnitChooseDose = navigateToCustomUnitChooseDose,
        suggestions = viewModel.filteredSuggestions.collectAsState().value,
        searchText = searchText,
        onChangeSearchText = {
            viewModel.updateSearchText(it)
        },
        filteredSubstances = viewModel.filteredSubstancesFlow.collectAsState().value,
        filteredCustomUnits = viewModel.filteredCustomUnitsFlow.collectAsState().value,
        filteredCustomSubstances = viewModel.filteredCustomSubstancesFlow.collectAsState().value
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddIngestionSearchScreen(
    navigateToCheckInteractions: (substanceName: String) -> Unit,
    navigateToChooseRoute: (substanceName: String) -> Unit,
    navigateToCheckSaferUse: (substanceName: String) -> Unit,
    navigateToDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToCustomDose: (customSubstanceName: String, route: AdministrationRoute) -> Unit,
    navigateToChooseTime: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean, estimatedDoseStandardDeviation: Double?, customUnitId: Int?) -> Unit,
    navigateToCustomSubstanceChooseRoute: (customSubstanceName: String) -> Unit,
    navigateToAddCustomSubstanceScreen: () -> Unit,
    navigateToCustomUnitChooseDose: (customUnitId: Int) -> Unit,
    suggestions: List<Suggestion>,
    searchText: String,
    onChangeSearchText: (searchText: String) -> Unit,
    filteredSubstances: List<SubstanceModel>,
    filteredCustomUnits: List<CustomUnit>,
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
            LinearProgressIndicator(
                progress = { 0.17f },
                modifier = Modifier.fillMaxWidth().clearAndSetSemantics {  },
            )
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
                    }.clearAndSetSemantics {  },
                placeholder = { Text(text = "Search substances") },
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
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Words,
                ),
                singleLine = true
            )
            LazyColumn {
                if (suggestions.isNotEmpty()) {
                    stickyHeader {
                        SectionHeader(title = "Quick logging")
                    }
                }
                itemsIndexed(suggestions) { index, suggestion ->
                    SuggestionRow(
                        suggestion = suggestion,
                        navigateToDose = navigateToDose,
                        navigateToCustomUnitChooseDose = navigateToCustomUnitChooseDose,
                        navigateToCustomDose = navigateToCustomDose,
                        navigateToChooseTime = navigateToChooseTime
                    )
                    if (index < suggestions.size - 1) {
                        HorizontalDivider()
                    }
                }
                if (filteredCustomSubstances.isNotEmpty()) {
                    stickyHeader {
                        SectionHeader(title = "Custom substances")
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
                        HorizontalDivider()
                    }
                }
                if (filteredCustomUnits.isNotEmpty()) {
                    stickyHeader {
                        SectionHeader(title = "Custom units")
                    }
                }
                itemsIndexed(filteredCustomUnits) { index, customUnit ->
                    CustomUnitRowAddIngestion(
                        customUnit = customUnit,
                        navigateToCustomUnitChooseDose = navigateToCustomUnitChooseDose)
                    if (index < filteredCustomUnits.size - 1) {
                        HorizontalDivider()
                    }
                }
                if (filteredSubstances.isNotEmpty()) {
                    stickyHeader {
                        SectionHeader(title = "Substances")
                    }
                }
                items(filteredSubstances) { substance ->
                    SubstanceRowAddIngestion(substanceModel = substance, onTap = {
                        if (substance.hasSaferUse) {
                            navigateToCheckSaferUse(substance.name)
                        } else if (substance.hasInteractions) {
                            navigateToCheckInteractions(substance.name)
                        } else {
                            navigateToChooseRoute(substance.name)
                        }
                    })
                    HorizontalDivider()
                }
                item {
                    HorizontalDivider()
                    TextButton(
                        onClick = navigateToAddCustomSubstanceScreen,
                        modifier = Modifier.padding(horizontal = horizontalPadding)
                    ) {
                        Icon(
                            Icons.Outlined.Add, contentDescription = "Add"
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = "Add custom substance")
                    }
                    HorizontalDivider()
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