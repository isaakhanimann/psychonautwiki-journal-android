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

package com.isaakhanimann.journal.ui.tabs.stats.substancecompanion

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.Experience
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.data.room.experiences.entities.StomachFullness
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.utils.getInstant

class SubstanceCompanionScreenPreviewProvider :
    PreviewParameterProvider<Pair<SubstanceCompanion, List<IngestionsBurst>>> {
    override val values: Sequence<Pair<SubstanceCompanion, List<IngestionsBurst>>> = sequenceOf(
        Pair(
            first = SubstanceCompanion(
                substanceName = "Cocaine",
                color = AdaptiveColor.BLUE
            ),
            second = listOf(
                IngestionsBurst(
                    timeUntil = "35 min",
                    experience = Experience(
                        id = 1,
                        title = "Vienna Weekend",
                        creationDate = getInstant(
                            year = 2022,
                            month = 7,
                            day = 20,
                            hourOfDay = 14,
                            minute = 20
                        )!!,
                        sortDate = getInstant(
                            year = 2022,
                            month = 7,
                            day = 20,
                            hourOfDay = 14,
                            minute = 20
                        )!!,
                        text = "",
                        isFavorite = false,
                        location = null
                    ),
                    ingestions = listOf(
                        IngestionsBurst.IngestionAndCustomUnit(
                            ingestion = Ingestion(
                                substanceName = "Cocaine",
                                time = getInstant(
                                    year = 2022,
                                    month = 7,
                                    day = 20,
                                    hourOfDay = 14,
                                    minute = 20
                                )!!,
                                administrationRoute = AdministrationRoute.INSUFFLATED,
                                dose = 20.0,
                                isDoseAnEstimate = false,
                                estimatedDoseStandardDeviation = null,
                                units = "mg",
                                experienceId = 0,
                                notes = "This is one note",
                                stomachFullness = StomachFullness.EMPTY,
                                consumerName = null,
                                customUnitId = null
                            ),
                            customUnit = null
                        ),
                        IngestionsBurst.IngestionAndCustomUnit(
                            ingestion = Ingestion(
                                substanceName = "Cocaine",
                                time = getInstant(
                                    year = 2022,
                                    month = 7,
                                    day = 20,
                                    hourOfDay = 13,
                                    minute = 40
                                )!!,
                                administrationRoute = AdministrationRoute.INSUFFLATED,
                                dose = 30.0,
                                isDoseAnEstimate = false,
                                estimatedDoseStandardDeviation = null,
                                units = "mg",
                                experienceId = 0,
                                notes = "This is one note",
                                stomachFullness = StomachFullness.EMPTY,
                                consumerName = null,
                                customUnitId = null
                            ),
                            customUnit = null
                        )
                    )
                ),
                IngestionsBurst(
                    timeUntil = "2 weeks",
                    experience = Experience(
                        id = 1,
                        title = "21. Birthday",
                        creationDate = getInstant(
                            year = 2022,
                            month = 7,
                            day = 4,
                            hourOfDay = 14,
                            minute = 20
                        )!!,
                        sortDate = getInstant(
                            year = 2022,
                            month = 7,
                            day = 4,
                            hourOfDay = 14,
                            minute = 20
                        )!!,
                        text = "",
                        isFavorite = false,
                        location = null
                    ),
                    ingestions = listOf(
                        IngestionsBurst.IngestionAndCustomUnit(
                            ingestion = Ingestion(
                                substanceName = "Cocaine",
                                time = getInstant(
                                    year = 2022,
                                    month = 7,
                                    day = 4,
                                    hourOfDay = 14,
                                    minute = 20
                                )!!,
                                administrationRoute = AdministrationRoute.INSUFFLATED,
                                dose = 10.0,
                                isDoseAnEstimate = false,
                                estimatedDoseStandardDeviation = null,
                                units = "mg",
                                experienceId = 0,
                                notes = "This is one note",
                                stomachFullness = StomachFullness.EMPTY,
                                consumerName = null,
                                customUnitId = null
                            ),
                            customUnit = null
                        ),
                        IngestionsBurst.IngestionAndCustomUnit(
                            ingestion = Ingestion(
                                substanceName = "Cocaine",
                                time = getInstant(
                                    year = 2022,
                                    month = 7,
                                    day = 4,
                                    hourOfDay = 13,
                                    minute = 40
                                )!!,
                                administrationRoute = AdministrationRoute.INSUFFLATED,
                                dose = 20.0,
                                isDoseAnEstimate = false,
                                estimatedDoseStandardDeviation = null,
                                units = "mg",
                                experienceId = 0,
                                notes = "This is one note",
                                stomachFullness = StomachFullness.EMPTY,
                                consumerName = null,
                                customUnitId = null
                            ),
                            customUnit = null
                        )
                    )
                )
            )
        )
    )
}