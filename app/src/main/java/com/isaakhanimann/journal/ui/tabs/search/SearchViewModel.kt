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

package com.isaakhanimann.journal.ui.tabs.search

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.substances.classes.SubstanceWithCategories
import com.isaakhanimann.journal.data.substances.repositories.SubstanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    experienceRepo: ExperienceRepository, substanceRepo: SubstanceRepository
) : ViewModel() {

    private val _searchTextFlow = MutableStateFlow("")
    val searchTextFlow = _searchTextFlow.asStateFlow()

    private val filtersFlow = MutableStateFlow(emptyList<String>())

    private val customChipName = "custom"

    fun onFilterTapped(filterName: String) {
        viewModelScope.launch {
            if (filterName == customChipName) {
                isShowingCustomSubstancesFlow.emit(isShowingCustomSubstancesFlow.value.not())
            }
            val filters = filtersFlow.value.toMutableList()
            if (filters.contains(filterName)) {
                filters.remove(filterName)
            } else {
                filters.add(filterName)
            }
            filtersFlow.emit(filters)
        }
    }

    private val isShowingCustomSubstancesFlow = MutableStateFlow(false)

    val chipCategoriesFlow: StateFlow<List<CategoryChipModel>> = filtersFlow.map { filters ->
        substanceRepo.getAllCategories().map { category ->
            val isActive = filters.contains(category.name)
            CategoryChipModel(
                chipName = category.name, color = category.color, isActive = isActive
            )
        }
    }.combine(isShowingCustomSubstancesFlow) { chips, isShowingCustom ->
        val newChips = chips.toMutableList()
        newChips.add(
            0, CategoryChipModel(
                chipName = customChipName, color = customColor, isActive = isShowingCustom
            )
        )
        return@combine newChips
    }.stateIn(
        initialValue = emptyList(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    val customColor = Color.Cyan

    val filteredSubstancesFlow = filtersFlow.map { filters ->
        // find substances matching all categories
        substanceRepo.getAllSubstancesWithCategories().filter { substanceWithCategories ->
            filters.all { substanceWithCategories.substance.categories.contains(it) }
        }
        // find substances matching our search string
    }.combine(searchTextFlow) { substances, searchText ->
        getMatchingSubstances(searchText, substances)
    }
        // also rank results by recently used & being a common drug
        .combine(experienceRepo.getSortedLastUsedSubstanceNamesFlow(limit = 200)) { filteredSubstances, sortedRecentlyUsed ->
            val recentNames = sortedRecentlyUsed.distinct()
            // recently used substances matching our search
            val showFirst =
                recentNames.filter { recent -> filteredSubstances.any { it.substance.name == recent } }
                    .mapNotNull {
                        substanceRepo.getSubstanceWithCategories(
                            substanceName = it
                        )
                    }
            // common substances matching our search
            val showSecond =
                filteredSubstances.filter { sub -> sub.categories.any { cat -> cat.name == "common" } }

            val sortedResults =
                (showFirst + showSecond + filteredSubstances).distinctBy { it.substance.name }
            return@combine sortedResults.map { it.toSubstanceModel() }
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    fun filterSubstances(searchText: String) {
        viewModelScope.launch {
            _searchTextFlow.emit(searchText)
        }
    }

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

    companion object {
        // both needle and haystack are preprocessed by removing '-' and whitespaces
        fun getMatchingSubstances(
            searchText: String, substances: List<SubstanceWithCategories>
        ): List<SubstanceWithCategories> {
            return if (searchText.isEmpty()) {
                substances
            } else {
                val searchString = searchText.replace(Regex("[- ]"), "")

                // substances whose primary name begins with the search string
                val mainPrefixMatches = substances.filter { substanceWithCategories ->
                    substanceWithCategories.substance.name.replace(Regex("[- ]"), "").startsWith(
                        prefix = searchString, ignoreCase = true
                    )
                }

                // substances with any name beginning with the search string
                val prefixMatches = substances.filter { substanceWithCategories ->
                    val allNames =
                        substanceWithCategories.substance.commonNames + substanceWithCategories.substance.name
                    allNames.any { name ->
                        name.replace(Regex("[- ]"), "").startsWith(
                            prefix = searchString, ignoreCase = true
                        )
                    }
                }

                // substances containing the search string in any of their names
                val matches = substances.filter { substanceWithCategories ->
                    val allNames =
                        substanceWithCategories.substance.commonNames + substanceWithCategories.substance.name
                    allNames.any { name ->
                        name.replace(Regex("[- ]"), "").contains(
                            other = searchString, ignoreCase = true
                        )
                    }
                }
                return (mainPrefixMatches + prefixMatches + matches).distinctBy { it.substance.name }
            }
        }
    }
}

data class CategoryChipModel(
    val chipName: String, val color: Color, val isActive: Boolean
)

data class SubstanceModel(
    val name: String,
    val commonNames: List<String>,
    val categories: List<CategoryModel>,
    val hasSaferUse: Boolean,
    val hasInteractions: Boolean
)

data class CategoryModel(
    val name: String, val color: Color
)
