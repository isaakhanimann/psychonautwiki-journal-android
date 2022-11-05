package com.isaakhanimann.journal.ui.journal

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.Experience
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.journal.data.room.experiences.relations.ExperienceWithIngestionsAndCompanions
import com.isaakhanimann.journal.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.utils.getInstant

class JournalScreenPreviewProvider :
    PreviewParameterProvider<List<ExperienceWithIngestionsAndCompanions>> {
    override val values: Sequence<List<ExperienceWithIngestionsAndCompanions>> =
        sequenceOf(
            listOf(
                ExperienceWithIngestionsAndCompanions(
                    experience = Experience(
                        id = 0,
                        title = "Festival",
                        text = "Some notes",
                        isFavorite = true,
                        sortDate = getInstant(
                            year = 2022,
                            month = 7,
                            day = 5,
                            hourOfDay = 14,
                            minute = 20
                        )!!,
                    ),
                    ingestionsWithCompanions = listOf(
                        IngestionWithCompanion(
                            ingestion = Ingestion(
                                substanceName = "MDMA",
                                time = getInstant(
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
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "MDMA",
                                color = AdaptiveColor.PINK
                            )
                        ),
                        IngestionWithCompanion(
                            ingestion = Ingestion(
                                substanceName = "Cocaine",
                                time = getInstant(
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
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "Cocaine",
                                color = AdaptiveColor.BLUE
                            )
                        ),
                        IngestionWithCompanion(
                            ingestion = Ingestion(
                                substanceName = "Ketamine",
                                time = getInstant(
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
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "Ketamine",
                                color = AdaptiveColor.MINT
                            )
                        )
                    )
                ),
                ExperienceWithIngestionsAndCompanions(
                    experience = Experience(
                        id = 0,
                        title = "Bachelor Party",
                        text = "Some notes",
                        sortDate = getInstant(
                            year = 2022,
                            month = 6,
                            day = 21,
                            hourOfDay = 12,
                            minute = 20
                        )!!,
                    ),
                    ingestionsWithCompanions = listOf(
                        IngestionWithCompanion(
                            ingestion = Ingestion(
                                substanceName = "MDMA",
                                time = getInstant(
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
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "MDMA",
                                color = AdaptiveColor.PINK
                            )
                        )
                    )
                ),
                ExperienceWithIngestionsAndCompanions(
                    experience = Experience(
                        id = 0,
                        title = "Liam's Birthday",
                        text = "Some notes",
                        sortDate = getInstant(
                            year = 2021,
                            month = 9,
                            day = 2,
                            hourOfDay = 18,
                            minute = 13
                        )!!,
                    ),
                    ingestionsWithCompanions = listOf(
                        IngestionWithCompanion(
                            ingestion = Ingestion(
                                substanceName = "Cocaine",
                                time = getInstant(
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
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "Cocaine",
                                color = AdaptiveColor.BLUE
                            )
                        ),
                        IngestionWithCompanion(
                            ingestion = Ingestion(
                                substanceName = "Ketamine",
                                time = getInstant(
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
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "Ketamine",
                                color = AdaptiveColor.MINT
                            )
                        )

                    )
                ),
                ExperienceWithIngestionsAndCompanions(
                    experience = Experience(
                        id = 0,
                        title = "Last day in Stockholm",
                        text = "Some notes",
                        sortDate = getInstant(
                            year = 2021,
                            month = 7,
                            day = 22,
                            hourOfDay = 18,
                            minute = 13
                        )!!
                    ),
                    ingestionsWithCompanions = listOf(
                        IngestionWithCompanion(
                            ingestion = Ingestion(
                                substanceName = "LSD",
                                time = getInstant(
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
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "LSD",
                                color = AdaptiveColor.PURPLE
                            )
                        ),
                        IngestionWithCompanion(
                            ingestion = Ingestion(
                                substanceName = "Cocaine",
                                time = getInstant(
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
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "Cocaine",
                                color = AdaptiveColor.BLUE
                            )
                        )
                    )
                )
            ),
            listOf()
        )
}