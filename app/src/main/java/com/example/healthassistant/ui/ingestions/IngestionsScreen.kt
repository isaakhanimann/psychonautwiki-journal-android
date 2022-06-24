package com.example.healthassistant.ui.ingestions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.ui.experiences.SectionTitle
import com.example.healthassistant.ui.experiences.experience.IngestionRow

@Composable
fun IngestionsScreen(
    navigateToIngestion: (ingestionId: Int) -> Unit,
    viewModel: IngestionsViewModel = hiltViewModel()
) {
    IngestionsScreen(
        navigateToIngestion = navigateToIngestion,
        groupedIngestions = viewModel.ingestionsGrouped.collectAsState().value,
        filterOptions = viewModel.filterOptions.collectAsState().value,
        numberOfActiveFilters = viewModel.numberOfActiveFilters
    )
}

@Preview
@Composable
fun IngestionsScreenPreview() {
    IngestionsScreen(
        navigateToIngestion = {},
        groupedIngestions = emptyMap(),
        filterOptions = listOf(),
        numberOfActiveFilters = 1
    )
}

@Composable
fun IngestionsScreen(
    navigateToIngestion: (ingestionId: Int) -> Unit,
    groupedIngestions: Map<String, List<Ingestion>>,
    filterOptions: List<IngestionsViewModel.FilterOption>,
    numberOfActiveFilters: Int,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Ingestions") },
                actions = {
                    var isExpanded by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .wrapContentSize(Alignment.TopEnd)
                    ) {
                        IconButton(
                            onClick = { isExpanded = true },
                        ) {
                            BadgedBox(badge = {
                                if (numberOfActiveFilters != 0) {
                                    Badge { Text(numberOfActiveFilters.toString()) }
                                }
                            }) {
                                Icon(
                                    Icons.Filled.FilterList,
                                    contentDescription = "Filter"
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = { isExpanded = false }
                        ) {
                            filterOptions.forEach { filterOption ->
                                DropdownMenuItem(
                                    onClick = {
                                        filterOption.onTap()
                                    },
                                    enabled = filterOption.isEnabled
                                ) {
                                    if (filterOption.hasCheck) {
                                        Icon(
                                            Icons.Filled.Check,
                                            contentDescription = "Check",
                                            modifier = Modifier.size(ButtonDefaults.IconSize)
                                        )
                                    } else {
                                        Spacer(Modifier.size(ButtonDefaults.IconSize))
                                    }
                                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                    Text(filterOption.name)
                                }
                            }
                        }
                    }
                }
            )
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                groupedIngestions.forEach { (year, ingestionsInYear) ->
                    item {
                        SectionTitle(title = year)
                    }
                    items(ingestionsInYear.size) { i ->
                        val ingestion = ingestionsInYear[i]
                        IngestionRow(ingestion = ingestion, deleteIngestion = { /*TODO*/ })
                        if (i < ingestionsInYear.size) {
                            Divider()
                        }
                    }
                }
            }
            if (groupedIngestions.isEmpty()) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = "No Ingestions Yet")
                }
            }
        }
    }
}