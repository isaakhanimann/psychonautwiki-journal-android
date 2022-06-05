package com.example.healthassistant.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.room.experiences.entities.ExperienceWithIngestions

@Composable
fun HomeScreen(
    navigateToAddExperience: () -> Unit,
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    navigateToEditExperienceScreen: (experienceId: Int) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreen(
        navigateToAddExperience = navigateToAddExperience,
        navigateToExperiencePopNothing = navigateToExperiencePopNothing,
        navigateToEditExperienceScreen = navigateToEditExperienceScreen,
        groupedExperiences = homeViewModel.experiencesGrouped.collectAsState().value,
        deleteExperience = homeViewModel::deleteExperienceWithIngestions,
        filterOptions = homeViewModel.filterOptions.collectAsState().value,
    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        navigateToAddExperience = {},
        navigateToExperiencePopNothing = {},
        navigateToEditExperienceScreen = {},
        groupedExperiences = emptyMap(),
        deleteExperience = {},
        filterOptions = listOf(),
    )
}

@Composable
fun HomeScreen(
    navigateToAddExperience: () -> Unit,
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    navigateToEditExperienceScreen: (experienceId: Int) -> Unit,
    groupedExperiences: Map<String, List<ExperienceWithIngestions>>,
    deleteExperience: (ExperienceWithIngestions) -> Unit,
    filterOptions: List<HomeViewModel.FilterOption>,
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
                            Icon(Icons.Default.Favorite, contentDescription = "Filter")
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
                                        Icon(Icons.Default.Check, "Check")
                                    }
                                    Text(filterOption.name)
                                }
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToAddExperience) {
                Icon(Icons.Default.Add, "Add New Experience")
            }
        }
    ) {
        ExperiencesList(
            groupedExperiences = groupedExperiences,
            navigateToExperiencePopNothing = navigateToExperiencePopNothing,
            navigateToEditExperienceScreen = navigateToEditExperienceScreen,
            navigateToAddExperience = navigateToAddExperience,
            deleteExperience = deleteExperience
        )
    }
}

@Composable
fun ExperiencesList(
    groupedExperiences: Map<String, List<ExperienceWithIngestions>>,
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    navigateToEditExperienceScreen: (experienceId: Int) -> Unit,
    navigateToAddExperience: () -> Unit,
    deleteExperience: (ExperienceWithIngestions) -> Unit
) {
    if (groupedExperiences.isEmpty()) {
        Button(
            onClick = navigateToAddExperience,
            modifier = Modifier.padding(10.dp)
        ) {
            Text("Add Your First Experience")
        }
    } else {
        LazyColumn {
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
}