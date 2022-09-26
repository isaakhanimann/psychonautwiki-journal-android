package com.isaakhanimann.healthassistant.ui.journal.experience.editingestion

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.ui.theme.HealthAssistantTheme


@Composable
fun EditIngestionScreen(
    viewModel: EditIngestionViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    EditIngestionScreen(
        note = viewModel.note,
        isEstimate = viewModel.isEstimate,
        navigateBack = navigateBack,
        deleteIngestion = viewModel::deleteIngestion,
        onDone = {
            viewModel.onDoneTap()
            navigateBack()
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditIngestionScreenPreview() {
    HealthAssistantTheme {
        EditIngestionScreen(
            note = "This is my note",
            isEstimate = false,
            navigateBack = {},
            deleteIngestion = {},
            onDone = {}
        )
    }
}

@Composable
fun EditIngestionScreen(
    note: String,
    isEstimate: Boolean,
    navigateBack: () -> Unit,
    deleteIngestion: () -> Unit,
    onDone: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit Ingestion") }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onDone,
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
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Divider()
            TextField(value = note, onValueChange = {})
            Divider()
            Checkbox(checked = isEstimate, onCheckedChange = {})
            Divider()
            var isShowingDeleteDialog by remember { mutableStateOf(false) }

            TextButton(
                onClick = { isShowingDeleteDialog = true },
            ) {
                Text("Delete Ingestion", style = MaterialTheme.typography.caption)
            }
            if (isShowingDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { isShowingDeleteDialog = false },
                    title = {
                        Text(text = "Delete Ingestion?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                isShowingDeleteDialog = false
                                deleteIngestion()
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
        }
    }
}