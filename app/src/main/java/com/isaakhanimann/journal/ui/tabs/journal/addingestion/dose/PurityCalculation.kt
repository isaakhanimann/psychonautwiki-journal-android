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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun PurityCalculation(
    purityText: String,
    onPurityChange: (purity: String) -> Unit,
    isValidPurity: Boolean,
    convertedDoseAndUnitText: String?
) {
    Column(horizontalAlignment = Alignment.Start) {
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            value = purityText,
            onValueChange = {
                onPurityChange(it.replace(oldChar = ',', newChar = '.'))
            },
            label = { Text("Purity") },
            isError = !isValidPurity,
            trailingIcon = {
                Text(
                    text = "%",
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                )
            },
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (!isValidPurity) {
            Text(
                text = "Purity must be between 1 and 100%",
                color = MaterialTheme.colorScheme.error,
            )
        }
        if (convertedDoseAndUnitText != null) {
            val textStyle = MaterialTheme.typography.titleMedium
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Impure dose", style = textStyle)
                Text(text = convertedDoseAndUnitText, style = textStyle)
            }
        }
    }
}