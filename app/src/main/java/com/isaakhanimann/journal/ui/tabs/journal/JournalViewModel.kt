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

package com.isaakhanimann.journal.ui.tabs.journal

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.substances.repositories.SearchRepository
import com.isaakhanimann.journal.ui.tabs.settings.combinations.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

val IS_MIGRATED_0 = booleanPreferencesKey("is_migrated_0")
val IS_MIGRATED_1 = booleanPreferencesKey("is_migrated_1")

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    searchRepository: SearchRepository,
    private val dataStore: DataStore<Preferences>,
    private val userPreferences: UserPreferences,
) : ViewModel() {


    val isTimeRelativeToNow = mutableStateOf(false)

    fun onChangeRelative(isRelative: Boolean) {
        isTimeRelativeToNow.value = isRelative
    }

    val isSearchEnabled = mutableStateOf(false)

    fun resetAddIngestionTimes() = viewModelScope.launch {
        userPreferences.saveLastIngestionTimeOfExperience(null)
        userPreferences.saveClonedIngestionTime(null)
    }

    fun maybeMigrate() {
        viewModelScope.launch {
            dataStore.edit { settings ->
                if (settings[IS_MIGRATED_0] == null) {
                    experienceRepo.migrateBenzydamine()
                    settings[IS_MIGRATED_0] = true
                }
                if (settings[IS_MIGRATED_1] == null) {
                    experienceRepo.migrateCannabisAndMushroomUnits()
                    settings[IS_MIGRATED_1] = true
                }
            }
        }
    }

    fun onChangeOfIsSearchEnabled(newValue: Boolean) {
        if (newValue) {
            isSearchEnabled.value = true
        } else {
            isSearchEnabled.value = false
            viewModelScope.launch {
                searchTextFlow.emit("")
            }
        }
    }

    val isFavoriteEnabledFlow = MutableStateFlow(false)

    fun onChangeFavorite(isFavorite: Boolean) {
        viewModelScope.launch {
            isFavoriteEnabledFlow.emit(isFavorite)
        }
    }

    val searchTextFlow = MutableStateFlow("")

    fun search(newSearchText: String) {
        viewModelScope.launch {
            searchTextFlow.emit(newSearchText)
        }
    }

    val experiences =
        experienceRepo.getSortedExperienceWithIngestionsCompanionsAndRatingsFlow()
            .combine(searchTextFlow) { experiencesWithIngestions, searchText ->
                Pair(first = experiencesWithIngestions, second = searchText)
            }
            .combine(experienceRepo.getCustomSubstancesFlow()) { pair, customSubstances ->
                Pair(first = pair, second = customSubstances)
            }
            .combine(isFavoriteEnabledFlow) { pair, isFavoriteEnabled ->
                var experiencesWithIngestions = pair.first.first
                val searchText = pair.first.second
                val customSubstances = pair.second

                if (isFavoriteEnabled) {
                    // only favorites
                    experiencesWithIngestions =
                        experiencesWithIngestions.filter { it.experience.isFavorite }
                }
                if (searchText.isNotEmpty()) {
                    // collect substances with matching names
                    val matchingSubstances = searchRepository.getMatchingSubstances(
                        searchText = searchText,
                        filterCategories = emptyList(),
                        recentlyUsedSubstanceNamesSorted = emptyList()
                    ).map { it.substance.name } + customSubstances.map { it.name }.filter {
                        it.contains(other = searchText, ignoreCase = true)
                    }

                    // experience title, text or some consumed substance must contain search string or the consumer needs to match
                    experiencesWithIngestions = experiencesWithIngestions.filter {
                        it.experience.title.contains(
                            other = searchText,
                            ignoreCase = true
                        ) || it.ingestionsWithCompanions.any { ingestionWithCompanion ->
                            val isSubstanceAMatch =
                                matchingSubstances.any { name -> name == ingestionWithCompanion.substanceCompanion?.substanceName }
                            val isConsumerAMatch =
                                ingestionWithCompanion.ingestion.consumerName?.contains(
                                    searchText,
                                    ignoreCase = true
                                )
                                    ?: false
                            isSubstanceAMatch || isConsumerAMatch
                        } || it.experience.text.contains(
                            other = searchText,
                            ignoreCase = true
                        )
                    }
                }
                return@combine experiencesWithIngestions
            }
            .stateIn(
                initialValue = emptyList(),
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000)
            )
}