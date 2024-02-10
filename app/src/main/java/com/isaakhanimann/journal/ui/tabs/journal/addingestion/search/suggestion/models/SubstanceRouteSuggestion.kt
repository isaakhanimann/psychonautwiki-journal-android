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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.models

import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.CustomUnit
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import java.time.Instant

data class SubstanceRouteSuggestion(
    val color: AdaptiveColor,
    val route: AdministrationRoute,
    val substanceName: String,
    val isCustomSubstance: Boolean,
    val dosesAndUnit: List<DoseAndUnit>,
    val customUnitDoses: List<CustomUnitDose>,
    val customUnits: List<CustomUnit>,
    val lastUsed: Instant
)

data class DoseAndUnit(
    val dose: Double?,
    val unit: String?,
    val isEstimate: Boolean,
    val estimatedDoseVariance: Double?
)

data class CustomUnitDose(
    val dose: Double,
    val isEstimate: Boolean,
    val estimatedDoseVariance: Double?,
    val customUnit: CustomUnit
)

