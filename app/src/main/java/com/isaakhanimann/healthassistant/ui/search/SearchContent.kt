package com.isaakhanimann.healthassistant.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.ui.search.substancerow.SubstanceRow

@Composable
fun SearchContent(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = hiltViewModel(),
    onSubstanceTap: (substanceName: String) -> Unit
) {
    Column(modifier = modifier) {
        SearchField(
            searchText = searchViewModel.searchTextFlow.collectAsState().value,
            onChange = {
                searchViewModel.filterSubstances(searchText = it)
            }
        )
        val categories = searchViewModel.chipCategoriesFlow.collectAsState().value
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.padding(top = 4.dp)
        ) {
            item {
                Spacer(modifier = Modifier.width(2.dp))
            }
            items(categories.size) {
                val categoryChipModel = categories[it]
                CategoryChipDynamic(categoryChipModel = categoryChipModel) {
                    searchViewModel.onFilterTapped(filterName = categoryChipModel.chipName)
                }
            }
            item {
                Spacer(modifier = Modifier.width(2.dp))
            }
        }
        val filteredSubstances = searchViewModel.filteredSubstances.collectAsState().value
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

@Composable
fun SearchField(
    searchText: String,
    onChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = searchText,
        onValueChange = { value ->
            onChange(value)
        },
        modifier = Modifier
            .fillMaxWidth(),
        placeholder = { Text(text = "Search Substances") },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
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
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        singleLine = true
    )
}