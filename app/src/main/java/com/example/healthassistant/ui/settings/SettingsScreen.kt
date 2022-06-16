package com.example.healthassistant.ui.settings

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.initialDate

@Preview
@Composable
fun SettingsPreview() {
    SettingsScreen(
        updateSubstances = { _: () -> Unit, _: () -> Unit -> run {} },
        resetSubstances = {},
        isUpdating = false,
        date = "14 Jan 2022 15:35"
    )
}

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel = hiltViewModel()) {
    SettingsScreen(
        updateSubstances = settingsViewModel::updateSubstances,
        resetSubstances = settingsViewModel::resetSubstances,
        isUpdating = settingsViewModel.isUpdating,
        date = settingsViewModel.dateStringFlow.collectAsState(initial = initialDate.asTextWithDateAndTime).value
    )
}

@Composable
fun SettingsScreen(
    updateSubstances: (onSuccess: () -> Unit, onError: () -> Unit) -> Unit,
    resetSubstances: () -> Unit,
    isUpdating: Boolean,
    date: String
) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Settings") },
        )
    }) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = "Last Substance Update", style = MaterialTheme.typography.caption)
            Text(date, style = MaterialTheme.typography.h6)
            val context = LocalContext.current
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(
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
                    Icon(
                        Icons.Filled.Update,
                        contentDescription = "Update Substances",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Update")
                }
                TextButton(
                    onClick = resetSubstances,
                    enabled = !isUpdating
                ) {
                    Icon(
                        Icons.Filled.RestartAlt,
                        contentDescription = "Reset Substances",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Reset")
                }
                if (isUpdating) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}