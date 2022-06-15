package com.example.healthassistant.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Preview
@Composable
fun SettingsPreview() {
    SettingsScreen(incrementCounter = {}, counterValue = 1)
}

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel = hiltViewModel()) {
    SettingsScreen(
        incrementCounter = settingsViewModel::onIncrementTap,
        counterValue = settingsViewModel.counter.collectAsState(initial = 0).value
    )
}

@Composable
fun SettingsScreen(
    incrementCounter: ()->Unit,
    counterValue: Int
) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Settings") },
        )
    }) {
        Column {
            Button(onClick = incrementCounter) {
                Text(text = "Increment Counter")
            }
            Text(text = "counter: $counterValue")
        }
    }
}