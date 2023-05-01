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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.ui.tabs.journal.components.NavigationButton

@Composable
fun SearchScreenWithDrawerButton(
    searchViewModel: SearchViewModel = hiltViewModel(),
    onSubstanceTap: (substanceName: String) -> Unit,
    onCustomSubstanceTap: (substanceName: String) -> Unit,
    navigateToAddCustomSubstanceScreen: () -> Unit,
    openNavigationDrawer: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row {
            NavigationButton(onClick = openNavigationDrawer)
            SearchField(
                searchText = searchViewModel.searchTextFlow.collectAsState().value,
                onChange = {
                    searchViewModel.filterSubstances(searchText = it)
                },
                categories = searchViewModel.chipCategoriesFlow.collectAsState().value,
                onFilterTapped = searchViewModel::onFilterTapped
            )
        }
        SearchResults(
            activeFilters = searchViewModel.chipCategoriesFlow.collectAsState().value.filter { it.isActive },
            onFilterTapped = searchViewModel::onFilterTapped,
            filteredSubstances = searchViewModel.filteredSubstancesFlow.collectAsState().value,
            filteredCustomSubstances = searchViewModel.filteredCustomSubstancesFlow.collectAsState().value,
            navigateToAddCustomSubstanceScreen = navigateToAddCustomSubstanceScreen,
            onSubstanceTap = onSubstanceTap,
            onCustomSubstanceTap = onCustomSubstanceTap,
            customColor = searchViewModel.customColor
        )
    }
}
