package com.isaakhanimann.healthassistant.ui.settings

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.ui.theme.horizontalPadding

@Preview
@Composable
fun SettingsPreview() {
    SettingsScreen(
        deleteEverything = {},
        navigateToFAQ = {},
        importFile = {},
        exportFile = {}
    )
}

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navigateToFAQ: () -> Unit
) {
    SettingsScreen(
        navigateToFAQ,
        deleteEverything = viewModel::deleteEverything,
        importFile = viewModel::importFile,
        exportFile = viewModel::exportFile
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navigateToFAQ: () -> Unit,
    deleteEverything: () -> Unit,
    importFile: (uri: Uri?) -> Unit,
    exportFile: (uri: Uri?) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            val launcherImport =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { result ->
                    importFile(result)
                }
            SettingsButton(imageVector = Icons.Outlined.FileDownload, text = "Import File") {
                launcherImport.launch("*/*")
            }
            Divider()
            val launcherExport =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.CreateDocument(
                        mimeType = "application/json"
                    )
                ) { uri ->
                    exportFile(uri)
                }
            SettingsButton(imageVector = Icons.Outlined.FileUpload, text = "Export File") {
                launcherExport.launch("Journal.json")
            }
            Divider()
            val uriHandler = LocalUriHandler.current
            SettingsButton(
                imageVector = Icons.Outlined.OpenInBrowser,
                text = "Responsible Drug Use"
            ) {
                uriHandler.openUri("https://psychonautwiki.org/wiki/Responsible_drug_use")
            }
            Divider()
            SettingsButton(imageVector = Icons.Outlined.QuestionAnswer, text = "FAQ") {
                navigateToFAQ()
            }
            Divider()
            SettingsButton(
                imageVector = Icons.Outlined.ContactSupport,
                text = "Question / Feedback / Bug Report"
            ) {
                uriHandler.openUri("https://t.me/isaakhanimann")
            }
            Divider()
            SettingsButton(imageVector = Icons.Outlined.Code, text = "Source Code") {
                uriHandler.openUri("https://github.com/isaakhanimann/HealthAssistant")
            }
            Divider()
            var isShowingDeleteDialog by remember { mutableStateOf(false) }
            SettingsButton(imageVector = Icons.Outlined.DeleteForever, text = "Delete Everything") {
                isShowingDeleteDialog = true
            }
            if (isShowingDeleteDialog) {
                val context = LocalContext.current
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

@Composable
fun SettingsButton(imageVector: ImageVector, text: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.padding(horizontal = horizontalPadding)
    ) {
        Icon(
            imageVector,
            contentDescription = imageVector.name,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text)
        Spacer(modifier = Modifier.weight(1f))
    }
}