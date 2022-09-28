package com.isaakhanimann.healthassistant.ui.search.custom

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AddCustomSubstance(
    navigateBack: () -> Unit,
    viewModel: AddCustomViewModel = hiltViewModel()
) {
    AddCustomSubstance(
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
        isDoneEnabled = viewModel.isValid
    )
}

@Preview
@Composable
fun AddCustomSubstancePreview() {
    AddCustomSubstance(
        name = "Medication",
        units = "mg",
        description = "My medication has a very long description to see how the text fits into the textfield, to make sure it looks good.",
        onNameChange = {},
        onUnitsChange = {},
        onDescriptionChange = {},
        onDoneTap = {},
        isDoneEnabled = true
    )
}

@Composable
fun AddCustomSubstance(
    name: String,
    units: String,
    description: String,
    onNameChange: (String) -> Unit,
    onUnitsChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDoneTap: () -> Unit,
    isDoneEnabled: Boolean
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Add Custom Substance") })
        },
        floatingActionButton = {
            if (isDoneEnabled) {
                ExtendedFloatingActionButton(
                    onClick = {
                        onDoneTap()
                        Toast.makeText(
                            context,
                            "Custom Substance Added",
                            Toast.LENGTH_SHORT
                        ).show()
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
    ) {
        Column(modifier = Modifier.padding(horizontal = 5.dp, vertical = 10.dp)) {
            val focusManager = LocalFocusManager.current
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Name") },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}