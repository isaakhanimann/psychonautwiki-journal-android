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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp

@Composable
fun SearchField(
    modifier: Modifier,
    searchText: String,
    onChange: (searchText: String) -> Unit,
    categories: List<CategoryChipModel>,
    onFilterTapped: (filterName: String) -> Unit,
    isShowingFilter: Boolean
) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = searchText,
        onValueChange = { value ->
            onChange(value)
        },
        modifier = modifier,
        placeholder = { Text(text = "Search substances") },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
            )
        },
        trailingIcon = {
            Row {
                // clear search button
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = {
                        onChange("")
                    }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                        )
                    }
                }
                // show filters button
                if (isShowingFilter) {
                    var isExpanded by remember { mutableStateOf(false) }
                    val activeFilters = categories.filter { it.isActive }
                    BadgedBox(
                        modifier = Modifier
                            .clickable(onClick = { isExpanded = true })
                            .padding(horizontal = 16.dp), badge = {
                            if (activeFilters.isNotEmpty()) {
                                Badge { Text(activeFilters.size.toString()) }
                            }
                        }) {
                        Icon(
                            Icons.Default.FilterList, contentDescription = "Filter"
                        )
                    }
                    DropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false },
                    ) {
                        categories.forEach { categoryChipModel ->
                            DropdownMenuItem(text = { Text(categoryChipModel.chipName) },
                                onClick = { onFilterTapped(categoryChipModel.chipName) },
                                leadingIcon = {
                                    if (categoryChipModel.isActive) {
                                        Icon(
                                            Icons.Filled.Check,
                                            contentDescription = "Check",
                                            modifier = Modifier.size(ButtonDefaults.IconSize)
                                        )
                                    }
                                })
                        }
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
}