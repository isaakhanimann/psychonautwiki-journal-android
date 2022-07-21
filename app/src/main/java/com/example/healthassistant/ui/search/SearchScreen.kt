package com.example.healthassistant.ui.search

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable

@Composable
fun SearchScreen(
    navigateToSettings: () -> Unit,
    navigateToSubstanceScreen: (substanceName: String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Substances") },
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
        SearchContent(
            onSubstanceTap = {
                navigateToSubstanceScreen(it.name)
            }
        )
    }
}
