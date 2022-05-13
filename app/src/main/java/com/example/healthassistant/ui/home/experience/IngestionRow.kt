package com.example.healthassistant.ui.home.experience

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.healthassistant.data.experiences.entities.Ingestion

@Preview
@Composable
fun IngestionRow(
    @PreviewParameter(IngestionPreviewProvider::class) ingestion: Ingestion
) {
    Text(text = ingestion.substanceName)
}