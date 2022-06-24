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
import com.example.healthassistant.data.room.experiences.entities.ExperienceWithIngestions
import com.example.healthassistant.ui.experiences.ExperienceRow
import com.example.healthassistant.ui.experiences.SectionTitle

@Composable
fun IngestionsScreen(
    navigateToIngestion: (ingestionId: Int) -> Unit,
    viewModel: IngestionsViewModel = hiltViewModel()
) {
    IngestionsScreen(
        navigateToIngestion = navigateToIngestion,
        groupedExperiences = viewModel.experiencesGrouped.collectAsState().value,
        filterOptions = viewModel.filterOptions.collectAsState().value,
        numberOfActiveFilters = viewModel.numberOfActiveFilters
    )
}

@Preview
@Composable
fun IngestionsScreenPreview() {
    IngestionsScreen(
        navigateToIngestion = {},
        groupedExperiences = emptyMap(),
        filterOptions = listOf(),
        numberOfActiveFilters = 1
    )
}

@Composable
fun IngestionsScreen(
    navigateToIngestion: (ingestionId: Int) -> Unit,
    groupedExperiences: Map<String, List<ExperienceWithIngestions>>,
    filterOptions: List<IngestionsViewModel.FilterOption>,
    numberOfActiveFilters: Int,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Experiences") },
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
                groupedExperiences.forEach { (year, experiencesInYear) ->
                    item {
                        SectionTitle(title = year)
                    }
                    items(experiencesInYear.size) { i ->
                        val experienceWithIngestions = experiencesInYear[i]
                        ExperienceRow(
                            experienceWithIngestions,
                            navigateToExperienceScreen = {},
                            navigateToEditExperienceScreen = {},
                            deleteExperienceWithIngestions = {}
                        )
                        if (i < experiencesInYear.size) {
                            Divider()
                        }
                    }
                }
            }
            if (groupedExperiences.isEmpty()) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = "No Experiences Yet")
                }
            }
        }
    }
}