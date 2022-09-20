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
        Text(text = acceptConditionsViewModel.isAcceptedFlow.collectAsState(initial = false).value.toString())
        Button(onClick = acceptConditionsViewModel::toggleAccepted) {
            Text(text = "Toggle")
        }
    }
}