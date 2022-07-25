package com.isaakhanimann.healthassistant.ui.experiences

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.healthassistant.data.room.experiences.entities.*
import com.isaakhanimann.healthassistant.data.room.experiences.relations.ExperienceWithIngestionsAndCompanions
import com.isaakhanimann.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import com.isaakhanimann.healthassistant.ui.utils.getDate

class ExperiencesScreenPreviewProvider :
    PreviewParameterProvider<Map<String, List<ExperienceWithIngestionsAndCompanions>>> {
    override val values: Sequence<Map<String, List<ExperienceWithIngestionsAndCompanions>>> =
        sequenceOf(
            mapOf(
                Pair(
                    first = "2022", second = listOf(
                        ExperienceWithIngestionsAndCompanions(
                            experience = Experience(
                                id = 0,
                                title = "Festival",
                                text = "Some notes",
                                sentiment = Sentiment.SATISFIED
                            ),
                            ingestionsWithCompanions = listOf(
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
                                        notes = null,
                                        sentiment = Sentiment.SATISFIED
                                    ),
                                    substanceCompanion = SubstanceCompanion(
                                        substanceName = "MDMA",
                                        color = SubstanceColor.PINK
                                    )
                                ),
                                IngestionWithCompanion(
                                    ingestion = Ingestion(
                                        substanceName = "Cocaine",
                                        time = getDate(
                                            year = 2022,
                                            month = 7,
                                            day = 5,
                                            hourOfDay = 14,
                                            minute = 20
                                        )!!,
                                        administrationRoute = AdministrationRoute.INSUFFLATED,
                                        dose = 30.0,
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
                                IngestionWithCompanion(
                                    ingestion = Ingestion(
                                        substanceName = "Ketamine",
                                        time = getDate(
                                            year = 2022,
                                            month = 7,
                                            day = 5,
                                            hourOfDay = 14,
                                            minute = 20
                                        )!!,
                                        administrationRoute = AdministrationRoute.INSUFFLATED,
                                        dose = 50.0,
                                        isDoseAnEstimate = false,
                                        units = "mg",
                                        experienceId = 0,
                                        notes = null,
                                        sentiment = Sentiment.SATISFIED
                                    ),
                                    substanceCompanion = SubstanceCompanion(
                                        substanceName = "Ketamine",
                                        color = SubstanceColor.MINT
                                    )
                                )
                            )
                        ),
                        ExperienceWithIngestionsAndCompanions(
                            experience = Experience(
                                id = 0,
                                title = "Bachelor Party",
                                text = "Some notes",
                                sentiment = Sentiment.VERY_SATISFIED
                            ),
                            ingestionsWithCompanions = listOf(
                                IngestionWithCompanion(
                                    ingestion = Ingestion(
                                        substanceName = "MDMA",
                                        time = getDate(
                                            year = 2022,
                                            month = 6,
                                            day = 21,
                                            hourOfDay = 12,
                                            minute = 20
                                        )!!,
                                        administrationRoute = AdministrationRoute.ORAL,
                                        dose = 90.0,
                                        isDoseAnEstimate = false,
                                        units = "mg",
                                        experienceId = 0,
                                        notes = null,
                                        sentiment = Sentiment.SATISFIED
                                    ),
                                    substanceCompanion = SubstanceCompanion(
                                        substanceName = "MDMA",
                                        color = SubstanceColor.PINK
                                    )
                                )
                            )
                        )

                    )
                ),
                Pair(
                    first = "2021", second = listOf(
                        ExperienceWithIngestionsAndCompanions(
                            experience = Experience(
                                id = 0,
                                title = "Liam's Birthday",
                                text = "Some notes",
                                sentiment = Sentiment.SATISFIED
                            ),
                            ingestionsWithCompanions = listOf(
                                IngestionWithCompanion(
                                    ingestion = Ingestion(
                                        substanceName = "Cocaine",
                                        time = getDate(
                                            year = 2021,
                                            month = 9,
                                            day = 2,
                                            hourOfDay = 18,
                                            minute = 13
                                        )!!,
                                        administrationRoute = AdministrationRoute.INSUFFLATED,
                                        dose = 30.0,
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
                                IngestionWithCompanion(
                                    ingestion = Ingestion(
                                        substanceName = "Ketamine",
                                        time = getDate(
                                            year = 2021,
                                            month = 9,
                                            day = 2,
                                            hourOfDay = 18,
                                            minute = 13
                                        )!!,
                                        administrationRoute = AdministrationRoute.INSUFFLATED,
                                        dose = 20.0,
                                        isDoseAnEstimate = false,
                                        units = "mg",
                                        experienceId = 0,
                                        notes = null,
                                        sentiment = Sentiment.SATISFIED
                                    ),
                                    substanceCompanion = SubstanceCompanion(
                                        substanceName = "Ketamine",
                                        color = SubstanceColor.MINT
                                    )
                                )

                            )
                        ),
                        ExperienceWithIngestionsAndCompanions(
                            experience = Experience(
                                id = 0,
                                title = "Last day in Stockholm",
                                text = "Some notes",
                                sentiment = Sentiment.DISSATISFIED
                            ),
                            ingestionsWithCompanions = listOf(
                                IngestionWithCompanion(
                                    ingestion = Ingestion(
                                        substanceName = "LSD",
                                        time = getDate(
                                            year = 2021,
                                            month = 7,
                                            day = 22,
                                            hourOfDay = 18,
                                            minute = 13
                                        )!!,
                                        administrationRoute = AdministrationRoute.ORAL,
                                        dose = 90.0,
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
                                IngestionWithCompanion(
                                    ingestion = Ingestion(
                                        substanceName = "Cocaine",
                                        time = getDate(
                                            year = 2021,
                                            month = 7,
                                            day = 22,
                                            hourOfDay = 18,
                                            minute = 13
                                        )!!,
                                        administrationRoute = AdministrationRoute.INSUFFLATED,
                                        dose = 20.0,
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
                                )
                            )
                        )
                    )
                )
            ),
            mapOf()
        )
}