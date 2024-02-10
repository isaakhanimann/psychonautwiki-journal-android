/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.journal

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.Experience
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.data.room.experiences.entities.Location
import com.isaakhanimann.journal.data.room.experiences.entities.ShulginRating
import com.isaakhanimann.journal.data.room.experiences.entities.ShulginRatingOption
import com.isaakhanimann.journal.data.room.experiences.entities.StomachFullness
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.journal.data.room.experiences.relations.ExperienceWithIngestionsCompanionsAndRatings
import com.isaakhanimann.journal.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.utils.getInstant
import java.time.Instant
import java.time.temporal.ChronoUnit

class JournalScreenPreviewProvider :
    PreviewParameterProvider<List<ExperienceWithIngestionsCompanionsAndRatings>> {
    override val values: Sequence<List<ExperienceWithIngestionsCompanionsAndRatings>> =
        sequenceOf(
            listOf(
                ExperienceWithIngestionsCompanionsAndRatings(
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
                        location = Location(name = "Max place", longitude = 4.0, latitude = 5.0)
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
                                estimatedDoseVariance = null,
                                units = "mg",
                                experienceId = 0,
                                notes = null,
                                stomachFullness = StomachFullness.EMPTY,
                                consumerName = null,
                                customUnitId = null
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
                                estimatedDoseVariance = null,
                                units = "mg",
                                experienceId = 0,
                                notes = null,
                                stomachFullness = StomachFullness.EMPTY,
                                consumerName = null,
                                customUnitId = null
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
                                estimatedDoseVariance = null,
                                units = "mg",
                                experienceId = 0,
                                notes = null,
                                stomachFullness = StomachFullness.EMPTY,
                                consumerName = null,
                                customUnitId = null
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "Ketamine",
                                color = AdaptiveColor.MINT
                            )
                        )
                    ),
                    ratings = listOf(
                        ShulginRating(
                            time = Instant.now().minus(15, ChronoUnit.MINUTES),
                            creationDate = Instant.now(),
                            option = ShulginRatingOption.TWO_PLUS,
                            experienceId = 0
                        )
                    )
                ),
                ExperienceWithIngestionsCompanionsAndRatings(
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
                        location = Location(name = "Max place", longitude = 4.0, latitude = 5.0)
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
                                estimatedDoseVariance = null,
                                units = "mg",
                                experienceId = 0,
                                notes = null,
                                stomachFullness = StomachFullness.EMPTY,
                                consumerName = null,
                                customUnitId = null
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "MDMA",
                                color = AdaptiveColor.PINK
                            )
                        )
                    ),
                    ratings = listOf()
                ),
                ExperienceWithIngestionsCompanionsAndRatings(
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
                        location = Location(name = "Max place", longitude = 4.0, latitude = 5.0)
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
                                estimatedDoseVariance = null,
                                units = "mg",
                                experienceId = 0,
                                notes = null,
                                stomachFullness = StomachFullness.EMPTY,
                                consumerName = null,
                                customUnitId = null
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
                                estimatedDoseVariance = null,
                                units = "mg",
                                experienceId = 0,
                                notes = null,
                                stomachFullness = StomachFullness.EMPTY,
                                consumerName = null,
                                customUnitId = null
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "Ketamine",
                                color = AdaptiveColor.MINT
                            )
                        )
                    ),
                    ratings = listOf(
                        ShulginRating(
                            time = Instant.now().minus(15, ChronoUnit.MINUTES),
                            creationDate = Instant.now(),
                            option = ShulginRatingOption.TWO_PLUS,
                            experienceId = 0
                        )
                    )
                ),
                ExperienceWithIngestionsCompanionsAndRatings(
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
                        )!!,
                        location = Location(name = "Max place", longitude = 4.0, latitude = 5.0)
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
                                estimatedDoseVariance = null,
                                units = "µg",
                                experienceId = 0,
                                notes = null,
                                stomachFullness = StomachFullness.EMPTY,
                                consumerName = null,
                                customUnitId = null
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
                                estimatedDoseVariance = null,
                                units = "mg",
                                experienceId = 0,
                                notes = null,
                                stomachFullness = StomachFullness.EMPTY,
                                consumerName = null,
                                customUnitId = null
                            ),
                            substanceCompanion = SubstanceCompanion(
                                substanceName = "Cocaine",
                                color = AdaptiveColor.BLUE
                            )
                        )
                    ),
                    ratings = listOf()
                ),
            ),
            listOf()
        )
}