package com.isaakhanimann.healthassistant.ui.addingestion.dose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import com.isaakhanimann.healthassistant.ui.theme.horizontalPadding

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
                color = MaterialTheme.colors.error,
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