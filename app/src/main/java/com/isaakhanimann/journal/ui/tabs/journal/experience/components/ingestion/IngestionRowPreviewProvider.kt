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

package com.isaakhanimann.journal.ui.tabs.journal.experience.components.ingestion

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.CustomUnit
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.data.room.experiences.entities.StomachFullness
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.journal.data.room.experiences.relations.IngestionWithCompanionAndCustomUnit
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.tabs.journal.experience.models.IngestionElement
import java.time.Instant

class IngestionRowPreviewProvider : PreviewParameterProvider<IngestionElement> {
    override val values: Sequence<IngestionElement> = sequenceOf(
        IngestionElement(
            ingestionWithCompanionAndCustomUnit = IngestionWithCompanionAndCustomUnit(
                ingestion = Ingestion(
                    substanceName = "MDMA",
                    time = Instant.now(),
                    endTime = null,
                    administrationRoute = AdministrationRoute.ORAL,
                    dose = 90.0,
                    isDoseAnEstimate = false,
                    estimatedDoseStandardDeviation = null,
                    units = "mg",
                    experienceId = 0,
                    notes = "This is a very long note which I wrote to see how it looks like if the note spans more than one line in the ingestion row.",
                    stomachFullness = StomachFullness.EMPTY,
                    consumerName = null,
                    customUnitId = null
                ),
                substanceCompanion = SubstanceCompanion(
                    substanceName = "MDMA",
                    color = AdaptiveColor.PINK
                ),
                customUnit = null
            ),
            roaDuration = null,
            numDots = 2
        ),
        IngestionElement(
            ingestionWithCompanionAndCustomUnit = IngestionWithCompanionAndCustomUnit(
                ingestion = Ingestion(
                    substanceName = "2C-B",
                    time = Instant.now(),
                    endTime = null,
                    administrationRoute = AdministrationRoute.ORAL,
                    dose = 1.5,
                    isDoseAnEstimate = false,
                    estimatedDoseStandardDeviation = null,
                    units = "pill",
                    experienceId = 0,
                    notes = "This is a very long note which I wrote to see how it looks like if the note spans more than one line in the ingestion row.",
                    stomachFullness = StomachFullness.EMPTY,
                    consumerName = null,
                    customUnitId = 2
                ),
                substanceCompanion = SubstanceCompanion(
                    substanceName = "2C-B",
                    color = AdaptiveColor.GREEN
                ),
                customUnit = CustomUnit(
                    id = 2,
                    substanceName = "2C-B",
                    name = "Red rocket",
                    administrationRoute = AdministrationRoute.ORAL,
                    dose = 14.0,
                    estimatedDoseStandardDeviation = 4.0,
                    isEstimate = true,
                    isArchived = false,
                    unit = "pill",
                    unitPlural = "pills",
                    originalUnit = "mg",
                    note = "this is a note"
                )
            ),
            roaDuration = null,
            numDots = 8
        ),
        IngestionElement(
            ingestionWithCompanionAndCustomUnit = IngestionWithCompanionAndCustomUnit(
                ingestion = Ingestion(
                    substanceName = "LSD",
                    time = Instant.now(),
                    endTime = null,
                    administrationRoute = AdministrationRoute.ORAL,
                    dose = null,
                    isDoseAnEstimate = false,
                    estimatedDoseStandardDeviation = null,
                    units = "µg",
                    experienceId = 0,
                    notes = null,
                    stomachFullness = StomachFullness.EMPTY,
                    consumerName = null,
                    customUnitId = null
                ),
                substanceCompanion = SubstanceCompanion(
                    substanceName = "LSD",
                    color = AdaptiveColor.BLUE
                ),
                customUnit = null
            ),
            roaDuration = null,
            numDots = null
        )
    )
}