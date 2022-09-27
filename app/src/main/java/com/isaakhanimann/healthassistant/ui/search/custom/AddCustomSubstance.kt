package com.isaakhanimann.healthassistant.ui.search.custom

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun AddCustomSubstancePreview() {
    AddCustomSubstance(navigateBack = {})
}

@Composable
fun AddCustomSubstance(navigateBack: () -> Unit) {
    Text(text = "Add Custom")
}