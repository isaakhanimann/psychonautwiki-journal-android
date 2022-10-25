package com.isaakhanimann.healthassistant.ui.settings

import android.widget.Toast
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.ui.theme.horizontalPadding
import com.isaakhanimann.healthassistant.ui.utils.JournalTopAppBar

@Preview
@Composable
fun SettingsPreview() {
    SettingsScreen(
        deleteEverything = {},
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
        deleteEverything = settingsViewModel::deleteEverything
    )
}

@Composable
fun SettingsScreen(
    navigateToFAQ: () -> Unit,
    deleteEverything: () -> Unit,
) {
    Scaffold(topBar = {
        JournalTopAppBar(title = "Settings")
    }) {
        Column {
            val uriHandler = LocalUriHandler.current
            TextButton(
                onClick = {
                    uriHandler.openUri("https://psychonautwiki.org/wiki/Responsible_drug_use")
                },
                modifier = Modifier.padding(horizontal = horizontalPadding)
            ) {
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
            TextButton(
                onClick = navigateToFAQ,
                modifier = Modifier.padding(horizontal = horizontalPadding)
            ) {
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
            TextButton(
                onClick = {
                    uriHandler.openUri("https://t.me/isaakhanimann")
                },
                modifier = Modifier.padding(horizontal = horizontalPadding)
            ) {
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
                },
                modifier = Modifier.padding(horizontal = horizontalPadding)
            ) {
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
                },
                modifier = Modifier.padding(horizontal = horizontalPadding)
            ) {
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
            Text(
                text = "Last Substance Update: 25. October 2022",
                modifier = Modifier
                    .padding(horizontal = horizontalPadding, vertical = 10.dp)
                    .fillMaxWidth()
            )
        }
    }
}