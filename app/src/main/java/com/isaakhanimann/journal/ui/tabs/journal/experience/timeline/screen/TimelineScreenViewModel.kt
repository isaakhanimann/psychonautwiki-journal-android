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

package com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.relations.IngestionWithCompanionAndCustomUnit
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDose
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.journal.data.substances.repositories.SubstanceRepository
import com.isaakhanimann.journal.ui.ME
import com.isaakhanimann.journal.ui.main.navigation.routers.CONSUMER_NAME_KEY
import com.isaakhanimann.journal.ui.main.navigation.routers.EXPERIENCE_ID_KEY
import com.isaakhanimann.journal.ui.tabs.journal.experience.models.IngestionElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TimelineScreenViewModel @Inject constructor(
    experienceRepo: ExperienceRepository,
    private val substanceRepo: SubstanceRepository,
    state: SavedStateHandle
) : ViewModel() {

    private val experienceID = state.get<Int>(EXPERIENCE_ID_KEY)!!
    val consumerName = state.get<String>(CONSUMER_NAME_KEY)!!

    private val ingestionsWithCompanionsFlow = experienceRepo.getIngestionsWithCompanionsFlow(experienceID)
        .map { ingestions ->
            ingestions.filter {
                if (consumerName == ME) {
                    it.ingestion.consumerName == null
                } else {
                    it.ingestion.consumerName == consumerName
                }
            }
        }

    val ratingsFlow =
        experienceRepo.getRatingsFlow(experienceID)
            .stateIn(
                initialValue = emptyList(),
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000)
            )

    val timedNotesFlow =
        experienceRepo.getTimedNotesFlowSorted(experienceID)
            .stateIn(
                initialValue = emptyList(),
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000)
            )


    private val sortedIngestionsWithCompanionsFlow =
        ingestionsWithCompanionsFlow.map { ingestionsWithCompanions ->
            ingestionsWithCompanions.sortedBy { it.ingestion.time }
        }

    private data class IngestionWithAssociatedData(
        val ingestionWithCompanionAndCustomUnit: IngestionWithCompanionAndCustomUnit,
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
                    ingestionWithCompanionAndCustomUnit = oneIngestionWithComp,
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

    private fun getIngestionElements(sortedIngestionsWith: List<IngestionWithAssociatedData>): List<IngestionElement> {
        return sortedIngestionsWith.map { ingestionWith ->
            val numDots =
                ingestionWith.roaDose?.getNumDots(
                    ingestionWith.ingestionWithCompanionAndCustomUnit.ingestion.dose,
                    ingestionUnits = ingestionWith.ingestionWithCompanionAndCustomUnit.ingestion.units
                )
            IngestionElement(
                ingestionWithCompanionAndCustomUnit = ingestionWith.ingestionWithCompanionAndCustomUnit,
                roaDuration = ingestionWith.roaDuration,
                numDots = numDots
            )
        }
    }
}