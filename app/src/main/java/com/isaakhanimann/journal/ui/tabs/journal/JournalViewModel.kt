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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.relations.ExperienceWithIngestionsCompanionsAndRatings
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.time.hourLimitToSeparateIngestions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject


@HiltViewModel
class JournalViewModel @Inject constructor(
    experienceRepo: ExperienceRepository
) : ViewModel() {


    val isTimeRelativeToNow = mutableStateOf(false)

    fun onChangeRelative(isRelative: Boolean) {
        isTimeRelativeToNow.value = isRelative
    }
    val isSearchEnabled = mutableStateOf(false)

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

    val currentAndPreviousExperiences =
        experienceRepo.getSortedExperienceWithIngestionsCompanionsAndRatingsFlow()
            .combine(searchTextFlow) { experiencesWithIngestions, searchText ->
                Pair(first = experiencesWithIngestions, second = searchText)
            }
            .combine(isFavoriteEnabledFlow) { pair, isFavoriteEnabled ->
                val experiencesWithIngestions = pair.first
                val searchText = pair.second
                val filtered = if (searchText.isEmpty() && !isFavoriteEnabled) {
                    if (isFavoriteEnabled) {
                        experiencesWithIngestions.filter { it.experience.isFavorite }
                    } else {
                        experiencesWithIngestions
                    }
                } else {
                    if (isFavoriteEnabled) {
                        experiencesWithIngestions.filter {
                            it.experience.isFavorite && it.experience.title.contains(
                                other = searchText,
                                ignoreCase = true
                            )
                        }
                    } else {
                        experiencesWithIngestions.filter {
                            it.experience.title.contains(
                                other = searchText,
                                ignoreCase = true
                            )
                        }
                    }
                }
                val current = filtered.firstOrNull { experience ->
                    experience.ingestionsWithCompanions.any {
                        it.ingestion.time > Instant.now().minus(
                            hourLimitToSeparateIngestions, ChronoUnit.HOURS
                        )
                    }
                }
                val previous = if (current != null) filtered.drop(1) else filtered
                return@combine CurrentAndPreviousExperiences(
                    currentExperience = current,
                    previousExperiences = previous
                )
            }
            .stateIn(
                initialValue = CurrentAndPreviousExperiences(
                    currentExperience = null,
                    previousExperiences = emptyList()
                ),
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000)
            )
}

data class CurrentAndPreviousExperiences(
    val currentExperience: ExperienceWithIngestionsCompanionsAndRatings?,
    val previousExperiences: List<ExperienceWithIngestionsCompanionsAndRatings>
)