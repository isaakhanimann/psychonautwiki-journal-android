package com.example.healthassistant.ui.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.initialDate
import java.util.*

@Preview
@Composable
fun SettingsPreview() {
    SettingsScreen(
        updateSubstances = { _: () -> Unit, _: () -> Unit -> run {} },
        resetSubstances = {},
        isUpdating = false,
        date = Date()
    )
}

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel = hiltViewModel()) {
    SettingsScreen(
        updateSubstances = settingsViewModel::updateSubstances,
        resetSubstances = settingsViewModel::resetSubstances,
        isUpdating = settingsViewModel.isUpdating,
        date = settingsViewModel.dateFlow.collectAsState(initial = initialDate).value
    )
}

@Composable
fun SettingsScreen(
    updateSubstances: (onSuccess: () -> Unit, onError: () -> Unit) -> Unit,
    resetSubstances: () -> Unit,
    isUpdating: Boolean,
    date: Date
) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Settings") },
        )
    }) {
        val context = LocalContext.current
        Box(contentAlignment = Alignment.Center) {
            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                Button(
                    onClick = {
                        updateSubstances(
                            {
                                Toast.makeText(
                                    context, "Update Success",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            {
                                Toast.makeText(
                                    context, "Try Again Later",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    },
                    enabled = !isUpdating
                ) {
                    Text(text = "Update Substances")
                }
                Button(
                    onClick = resetSubstances,
                    enabled = !isUpdating
                ) {
                    Text(text = "Reset Substances")
                }
                Text(text = "Last Update: $date")
            }
            if (isUpdating) {
                CircularProgressIndicator()
            }
        }
    }
}