package com.isaakhanimann.healthassistant.ui.ingestions

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Sentiment
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import com.isaakhanimann.healthassistant.data.substances.DoseClass
import com.isaakhanimann.healthassistant.ui.utils.getDate

class IngestionsScreenPreviewProvider :
    PreviewParameterProvider<Map<String, List<IngestionsViewModel.IngestionElement>>> {
    override val values: Sequence<Map<String, List<IngestionsViewModel.IngestionElement>>> = sequenceOf(
        mapOf(
            Pair(
                first = "2022", second = listOf(
                    IngestionsViewModel.IngestionElement(
                        ingestionWithCompanion = IngestionWithCompanion(
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
                                notes = "This is one note",
                                sentiment = Sentiment.SATISFIED
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "MDMA",
                                color = SubstanceColor.PINK
                            )
                        ),
                        doseClass = DoseClass.COMMON
                    ),
                    IngestionsViewModel.IngestionElement(
                        ingestionWithCompanion = IngestionWithCompanion(
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
                                notes = null,
                                sentiment = Sentiment.NEUTRAL
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "LSD",
                                color = SubstanceColor.PURPLE
                            )
                        ),
                        doseClass = null
                    ),
                    IngestionsViewModel.IngestionElement(
                        ingestionWithCompanion = IngestionWithCompanion(
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
                                notes = "Note",
                                sentiment = null
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "LSD",
                                color = SubstanceColor.PURPLE
                            )
                        ),
                        doseClass = null
                    ),
                    IngestionsViewModel.IngestionElement(
                        ingestionWithCompanion = IngestionWithCompanion(
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
                                notes = null,
                                sentiment = Sentiment.SATISFIED
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "LSD",
                                color = SubstanceColor.PURPLE
                            )
                        ),
                        doseClass = null
                    ),
                    IngestionsViewModel.IngestionElement(
                        ingestionWithCompanion = IngestionWithCompanion(
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
                                notes = null,
                                sentiment = Sentiment.SATISFIED
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "LSD",
                                color = SubstanceColor.PURPLE
                            )
                        ),
                        doseClass = null
                    ),
                )
            ),
            Pair(
                first = "2021", second = listOf(
                    IngestionsViewModel.IngestionElement(
                        ingestionWithCompanion = IngestionWithCompanion(
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
                                notes = "This is one note",
                                sentiment = Sentiment.VERY_SATISFIED
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "MDMA",
                                color = SubstanceColor.PINK
                            )
                        ),
                        doseClass = DoseClass.COMMON
                    ),
                    IngestionsViewModel.IngestionElement(
                        ingestionWithCompanion = IngestionWithCompanion(
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
                                notes = null,
                                sentiment = Sentiment.DISSATISFIED
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "LSD",
                                color = SubstanceColor.PURPLE
                            )
                        ),
                        doseClass = null
                    ),
                    IngestionsViewModel.IngestionElement(
                        ingestionWithCompanion = IngestionWithCompanion(
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
                                notes = "Note",
                                sentiment = Sentiment.SATISFIED
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "Ketamine",
                                color = SubstanceColor.MINT
                            )
                        ),
                        doseClass = DoseClass.STRONG
                    ),
                    IngestionsViewModel.IngestionElement(
                        ingestionWithCompanion = IngestionWithCompanion(
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
                                notes = null,
                                sentiment = Sentiment.SATISFIED
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "Cocaine",
                                color = SubstanceColor.BLUE
                            )
                        ),
                        doseClass = DoseClass.COMMON
                    ),
                    IngestionsViewModel.IngestionElement(
                        ingestionWithCompanion = IngestionWithCompanion(
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
                                dose = 500.0,
                                isDoseAnEstimate = false,
                                units = "µg",
                                experienceId = 0,
                                notes = null,
                                sentiment = Sentiment.SATISFIED
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "LSD",
                                color = SubstanceColor.PURPLE
                            )
                        ),
                        doseClass = DoseClass.HEAVY
                    ),
                )
            )
        ),
        mapOf()
    )
}