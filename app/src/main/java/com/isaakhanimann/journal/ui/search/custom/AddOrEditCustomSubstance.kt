package com.isaakhanimann.journal.ui.search.custom

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AddCustomSubstance(
    navigateBack: () -> Unit,
    viewModel: AddCustomViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    AddOrEditCustomSubstance(
        name = viewModel.name,
        units = viewModel.units,
        description = viewModel.description,
        onNameChange = { viewModel.name = it },
        onUnitsChange = { viewModel.units = it },
        onDescriptionChange = { viewModel.description = it },
        onDoneTap = {
            viewModel.onDoneTap()
            Toast.makeText(
                context,
                "Custom Substance Added",
                Toast.LENGTH_SHORT
            ).show()
            navigateBack()
        },
        isDoneEnabled = viewModel.isValid,
        title = "Add Custom Substance",
        isShowingDelete = false,
        deleteAndNavigate = {}
    )
}

@Composable
fun EditCustomSubstance(
    navigateBack: () -> Unit,
    viewModel: EditCustomViewModel = hiltViewModel()
) {
    AddOrEditCustomSubstance(
        name = viewModel.name,
        units = viewModel.units,
        description = viewModel.description,
        onNameChange = { viewModel.name = it },
        onUnitsChange = { viewModel.units = it },
        onDescriptionChange = { viewModel.description = it },
        onDoneTap = {
            viewModel.onDoneTap()
            navigateBack()
        },
        isDoneEnabled = viewModel.isValid,
        title = "Edit Custom Substance",
        isShowingDelete = true,
        deleteAndNavigate = {
            viewModel.deleteCustomSubstance()
            navigateBack()
        }
    )
}


@Preview
@Composable
fun AddCustomSubstancePreview() {
    AddOrEditCustomSubstance(
        name = "Medication",
        units = "mg",
        description = "My medication has a very long description to see how the text fits into the text field, to make sure it looks good.",
        onNameChange = {},
        onUnitsChange = {},
        onDescriptionChange = {},
        onDoneTap = {},
        isDoneEnabled = true,
        title = "Add Custom Substance",
        isShowingDelete = false,
        deleteAndNavigate = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrEditCustomSubstance(
    name: String,
    units: String,
    description: String,
    onNameChange: (String) -> Unit,
    onUnitsChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDoneTap: () -> Unit,
    isDoneEnabled: Boolean,
    title: String,
    isShowingDelete: Boolean,
    deleteAndNavigate: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(title) })
        },
        floatingActionButton = {
            if (isDoneEnabled) {
                ExtendedFloatingActionButton(
                    onClick = onDoneTap,
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
        Column(modifier = Modifier
            .padding(padding)
            .padding(horizontal = 5.dp, vertical = 10.dp)) {
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
                OutlinedButton(onClick = { onUnitsChange("ml") }) {
                    Text(text = "ml")
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
            if (isShowingDelete) {
                var isShowingDeleteDialog by remember { mutableStateOf(false) }
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = { isShowingDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete Substance")
                }
                if (isShowingDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { isShowingDeleteDialog = false },
                        title = {
                            Text(text = "Delete Substance?")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    isShowingDeleteDialog = false
                                    deleteAndNavigate()
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
        }
    }
}