/*
 * Copyright (c) 2022. Isaak Hanimann.
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


    private val recentlyUsedSubstancesFlow: Flow<List<SubstanceWithCategories>> =
        experienceRepo.getLastUsedSubstanceNamesFlow(limit = 100).map { lastUsedSubstanceNames ->
            lastUsedSubstanceNames.mapNotNull {
                substanceRepo.getSubstanceWithCategories(
                    substanceName = it
                )
            }
        }

    private val _searchTextFlow = MutableStateFlow("")
    val searchTextFlow = _searchTextFlow.asStateFlow()

    private val filtersFlow = MutableStateFlow(listOf("common"))

    private val youUsedChipName = "used"
    private val customChipName = "custom"

    fun onFilterTapped(filterName: String) {
        viewModelScope.launch {
            when (filterName) {
                youUsedChipName -> {
                    isShowingYouUsedFlow.emit(isShowingYouUsedFlow.value.not())
                }
                else -> {
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
        }
    }

    private val isShowingYouUsedFlow = MutableStateFlow(false)
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
        }.combine(isShowingYouUsedFlow) { chips, isShowingYouUsed ->
            val newChips = chips.toMutableList()
            newChips.add(
                0, CategoryChipModel(
                    chipName = youUsedChipName,
                    color = Color.Magenta,
                    isActive = isShowingYouUsed
                )
            )
            return@combine newChips
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

    private val allOrYouUsedSubstances =
        recentlyUsedSubstancesFlow.combine(isShowingYouUsedFlow) { recents, isShowingYouUsed ->
            if (isShowingYouUsed) {
                return@combine recents
            } else {
                return@combine substanceRepo.getAllSubstancesWithCategories()
            }
        }

    val filteredSubstancesFlow =
        allOrYouUsedSubstances.combine(filtersFlow) { substances, filters ->
            substances.filter { substanceWithCategories ->
                filters.all { substanceWithCategories.substance.categories.contains(it) }
            }
        }.combine(searchTextFlow) { substances, searchText ->
            getMatchingSubstances(searchText, substances)
        }.map { substancesWithCategories ->
            substancesWithCategories.map {
                SubstanceModel(
                    name = it.substance.name,
                    commonNames = it.substance.commonNames,
                    categories = it.categories.map { category ->
                        CategoryModel(
                            name = category.name,
                            color = category.color
                        )
                    }
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
    val categories: List<CategoryModel>
)

data class CategoryModel(
    val name: String,
    val color: Color
)
