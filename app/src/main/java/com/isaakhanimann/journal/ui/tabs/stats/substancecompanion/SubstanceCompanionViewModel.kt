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

package com.isaakhanimann.journal.ui.tabs.stats.substancecompanion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.CustomUnit
import com.isaakhanimann.journal.data.room.experiences.entities.Experience
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.journal.data.substances.repositories.SubstanceRepository
import com.isaakhanimann.journal.ui.main.navigation.routers.CONSUMER_NAME_KEY
import com.isaakhanimann.journal.ui.main.navigation.routers.SUBSTANCE_NAME_KEY
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.models.CustomUnitDose
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.toReadableString
import com.isaakhanimann.journal.ui.utils.getTimeDifferenceText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class SubstanceCompanionViewModel @Inject constructor(
    experienceRepo: ExperienceRepository,
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
    val consumerName = state.get<String>(CONSUMER_NAME_KEY)

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
        experienceRepo.getSortedIngestionsWithExperienceAndCustomUnitFlow(substanceName)
            .map { list -> list.filter { it.ingestion.consumerName == consumerName } }
            .combine(currentTimeFlow) { sortedIngestionsWithExperiences, currentTime ->
                val experiencesWithIngestions =
                    sortedIngestionsWithExperiences.groupBy { it.ingestion.experienceId }
                var lastDate = currentTime
                val allIngestionBursts: MutableList<IngestionsBurst> = mutableListOf()
                for (oneExperience in experiencesWithIngestions) {
                    val experience = oneExperience.value.firstOrNull()?.experience ?: continue
                    val ingestionsSorted = oneExperience.value.map { IngestionsBurst.IngestionAndCustomUnit(
                        ingestion = it.ingestion,
                        customUnit = it.customUnit
                    ) }.sortedBy { it.ingestion.time }
                    val experienceStart = ingestionsSorted.first().ingestion.time
                    val experienceEnd = ingestionsSorted.last().ingestion.time
                    val diffText = getTimeDifferenceText(
                        fromInstant = experienceEnd,
                        toInstant = lastDate
                    )
                    allIngestionBursts.add(
                        IngestionsBurst(
                            timeUntil = diffText,
                            experience = experience,
                            ingestions = ingestionsSorted
                        )
                    )
                    lastDate = experienceStart
                }
                return@combine allIngestionBursts
            }.stateIn(
                initialValue = emptyList(),
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000)
            )
}

data class IngestionsBurst(
    val timeUntil: String,
    val experience: Experience,
    val ingestions: List<IngestionAndCustomUnit>
) {
    data class IngestionAndCustomUnit(
        val ingestion: Ingestion,
        val customUnit: CustomUnit?
    ) {
        val customUnitDose: CustomUnitDose?
            get() = ingestion.dose?.let { doseUnwrapped ->
                customUnit?.let { customUnitUnwrapped ->
                    CustomUnitDose(
                        dose = doseUnwrapped,
                        isEstimate = ingestion.isDoseAnEstimate,
                        estimatedDoseStandardDeviation = ingestion.estimatedDoseStandardDeviation,
                        customUnit = customUnitUnwrapped
                    )
                }
            }
        val doseDescription: String
            get() = customUnitDose?.doseDescription ?: ingestionDoseDescription

        private val ingestionDoseDescription get() = ingestion.dose?.let { dose ->
            ingestion.estimatedDoseStandardDeviation?.let { estimatedDoseStandardDeviation ->
                "${dose.toReadableString()}±${estimatedDoseStandardDeviation.toReadableString()} ${ingestion.units}"
            } ?: run {
                val description = "${dose.toReadableString()} ${ingestion.units}"
                if (ingestion.isDoseAnEstimate) {
                    "~$description"
                } else {
                    description
                }
            }
        } ?: "Unknown dose"
    }
}