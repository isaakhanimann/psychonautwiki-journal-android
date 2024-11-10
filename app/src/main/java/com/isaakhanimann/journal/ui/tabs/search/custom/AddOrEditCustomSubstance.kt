/*
 * Copyright (c) 2022. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.search.custom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomSubstanceScreen(
    navigateBack: () -> Unit,
    initialName: String = "",
    viewModel: AddCustomSubstanceViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.name = initialName
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add custom substance") })
        },
        floatingActionButton = {
            if (viewModel.isValid) {
                ExtendedFloatingActionButton(
                    modifier = Modifier.imePadding(),
                    onClick = {
                        viewModel.onDoneTap()
                        navigateBack()
                    },
                    icon = {
                        Icon(
                            Icons.Filled.Done,
                            contentDescription = "Done"
                        )
                    },
                    text = { Text("Done") },
                )
            }
        }
    ) { padding ->
        AddOrEditCustomSubstanceContent(
            padding = padding,
            name = viewModel.name,
            units = viewModel.units,
            description = viewModel.description,
            onNameChange = { viewModel.name = it },
            onUnitsChange = { viewModel.units = it },
            onDescriptionChange = { viewModel.description = it },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCustomSubstanceScreen(
    navigateBack: () -> Unit,
    viewModel: EditCustomSubstanceViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Edit custom substance") }, actions = {
                var isShowingDeleteDialog by remember { mutableStateOf(false) }
                IconButton(
                    onClick = { isShowingDeleteDialog = true },
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete substance",
                    )
                }
                AnimatedVisibility(visible = isShowingDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { isShowingDeleteDialog = false },
                        title = {
                            Text(text = "Delete substance?")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    isShowingDeleteDialog = false
                                    viewModel.deleteCustomSubstance()
                                    navigateBack()
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
            })
        },
        floatingActionButton = {
            if (viewModel.isValid) {
                ExtendedFloatingActionButton(
                    modifier = Modifier.imePadding(),
                    onClick = {
                        viewModel.onDoneTap()
                        navigateBack()
                    },
                    icon = {
                        Icon(
                            Icons.Filled.Done,
                            contentDescription = "Done"
                        )
                    },
                    text = { Text("Done") },
                )
            }
        }
    ) { padding ->
        AddOrEditCustomSubstanceContent(
            padding = padding,
            name = viewModel.name,
            units = viewModel.units,
            description = viewModel.description,
            onNameChange = { viewModel.name = it },
            onUnitsChange = { viewModel.units = it },
            onDescriptionChange = { viewModel.description = it }
        )
    }
}


@Preview
@Composable
fun AddCustomSubstancePreview() {
    AddOrEditCustomSubstanceContent(
        name = "Medication",
        units = "mg",
        description = "My medication has a very long description to see how the text fits into the text field, to make sure it looks good.",
        onNameChange = {},
        onUnitsChange = {},
        onDescriptionChange = {},
        padding = PaddingValues(0.dp)
    )
}

@Composable
private fun AddOrEditCustomSubstanceContent(
    padding: PaddingValues,
    name: String,
    onNameChange: (String) -> Unit,
    units: String,
    onUnitsChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = horizontalPadding)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(5.dp))
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Name") },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = units,
            onValueChange = onUnitsChange,
            label = { Text("Units") },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(onClick = { onUnitsChange("µg") }) {
                Text(text = "µg")
            }
            OutlinedButton(onClick = { onUnitsChange("mg") }) {
                Text(text = "mg")
            }
            OutlinedButton(onClick = { onUnitsChange("g") }) {
                Text(text = "g")
            }
            OutlinedButton(onClick = { onUnitsChange("mL") }) {
                Text(text = "mL")
            }
        }
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Description") },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))

    }
}