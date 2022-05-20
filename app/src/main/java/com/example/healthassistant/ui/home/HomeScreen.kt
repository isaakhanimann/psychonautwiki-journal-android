package com.example.healthassistant.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.healthassistant.data.experiences.entities.Experience
import com.example.healthassistant.ui.main.routers.navigateToAddExperience
import com.example.healthassistant.ui.main.routers.navigateToExperience
import com.example.healthassistant.ui.previewproviders.ExperiencePreviewProvider
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
                val experience = experiencesInYear[i]
                ExperienceRow(experience, navigateToExperienceScreen = {
                    navController.navigateToExperience(experienceId = experience.id)
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

@Preview
@Composable
fun ExperienceRow(
    @PreviewParameter(ExperiencePreviewProvider::class) experience: Experience,
    navigateToExperienceScreen: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .clickable {
                navigateToExperienceScreen()
            }
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),

        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = experience.title)
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            val formatter = SimpleDateFormat("dd MMMM", Locale.getDefault())
            val creationDateText = formatter.format(experience.date) ?: ""
            Text(creationDateText)
        }
    }
}