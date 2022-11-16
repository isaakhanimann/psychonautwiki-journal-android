/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.dose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@OptIn(ExperimentalMaterial3Api::class)
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
            onValueChange = onPurityChange,
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
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Converted Amount")
                Text(text = convertedDoseAndUnitText)
            }
        }
    }
}