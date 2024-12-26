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
import com.isaakhanimann.journal.data.room.experiences.relations.IngestionWithCompanionAndCustomUnit
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.data.substances.classes.Substance
import com.isaakhanimann.journal.data.substances.repositories.SearchRepository
import com.isaakhanimann.journal.data.substances.repositories.SubstanceRepository
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.models.CustomUnitDoseSuggestion
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.models.DoseAndUnit
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.models.Suggestion
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

    val filteredCustomUnitsFlow = combine(
        customUnitsFlow,
        filteredSubstancesFlow,
        searchTextFlow
    ) { customUnit, filteredSubstances, searchText ->
        customUnit.filter { custom ->
            filteredSubstances.any { it.name == custom.substanceName } || custom.name.contains(
                other = searchText,
                ignoreCase = true
            ) || custom.substanceName.contains(
                other = searchText,
                ignoreCase = true
            ) || custom.unit.contains(
                other = searchText,
                ignoreCase = true
            ) || custom.note.contains(other = searchText, ignoreCase = true)
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

    val filteredSuggestions: StateFlow<List<Suggestion>> = combine(
        experienceRepo.getSortedIngestionsWithSubstanceCompanionsFlow(limit = 300),
        customSubstancesFlow,
        filteredSubstancesFlow,
        searchTextFlow
    ) { ingestions, customSubstances, filteredSubstances, searchText ->
        val suggestions = getSuggestions(ingestions, customSubstances)
        return@combine suggestions.filter { sug ->
            sug.isInSearch(
                searchText = searchText,
                substanceNames = filteredSubstances.map { it.name })
        }
    }.stateIn(
        initialValue = emptyList(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )


    private fun getSuggestions(
        ingestions: List<IngestionWithCompanionAndCustomUnit>,
        customSubstances: List<CustomSubstance>
    ): List<Suggestion> {
        val groupedBySubstance = ingestions.groupBy { it.ingestion.substanceName }
        val suggestions = groupedBySubstance.flatMap { entry ->
            return@flatMap getSuggestionsForSubstance(
                substanceName = entry.key,
                ingestionsGroupedBySubstance = entry.value,
                customSubstances = customSubstances
            )
        }
        return suggestions.sortedByDescending { it.sortInstant }
    }

    private fun getSuggestionsForSubstance(
        substanceName: String,
        ingestionsGroupedBySubstance: List<IngestionWithCompanionAndCustomUnit>,
        customSubstances: List<CustomSubstance>
    ): List<Suggestion> {
        val color =
            ingestionsGroupedBySubstance.firstOrNull()?.substanceCompanion?.color
                ?: return emptyList()
        val substance = substanceRepo.getSubstance(substanceName)
        val isPredefinedSubstance = substance != null
        val customSubstance = customSubstances.firstOrNull { it.name == substanceName }
        val groupedRoute =
            ingestionsGroupedBySubstance.groupBy { it.ingestion.administrationRoute }
        if (!isPredefinedSubstance && customSubstance == null) {
            return emptyList()
        } else {
            val suggestions = groupedRoute.flatMap { routeEntry ->
                val administrationRoute = routeEntry.key
                val ingestionsForSubstanceAndRoute = routeEntry.value

                val customUnitSuggestions = getCustomUnitSuggestionsForSubstance(
                    ingestionsForSubstanceAndRoute = ingestionsForSubstanceAndRoute,
                    color = color
                )
                val results = mutableListOf<Suggestion>()
                results.addAll(customUnitSuggestions)
                if (substance != null) {
                    val pureSubstanceSuggestion = getPureSubstanceSuggestion(
                        ingestionsForSubstanceAndRoute = ingestionsForSubstanceAndRoute,
                        substance = substance,
                        administrationRoute = administrationRoute,
                        color = color
                    )
                    if (pureSubstanceSuggestion != null) {
                        results.add(pureSubstanceSuggestion)
                    }
                }
                if (customSubstance != null) {
                    val customSubstanceSuggestion = getCustomSubstanceSuggestion(
                        ingestionsForSubstanceAndRoute = ingestionsForSubstanceAndRoute,
                        customSubstance = customSubstance,
                        administrationRoute = administrationRoute,
                        color = color
                    )
                    if (customSubstanceSuggestion != null) {
                        results.add(customSubstanceSuggestion)
                    }
                }
                return@flatMap results
            }
            return suggestions
        }
    }

    private fun getCustomUnitSuggestionsForSubstance(
        ingestionsForSubstanceAndRoute: List<IngestionWithCompanionAndCustomUnit>,
        color: AdaptiveColor
    ): List<Suggestion.CustomUnitSuggestion> {
        val groupedByUnit = ingestionsForSubstanceAndRoute.filter { it.customUnit != null }
            .groupBy { it.customUnit }
        return groupedByUnit.mapNotNull { unitGroup ->
            val customUnit = unitGroup.key ?: return@mapNotNull null
            val ingestionsWithUnit = unitGroup.value
            val dosesAndUnit = ingestionsWithUnit.map { it.ingestion }.map { ingestion ->
                CustomUnitDoseSuggestion(
                    dose = ingestion.dose,
                    isEstimate = ingestion.isDoseAnEstimate,
                    estimatedDoseStandardDeviation = ingestion.estimatedDoseStandardDeviation
                )
            }.distinctBy { it.comparatorValue }.take(8)
            if (dosesAndUnit.isEmpty()) {
                return@mapNotNull null
            }
            return@mapNotNull Suggestion.CustomUnitSuggestion(
                customUnit = customUnit,
                adaptiveColor = color,
                dosesAndUnit = dosesAndUnit,
                sortInstant = ingestionsWithUnit.mapNotNull { it.ingestion.creationDate }
                    .maxOfOrNull { it } ?: Instant.MIN
            )
        }
    }

    private fun getPureSubstanceSuggestion(
        ingestionsForSubstanceAndRoute: List<IngestionWithCompanionAndCustomUnit>,
        substance: Substance,
        administrationRoute: AdministrationRoute,
        color: AdaptiveColor
    ): Suggestion.PureSubstanceSuggestion? {
        val ingestionsToConsider = ingestionsForSubstanceAndRoute.asSequence().filter { it.customUnit == null }
            .map { it.ingestion }
        if (ingestionsToConsider.count() == 0) {
            return null
        }
        val dosesAndUnit = ingestionsToConsider
                .mapNotNull { ingestion ->
                    val unit = ingestion.units ?: return@mapNotNull null
                    return@mapNotNull DoseAndUnit(
                        dose = ingestion.dose,
                        unit = unit,
                        isEstimate = ingestion.isDoseAnEstimate,
                        estimatedDoseStandardDeviation = ingestion.estimatedDoseStandardDeviation
                    )
                }.distinctBy { it.comparatorValue }.take(8).toList()
        return Suggestion.PureSubstanceSuggestion(
            administrationRoute = administrationRoute,
            substanceName = substance.name,
            adaptiveColor = color,
            dosesAndUnit = dosesAndUnit,
            sortInstant = ingestionsForSubstanceAndRoute.mapNotNull { it.ingestion.creationDate }
                .maxOfOrNull { it } ?: Instant.MIN
        )
    }

    private fun getCustomSubstanceSuggestion(
        ingestionsForSubstanceAndRoute: List<IngestionWithCompanionAndCustomUnit>,
        customSubstance: CustomSubstance,
        administrationRoute: AdministrationRoute,
        color: AdaptiveColor
    ): Suggestion.CustomSubstanceSuggestion? {
        val ingestionsToConsider = ingestionsForSubstanceAndRoute.asSequence().filter { it.customUnit == null }
            .map { it.ingestion }
        if (ingestionsToConsider.count() == 0) {
            return null
        }
        val dosesAndUnit = ingestionsToConsider
                .mapNotNull { ingestion ->
                    val unit = ingestion.units ?: return@mapNotNull null
                    return@mapNotNull DoseAndUnit(
                        dose = ingestion.dose,
                        unit = unit,
                        isEstimate = ingestion.isDoseAnEstimate,
                        estimatedDoseStandardDeviation = ingestion.estimatedDoseStandardDeviation
                    )
                }.distinctBy { it.comparatorValue }.take(8).toList()
        return Suggestion.CustomSubstanceSuggestion(
            administrationRoute = administrationRoute,
            customSubstance = customSubstance,
            adaptiveColor = color,
            dosesAndUnit = dosesAndUnit,
            sortInstant = ingestionsForSubstanceAndRoute.mapNotNull { it.ingestion.creationDate }
                .maxOfOrNull { it } ?: Instant.MIN
        )
    }
}