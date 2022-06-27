package com.example.healthassistant.ui.experiences

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.room.experiences.relations.ExperienceWithIngestions
import com.example.healthassistant.ui.previewproviders.ExperiencesScreenPreviewProvider

@Composable
fun ExperiencesScreen(
    navigateToAddExperience: () -> Unit,
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    navigateToEditExperienceScreen: (experienceId: Int) -> Unit,
    experiencesViewModel: ExperiencesViewModel = hiltViewModel()
) {
    ExperiencesScreen(
        navigateToAddExperience = navigateToAddExperience,
        navigateToExperiencePopNothing = navigateToExperiencePopNothing,
        navigateToEditExperienceScreen = navigateToEditExperienceScreen,
        groupedExperiences = experiencesViewModel.experiencesGrouped.collectAsState().value,
    )
}

@Preview
@Composable
fun ExperiencesScreenPreview(
    @PreviewParameter(
        ExperiencesScreenPreviewProvider::class,
    ) groupedExperiences: Map<String, List<ExperienceWithIngestions>>,
) {
    ExperiencesScreen(
        navigateToAddExperience = {},
        navigateToExperiencePopNothing = {},
        navigateToEditExperienceScreen = {},
        groupedExperiences = groupedExperiences,
    )
}

@Composable
fun ExperiencesScreen(
    navigateToAddExperience: () -> Unit,
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    navigateToEditExperienceScreen: (experienceId: Int) -> Unit,
    groupedExperiences: Map<String, List<ExperienceWithIngestions>>,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Experiences") },
                actions = {
                    IconButton(
                        onClick = navigateToAddExperience,
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add Icon"
                        )
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
                    }
                )
                if (i < experiencesInYear.size) {
                    Divider()
                }
            }
        }
    }
}