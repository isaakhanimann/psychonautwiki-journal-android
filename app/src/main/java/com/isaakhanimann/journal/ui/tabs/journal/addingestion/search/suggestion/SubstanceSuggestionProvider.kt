/*
 * Copyright (c) 2023. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.models.DoseAndUnit
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.models.SubstanceRouteSuggestion
import com.isaakhanimann.journal.ui.utils.getInstant

class SubstanceSuggestionProvider : PreviewParameterProvider<SubstanceRouteSuggestion> {
    override val values: Sequence<SubstanceRouteSuggestion> = sequenceOf(
        SubstanceRouteSuggestion(
            color = AdaptiveColor.PINK,
            route = AdministrationRoute.ORAL,
            substanceName = "MDMA",
            isCustomSubstance = false,
            dosesAndUnit = listOf(
                DoseAndUnit(
                    dose = 50.0,
                    unit = "mg",
                    isEstimate = false,
                    estimatedDoseVariance = null
                ),
                DoseAndUnit(
                    dose = 100.0,
                    unit = "mg",
                    isEstimate = false,
                    estimatedDoseVariance = null
                ),
                DoseAndUnit(
                    dose = null,
                    unit = "mg",
                    isEstimate = false,
                    estimatedDoseVariance = null
                ),
            ),
            customUnitDoses = emptyList(),
            customUnits = emptyList(),
            lastUsed = getInstant(year = 2023, month = 4, day = 10, hourOfDay = 5, minute = 20)!!
        ),
        SubstanceRouteSuggestion(
            color = AdaptiveColor.BLUE,
            route = AdministrationRoute.INSUFFLATED,
            substanceName = "Amphetamine",
            isCustomSubstance = false,
            dosesAndUnit = listOf(
                DoseAndUnit(
                    dose = 10.0,
                    unit = "mg",
                    isEstimate = false,
                    estimatedDoseVariance = null
                ),
                DoseAndUnit(
                    dose = 20.0,
                    unit = "mg",
                    isEstimate = false,
                    estimatedDoseVariance = null
                ),
                DoseAndUnit(
                    dose = null,
                    unit = "mg",
                    isEstimate = false,
                    estimatedDoseVariance = null
                ),
            ),
            customUnitDoses = emptyList(),
            customUnits = emptyList(),
            lastUsed = getInstant(year = 2023, month = 3, day = 10, hourOfDay = 5, minute = 20)!!
        ),
        SubstanceRouteSuggestion(
            color = AdaptiveColor.BLUE,
            route = AdministrationRoute.ORAL,
            substanceName = "Amphetamine",
            isCustomSubstance = false,
            dosesAndUnit = listOf(
                DoseAndUnit(
                    dose = 20.0,
                    unit = "mg",
                    isEstimate = false,
                    estimatedDoseVariance = null
                ),
            ),
            customUnitDoses = emptyList(),
            customUnits = emptyList(),
            lastUsed = getInstant(year = 2023, month = 3, day = 10, hourOfDay = 5, minute = 20)!!
        )
    )
}