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
import com.isaakhanimann.journal.data.substances.repositories.SearchRepository
import com.isaakhanimann.journal.data.substances.repositories.SubstanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddIngestionSearchViewModel @Inject constructor(
    experienceRepo: ExperienceRepository,
    val substanceRepo: SubstanceRepository,
    private val searchRepo: SearchRepository,
) : ViewModel() {

    private val _searchTextFlow = MutableStateFlow("")
    val searchTextFlow = _searchTextFlow.asStateFlow()

    fun filterSubstances(searchText: String) {
        viewModelScope.launch {
            _searchTextFlow.emit(searchText)
        }
    }

    val filteredSubstancesFlow = combine(searchTextFlow, experienceRepo.getSortedLastUsedSubstanceNamesFlow(limit = 200)) { searchText, recents ->
        return@combine searchRepo.getMatchingSubstances(searchText = searchText, filterCategories = emptyList<String>(), recentlyUsedSubstanceNamesSorted = recents).map { it.toSubstanceModel() }
    }.stateIn(
        initialValue = emptyList(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    private val customSubstancesFlow = experienceRepo.getCustomSubstancesFlow()

    val filteredCustomSubstancesFlow =
        customSubstancesFlow.combine(searchTextFlow) { customSubstances, searchText ->
            customSubstances.filter { custom ->
                custom.name.contains(
                    other = searchText, ignoreCase = true
                )
            }
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    val substanceSuggestionRows: StateFlow<List<SubstanceSuggestion>> =
        experienceRepo.getSortedIngestionsWithSubstanceCompanionsFlow(limit = 150)
            .combine(customSubstancesFlow) { ingestions, customSubstances ->
                return@combine getSubstanceSuggestions(ingestions, customSubstances)
            }.stateIn(
                initialValue = emptyList(),
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000)
            )

    private fun getSubstanceSuggestions(
        ingestions: List<IngestionWithCompanion>,
        customSubstances: List<CustomSubstance>
    ): List<SubstanceSuggestion> {
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
                return@mapNotNull SubstanceSuggestion(
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

data class SubstanceSuggestion(
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