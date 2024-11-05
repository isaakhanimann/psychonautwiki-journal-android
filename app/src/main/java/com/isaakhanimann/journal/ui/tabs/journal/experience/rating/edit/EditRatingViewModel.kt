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

package com.isaakhanimann.journal.ui.tabs.journal.experience.rating.edit

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
import com.isaakhanimann.journal.ui.main.navigation.graphs.EditRatingRoute
import com.isaakhanimann.journal.ui.utils.getInstant
import com.isaakhanimann.journal.ui.utils.getLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class EditRatingViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {
    private val ratingId: Int
    var selectedRatingOption by mutableStateOf(ShulginRatingOption.TWO_PLUS)
    var localDateTimeFlow = MutableStateFlow(LocalDateTime.now())
    var rating: ShulginRating? = null
    var isOverallRatingFlow = MutableStateFlow(false)

    init {
        val editRatingRoute = state.toRoute<EditRatingRoute>()
        val ratingId = editRatingRoute.ratingId
        this.ratingId = ratingId
        viewModelScope.launch {
            val loadedRating = experienceRepo.getRating(id = ratingId) ?: return@launch
            rating = loadedRating
            loadedRating.time?.also { time ->
                localDateTimeFlow.emit(time.getLocalDateTime())
            } ?: run {
                isOverallRatingFlow.value = true
            }
            selectedRatingOption = loadedRating.option
        }
    }

    fun onChangeTime(newLocalDateTime: LocalDateTime) {
        viewModelScope.launch {
            localDateTimeFlow.emit(newLocalDateTime)
        }
    }

    fun onChangeRating(newRating: ShulginRatingOption) {
        selectedRatingOption = newRating
    }

    fun delete() {
        viewModelScope.launch {
            rating?.let {
                experienceRepo.delete(it)
            }
        }
    }

    fun onDoneTap() {
        viewModelScope.launch {
            val selectedInstant = localDateTimeFlow.firstOrNull()?.getInstant() ?: return@launch
            rating?.let {
                it.time = if (isOverallRatingFlow.value) null else selectedInstant
                it.option = selectedRatingOption
                experienceRepo.update(it)
            }
        }
    }
}