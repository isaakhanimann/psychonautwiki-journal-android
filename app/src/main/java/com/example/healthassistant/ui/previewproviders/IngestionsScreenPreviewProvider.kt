package com.example.healthassistant.ui.previewproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.room.experiences.entities.SubstanceColor
import com.example.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.example.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.example.healthassistant.data.substances.AdministrationRoute

class IngestionsScreenPreviewProvider :
    PreviewParameterProvider<Map<String, List<IngestionWithCompanion>>> {
    override val values: Sequence<Map<String, List<IngestionWithCompanion>>> = sequenceOf(
        mapOf(
            Pair(
                first = "2022", second = listOf(
                    IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "MDMA",
                            time = getDate(
                                year = 2022,
                                month = 7,
                                day = 5,
                                hourOfDay = 14,
                                minute = 20
                            )!!,
                            administrationRoute = AdministrationRoute.ORAL,
                            dose = 90.0,
                            isDoseAnEstimate = false,
                            units = "mg",
                            experienceId = 0,
                            notes = "This is one note"
                        ),
                        substanceCompanion = SubstanceCompanion(
                            substanceName = "MDMA",
                            color = SubstanceColor.PINK
                        )
                    ),
                    IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "LSD",
                            time = getDate(
                                year = 2022,
                                month = 7,
                                day = 5,
                                hourOfDay = 12,
                                minute = 30
                            )!!,
                            administrationRoute = AdministrationRoute.ORAL,
                            dose = null,
                            isDoseAnEstimate = false,
                            units = "µg",
                            experienceId = 0,
                            notes = null
                        ),
                        substanceCompanion = SubstanceCompanion(
                            substanceName = "LSD",
                            color = SubstanceColor.PURPLE
                        )
                    ),
                    IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "LSD",
                            time = getDate(
                                year = 2022,
                                month = 6,
                                day = 3,
                                hourOfDay = 9,
                                minute = 5
                            )!!,
                            administrationRoute = AdministrationRoute.ORAL,
                            dose = null,
                            isDoseAnEstimate = false,
                            units = "µg",
                            experienceId = 0,
                            notes = "Note"
                        ),
                        substanceCompanion = SubstanceCompanion(
                            substanceName = "LSD",
                            color = SubstanceColor.PURPLE
                        )
                    ),
                    IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "LSD",
                            time = getDate(
                                year = 2022,
                                month = 3,
                                day = 5,
                                hourOfDay = 11,
                                minute = 20
                            )!!,
                            administrationRoute = AdministrationRoute.ORAL,
                            dose = null,
                            isDoseAnEstimate = false,
                            units = "µg",
                            experienceId = 0,
                            notes = null
                        ),
                        substanceCompanion = SubstanceCompanion(
                            substanceName = "LSD",
                            color = SubstanceColor.PURPLE
                        )
                    ),
                    IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "LSD",
                            time = getDate(
                                year = 2022,
                                month = 1,
                                day = 5,
                                hourOfDay = 23,
                                minute = 32
                            )!!,
                            administrationRoute = AdministrationRoute.ORAL,
                            dose = null,
                            isDoseAnEstimate = false,
                            units = "µg",
                            experienceId = 0,
                            notes = null
                        ),
                        substanceCompanion = SubstanceCompanion(
                            substanceName = "LSD",
                            color = SubstanceColor.PURPLE
                        )
                    ),
                )
            ),
            Pair(
                first = "2021", second = listOf(
                    IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "MDMA",
                            time = getDate(
                                year = 2021,
                                month = 7,
                                day = 5,
                                hourOfDay = 14,
                                minute = 20
                            )!!,
                            administrationRoute = AdministrationRoute.ORAL,
                            dose = 90.0,
                            isDoseAnEstimate = false,
                            units = "mg",
                            experienceId = 0,
                            notes = "This is one note"
                        ),
                        substanceCompanion = SubstanceCompanion(
                            substanceName = "MDMA",
                            color = SubstanceColor.PINK
                        )
                    ),
                    IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "LSD",
                            time = getDate(
                                year = 2021,
                                month = 7,
                                day = 5,
                                hourOfDay = 12,
                                minute = 30
                            )!!,
                            administrationRoute = AdministrationRoute.ORAL,
                            dose = null,
                            isDoseAnEstimate = false,
                            units = "µg",
                            experienceId = 0,
                            notes = null
                        ),
                        substanceCompanion = SubstanceCompanion(
                            substanceName = "LSD",
                            color = SubstanceColor.PURPLE
                        )
                    ),
                    IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "Ketamine",
                            time = getDate(
                                year = 2021,
                                month = 6,
                                day = 3,
                                hourOfDay = 9,
                                minute = 5
                            )!!,
                            administrationRoute = AdministrationRoute.ORAL,
                            dose = 80.0,
                            isDoseAnEstimate = true,
                            units = "mg",
                            experienceId = 0,
                            notes = "Note"
                        ),
                        substanceCompanion = SubstanceCompanion(
                            substanceName = "Ketamine",
                            color = SubstanceColor.MINT
                        )
                    ),
                    IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "Cocaine",
                            time = getDate(
                                year = 2021,
                                month = 3,
                                day = 5,
                                hourOfDay = 11,
                                minute = 20
                            )!!,
                            administrationRoute = AdministrationRoute.ORAL,
                            dose = 50.0,
                            isDoseAnEstimate = false,
                            units = "mg",
                            experienceId = 0,
                            notes = null
                        ),
                        substanceCompanion = SubstanceCompanion(
                            substanceName = "Cocaine",
                            color = SubstanceColor.BLUE
                        )
                    ),
                    IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "LSD",
                            time = getDate(
                                year = 2021,
                                month = 1,
                                day = 5,
                                hourOfDay = 23,
                                minute = 32
                            )!!,
                            administrationRoute = AdministrationRoute.ORAL,
                            dose = 100.0,
                            isDoseAnEstimate = false,
                            units = "µg",
                            experienceId = 0,
                            notes = null
                        ),
                        substanceCompanion = SubstanceCompanion(
                            substanceName = "LSD",
                            color = SubstanceColor.PURPLE
                        )
                    ),
                )
            )
        ),
        mapOf()
    )
}