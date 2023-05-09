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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    experienceRepo: ExperienceRepository,
    substanceRepo: SubstanceRepository
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

    val chipCategoriesFlow: StateFlow<List<CategoryChipModel>> =
        filtersFlow.map { filters ->
            substanceRepo.getAllCategories().map { category ->
                val isActive = filters.contains(category.name)
                CategoryChipModel(
                    chipName = category.name,
                    color = category.color,
                    isActive = isActive
                )
            }
        }.combine(isShowingCustomSubstancesFlow) { chips, isShowingCustom ->
            val newChips = chips.toMutableList()
            newChips.add(
                0, CategoryChipModel(
                    chipName = customChipName,
                    color = customColor,
                    isActive = isShowingCustom
                )
            )
            return@combine newChips
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    val customColor = Color.Cyan

    val filteredSubstancesFlow =
        filtersFlow.map { filters ->
            substanceRepo.getAllSubstancesWithCategories().filter { substanceWithCategories ->
                filters.all { substanceWithCategories.substance.categories.contains(it) }
            }
        }.combine(searchTextFlow) { substances, searchText ->
            getMatchingSubstances(searchText, substances)
        }.combine(experienceRepo.getSortedLastUsedSubstanceNamesFlow(limit = 200)) { filteredSubstances, sortedRecentlyUsed ->
            val recentNames = sortedRecentlyUsed.distinct()
            val showFirst =
                recentNames.filter { recent -> filteredSubstances.any { it.substance.name == recent } }.mapNotNull {
                    substanceRepo.getSubstanceWithCategories(
                        substanceName = it
                    )
                }
            val showSecond = filteredSubstances.filter { sub -> sub.categories.any { cat -> cat.name == "common" } }
            val sortedResults = (showFirst + showSecond + filteredSubstances).distinctBy { it.substance.name }
            return@combine sortedResults.map {
                SubstanceModel(
                    name = it.substance.name,
                    commonNames = it.substance.commonNames,
                    categories = it.categories.map { category ->
                        CategoryModel(
                            name = category.name,
                            color = category.color
                        )
                    },
                    hasSaferUse = it.substance.saferUse.isNotEmpty(),
                    hasInteractions = it.substance.hasInteractions
                )
            }
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
                    other = searchText,
                    ignoreCase = true
                )
            }
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    companion object {
        fun getMatchingSubstances(
            searchText: String,
            substances: List<SubstanceWithCategories>
        ): List<SubstanceWithCategories> {
            return if (searchText.isEmpty()) {
                substances
            } else {
                if (searchText.length < 3) {
                    substances.filter { substanceWithCategories ->
                        substanceWithCategories.substance.name.startsWith(
                            prefix = searchText,
                            ignoreCase = true
                        ) ||
                                substanceWithCategories.substance.commonNames.any { commonName ->
                                    commonName.startsWith(prefix = searchText, ignoreCase = true)
                                }
                    }
                } else {
                    val containing = substances.filter { substanceWithCategories ->
                        substanceWithCategories.substance.name.contains(
                            other = searchText,
                            ignoreCase = true
                        ) ||
                                substanceWithCategories.substance.commonNames.any { commonName ->
                                    commonName.contains(other = searchText, ignoreCase = true)
                                }
                    }
                    val prefixAndContainingMatches =
                        containing.partition { substanceWithCategories ->
                            substanceWithCategories.substance.name.startsWith(
                                prefix = searchText,
                                ignoreCase = true
                            ) ||
                                    substanceWithCategories.substance.commonNames.any { commonName ->
                                        commonName.startsWith(
                                            prefix = searchText,
                                            ignoreCase = true
                                        )
                                    }
                        }
                    prefixAndContainingMatches.first + prefixAndContainingMatches.second
                }
            }
        }
    }
}

data class CategoryChipModel(
    val chipName: String,
    val color: Color,
    val isActive: Boolean
)

data class SubstanceModel(
    val name: String,
    val commonNames: List<String>,
    val categories: List<CategoryModel>,
    val hasSaferUse: Boolean,
    val hasInteractions: Boolean
)

data class CategoryModel(
    val name: String,
    val color: Color
)
