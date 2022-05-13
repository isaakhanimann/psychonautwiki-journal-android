package com.example.healthassistant.ui.previewproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.experiences.entities.Ingestion
import java.util.*

class IngestionPreviewProvider : PreviewParameterProvider<Ingestion> {
    override val values: Sequence<Ingestion> = sequenceOf(
        Ingestion(
            substanceName = "MDMA",
            time = Date(),
            administrationRoute = "oral",
            dose = 90.0,
            units = "mg",
            color = "purple",
            experienceId = 0
        )
    )
}