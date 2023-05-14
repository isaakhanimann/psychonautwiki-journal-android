/*
 * Copyright (c) 2023. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.ui.tabs.search.substancerow.SubstanceRow
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel(),
    onSubstanceTap: (substanceModel: SubstanceModel) -> Unit,
    onCustomSubstanceTap: (substanceName: String) -> Unit,
    navigateToAddCustomSubstanceScreen: () -> Unit,
) {
    Column {
        SearchField(
            searchText = searchViewModel.searchTextFlow.collectAsState().value,
            onChange = {
                searchViewModel.filterSubstances(searchText = it)
            },
            categories = searchViewModel.chipCategoriesFlow.collectAsState().value,
            onFilterTapped = searchViewModel::onFilterTapped,
            isShowingFilter = true
        )
        val activeFilters =
            searchViewModel.chipCategoriesFlow.collectAsState().value.filter { it.isActive }
        val onFilterTapped = searchViewModel::onFilterTapped
        val filteredSubstances = searchViewModel.filteredSubstancesFlow.collectAsState().value
        val filteredCustomSubstances =
            searchViewModel.filteredCustomSubstancesFlow.collectAsState().value
        val customColor = searchViewModel.customColor
        if (activeFilters.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.padding(vertical = 6.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.width(4.dp))
                }
                items(activeFilters.size) {
                    val categoryChipModel = activeFilters[it]
                    CategoryChipDelete(categoryChipModel = categoryChipModel) {
                        onFilterTapped(categoryChipModel.chipName)
                    }
                }
                item {
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
        if (filteredSubstances.isEmpty() && filteredCustomSubstances.isEmpty()) {
            Column {
                val activeCategoryNames = activeFilters.filter { it.isActive }.map { it.chipName }
                if (activeCategoryNames.isEmpty()) {
                    Text("No matching substance found", modifier = Modifier.padding(10.dp))
                } else if (activeCategoryNames.size == 1) {
                    Text(
                        "No matching substance with the tag '${activeCategoryNames[0]}' found",
                        modifier = Modifier.padding(10.dp)
                    )
                } else {
                    val names = activeCategoryNames.joinToString(separator = "', '")
                    Text(
                        "No matching substance with tags '$names' found",
                        modifier = Modifier.padding(10.dp)
                    )
                }
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
        } else {
            LazyColumn {
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
                        onCustomSubstanceTap(customSubstance.name)
                    })
                    Divider()
                }
                items(filteredSubstances) { substance ->
                    SubstanceRow(substanceModel = substance, onTap = {
                        onSubstanceTap(substance)
                    })
                    Divider()
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
            }
        }
    }
}
