/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.settings

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ContactSupport
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.ui.VERSION_NAME
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.CardWithTitle
import com.isaakhanimann.journal.ui.theme.horizontalPadding
import com.isaakhanimann.journal.ui.utils.getStringOfPattern
import kotlinx.coroutines.launch
import java.time.Instant

@Preview
@Composable
fun SettingsPreview() {
    SettingsScreen(
        deleteEverything = {},
        navigateToFAQ = {},
        navigateToComboSettings = {},
        navigateToSubstanceColors = {},
        navigateToCustomUnits = {},
        navigateToDonate = {},
        importFile = {},
        exportFile = {},
        snackbarHostState = remember { SnackbarHostState() },
        areDosageDotsHidden = false,
        saveDosageDotsAreHidden = {}
    )
}

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navigateToFAQ: () -> Unit,
    navigateToComboSettings: () -> Unit,
    navigateToSubstanceColors: () -> Unit,
    navigateToCustomUnits: () -> Unit,
    navigateToDonate: () -> Unit,
) {
    SettingsScreen(
        navigateToFAQ = navigateToFAQ,
        navigateToComboSettings = navigateToComboSettings,
        navigateToSubstanceColors = navigateToSubstanceColors,
        navigateToCustomUnits = navigateToCustomUnits,
        navigateToDonate = navigateToDonate,
        deleteEverything = viewModel::deleteEverything,
        importFile = viewModel::importFile,
        exportFile = viewModel::exportFile,
        snackbarHostState = viewModel.snackbarHostState,
        areDosageDotsHidden = viewModel.areDosageDotsHiddenFlow.collectAsState().value,
        saveDosageDotsAreHidden = viewModel::saveDosageDotsAreHidden
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navigateToFAQ: () -> Unit,
    navigateToComboSettings: () -> Unit,
    navigateToSubstanceColors: () -> Unit,
    navigateToCustomUnits: () -> Unit,
    navigateToDonate: () -> Unit,
    deleteEverything: () -> Unit,
    importFile: (uri: Uri) -> Unit,
    exportFile: (uri: Uri) -> Unit,
    snackbarHostState: SnackbarHostState,
    areDosageDotsHidden: Boolean,
    saveDosageDotsAreHidden: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = horizontalPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CardWithTitle(title = "UI", innerPaddingHorizontal = 0.dp) {
                SettingsButton(
                    imageVector = Icons.Outlined.Medication,
                    text = "Custom units"
                ) {
                    navigateToCustomUnits()
                }
                HorizontalDivider()
                SettingsButton(
                    imageVector = Icons.Outlined.Palette,
                    text = "Substance colors"
                ) {
                    navigateToSubstanceColors()
                }
                HorizontalDivider()
                SettingsButton(
                    imageVector = Icons.Outlined.WarningAmber,
                    text = "Interaction settings"
                ) {
                    navigateToComboSettings()
                }
                HorizontalDivider()
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = horizontalPadding),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Hide dosage dots")
                    Switch(
                        checked = areDosageDotsHidden,
                        onCheckedChange = saveDosageDotsAreHidden)
                }
            }
            CardWithTitle(title = "App data", innerPaddingHorizontal = 0.dp) {
                var isShowingExportDialog by remember { mutableStateOf(false) }
                SettingsButton(imageVector = Icons.Outlined.FileUpload, text = "Export File") {
                    isShowingExportDialog = true
                }
                val jsonMIMEType = "application/json"
                val launcherExport =
                    rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.CreateDocument(
                            mimeType = jsonMIMEType
                        )
                    ) { uri ->
                        if (uri != null) {
                            exportFile(uri)
                        }
                    }
                AnimatedVisibility(visible = isShowingExportDialog) {
                    AlertDialog(
                        onDismissRequest = { isShowingExportDialog = false },
                        title = {
                            Text(text = "Export?")
                        },
                        text = {
                            Text("This will export all your data from the app into a file so you can send it to someone or import it again on a new phone")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    isShowingExportDialog = false
                                    launcherExport.launch("Journal ${Instant.now().getStringOfPattern("dd MMM yyyy")}.json")
                                }
                            ) {
                                Text("Export")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { isShowingExportDialog = false }
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }
                HorizontalDivider()
                var isShowingImportDialog by remember { mutableStateOf(false) }
                SettingsButton(imageVector = Icons.Outlined.FileDownload, text = "Import file") {
                    isShowingImportDialog = true
                }
                val launcherImport =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
                        if (uri != null) {
                            importFile(uri)
                        }
                    }
                AnimatedVisibility(visible = isShowingImportDialog) {
                    AlertDialog(
                        onDismissRequest = { isShowingImportDialog = false },
                        title = {
                            Text(text = "Import file?")
                        },
                        text = {
                            Text("Import a file that was exported before. Note that this will delete the data that you already have in the app.")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    isShowingImportDialog = false
                                    launcherImport.launch(jsonMIMEType)
                                }
                            ) {
                                Text("Import")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { isShowingImportDialog = false }
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }
                HorizontalDivider()
                var isShowingDeleteDialog by remember { mutableStateOf(false) }
                SettingsButton(
                    imageVector = Icons.Outlined.DeleteForever,
                    text = "Delete everything"
                ) {
                    isShowingDeleteDialog = true
                }
                val scope = rememberCoroutineScope()
                AnimatedVisibility(visible = isShowingDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { isShowingDeleteDialog = false },
                        title = {
                            Text(text = "Delete everything?")
                        },
                        text = {
                            Text("This will delete all your experiences, ingestions and custom substances.")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    isShowingDeleteDialog = false
                                    deleteEverything()
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Deleted everything",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
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
            }
            val uriHandler = LocalUriHandler.current
            CardWithTitle(title = "Feedback", innerPaddingHorizontal = 0.dp) {
                SettingsButton(imageVector = Icons.Outlined.QuestionAnswer, text = "FAQ") {
                    navigateToFAQ()
                }
                HorizontalDivider()
                SettingsButton(
                    imageVector = Icons.AutoMirrored.Outlined.ContactSupport,
                    text = "Question / feedback / bug report"
                ) {
                    uriHandler.openUri("https://t.me/+ss8uZhBF6g00MTY8")
                }
                HorizontalDivider()
                SettingsButton(imageVector = Icons.Outlined.VolunteerActivism, text = "Donate") {
                    navigateToDonate()
                }
            }
            CardWithTitle(title = "App", innerPaddingHorizontal = 0.dp) {
                SettingsButton(imageVector = Icons.Outlined.Code, text = "Source Code") {
                    uriHandler.openUri("https://github.com/isaakhanimann/psychonautwiki-journal-android")
                }
                HorizontalDivider()
                val context = LocalContext.current
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, SHARE_APP_URL)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                SettingsButton(imageVector = Icons.Outlined.Share, text = "Share") {
                    context.startActivity(shareIntent)
                }
                HorizontalDivider()
                Text(
                    text = "Version $VERSION_NAME",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .padding(vertical = 10.dp)
                )
            }
        }
    }
}

const val SHARE_APP_URL = "https://psychonautwiki.org/wiki/PsychonautWiki_Journal"


@Composable
fun SettingsButton(imageVector: ImageVector, text: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 2.dp)
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