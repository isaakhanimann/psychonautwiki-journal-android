package com.example.healthassistant.ui.ingestions.ingestion.edit.note

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun EditIngestionNoteScreen(
    navigateBack: () -> Unit,
    viewModel: EditIngestionNoteViewModel = hiltViewModel()
) {
    EditIngestionNoteScreen(
        onDoneTap = {
            viewModel.onDoneTap()
            navigateBack()
        },
        note = viewModel.note,
        onNoteChange = { viewModel.note = it }
    )
}

@Preview
@Composable
fun EditIngestionNoteScreenPreview() {
    EditIngestionNoteScreen(
        onDoneTap = {},
        note = "Here are some sample notes",
        onNoteChange = {}
    )
}

@Composable
fun EditIngestionNoteScreen(
    onDoneTap: () -> Unit,
    note: String,
    onNoteChange: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit Note") }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onDoneTap,
                icon = {
                    Icon(
                        Icons.Filled.Done,
                        contentDescription = "Done Icon"
                    )
                },
                text = { Text("Done") }
            )
        }
    ) {
        OutlinedTextField(
            value = note,
            onValueChange = onNoteChange,
            label = { Text(text = "Note", style = MaterialTheme.typography.subtitle1) },
            keyboardActions = KeyboardActions(onDone = { onDoneTap() }),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier.padding(10.dp).fillMaxSize()
        )
    }
}