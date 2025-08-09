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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.isaakhanimann.journal.R

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
            Text(text = stringResource(R.string.don_t_know_the_dose), style = MaterialTheme.typography.headlineSmall)
        },
        text = {
            Text(
                stringResource(R.string.dont_know_the_dose_description)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    dismiss()
                    useUnknownDoseAndNavigate()
                }
            ) {
                Text(stringResource(R.string.log_unknown_dose))
            }
        },
        dismissButton = {
            TextButton(
                onClick = dismiss
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}