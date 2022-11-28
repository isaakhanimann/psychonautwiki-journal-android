/*
 * Copyright (c) 2022. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.journal.experience

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.journal.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.data.substances.classes.InteractionType
import com.isaakhanimann.journal.data.substances.classes.roa.DoseClass
import com.isaakhanimann.journal.data.substances.classes.roa.DurationRange
import com.isaakhanimann.journal.data.substances.classes.roa.DurationUnits
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.interactions.Interaction
import com.isaakhanimann.journal.ui.utils.getInstant

class OneExperienceScreenPreviewProvider :
    PreviewParameterProvider<OneExperienceScreenModel> {

    override val values: Sequence<OneExperienceScreenModel> = sequenceOf(
        OneExperienceScreenModel(
            isFavorite = false,
            title = "Day at Lake Geneva",
            firstIngestionTime = getInstant(
                year = 2022,
                month = 2,
                day = 19,
                hourOfDay = 20,
                minute = 5
            )!!,
            notes = "Some Notes",
            isShowingAddIngestionButton = true,
            ingestionElements = listOf(
                IngestionElement(
                    ingestionWithCompanion = IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "MDMA",
                            time = getInstant(
                                year = 2022,
                                month = 2,
                                day = 19,
                                hourOfDay = 20,
                                minute = 5
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
                    roaDuration = RoaDuration(
                        onset = DurationRange(
                            min = 30f,
                            max = 45f,
                            units = DurationUnits.MINUTES
                        ),
                        comeup = DurationRange(
                            min = 15f,
                            max = 30f,
                            units = DurationUnits.MINUTES
                        ),
                        peak = DurationRange(
                            min = 1.5f,
                            max = 2.5f,
                            units = DurationUnits.HOURS
                        ),
                        offset = DurationRange(
                            min = 1f,
                            max = 1.5f,
                            units = DurationUnits.HOURS
                        ),
                        total = DurationRange(
                            min = 3f,
                            max = 6f,
                            units = DurationUnits.HOURS
                        ),
                        afterglow = DurationRange(
                            min = 12f,
                            max = 48f,
                            units = DurationUnits.HOURS
                        )
                    ),
                    doseClass = DoseClass.COMMON
                ),
                IngestionElement(
                    ingestionWithCompanion = IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "Cocaine",
                            time = getInstant(
                                year = 2022,
                                month = 2,
                                day = 19,
                                hourOfDay = 23,
                                minute = 5
                            )!!,
                            administrationRoute = AdministrationRoute.INSUFFLATED,
                            dose = 40.0,
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
                    roaDuration = RoaDuration(
                        onset = DurationRange(
                            min = 1f,
                            max = 10f,
                            units = DurationUnits.MINUTES
                        ),
                        comeup = DurationRange(
                            min = 5f,
                            max = 15f,
                            units = DurationUnits.MINUTES
                        ),
                        peak = DurationRange(
                            min = 15f,
                            max = 30f,
                            units = DurationUnits.MINUTES
                        ),
                        offset = DurationRange(
                            min = 10f,
                            max = 30f,
                            units = DurationUnits.MINUTES
                        ),
                        total = DurationRange(
                            min = 10f,
                            max = 90f,
                            units = DurationUnits.MINUTES
                        ),
                        afterglow = null
                    ),
                    doseClass = DoseClass.COMMON
                ),
                IngestionElement(
                    ingestionWithCompanion = IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "Cocaine",
                            time = getInstant(
                                year = 2022,
                                month = 2,
                                day = 20,
                                hourOfDay = 1,
                                minute = 15
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
                    ),
                    roaDuration = RoaDuration(
                        onset = DurationRange(
                            min = 1f,
                            max = 10f,
                            units = DurationUnits.MINUTES
                        ),
                        comeup = DurationRange(
                            min = 5f,
                            max = 15f,
                            units = DurationUnits.MINUTES
                        ),
                        peak = DurationRange(
                            min = 15f,
                            max = 30f,
                            units = DurationUnits.MINUTES
                        ),
                        offset = DurationRange(
                            min = 10f,
                            max = 30f,
                            units = DurationUnits.MINUTES
                        ),
                        total = DurationRange(
                            min = 10f,
                            max = 90f,
                            units = DurationUnits.MINUTES
                        ),
                        afterglow = null
                    ),
                    doseClass = DoseClass.LIGHT
                )
            ),
            cumulativeDoses = listOf(
                CumulativeDose(
                    substanceName = "Cocaine",
                    cumulativeDose = 60.0,
                    units = "mg",
                    isEstimate = false,
                    doseClass = DoseClass.STRONG
                )
            ),
            interactions = listOf(
                Interaction(
                    aName = "MDMA",
                    bName = "Cocaine",
                    interactionType = InteractionType.UNSAFE,
                    interactionExplanationURL = "www.google.com"
                )
            )
        )
    )
}