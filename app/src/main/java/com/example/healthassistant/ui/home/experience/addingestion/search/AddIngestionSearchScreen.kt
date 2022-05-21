package com.example.healthassistant.ui.home.experience.addingestion.search

import androidx.compose.runtime.Composable
import com.example.healthassistant.ui.search.SearchScreen

@Composable
fun AddIngestionSearchScreen(navigateToAddIngestionScreens: (substanceName: String) -> Unit) {
    SearchScreen(
        onSubstanceTap = {
            navigateToAddIngestionScreens(it.name)
        }
    )
}