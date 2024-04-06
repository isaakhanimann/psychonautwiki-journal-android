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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.isaakhanimann.journal.data.substances.classes.roa.DoseClass
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDose
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.dose.RoaDosePreviewProvider
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.toReadableString


@Preview
@Composable
fun CurrentDoseClassInfoPreview(@PreviewParameter(RoaDosePreviewProvider::class) roaDose: RoaDose) {
    CurrentDoseClassInfo(currentDoseClass = DoseClass.LIGHT, roaDose = roaDose)
}

@Composable
fun CurrentDoseClassInfo(currentDoseClass: DoseClass, roaDose: RoaDose) {
    val doseColor = currentDoseClass.getComposeColor(isSystemInDarkTheme())
    var isShowingDoseClassDialog by remember { mutableStateOf(false) }
    TextButton(
        onClick = {
            isShowingDoseClassDialog = true
        },
    ) {
        Icon(
            Icons.Outlined.Info,
            contentDescription = "Info",
            tint = doseColor
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        val text = when (currentDoseClass) {
            DoseClass.THRESHOLD -> "threshold ${roaDose.lightMin?.toReadableString()} ${roaDose.units}"
            DoseClass.LIGHT -> "light ${roaDose.lightMin?.toReadableString()}-${roaDose.commonMin?.toReadableString()} ${roaDose.units}"
            DoseClass.COMMON -> "common ${roaDose.commonMin?.toReadableString()}-${roaDose.strongMin?.toReadableString()} ${roaDose.units}"
            DoseClass.STRONG -> "strong ${roaDose.strongMin?.toReadableString()}-${roaDose.heavyMin?.toReadableString()} ${roaDose.units}"
            DoseClass.HEAVY -> "heavy ${roaDose.heavyMin?.toReadableString()} ${roaDose.units}-.."
        }
        Text(
            text = text,
            color = doseColor,
            style = MaterialTheme.typography.labelLarge
        )
    }
    AnimatedVisibility(visible = isShowingDoseClassDialog) {
        AlertDialog(
            onDismissRequest = { isShowingDoseClassDialog = false },
            title = {
                Text(
                    text = "${currentDoseClass.name} DOSAGE",
                    style = MaterialTheme.typography.titleLarge,
                )
            },
            text = {
                Text(
                    text = currentDoseClass.description,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            confirmButton = {
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { isShowingDoseClassDialog = false }
                ) {
                    Text("Dismiss")
                }
            },
        )
    }
}