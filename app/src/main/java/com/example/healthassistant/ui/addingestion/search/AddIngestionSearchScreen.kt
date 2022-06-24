package com.example.healthassistant.ui.addingestion.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthassistant.ui.search.SearchScreen

@Composable
fun AddIngestionSearchScreen(navigateToCheckInteractions: (substanceName: String) -> Unit) {
    Column {
        LinearProgressIndicator(progress = 0.17f, modifier = Modifier.fillMaxWidth())
        SearchScreen(
            onSubstanceTap = {
                navigateToCheckInteractions(it.name)
            }
        )
    }
}