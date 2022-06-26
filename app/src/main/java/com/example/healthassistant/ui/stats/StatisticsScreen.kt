package com.example.healthassistant.ui.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.ui.previewproviders.StatisticsPreviewProvider


@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel(),
    navigateToSettings: () -> Unit
) {
    StatisticsScreen(
        navigateToSettings = navigateToSettings,
        substancesLastUsed = viewModel.substanceStats.collectAsState().value
    )
}

@Preview
@Composable
fun StatisticsPreview(
    @PreviewParameter(
        StatisticsPreviewProvider::class,
    ) substancesLastUsed: List<SubstanceStat>
) {
    StatisticsScreen(
        navigateToSettings = {},
        substancesLastUsed = substancesLastUsed
    )
}

@Composable
fun StatisticsScreen(
    navigateToSettings: () -> Unit,
    substancesLastUsed: List<SubstanceStat>
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(substancesLastUsed.size) { i ->
                val subStat = substancesLastUsed[i]
                Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) {
                    Text(text = subStat.substanceName, style = MaterialTheme.typography.h6)
                    Text(text = "Last used ${subStat.lastUsedText} ago")
                }
                if (i < substancesLastUsed.size) {
                    Divider()
                }
            }
        }
    }
}