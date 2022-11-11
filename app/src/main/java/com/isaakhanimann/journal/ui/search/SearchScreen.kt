/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.ui.search.substancerow.SubstanceRow
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = hiltViewModel(),
    onSubstanceTap: (substanceName: String) -> Unit,
    onCustomSubstanceTap: (substanceName: String) -> Unit,
    navigateToAddCustomSubstanceScreen: () -> Unit,
) {
    Column(modifier = modifier) {
        SearchField(
            searchText = searchViewModel.searchTextFlow.collectAsState().value,
            onChange = {
                searchViewModel.filterSubstances(searchText = it)
            },
            categories = searchViewModel.chipCategoriesFlow.collectAsState().value,
            onFilterTapped = searchViewModel::onFilterTapped
        )
        val activeFilters =
            searchViewModel.chipCategoriesFlow.collectAsState().value.filter { it.isActive }
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
                        searchViewModel.onFilterTapped(filterName = categoryChipModel.chipName)
                    }
                }
                item {
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
        val filteredCustomSubstances =
            searchViewModel.filteredCustomSubstancesFlow.collectAsState().value
        val filteredSubstances = searchViewModel.filteredSubstancesFlow.collectAsState().value
        if (filteredSubstances.isEmpty() && filteredCustomSubstances.isEmpty()) {
            Column {
                val activeCategoryNames =
                    activeFilters.filter { it.isActive }.map { it.chipName }
                if (activeCategoryNames.isEmpty()) {
                    Text("None found", modifier = Modifier.padding(10.dp))

                } else {
                    val names = activeCategoryNames.joinToString(separator = ", ")
                    Text("None found in $names", modifier = Modifier.padding(10.dp))
                }
                TextButton(
                    onClick = navigateToAddCustomSubstanceScreen,
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    Icon(
                        Icons.Outlined.Add,
                        contentDescription = "Add"
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "Add Custom Substance")
                }
            }
        } else {
            LazyColumn {
                items(filteredSubstances) { substance ->
                    SubstanceRow(substanceModel = substance, onTap = onSubstanceTap)
                    Divider()
                }
                items(filteredCustomSubstances) { customSubstance ->
                    SubstanceRow(substanceModel = SubstanceModel(
                        name = customSubstance.name,
                        commonNames = emptyList(),
                        categories = listOf(
                            CategoryModel(
                                name = "custom",
                                color = searchViewModel.customColor
                            )
                        )
                    ), onTap = {
                        onCustomSubstanceTap(customSubstance.name)
                    })
                    Divider()
                }
                item {
                    TextButton(
                        onClick = navigateToAddCustomSubstanceScreen,
                        modifier = Modifier.padding(horizontal = horizontalPadding)
                    ) {
                        Icon(
                            Icons.Outlined.Add,
                            contentDescription = "Add"
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = "Add Custom Substance")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    searchText: String,
    onChange: (searchText: String) -> Unit,
    categories: List<CategoryChipModel>,
    onFilterTapped: (filterName: String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = searchText,
        onValueChange = { value ->
            onChange(value)
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Search Substances") },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
            )
        },
        trailingIcon = {
            val endPaddingIcon = 5.dp
            if (searchText != "") {
                IconButton(
                    onClick = {
                        onChange("")
                    },
                    modifier = Modifier.padding(end = endPaddingIcon)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                    )
                }
            } else {
                var isExpanded by remember { mutableStateOf(false) }
                val activeFilters = categories.filter { it.isActive }
                IconButton(
                    onClick = { isExpanded = true },
                    modifier = Modifier.padding(end = endPaddingIcon)
                ) {
                    BadgedBox(
                        badge = {
                            if (activeFilters.isNotEmpty()) {
                                Badge { Text(activeFilters.size.toString()) }
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = "Filter"
                        )
                    }
                }
                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    categories.forEach { categoryChipModel ->
                        DropdownMenuItem(
                            text = { Text(categoryChipModel.chipName) },
                            onClick = { onFilterTapped(categoryChipModel.chipName) },
                            leadingIcon = {
                                if (categoryChipModel.isActive) {
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
        },
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            capitalization = KeyboardCapitalization.Words
        ),
        singleLine = true
    )
}