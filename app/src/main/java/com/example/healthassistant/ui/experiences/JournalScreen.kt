package com.example.healthassistant.ui.experiences

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.room.experiences.entities.ExperienceWithIngestions

@Composable
fun JournalScreen(
    navigateToAddExperience: () -> Unit,
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    navigateToEditExperienceScreen: (experienceId: Int) -> Unit,
    journalViewModel: JournalViewModel = hiltViewModel()
) {
    JournalScreen(
        navigateToAddExperience = navigateToAddExperience,
        navigateToExperiencePopNothing = navigateToExperiencePopNothing,
        navigateToEditExperienceScreen = navigateToEditExperienceScreen,
        groupedExperiences = journalViewModel.experiencesGrouped.collectAsState().value,
        deleteExperience = journalViewModel::deleteExperienceWithIngestions,
        filterOptions = journalViewModel.filterOptions.collectAsState().value,
        numberOfActiveFilters = journalViewModel.numberOfActiveFilters
    )
}

@Preview
@Composable
fun JournalScreenPreview() {
    JournalScreen(
        navigateToAddExperience = {},
        navigateToExperiencePopNothing = {},
        navigateToEditExperienceScreen = {},
        groupedExperiences = emptyMap(),
        deleteExperience = {},
        filterOptions = listOf(),
        numberOfActiveFilters = 1
    )
}

@Composable
fun JournalScreen(
    navigateToAddExperience: () -> Unit,
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    navigateToEditExperienceScreen: (experienceId: Int) -> Unit,
    groupedExperiences: Map<String, List<ExperienceWithIngestions>>,
    deleteExperience: (ExperienceWithIngestions) -> Unit,
    filterOptions: List<JournalViewModel.FilterOption>,
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
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = navigateToAddExperience,
                icon = {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add"
                    )
                },
                text = {
                    if (groupedExperiences.isEmpty()) {
                        Text("Add Your First Experience")
                    } else {
                        Text("New Experience")
                    }
                }
            )
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            ExperiencesList(
                groupedExperiences = groupedExperiences,
                navigateToExperiencePopNothing = navigateToExperiencePopNothing,
                navigateToEditExperienceScreen = navigateToEditExperienceScreen,
                deleteExperience = deleteExperience
            )
            if (groupedExperiences.isEmpty()) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = "No Experiences Yet")
                }
            }
        }
    }
}

@Composable
fun ExperiencesList(
    groupedExperiences: Map<String, List<ExperienceWithIngestions>>,
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    navigateToEditExperienceScreen: (experienceId: Int) -> Unit,
    deleteExperience: (ExperienceWithIngestions) -> Unit
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
                    navigateToExperienceScreen = {
                        navigateToExperiencePopNothing(experienceWithIngestions.experience.id)
                    },
                    navigateToEditExperienceScreen = {
                        navigateToEditExperienceScreen(experienceWithIngestions.experience.id)
                    },
                    deleteExperienceWithIngestions = {
                        deleteExperience(experienceWithIngestions)
                    }
                )
                if (i < experiencesInYear.size) {
                    Divider()
                }
            }
        }
    }
}