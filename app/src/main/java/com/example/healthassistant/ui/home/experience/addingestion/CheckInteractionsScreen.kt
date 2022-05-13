package com.example.healthassistant.ui.home.experience.addingestion

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.ui.previewproviders.SubstancePreviewProvider

@Preview
@Composable
fun CheckInteractionsScreen(@PreviewParameter(SubstancePreviewProvider::class) substance: Substance) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Check Interactions") }) }
    ) {
        Text(text = "List all interactions of ${substance.name} here including the loaded once")
    }
}