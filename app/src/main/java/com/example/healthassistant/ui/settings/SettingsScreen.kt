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
import com.example.healthassistant.data.initialDate
import java.util.*

@Preview
@Composable
fun SettingsPreview() {
    SettingsScreen(updateDateToNow = {}, date = Date())
}

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel = hiltViewModel()) {
    SettingsScreen(
        updateDateToNow = settingsViewModel::updateDateToNow,
        date = settingsViewModel.dateFlow.collectAsState(initial = initialDate).value
    )
}

@Composable
fun SettingsScreen(
    updateDateToNow: ()->Unit,
    date: Date
) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Settings") },
        )
    }) {
        Column {
            Button(onClick = updateDateToNow) {
                Text(text = "Update Date to Now")
            }
            Text(text = "counter: $date")
        }
    }
}