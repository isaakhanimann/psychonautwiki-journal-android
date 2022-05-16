package com.example.healthassistant.ui.previewproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.experiences.entities.Ingestion
import com.example.healthassistant.data.experiences.entities.IngestionColor
import java.util.*

class IngestionPreviewProvider : PreviewParameterProvider<Ingestion> {
    override val values: Sequence<Ingestion> = sequenceOf(
        Ingestion(
            substanceName = "MDMA",
            time = Date(),
            administrationRoute = "oral",
            dose = 90.0,
            isDoseAnEstimate = false,
            units = "mg",
            color = IngestionColor.INDIGO,
            experienceId = 0
        )
    )
}