package com.example.healthassistant.ui.home.experience.addingestion.search

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.healthassistant.ui.main.routers.navigateToAddIngestion
import com.example.healthassistant.ui.search.SearchScreen

@Composable
fun AddIngestionSearchScreen(navController: NavController, experienceId: Int?) {
    SearchScreen(onSubstanceTap = {
        navController.navigateToAddIngestion(substanceName = it.name, experienceId = experienceId)
    })
}