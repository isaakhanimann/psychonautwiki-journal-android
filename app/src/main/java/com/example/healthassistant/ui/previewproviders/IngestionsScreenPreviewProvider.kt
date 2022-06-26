package com.example.healthassistant.ui.previewproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.room.experiences.entities.IngestionColor
import com.example.healthassistant.data.substances.AdministrationRoute
import java.util.*

class IngestionsScreenPreviewProvider : PreviewParameterProvider<Map<String, List<Ingestion>>> {
    override val values: Sequence<Map<String, List<Ingestion>>> = sequenceOf(
        mapOf(
            Pair(
                first = "2022", second = listOf(
                    Ingestion(
                        substanceName = "MDMA",
                        time = Date(),
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = 90.0,
                        isDoseAnEstimate = false,
                        units = "mg",
                        color = IngestionColor.INDIGO,
                        experienceId = 0,
                        notes = "This is one note"
                    ),
                    Ingestion(
                        substanceName = "LSD",
                        time = Date(),
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = null,
                        isDoseAnEstimate = false,
                        units = "mg",
                        color = IngestionColor.BLUE,
                        experienceId = 0,
                        notes = null
                    )
                )
            )
        )

    )
}