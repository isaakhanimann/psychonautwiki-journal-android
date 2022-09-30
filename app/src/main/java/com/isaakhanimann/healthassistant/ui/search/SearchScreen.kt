package com.isaakhanimann.healthassistant.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.ui.search.substancerow.SubstanceRow
import com.isaakhanimann.healthassistant.ui.theme.horizontalPadding

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = hiltViewModel(),
    onSubstanceTap: (substanceName: String) -> Unit,
    onCustomSubstanceTap: (substanceName: String) -> Unit,
    isShowingSettings: Boolean,
    navigateToSettings: () -> Unit,
    navigateToAddCustomSubstanceScreen: () -> Unit,
) {
    Column(modifier = modifier) {
        SearchField(
            searchText = searchViewModel.searchTextFlow.collectAsState().value,
            onChange = {
                searchViewModel.filterSubstances(searchText = it)
            },
            isShowingSettings = isShowingSettings,
            navigateToSettings = navigateToSettings
        )
        val categories = searchViewModel.chipCategoriesFlow.collectAsState().value
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.padding(vertical = 6.dp)
        ) {
            item {
                Spacer(modifier = Modifier.width(4.dp))
            }
            items(categories.size) {
                val categoryChipModel = categories[it]
                CategoryChipDynamic(categoryChipModel = categoryChipModel) {
                    searchViewModel.onFilterTapped(filterName = categoryChipModel.chipName)
                }
            }
            item {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
        val isShowingCustomSubstances =
            searchViewModel.isShowingCustomSubstancesFlow.collectAsState().value
        if (isShowingCustomSubstances) {
            val customSubstances = searchViewModel.customSubstancesFlow.collectAsState().value
            LazyColumn {
                items(customSubstances) { customSubstance ->
                    Text(
                        text = customSubstance.name,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onCustomSubstanceTap(customSubstance.name)
                            }
                            .padding(horizontal = horizontalPadding, vertical = 6.dp),
                    )
                    Divider()
                }
                item {
                    TextButton(
                        onClick = navigateToAddCustomSubstanceScreen,
                        modifier = Modifier.padding(horizontal = horizontalPadding)
                    ) {
                        Text(text = "Add Custom Substance")
                    }
                }
            }
        } else {
            val filteredSubstances = searchViewModel.filteredSubstances.collectAsState().value
            if (filteredSubstances.isEmpty()) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text("None Found", modifier = Modifier.padding(10.dp))
                }
            } else {
                LazyColumn {
                    items(filteredSubstances.size) { i ->
                        val substance = filteredSubstances[i]
                        SubstanceRow(substanceModel = substance, onTap = onSubstanceTap)
                        if (i < filteredSubstances.size) {
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchField(
    searchText: String,
    onChange: (String) -> Unit,
    isShowingSettings: Boolean,
    navigateToSettings: () -> Unit,
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
            if (searchText != "") {
                IconButton(
                    onClick = {
                        onChange("")
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                    )
                }
            } else if (isShowingSettings) {
                IconButton(
                    onClick = navigateToSettings,
                ) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Navigate to Settings"
                    )
                }
            }
        },
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            capitalization = KeyboardCapitalization.Words
        ),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface
        )
    )
}