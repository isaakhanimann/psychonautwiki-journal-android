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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.CustomSubstance
import com.isaakhanimann.journal.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.data.substances.repositories.SubstanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AddIngestionSearchViewModel @Inject constructor(
    experienceRepo: ExperienceRepository,
    val substanceRepo: SubstanceRepository,
) : ViewModel() {

    private val customSubstancesFlow = experienceRepo.getCustomSubstancesFlow()

    val previousSubstanceRows: StateFlow<List<PreviousSubstance>> =
        experienceRepo.getSortedIngestionsWithSubstanceCompanionsFlow(limit = 150)
            .combine(customSubstancesFlow) { ingestions, customSubstances ->
                return@combine getPreviousSubstances(ingestions, customSubstances)
            }.stateIn(
                initialValue = emptyList(),
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000)
            )

    private fun getPreviousSubstances(
        ingestions: List<IngestionWithCompanion>,
        customSubstances: List<CustomSubstance>
    ): List<PreviousSubstance> {
        val grouped = ingestions.groupBy { it.ingestion.substanceName }
        return grouped.mapNotNull { entry ->
            val substanceName = entry.key
            val groupedIngestions = entry.value
            val color =
                groupedIngestions.firstOrNull()?.substanceCompanion?.color ?: return@mapNotNull null
            val isPredefinedSubstance = substanceRepo.getSubstance(substanceName) != null
            val isCustomSubstance = customSubstances.any { it.name == substanceName }
            if (!isPredefinedSubstance && !isCustomSubstance) {
                return@mapNotNull null
            } else {
                return@mapNotNull PreviousSubstance(
                    color = color,
                    substanceName = substanceName,
                    isCustom = isCustomSubstance,
                    routesWithDoses = groupedIngestions.groupBy { it.ingestion.administrationRoute }
                        .map { routeEntry ->
                            RouteWithDoses(
                                route = routeEntry.key,
                                doses = routeEntry.value.map { ingestionWithCompanion ->
                                    PreviousDose(
                                        dose = ingestionWithCompanion.ingestion.dose,
                                        unit = ingestionWithCompanion.ingestion.units,
                                        isEstimate = ingestionWithCompanion.ingestion.isDoseAnEstimate
                                    )
                                }.distinct().take(6)
                            )
                        }
                )
            }
        }
    }
}

data class PreviousSubstance(
    val color: AdaptiveColor,
    val substanceName: String,
    val isCustom: Boolean,
    val routesWithDoses: List<RouteWithDoses>
)

data class RouteWithDoses(
    val route: AdministrationRoute,
    val doses: List<PreviousDose>
)

data class PreviousDose(
    val dose: Double?,
    val unit: String?,
    val isEstimate: Boolean
)