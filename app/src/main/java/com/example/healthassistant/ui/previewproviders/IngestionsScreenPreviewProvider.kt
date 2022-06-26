package com.example.healthassistant.ui.previewproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.room.experiences.entities.IngestionColor
import com.example.healthassistant.data.substances.AdministrationRoute

class IngestionsScreenPreviewProvider : PreviewParameterProvider<Map<String, List<Ingestion>>> {
    override val values: Sequence<Map<String, List<Ingestion>>> = sequenceOf(
        mapOf(
            Pair(
                first = "2022", second = listOf(
                    Ingestion(
                        substanceName = "MDMA",
                        time = getDate(year = 2022, month = 7, day = 5, hourOfDay = 14, minute = 20)!!,
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = 90.0,
                        isDoseAnEstimate = false,
                        units = "mg",
                        color = IngestionColor.PINK,
                        experienceId = 0,
                        notes = "This is one note"
                    ),
                    Ingestion(
                        substanceName = "LSD",
                        time = getDate(year = 2022, month = 7, day = 5, hourOfDay = 12, minute = 30)!!,
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = null,
                        isDoseAnEstimate = false,
                        units = "µg",
                        color = IngestionColor.BLUE,
                        experienceId = 0,
                        notes = null
                    ),
                    Ingestion(
                        substanceName = "LSD",
                        time = getDate(year = 2022, month = 6, day = 3, hourOfDay = 9, minute = 5)!!,
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = null,
                        isDoseAnEstimate = false,
                        units = "µg",
                        color = IngestionColor.BLUE,
                        experienceId = 0,
                        notes = "Note"
                    ),
                    Ingestion(
                        substanceName = "LSD",
                        time = getDate(year = 2022, month = 3, day = 5, hourOfDay = 11, minute = 20)!!,
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = null,
                        isDoseAnEstimate = false,
                        units = "µg",
                        color = IngestionColor.BLUE,
                        experienceId = 0,
                        notes = null
                    ),
                    Ingestion(
                        substanceName = "LSD",
                        time = getDate(year = 2022, month = 1, day = 5, hourOfDay = 23, minute = 32)!!,
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = null,
                        isDoseAnEstimate = false,
                        units = "µg",
                        color = IngestionColor.BLUE,
                        experienceId = 0,
                        notes = null
                    )
                )
            ),
            Pair(
                first = "2021", second = listOf(
                    Ingestion(
                        substanceName = "MDMA",
                        time = getDate(year = 2021, month = 7, day = 5, hourOfDay = 14, minute = 20)!!,
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
                        time = getDate(year = 2021, month = 7, day = 5, hourOfDay = 12, minute = 30)!!,
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = null,
                        isDoseAnEstimate = false,
                        units = "µg",
                        color = IngestionColor.BLUE,
                        experienceId = 0,
                        notes = null
                    ),
                    Ingestion(
                        substanceName = "Ketamine",
                        time = getDate(year = 2021, month = 6, day = 3, hourOfDay = 9, minute = 5)!!,
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = 80.0,
                        isDoseAnEstimate = true,
                        units = "mg",
                        color = IngestionColor.GREEN,
                        experienceId = 0,
                        notes = "Note"
                    ),
                    Ingestion(
                        substanceName = "Cocaine",
                        time = getDate(year = 2021, month = 3, day = 5, hourOfDay = 11, minute = 20)!!,
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = 50.0,
                        isDoseAnEstimate = false,
                        units = "mg",
                        color = IngestionColor.YELLOW,
                        experienceId = 0,
                        notes = null
                    ),
                    Ingestion(
                        substanceName = "LSD",
                        time = getDate(year = 2021, month = 1, day = 5, hourOfDay = 23, minute = 32)!!,
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = 100.0,
                        isDoseAnEstimate = false,
                        units = "µg",
                        color = IngestionColor.BLUE,
                        experienceId = 0,
                        notes = null
                    )
                )
            )
        ),
        mapOf()
    )
}