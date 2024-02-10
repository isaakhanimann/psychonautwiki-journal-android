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

package com.isaakhanimann.journal.ui.tabs.journal.experience

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.data.room.experiences.entities.ShulginRating
import com.isaakhanimann.journal.data.room.experiences.entities.ShulginRatingOption
import com.isaakhanimann.journal.data.room.experiences.entities.StomachFullness
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.journal.data.room.experiences.entities.TimedNote
import com.isaakhanimann.journal.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.data.substances.classes.InteractionType
import com.isaakhanimann.journal.data.substances.classes.roa.DurationRange
import com.isaakhanimann.journal.data.substances.classes.roa.DurationUnits
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.interactions.Interaction
import com.isaakhanimann.journal.ui.tabs.journal.experience.models.ConsumerWithIngestions
import com.isaakhanimann.journal.ui.tabs.journal.experience.models.CumulativeDose
import com.isaakhanimann.journal.ui.tabs.journal.experience.models.IngestionElement
import com.isaakhanimann.journal.ui.tabs.journal.experience.models.InteractionExplanation
import com.isaakhanimann.journal.ui.tabs.journal.experience.models.OneExperienceScreenModel
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
            locationName = "Zurich",
            isCurrentExperience = true,
            ingestionElements = ingestionElements,
            cumulativeDoses = listOf(
                CumulativeDose(
                    substanceName = "Cocaine",
                    cumulativeDose = 60.0,
                    units = "mg",
                    isEstimate = false,
                    numDots = 6
                )
            ),
            interactions = listOf(
                Interaction(
                    aName = "MDMA",
                    bName = "Cocaine",
                    interactionType = InteractionType.UNSAFE,
                )
            ),
            interactionExplanations = listOf(
                InteractionExplanation(
                    name = "Cocaine",
                    url = "www.google.com"
                ),
                InteractionExplanation(
                    name = "MDMA",
                    url = "www.google.com"
                )
            ),
            ratings = listOf(
                ShulginRating(
                    time = getInstant(
                        year = 2022,
                        month = 2,
                        day = 20,
                        hourOfDay = 1,
                        minute = 15
                    )!!,
                    creationDate = getInstant(
                        year = 2022,
                        month = 2,
                        day = 20,
                        hourOfDay = 1,
                        minute = 15
                    )!!,
                    option = ShulginRatingOption.TWO_PLUS,
                    experienceId = 0
                ),
                ShulginRating(
                    time = getInstant(
                        year = 2022,
                        month = 2,
                        day = 20,
                        hourOfDay = 3,
                        minute = 15
                    )!!,
                    creationDate = getInstant(
                        year = 2022,
                        month = 3,
                        day = 20,
                        hourOfDay = 1,
                        minute = 15
                    )!!,
                    option = ShulginRatingOption.PLUS,
                    experienceId = 0
                )
            ),
            timedNotes = listOf(
                TimedNote(
                    creationDate = getInstant(
                        year = 2022,
                        month = 2,
                        day = 19,
                        hourOfDay = 23,
                        minute = 25
                    )!!,
                    time = getInstant(
                        year = 2022,
                        month = 2,
                        day = 19,
                        hourOfDay = 23,
                        minute = 25
                    )!!,
                    note = "Onset start",
                    color = AdaptiveColor.PURPLE,
                    experienceId = 0,
                    isPartOfTimeline = true
                ),
                TimedNote(
                    creationDate = getInstant(
                        year = 2022,
                        month = 2,
                        day = 19,
                        hourOfDay = 23,
                        minute = 25
                    )!!,
                    time = getInstant(
                        year = 2022,
                        month = 2,
                        day = 19,
                        hourOfDay = 23,
                        minute = 45
                    )!!,
                    note = "Peak start and this is a note that spans multiple lines, so long that we can see what a bigger layout looks like.",
                    color = AdaptiveColor.BLUE,
                    experienceId = 0,
                    isPartOfTimeline = true
                )
            ),
            consumersWithIngestions = listOf(
                ConsumerWithIngestions(consumerName = "David", ingestionElements = ingestionElements),
                ConsumerWithIngestions(consumerName = "Laura", ingestionElements = ingestionElements),
                ConsumerWithIngestions(consumerName = "Lorene", ingestionElements = ingestionElements),
            )
        )
    )

    companion object {
        val ingestionElements = listOf(
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
                numDots = 2
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
                        dose = 80.0,
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
                numDots = 3
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
                numDots = 2
            )
        )
    }
}