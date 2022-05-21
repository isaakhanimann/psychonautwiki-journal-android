package com.example.healthassistant.ui.previewproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.experiences.entities.Ingestion
import com.example.healthassistant.data.experiences.entities.IngestionColor
import com.example.healthassistant.data.substances.AdministrationRoute
import java.util.*

class IngestionPreviewProvider : PreviewParameterProvider<Ingestion> {
    override val values: Sequence<Ingestion> = sequenceOf(
        Ingestion(
            substanceName = "MDMA",
            time = Date(),
            administrationRoute = AdministrationRoute.ORAL,
            dose = 90.0,
            isDoseAnEstimate = false,
            units = "mg",
            color = IngestionColor.INDIGO,
            experienceId = 0
        ),
        Ingestion(
            substanceName = "LSD",
            time = Date(),
            administrationRoute = AdministrationRoute.ORAL,
            dose = null,
            isDoseAnEstimate = false,
            units = "mg",
            color = IngestionColor.BLUE,
            experienceId = 0
        )
    )
}