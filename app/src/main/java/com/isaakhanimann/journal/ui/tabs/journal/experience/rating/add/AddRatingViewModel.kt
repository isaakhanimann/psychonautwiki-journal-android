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

package com.isaakhanimann.journal.ui.tabs.journal.experience.rating.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.ShulginRating
import com.isaakhanimann.journal.data.room.experiences.entities.ShulginRatingOption
import com.isaakhanimann.journal.ui.main.navigation.graphs.AddRatingRoute
import com.isaakhanimann.journal.ui.utils.getInstant
import com.isaakhanimann.journal.ui.utils.getLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class AddRatingViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {
    var selectedRating by mutableStateOf(ShulginRatingOption.TWO_PLUS)
    var isThisOverallRating by mutableStateOf(false)
    val experienceId = state.toRoute<AddRatingRoute>().experienceId
    var localDateTimeFlow = MutableStateFlow(LocalDateTime.now())

    var isThereAlreadyAnOverallRatingFlow = experienceRepo.getRatingsFlow(experienceId).map { ratings ->
        ratings.any { it.time == null }
    }.stateIn(
        initialValue = true,
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    init {
        viewModelScope.launch {
            val experience = experienceRepo.getExperience(id = experienceId) ?: return@launch
            val isOldExperience = experience.sortDate.isBefore(Instant.now().minus(12, ChronoUnit.HOURS))
            if (isOldExperience) {
                localDateTimeFlow.emit(experience.sortDate.getLocalDateTime())
            }
        }
    }

    fun onChangeTime(newLocalDateTime: LocalDateTime) {
        viewModelScope.launch {
            localDateTimeFlow.emit(newLocalDateTime)
        }
    }

    fun onChangeRating(newRating: ShulginRatingOption) {
        selectedRating = newRating
    }

    fun onDoneTap() {
        viewModelScope.launch {
            val selectedInstant = if (isThisOverallRating) {
                null
            } else {
                localDateTimeFlow.firstOrNull()?.getInstant()
            }
            val newRating = ShulginRating(
                time = selectedInstant,
                creationDate = Instant.now(),
                option = selectedRating,
                experienceId = experienceId
            )
            experienceRepo.insert(newRating)
        }
    }
}