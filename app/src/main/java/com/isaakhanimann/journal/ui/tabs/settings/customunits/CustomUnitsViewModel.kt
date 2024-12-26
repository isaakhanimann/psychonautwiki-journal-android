/*
 * Copyright (c) 2024. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.settings.customunits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomUnitsViewModel @Inject constructor(
    experienceRepository: ExperienceRepository,
) : ViewModel() {

    private val _searchTextFlow = MutableStateFlow("")
    val searchTextFlow = _searchTextFlow.asStateFlow()

    fun onSearch(searchText: String) = viewModelScope.launch {
        _searchTextFlow.emit(searchText)
    }

    private val customUnitsFlow = experienceRepository.getCustomUnitsFlow(false)

    val filteredCustomUnitsFlow =
        customUnitsFlow.combine(searchTextFlow) { customUnits, searchText ->
            if (searchText.isEmpty()) {
                return@combine customUnits
            }
            return@combine customUnits.filter { custom ->
                custom.name.contains(
                    other = searchText, ignoreCase = true
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
}