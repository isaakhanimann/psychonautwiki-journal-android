package com.example.healthassistant.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.ui.experiences.SectionTitle

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel(),
    onSubstanceTap: (Substance) -> Unit
) {
    Column {
        SearchField(
            searchText = searchViewModel.searchTextFlow.collectAsState().value,
            onChange = {
                searchViewModel.filterSubstances(searchText = it)
            }
        )
        val recents = searchViewModel.filteredRecentlyUsed.collectAsState().value
        val commons = searchViewModel.filteredCommonlyUsed.collectAsState().value
        val others = searchViewModel.filteredOthers.collectAsState().value
        LazyColumn {
            if (recents.isNotEmpty()) {
                item {
                    SectionTitle(title = "Recently Used")
                }
                items(recents.size) { i ->
                    val substance = recents[i]
                    SubstanceRow(substance = substance, onTap = onSubstanceTap)
                    if (i < recents.size) {
                        Divider()
                    }
                }
            }
            if (commons.isNotEmpty()) {
                item {
                    SectionTitle(title = "Commonly Used")
                }
                items(commons.size) { i ->
                    val substance = commons[i]
                    SubstanceRow(substance = substance, onTap = onSubstanceTap)
                    if (i < commons.size) {
                        Divider()
                    }
                }
            }
            if (others.isNotEmpty()) {
                item {
                    SectionTitle(title = "Other")
                }
                items(others.size) { i ->
                    val substance = others[i]
                    SubstanceRow(substance = substance, onTap = onSubstanceTap)
                    if (i < others.size) {
                        Divider()
                    }
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

@Composable
fun SubstanceRow(substance: Substance, onTap: (Substance) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onTap(substance)
            }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = substance.name,
            style = MaterialTheme.typography.body1,
        )
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Column(horizontalAlignment = Alignment.End) {
                substance.commonNames.forEach { commonName ->
                    Text(text = commonName, style = MaterialTheme.typography.caption)
                }
            }
        }
    }
}