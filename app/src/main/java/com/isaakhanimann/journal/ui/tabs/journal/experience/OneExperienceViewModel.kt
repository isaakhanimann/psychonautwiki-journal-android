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

package com.isaakhanimann.journal.ui.tabs.journal.experience

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDose
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.journal.data.substances.repositories.SubstanceRepository
import com.isaakhanimann.journal.ui.main.navigation.routers.EXPERIENCE_ID_KEY
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.interactions.Interaction
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.interactions.InteractionChecker
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.time.hourLimitToSeparateIngestions
import com.isaakhanimann.journal.ui.tabs.settings.combinations.CombinationSettingsStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class OneExperienceViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    private val substanceRepo: SubstanceRepository,
    private val interactionChecker: InteractionChecker,
    combinationSettingsStorage: CombinationSettingsStorage,
    state: SavedStateHandle
) : ViewModel() {

    fun saveIsFavorite(isFavorite: Boolean) {
        viewModelScope.launch {
            val experience = experienceWithIngestionsFlow.firstOrNull()?.experience
            if (experience != null) {
                experience.isFavorite = isFavorite
                experienceRepo.update(experience)
            }
        }
    }

    private val experienceId = state.get<Int>(EXPERIENCE_ID_KEY)!!

    val experienceWithIngestionsFlow =
        experienceRepo.getExperienceWithIngestionsAndCompanionsFlow(experienceId)
            .stateIn(
                initialValue = null,
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000)
            )

    val isFavoriteFlow = experienceRepo.getExperienceFlow(experienceId).map { it?.isFavorite ?: false }.stateIn(
        initialValue = false,
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    private val currentTimeFlow: Flow<Instant> = flow {
        while (true) {
            emit(Instant.now())
            delay(timeMillis = 1000 * 10)
        }
    }

    val isShowingAddIngestionButtonFlow =
        experienceWithIngestionsFlow.combine(currentTimeFlow) { experienceWithIngestions, currentTime ->
            val ingestionTimes =
                experienceWithIngestions?.ingestionsWithCompanions?.map { it.ingestion.time }
            val lastIngestionTime = ingestionTimes?.maxOrNull() ?: return@combine false
            val limitAgo = currentTime.minus(hourLimitToSeparateIngestions, ChronoUnit.HOURS)
            return@combine limitAgo < lastIngestionTime
        }.stateIn(
            initialValue = false,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    private val sortedIngestionsWithCompanionsFlow =
        experienceWithIngestionsFlow.map { experience ->
            experience?.ingestionsWithCompanions?.sortedBy { it.ingestion.time } ?: emptyList()
        }

    private data class IngestionWithAssociatedData(
        val ingestionWithCompanion: IngestionWithCompanion,
        val roaDuration: RoaDuration?,
        val roaDose: RoaDose?
    )

    private val ingestionsWithAssociatedDataFlow: Flow<List<IngestionWithAssociatedData>> =
        sortedIngestionsWithCompanionsFlow.map { ingestionsWithComps ->
            ingestionsWithComps.map { oneIngestionWithComp ->
                val ingestion = oneIngestionWithComp.ingestion
                val roa = substanceRepo.getSubstance(oneIngestionWithComp.ingestion.substanceName)
                    ?.getRoa(ingestion.administrationRoute)
                val roaDuration = roa?.roaDuration
                IngestionWithAssociatedData(
                    ingestionWithCompanion = oneIngestionWithComp,
                    roaDuration = roaDuration,
                    roaDose = roa?.roaDose
                )
            }
        }

    val ingestionElementsFlow = ingestionsWithAssociatedDataFlow.map {
        getIngestionElements(it)
    }.stateIn(
        initialValue = emptyList(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    val cumulativeDosesFlow = ingestionsWithAssociatedDataFlow.map {
        getCumulativeDoses(it)
    }.stateIn(
        initialValue = emptyList(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    private fun getIngestionElements(sortedIngestionsWith: List<IngestionWithAssociatedData>): List<IngestionElement> {
        return sortedIngestionsWith.map { ingestionWith ->
            val numDots =
                ingestionWith.roaDose?.getNumDots(
                    ingestionWith.ingestionWithCompanion.ingestion.dose,
                    ingestionUnits = ingestionWith.ingestionWithCompanion.ingestion.units
                )
            IngestionElement(
                ingestionWithCompanion = ingestionWith.ingestionWithCompanion,
                roaDuration = ingestionWith.roaDuration,
                numDots = numDots
            )
        }
    }

    val interactionsFlow =
        sortedIngestionsWithCompanionsFlow.combine(combinationSettingsStorage.enabledInteractionsFlow) { ingestions, enabledInteractions ->
            val interactionsToCheck =
                ingestions.map { it.ingestion.substanceName }.plus(enabledInteractions).distinct()
            return@combine interactionsToCheck.flatMapIndexed { index: Int, interaction: String ->
                return@flatMapIndexed interactionsToCheck.drop(index + 1).mapNotNull { other ->
                    interactionChecker.getInteractionBetween(
                        interaction,
                        other
                    )
                }
            }.sortedByDescending { it.interactionType.dangerCount }
        }
            .flowOn(Dispatchers.IO) // if this wasn't on the background the navigation from journal screen to this screen would jump
            .stateIn(
                initialValue = emptyList(),
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000)
            )

    val interactionExplanationsFlow = interactionsFlow.map { interactions ->
        interactions.flatMap {
            listOf(it.aName, it.bName)
        }.distinct().mapNotNull {
            val substance = substanceRepo.getSubstance(substanceName = it) ?: return@mapNotNull null
            return@mapNotNull InteractionExplanation(
                name = substance.name,
                url = substance.interactionExplanationURL
            )
        }
    }.stateIn(
        initialValue = emptyList(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    fun deleteExperience() {
        viewModelScope.launch {
            experienceWithIngestionsFlow.firstOrNull()?.let {
                experienceRepo.delete(it)
            }
        }
    }

    private companion object {
        fun getCumulativeDoses(ingestions: List<IngestionWithAssociatedData>): List<CumulativeDose> {
            return ingestions.groupBy { it.ingestionWithCompanion.ingestion.substanceName }
                .mapNotNull { grouped ->
                    val groupedIngestions = grouped.value
                    if (groupedIngestions.size <= 1) return@mapNotNull null
                    if (groupedIngestions.any { it.ingestionWithCompanion.ingestion.dose == null }) return@mapNotNull null
                    val units = groupedIngestions.first().ingestionWithCompanion.ingestion.units
                    if (groupedIngestions.any { it.ingestionWithCompanion.ingestion.units != units }) return@mapNotNull null
                    val isEstimate =
                        groupedIngestions.any { it.ingestionWithCompanion.ingestion.isDoseAnEstimate }
                    val cumulativeDose =
                        groupedIngestions.mapNotNull { it.ingestionWithCompanion.ingestion.dose }
                            .sum()
                    val numDots = groupedIngestions.first().roaDose?.getNumDots(
                        ingestionDose = cumulativeDose,
                        ingestionUnits = units
                    )
                    CumulativeDose(
                        substanceName = grouped.key,
                        cumulativeDose = cumulativeDose,
                        units = units,
                        isEstimate = isEstimate,
                        numDots = numDots
                    )
                }
        }
    }
}

data class OneExperienceScreenModel(
    val isFavorite: Boolean,
    val title: String,
    val firstIngestionTime: Instant,
    val notes: String,
    val isShowingAddIngestionButton: Boolean,
    val ingestionElements: List<IngestionElement>,
    val cumulativeDoses: List<CumulativeDose>,
    val interactions: List<Interaction>,
    val interactionExplanations: List<InteractionExplanation>
)

data class CumulativeDose(
    val substanceName: String,
    val cumulativeDose: Double,
    val units: String?,
    val isEstimate: Boolean,
    val numDots: Int?
)

data class IngestionElement(
    val ingestionWithCompanion: IngestionWithCompanion,
    val roaDuration: RoaDuration?,
    val numDots: Int?
)

data class InteractionExplanation(
    val name: String,
    val url: String
)