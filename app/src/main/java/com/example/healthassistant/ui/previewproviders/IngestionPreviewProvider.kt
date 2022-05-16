package com.example.healthassistant.ui.previewproviders

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
            isDoseAnEstimate = false,
            units = "mg",
            colorArgb = Color.Blue.toArgb(),
            experienceId = 0
        )
    )
}