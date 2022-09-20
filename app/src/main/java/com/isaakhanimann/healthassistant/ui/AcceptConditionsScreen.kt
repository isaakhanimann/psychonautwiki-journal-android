package com.isaakhanimann.healthassistant.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AcceptConditionsScreen(
    acceptConditionsViewModel: AcceptConditionsViewModel = hiltViewModel()
) {
    Column {
        Text(text = acceptConditionsViewModel.exampleCounterFlow.collectAsState(initial = 0).value.toString())
        Button(onClick = acceptConditionsViewModel::incrementCounter) {
            Text(text = "Increment Counter")
        }
    }
}