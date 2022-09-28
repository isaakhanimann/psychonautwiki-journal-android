package com.isaakhanimann.healthassistant.ui.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.outlined.ContactSupport
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.data.initialDate

@Preview
@Composable
fun SettingsPreview() {
    SettingsScreen(
        deleteEverything = {},
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
        deleteEverything = settingsViewModel::deleteEverything,
        updateSubstances = settingsViewModel::updateSubstances,
        resetSubstances = settingsViewModel::resetSubstances,
        isUpdating = settingsViewModel.isUpdating,
        date = settingsViewModel.dateStringFlow.collectAsState(initial = initialDate.asTextWithDateAndTime).value
    )
}

@Composable
fun SettingsScreen(
    navigateToFAQ: () -> Unit,
    deleteEverything: () -> Unit,
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
        Column {
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
                    Icons.Default.OpenInBrowser,
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
                Text("Question / Feedback / Bug Report")
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
            val context = LocalContext.current
            var isShowingDeleteDialog by remember { mutableStateOf(false) }
            TextButton(
                onClick = {
                    isShowingDeleteDialog = true
                }) {
                Icon(
                    Icons.Filled.DeleteForever,
                    contentDescription = "Delete",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Delete Everything")
                Spacer(modifier = Modifier.weight(1f))
            }
            if (isShowingDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { isShowingDeleteDialog = false },
                    title = {
                        Text(text = "Delete Everything?")
                    },
                    text = {
                        Text("This will delete all your experiences, ingestions and custom substances.")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                isShowingDeleteDialog = false
                                deleteEverything()
                                Toast.makeText(
                                    context,
                                    "Everything Deleted",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { isShowingDeleteDialog = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
            Divider()
        }
    }
}