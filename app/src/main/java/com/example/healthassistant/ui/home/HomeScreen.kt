package com.example.healthassistant.ui.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    navigateToAddExperience: () -> Unit,
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    navigateToEditExperienceScreen: (experienceId: Int) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Experiences") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToAddExperience) {
                Icon(Icons.Default.Add, "Add New Experience")
            }
        }
    ) {
        ExperiencesList(
            homeViewModel = homeViewModel,
            navigateToExperiencePopNothing = navigateToExperiencePopNothing,
            navigateToEditExperienceScreen = navigateToEditExperienceScreen,
            navigateToAddExperience = navigateToAddExperience
        )
    }
}

@Composable
fun ExperiencesList(
    homeViewModel: HomeViewModel,
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    navigateToEditExperienceScreen: (experienceId: Int) -> Unit,
    navigateToAddExperience: () -> Unit,
) {
    val groupedExperiences = homeViewModel.experiencesGrouped.collectAsState().value
    if (groupedExperiences.isEmpty()) {
        Button(onClick = navigateToAddExperience) {
            Text("Add Your First Experience")
        }
    } else {
        LazyColumn {
            groupedExperiences.forEach { (year, experiencesInYear) ->
                item {
                    SectionTitle(title = year)
                }
                items(experiencesInYear.size) { i ->
                    val expAndIngs = experiencesInYear[i]
                    ExperienceRow(
                        expAndIngs,
                        navigateToExperienceScreen = {
                            navigateToExperiencePopNothing(expAndIngs.experience.id)
                        },
                        navigateToEditExperienceScreen = {
                            navigateToEditExperienceScreen(expAndIngs.experience.id)
                        },
                        deleteExperienceWithIngestions = {
                            homeViewModel.deleteExperienceWithIngestions(expAndIngs)
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