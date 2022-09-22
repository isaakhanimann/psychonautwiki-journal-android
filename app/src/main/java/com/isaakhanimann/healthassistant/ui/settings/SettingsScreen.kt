package com.isaakhanimann.healthassistant.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.outlined.ContactSupport
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.data.initialDate

@Preview
@Composable
fun SettingsPreview() {
    SettingsScreen(
        updateSubstances = { _: () -> Unit, _: () -> Unit -> run {} },
        resetSubstances = {},
        isUpdating = false,
        date = "14 Jan 2022 15:35",
        navigateToFAQ = {}
    )
}

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    navigateToFAQ: () -> Unit
) {
    SettingsScreen(
        navigateToFAQ,
        updateSubstances = settingsViewModel::updateSubstances,
        resetSubstances = settingsViewModel::resetSubstances,
        isUpdating = settingsViewModel.isUpdating,
        date = settingsViewModel.dateStringFlow.collectAsState(initial = initialDate.asTextWithDateAndTime).value
    )
}

@Composable
fun SettingsScreen(
    navigateToFAQ: () -> Unit,
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
//            Text(text = "Last Substance Update", style = MaterialTheme.typography.caption)
//            Text(date, style = MaterialTheme.typography.h6)
//            val context = LocalContext.current
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                TextButton(
//                    onClick = {
//                        updateSubstances(
//                            {
//                                Toast.makeText(
//                                    context, "Update Success",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            },
//                            {
//                                Toast.makeText(
//                                    context, "Try Again Later",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        )
//                    },
//                    enabled = !isUpdating
//                ) {
//                    Icon(
//                        Icons.Filled.Update,
//                        contentDescription = "Update Substances",
//                        modifier = Modifier.size(ButtonDefaults.IconSize)
//                    )
//                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
//                    Text("Update")
//                }
//                TextButton(
//                    onClick = resetSubstances,
//                    enabled = !isUpdating
//                ) {
//                    Icon(
//                        Icons.Filled.Restore,
//                        contentDescription = "Reset Substances",
//                        modifier = Modifier.size(ButtonDefaults.IconSize)
//                    )
//                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
//                    Text("Reset")
//                }
//                if (isUpdating) {
//                    CircularProgressIndicator()
//                }
//            }
//            Divider()
            val uriHandler = LocalUriHandler.current
            TextButton(
                onClick = {
                uriHandler.openUri("https://psychonautwiki.org/wiki/Responsible_drug_use")
            }) {
                Icon(
                    Icons.Filled.Launch,
                    contentDescription = "Open Link",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Responsible Drug Use")
                Spacer(modifier = Modifier.weight(1f))
            }
            Divider()
            TextButton(onClick = navigateToFAQ) {
                Icon(
                    Icons.Outlined.QuestionAnswer,
                    contentDescription = "Frequently Asked Questions",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("FAQ")
                Spacer(modifier = Modifier.weight(1f))
            }
            Divider()
            TextButton(onClick = {
                uriHandler.openUri("https://t.me/isaakhanimann")
            }) {
                Icon(
                    Icons.Outlined.ContactSupport,
                    contentDescription = "Contact Support",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Question / Feedback / Bug Reports")
                Spacer(modifier = Modifier.weight(1f))
            }
            Divider()
            TextButton(
                onClick = {
                    uriHandler.openUri("https://github.com/isaakhanimann/HealthAssistant")
                }) {
                Icon(
                    Icons.Filled.Code,
                    contentDescription = "Open Source Code",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Source Code")
                Spacer(modifier = Modifier.weight(1f))
            }
            Divider()
        }
    }
}