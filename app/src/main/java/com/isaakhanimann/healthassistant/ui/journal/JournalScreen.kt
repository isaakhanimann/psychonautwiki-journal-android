package com.isaakhanimann.healthassistant.ui.journal

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
import com.isaakhanimann.healthassistant.data.room.experiences.relations.ExperienceWithIngestionsAndCompanions

@Composable
fun JournalScreen(
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    navigateToAddIngestion: () -> Unit,
    experiencesViewModel: ExperiencesViewModel = hiltViewModel()
) {
    JournalScreen(
        navigateToExperiencePopNothing = navigateToExperiencePopNothing,
        navigateToAddIngestion = navigateToAddIngestion,
        groupedExperiences = experiencesViewModel.experiencesGrouped.collectAsState().value,
    )
}

@Preview
@Composable
fun ExperiencesScreenPreview(
    @PreviewParameter(
        JournalScreenPreviewProvider::class,
    ) groupedExperiences: Map<String, List<ExperienceWithIngestionsAndCompanions>>,
) {
    JournalScreen(
        navigateToExperiencePopNothing = {},
        navigateToAddIngestion = {},
        groupedExperiences = groupedExperiences,
    )
}

@Composable
fun JournalScreen(
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    navigateToAddIngestion: () -> Unit,
    groupedExperiences: Map<String, List<ExperienceWithIngestionsAndCompanions>>,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Experiences") }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = navigateToAddIngestion,
                icon = {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add"
                    )
                },
                text = { Text("Ingestion") },
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
    groupedExperiences: Map<String, List<ExperienceWithIngestionsAndCompanions>>,
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
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
                )
                if (i < experiencesInYear.size) {
                    Divider()
                }
            }
        }
    }
}