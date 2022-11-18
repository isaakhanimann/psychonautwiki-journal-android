/*
 * Copyright (c) 2022. Free Software Foundation, Inc.
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

package com.isaakhanimann.journal.ui.tabs.stats.substancecompanion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.Experience
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.journal.data.substances.repositories.SubstanceRepository
import com.isaakhanimann.journal.ui.main.navigation.routers.SUBSTANCE_NAME_KEY
import com.isaakhanimann.journal.ui.utils.getTimeDifferenceText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class SubstanceCompanionViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    substanceRepo: SubstanceRepository,
    state: SavedStateHandle
) : ViewModel() {

    private val currentTimeFlow: Flow<Instant> = flow {
        while (true) {
            emit(Instant.now())
            delay(timeMillis = 1000 * 10)
        }
    }

    private val substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!

    private val substance = substanceRepo.getSubstance(substanceName)
    val tolerance = substance?.tolerance
    val crossTolerances = substance?.crossTolerances ?: emptyList()

    val thisCompanionFlow: StateFlow<SubstanceCompanion?> =
        experienceRepo.getSubstanceCompanionFlow(substanceName).stateIn(
            initialValue = null,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    val ingestionBurstsFlow: StateFlow<List<IngestionsBurst>> =
        experienceRepo.getSortedIngestionsWithExperienceFlow(substanceName)
            .combine(currentTimeFlow) { sortedIngestionsWithExperiences, currentTime ->
                val experiencesWithIngestions =
                    sortedIngestionsWithExperiences.groupBy { it.experience.id }
                var lastDate = currentTime
                val allIngestionBursts: MutableList<IngestionsBurst> = mutableListOf()
                for (oneExperience in experiencesWithIngestions) {
                    val experience = oneExperience.value.firstOrNull()?.experience ?: continue
                    val newInstant = experience.sortDate
                    val diffText = getTimeDifferenceText(
                        fromInstant = newInstant,
                        toInstant = lastDate
                    )
                    allIngestionBursts.add(
                        IngestionsBurst(
                            timeUntil = diffText,
                            experience = experience,
                            ingestions = oneExperience.value.map { it.ingestion }
                        )
                    )
                    lastDate = newInstant
                }
                return@combine allIngestionBursts
            }.stateIn(
                initialValue = emptyList(),
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000)
            )

    private val companionsFlow = experienceRepo.getAllSubstanceCompanionsFlow()

    val alreadyUsedColorsFlow: StateFlow<List<AdaptiveColor>> =
        companionsFlow.map { companions ->
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


    fun updateColor(color: AdaptiveColor) {
        viewModelScope.launch {
            thisCompanionFlow.value?.let {
                it.color = color
                experienceRepo.update(substanceCompanion = it)
            }
        }
    }
}

data class IngestionsBurst(
    val timeUntil: String,
    val experience: Experience,
    val ingestions: List<Ingestion>
)