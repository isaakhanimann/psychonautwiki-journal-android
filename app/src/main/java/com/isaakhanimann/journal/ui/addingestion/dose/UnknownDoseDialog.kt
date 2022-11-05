package com.isaakhanimann.journal.ui.addingestion.dose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun UnknownDoseDialogPreview() {
    UnknownDoseDialog(
        useUnknownDoseAndNavigate = {},
        dismiss = {}
    )
}

@Composable
fun UnknownDoseDialog(
    useUnknownDoseAndNavigate: () -> Unit,
    dismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = dismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Warning, contentDescription = "Warning")
                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                Text(text = "Unknown Danger", style = MaterialTheme.typography.headlineSmall)
            }
        },
        text = {
            Text(
                "Administering the wrong dosage of a substance can lead to negative experiences such as extreme anxiety, uncomfortable physical side effects, hospitalization, or (in extreme cases) death.\n" +
                        "Read the dosage guide."
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    dismiss()
                    useUnknownDoseAndNavigate()
                }
            ) {
                Text("Continue")
            }
        },
        dismissButton = {
            TextButton(
                onClick = dismiss
            ) {
                Text("Cancel")
            }
        }
    )
}