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
import com.isaakhanimann.journal.data.room.experiences.entities.CustomSubstance
import com.isaakhanimann.journal.data.room.experiences.relations.IngestionWithCompanionAndCustomUnit
import com.isaakhanimann.journal.data.substances.repositories.SearchRepository
import com.isaakhanimann.journal.data.substances.repositories.SubstanceRepository
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.models.CustomUnitDose
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.models.DoseAndUnit
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.models.SubstanceRouteSuggestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class AddIngestionSearchViewModel @Inject constructor(
    experienceRepo: ExperienceRepository,
    val substanceRepo: SubstanceRepository,
    private val searchRepo: SearchRepository,
) : ViewModel() {

    private val _searchTextFlow = MutableStateFlow("")
    val searchTextFlow = _searchTextFlow.asStateFlow()

    fun updateSearchText(searchText: String) {
        viewModelScope.launch {
            _searchTextFlow.emit(searchText)
        }
    }

    val filteredSubstancesFlow = combine(
        searchTextFlow,
        experienceRepo.getSortedLastUsedSubstanceNamesFlow(limit = 200)
    ) { searchText, recents ->
        return@combine searchRepo.getMatchingSubstances(
            searchText = searchText,
            filterCategories = emptyList(),
            recentlyUsedSubstanceNamesSorted = recents
        ).map { it.toSubstanceModel() }
    }.stateIn(
        initialValue = emptyList(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    private val customUnitsFlow = experienceRepo.getCustomUnitsFlow(false)

    val filteredCustomUnitsFlow = combine(customUnitsFlow, filteredSubstancesFlow, searchTextFlow) { customUnit, filteredSubstances, searchText ->
            customUnit.filter { custom ->
                filteredSubstances.any { it.name == custom.substanceName } || custom.name.contains(
                    other = searchText,
                    ignoreCase = true
                )
            }
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    private val customSubstancesFlow = experienceRepo.getCustomSubstancesFlow()

    val filteredCustomSubstancesFlow =
        customSubstancesFlow.combine(searchTextFlow) { customSubstances, searchText ->
            customSubstances.filter { custom ->
                custom.name.contains(other = searchText, ignoreCase = true)
            }
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    val filteredSuggestions: StateFlow<List<SubstanceRouteSuggestion>> = combine(
        experienceRepo.getSortedIngestionsWithSubstanceCompanionsFlow(limit = 150),
        customSubstancesFlow,
        filteredSubstancesFlow,
        searchTextFlow
    ) { ingestions, customSubstances, filteredSubstances, searchText ->
        val suggestions = getSubstanceSuggestions(ingestions, customSubstances)
        return@combine suggestions.filter { sug ->
            filteredSubstances.any { it.name == sug.substanceName } || sug.substanceName.contains(
                other = searchText,
                ignoreCase = true
            )
        }
    }.stateIn(
        initialValue = emptyList(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )


    private fun getSubstanceSuggestions(
        ingestions: List<IngestionWithCompanionAndCustomUnit>,
        customSubstances: List<CustomSubstance>
    ): List<SubstanceRouteSuggestion> {
        val grouped = ingestions.groupBy { it.ingestion.substanceName }
        return grouped.flatMap { entry ->
            val substanceName = entry.key
            val ingestionsGroupedBySubstance = entry.value
            val color =
                ingestionsGroupedBySubstance.firstOrNull()?.substanceCompanion?.color ?: return@flatMap emptyList()
            val isPredefinedSubstance = substanceRepo.getSubstance(substanceName) != null
            val isCustomSubstance = customSubstances.any { it.name == substanceName }
            val groupedRoute = ingestionsGroupedBySubstance.groupBy { it.ingestion.administrationRoute }
            if (!isPredefinedSubstance && !isCustomSubstance) {
                return@flatMap emptyList<SubstanceRouteSuggestion>()
            } else {
                return@flatMap groupedRoute.mapNotNull { routeEntry ->
                    val dosesAndUnit = routeEntry.value.filter { it.customUnit == null }.map { ingestionWithCustomUnit ->
                        DoseAndUnit(
                            dose = ingestionWithCustomUnit.ingestion.dose,
                            unit = ingestionWithCustomUnit.ingestion.units ?: ingestionWithCustomUnit.customUnit?.unit ?: "",
                            isEstimate = ingestionWithCustomUnit.ingestion.isDoseAnEstimate,
                            estimatedDoseStandardDeviation = ingestionWithCustomUnit.ingestion.estimatedDoseStandardDeviation
                        )
                    }.distinct().take(6)
                    val customUnitDoses = routeEntry.value.mapNotNull CustomUnitMapNotNull@ { ingestionWithCustomUnit ->
                        val ingestion = ingestionWithCustomUnit.ingestion
                        val dose = ingestion.dose ?: return@CustomUnitMapNotNull null
                        ingestionWithCustomUnit.customUnit?.let {
                            if (!it.isArchived) {
                                CustomUnitDose(
                                    dose = dose,
                                    isEstimate = ingestion.isDoseAnEstimate,
                                    estimatedDoseStandardDeviation = ingestion.estimatedDoseStandardDeviation,
                                    customUnit = it
                                )
                            } else {
                                null
                            }

                        }
                    }.distinct().take(6)
                    val customUnits = filteredCustomUnitsFlow.value.filter { it.substanceName == substanceName && it.administrationRoute == routeEntry.key }
                    if (dosesAndUnit.isEmpty() && customUnitDoses.isEmpty() && customUnits.isEmpty()) {
                        return@mapNotNull null
                    } else {
                        return@mapNotNull SubstanceRouteSuggestion(
                            color = color,
                            route = routeEntry.key,
                            substanceName = substanceName,
                            isCustomSubstance = isCustomSubstance,
                            dosesAndUnit = dosesAndUnit,
                            customUnitDoses = customUnitDoses,
                            customUnits = customUnits,
                            lastUsed = routeEntry.value.maxOfOrNull { it.ingestion.time } ?: Instant.now()
                        )
                    }
                }
            }
        }
    }
}