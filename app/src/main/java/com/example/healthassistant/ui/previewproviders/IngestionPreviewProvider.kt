package com.example.healthassistant.ui.previewproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.substances.AdministrationRoute
import java.util.*

class IngestionPreviewProvider : PreviewParameterProvider<Ingestion> {
    override val values: Sequence<Ingestion> = sequenceOf(
        // todo: add color
        Ingestion(
            substanceName = "MDMA",
            time = Date(),
            administrationRoute = AdministrationRoute.ORAL,
            dose = 90.0,
            isDoseAnEstimate = false,
            units = "mg",
            experienceId = 0,
            notes = "This is a note"
        ),
        Ingestion(
            substanceName = "LSD",
            time = Date(),
            administrationRoute = AdministrationRoute.ORAL,
            dose = null,
            isDoseAnEstimate = false,
            units = "mg",
            experienceId = 0,
            notes = null
        )
    )
}