package com.example.healthassistant.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.healthassistant.Screen
import com.example.healthassistant.model.SubstanceModel

@Composable
fun SearchScreen(navController: NavController, searchViewModel: SearchViewModel) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SearchField(searchText = searchViewModel.searchText, onChange = {
            searchViewModel.searchText = it
            searchViewModel.filterSubstances()
        })
        SubstanceList(navController = navController, substances = searchViewModel.filteredSubstances)
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
        placeholder = { Text(text = "Search Substances")},
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
fun SubstanceList(navController: NavController, substances: List<SubstanceModel>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(substances) { substance ->
            SubstanceRow(substance = substance, onTap = { substanceName ->
                navController.navigate(Screen.Search.route + "/" + substanceName)
            })
        }
    }
}

@Composable
fun SubstanceRow(substance: SubstanceModel, onTap: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onTap(substance.name)
            },
        elevation = 4.dp
    ) {
        Row(modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp), verticalAlignment = Alignment.Bottom) {
            Text(text = substance.name, modifier = Modifier.padding(end = 10.dp), style = MaterialTheme.typography.body1)
        }
    }
}