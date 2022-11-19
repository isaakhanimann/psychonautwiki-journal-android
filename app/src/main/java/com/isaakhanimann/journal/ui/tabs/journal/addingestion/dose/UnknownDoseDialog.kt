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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.dose

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
            Text(text = "Don't know the dose?", style = MaterialTheme.typography.headlineSmall)
        },
        text = {
            Text(
                "You can log an unknown dose. But note that administering the wrong dosage of a substance can lead to negative experiences such as extreme anxiety, uncomfortable physical side effects, hospitalization, or (in extreme cases) death."
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    dismiss()
                    useUnknownDoseAndNavigate()
                }
            ) {
                Text("Log Unknown Dose")
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