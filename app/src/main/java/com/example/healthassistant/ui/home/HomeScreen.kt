package com.example.healthassistant.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.experiences.entities.ExperienceWithIngestions
import com.example.healthassistant.ui.previewproviders.ExperienceWithIngestionsPreviewProvider
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    navigateToAddExperience: () -> Unit,
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
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
            navigateToExperiencePopNothing = navigateToExperiencePopNothing
        )
    }
}

@Composable
fun ExperiencesList(
    homeViewModel: HomeViewModel,
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit
) {
    val groupedExperiences = homeViewModel.experiencesGrouped.collectAsState().value
    LazyColumn {
        groupedExperiences.forEach { (year, experiencesInYear) ->
            item {
                YearRow(year = year)
            }
            items(experiencesInYear.size) { i ->
                val expAndIngs = experiencesInYear[i]
                ExperienceRow(
                    expAndIngs,
                    navigateToExperienceScreen = {
                        navigateToExperiencePopNothing(expAndIngs.experience.id)
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

@Preview
@Composable
fun YearRow(year: String = "2022") {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.primary.copy(alpha = 0.2f)
    ) {
        Text(
            color = MaterialTheme.colors.primary,
            text = year,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}