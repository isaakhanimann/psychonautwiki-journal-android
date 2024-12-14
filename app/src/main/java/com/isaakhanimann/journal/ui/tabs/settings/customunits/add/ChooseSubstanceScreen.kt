/*
 * Copyright (c) 2024. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.settings.customunits.add


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.isaakhanimann.journal.data.room.experiences.entities.CustomSubstance
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.AddIngestionSearchViewModel
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.SubstanceRowAddIngestion
import com.isaakhanimann.journal.ui.tabs.search.CategoryModel
import com.isaakhanimann.journal.ui.tabs.search.SubstanceModel
import com.isaakhanimann.journal.ui.tabs.search.customColor
import com.isaakhanimann.journal.ui.tabs.search.substancerow.SubstanceRow

@Composable
fun ChooseSubstanceScreen(
    navigateToChooseRoute: (substanceName: String) -> Unit,
    navigateToCustomSubstanceChooseRoute: (customSubstanceId: Int) -> Unit,
    viewModel: AddIngestionSearchViewModel = hiltViewModel()
) {
    ChooseSubstanceScreen(
        navigateToChooseRoute = navigateToChooseRoute,
        navigateToCustomSubstanceChooseRoute = navigateToCustomSubstanceChooseRoute,
        searchText = viewModel.searchTextFlow.collectAsState().value,
        onChangeSearchText = {
            viewModel.updateSearchText(it)
        },
        filteredSubstances = viewModel.filteredSubstancesFlow.collectAsState().value,
        filteredCustomSubstances = viewModel.filteredCustomSubstancesFlow.collectAsState().value
    )
}

@Composable
private fun ChooseSubstanceScreen(
    navigateToChooseRoute: (substanceName: String) -> Unit,
    navigateToCustomSubstanceChooseRoute: (customSubstanceId: Int) -> Unit,
    searchText: String,
    onChangeSearchText: (searchText: String) -> Unit,
    filteredSubstances: List<SubstanceModel>,
    filteredCustomSubstances: List<CustomSubstance>,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .clearAndSetSemantics { },
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
                    }
                    .clearAndSetSemantics { },
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
                items(filteredSubstances) { substance ->
                    SubstanceRowAddIngestion(substanceModel = substance, onTap = {
                        navigateToChooseRoute(substance.name)
                    })
                    HorizontalDivider()
                }
                items(filteredCustomSubstances) { customSubstance ->
                    SubstanceRow(substanceModel = SubstanceModel(
                        name = customSubstance.name,
                        commonNames = emptyList(),
                        categories = listOf(
                            CategoryModel(
                                name = "custom", color = customColor
                            )
                        ),
                        hasSaferUse = false,
                        hasInteractions = false
                    ), onTap = {
                        navigateToCustomSubstanceChooseRoute(customSubstance.id)
                    })
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