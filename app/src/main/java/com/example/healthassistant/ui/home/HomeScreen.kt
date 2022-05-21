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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.healthassistant.data.experiences.entities.ExperienceWithIngestions
import com.example.healthassistant.ui.main.routers.navigateToAddExperience
import com.example.healthassistant.ui.main.routers.navigateToExperience
import com.example.healthassistant.ui.previewproviders.ExperienceWithIngestionsPreviewProvider
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Experiences") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigateToAddExperience()
            }) {
                Icon(Icons.Default.Add, "Add New Experience")
            }
        }
    ) {
        ExperiencesList(homeViewModel = homeViewModel, navController = navController)
    }
}

@Composable
fun ExperiencesList(homeViewModel: HomeViewModel, navController: NavController) {
    val groupedExperiences = homeViewModel.experiencesGrouped.collectAsState().value
    LazyColumn {
        groupedExperiences.forEach { (year, experiencesInYear) ->
            item {
                YearRow(year = year)
            }
            items(experiencesInYear.size) { i ->
                val expAndIngs = experiencesInYear[i]
                ExperienceRow(expAndIngs, navigateToExperienceScreen = {
                    navController.navigateToExperience(experienceId = expAndIngs.experience.id)
                })
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

@Preview(showBackground = true)
@Composable
fun ExperienceRow(
    @PreviewParameter(ExperienceWithIngestionsPreviewProvider::class) experienceWithIngs: ExperienceWithIngestions,
    navigateToExperienceScreen: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .clickable {
                navigateToExperienceScreen()
            }
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isDarkTheme = isSystemInDarkTheme()
            val circleSize = 45.dp
            if (experienceWithIngs.ingestions.size >= 2) {
                val brush = remember(experienceWithIngs.ingestions) {
                    val colors = experienceWithIngs.ingestions.map { it.color.getComposeColor(isDarkTheme) }
                    Brush.radialGradient(colors)
                }
                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .clip(CircleShape)
                        .background(brush)
                )
            } else if (experienceWithIngs.ingestions.size == 1) {
                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .clip(CircleShape)
                        .background(experienceWithIngs.ingestions.first().color.getComposeColor(isDarkTheme))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .clip(CircleShape)
                        .background(Color.LightGray.copy(0.1f))
                )
            }
            Column() {
                Text(
                    text = experienceWithIngs.experience.title,
                    style = MaterialTheme.typography.h5
                )
                val substanceNames = remember(experienceWithIngs.ingestions) {
                    experienceWithIngs.ingestions.map { it.substanceName }.distinct()
                        .joinToString(separator = ", ")
                }
                if (substanceNames.isNotEmpty()) {
                    Text(text = substanceNames)
                } else {
                    Text(text = "No substance yet")
                }
            }
        }
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            val formatter = SimpleDateFormat("dd MMMM", Locale.getDefault())
            val creationDateText = formatter.format(experienceWithIngs.experience.date) ?: ""
            Text(creationDateText)
        }
    }
}