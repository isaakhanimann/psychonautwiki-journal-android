/*
 * Copyright (c) 2023. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.settings.colors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubstanceColorsViewModel @Inject constructor(
    private val experienceRepository: ExperienceRepository,
) : ViewModel() {

    private val _substanceCompanionsFlow = MutableStateFlow<List<SubstanceCompanion>>(emptyList())
    val substanceCompanionsFlow: StateFlow<List<SubstanceCompanion>> = _substanceCompanionsFlow

    init {
        viewModelScope.launch {
            experienceRepository.getAllSubstanceCompanionsFlow().collect {
                _substanceCompanionsFlow.value = it
            }
        }
    }

    fun deleteUnusedSubstanceCompanions() = viewModelScope.launch {
        experienceRepository.deleteUnusedSubstanceCompanions()
    }

    fun updateColor(color: AdaptiveColor, substanceName: String) {
        viewModelScope.launch {
            val updatedList = _substanceCompanionsFlow.value.map { companion ->
                if (companion.substanceName == substanceName) {
                    companion.copy(color = color).also {
                        experienceRepository.update(it)
                    }
                } else {
                    companion
                }
            }
            _substanceCompanionsFlow.value = updatedList
        }
    }

    val alreadyUsedColorsFlow: StateFlow<List<AdaptiveColor>> =
        _substanceCompanionsFlow.map { companions ->
            companions.map { it.color }.distinct()
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    val otherColorsFlow: StateFlow<List<AdaptiveColor>> =
        alreadyUsedColorsFlow.map { alreadyUsedColors ->
            AdaptiveColor.values().filter {
                !alreadyUsedColors.contains(it)
            }
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

}