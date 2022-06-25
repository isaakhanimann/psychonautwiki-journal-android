package com.example.healthassistant.ui.stats

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun StatisticsPreview() {
    StatisticsScreen {}
}

@Composable
fun StatisticsScreen(navigateToSettings: () -> Unit) {
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

    }
}