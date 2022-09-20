package com.isaakhanimann.healthassistant.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun AcceptConditionsScreen(
    onTapAccept: () -> Unit
) {
    Column {
        Button(onClick = onTapAccept) {
            Text(text = "Toggle")
        }
    }
}