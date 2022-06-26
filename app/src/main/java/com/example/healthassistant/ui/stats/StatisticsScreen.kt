package com.example.healthassistant.ui.stats

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.room.experiences.entities.SubstanceLastUsed


@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel(),
    navigateToSettings: () -> Unit
) {
    StatisticsScreen(
        navigateToSettings = navigateToSettings,
        substanceStats = viewModel.substanceStats.collectAsState().value
    )
}

@Preview
@Composable
fun StatisticsPreview() {
    StatisticsScreen {}
}

@Composable
fun StatisticsScreen(
    navigateToSettings: () -> Unit,
    substanceStats: List<SubstanceLastUsed>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Statistics") },
                actions = {
                    IconButton(
                        onClick = navigateToSettings,
                    ) {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Navigate to Settings"
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(substanceStats.size) { i ->
                val subStat = substanceStats[i]
                Text(text = "${subStat.substanceName} last used at ${subStat.lastUsed}")
                if (i < substanceStats.size) {
                    Divider()
                }
            }
        }
    }
}