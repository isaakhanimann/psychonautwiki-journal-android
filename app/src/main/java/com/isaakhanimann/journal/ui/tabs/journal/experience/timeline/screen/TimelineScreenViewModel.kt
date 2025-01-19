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
import androidx.navigation.toRoute
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.relations.IngestionWithCompanionAndCustomUnit
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDose
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.journal.data.substances.repositories.SubstanceRepository
import com.isaakhanimann.journal.ui.YOU
import com.isaakhanimann.journal.ui.main.navigation.graphs.TimelineScreenRoute
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.time.hourLimitToSeparateIngestions
import com.isaakhanimann.journal.ui.tabs.journal.experience.ExperienceViewModel
import com.isaakhanimann.journal.ui.tabs.journal.experience.TimelineDisplayOption
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.SavedTimeDisplayOption
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.TimeDisplayOption
import com.isaakhanimann.journal.ui.tabs.journal.experience.models.IngestionElement
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.AllTimelinesModel
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.DataForOneRating
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.DataForOneTimedNote
import com.isaakhanimann.journal.ui.tabs.settings.combinations.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class TimelineScreenViewModel @Inject constructor(
    experienceRepo: ExperienceRepository,
    private val substanceRepo: SubstanceRepository,
    userPreferences: UserPreferences,
    state: SavedStateHandle
) : ViewModel() {

    private val areSubstanceHeightsIndependentFlow =
        userPreferences.areSubstanceHeightsIndependentFlow.stateIn(
            initialValue = false,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    private val timelineRoute = state.toRoute<TimelineScreenRoute>()
    private val experienceID = timelineRoute.experienceId
    val consumerName = timelineRoute.consumerName

    private val ingestionsWithCompanionsFlow =
        experienceRepo.getIngestionsWithCompanionsFlow(experienceID)
            .map { ingestions ->
                ingestions.filter {
                    if (consumerName == YOU) {
                        it.ingestion.consumerName == null
                    } else {
                        it.ingestion.consumerName == consumerName
                    }
                }
            }

    private val ratingsFlow = experienceRepo.getRatingsFlow(experienceID)

    private val timedNotesFlow = experienceRepo.getTimedNotesFlowSorted(experienceID)

    private val currentTimeFlow: Flow<Instant> = flow {
        while (true) {
            emit(Instant.now())
            delay(timeMillis = 1000 * 10)
        }
    }

    private val isCurrentExperienceFlow =
        ingestionsWithCompanionsFlow.combine(currentTimeFlow) { ingestionsWithCompanions, currentTime ->
            val ingestionTimes =
                ingestionsWithCompanions.map { it.ingestion.time }
            val lastIngestionTime = ingestionTimes.maxOrNull() ?: return@combine false
            val limitAgo = currentTime.minus(hourLimitToSeparateIngestions, ChronoUnit.HOURS)
            return@combine limitAgo < lastIngestionTime
        }

    val timeDisplayOptionFlow =
        userPreferences.savedTimeDisplayOptionFlow.combine(isCurrentExperienceFlow) { savedOption: SavedTimeDisplayOption, isCurrentExperience: Boolean ->
            when (savedOption) {
                SavedTimeDisplayOption.AUTO -> if (isCurrentExperience) TimeDisplayOption.RELATIVE_TO_NOW else TimeDisplayOption.REGULAR
                SavedTimeDisplayOption.RELATIVE_TO_NOW -> TimeDisplayOption.RELATIVE_TO_NOW
                SavedTimeDisplayOption.RELATIVE_TO_START -> TimeDisplayOption.RELATIVE_TO_START
                SavedTimeDisplayOption.TIME_BETWEEN -> TimeDisplayOption.TIME_BETWEEN
                SavedTimeDisplayOption.REGULAR -> TimeDisplayOption.REGULAR
            }
        }.stateIn(
            initialValue = TimeDisplayOption.REGULAR,
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

    private val dataForEffectLinesFlow =
        ingestionsWithAssociatedDataFlow.map { ingestionWithAssociatedData ->
            val ingestionElements = getIngestionElements(ingestionWithAssociatedData)
            val substances =
                ingestionElements.mapNotNull { substanceRepo.getSubstance(it.ingestionWithCompanionAndCustomUnit.ingestion.substanceName) }
            ExperienceViewModel.getDataForEffectTimelines(
                ingestionElements = ingestionElements,
                substances = substances
            )
        }

    val timelineDisplayOptionFlow = combine(
        dataForEffectLinesFlow,
        ratingsFlow,
        timedNotesFlow,
        areSubstanceHeightsIndependentFlow
    ) { dataForEffectLines, ratings, timedNotesSorted, areSubstanceHeightsIndependent ->

        val newRatings = if (consumerName == YOU) ratings else emptyList()
        val newTimedNotes = if (consumerName == YOU) timedNotesSorted else emptyList()

        if (dataForEffectLines.isEmpty()) {
            return@combine TimelineDisplayOption.NotWorthDrawing
        } else {
            val dataForRatings = newRatings.mapNotNull {
                val ratingTime = it.time
                return@mapNotNull if (ratingTime == null) {
                    null
                } else {
                    DataForOneRating(
                        time = ratingTime,
                        option = it.option
                    )
                }
            }
            val dataForTimedNotes =
                newTimedNotes.filter { it.isPartOfTimeline }
                    .map {
                        DataForOneTimedNote(time = it.time, color = it.color)
                    }
            val isWorthDrawing =
                dataForEffectLines.isNotEmpty() && !(dataForEffectLines.all { it.roaDuration == null } && newRatings.isEmpty() && newTimedNotes.isEmpty())
            if (isWorthDrawing) {
                val model = AllTimelinesModel(
                    dataForLines = dataForEffectLines,
                    dataForRatings = dataForRatings,
                    timedNotes = dataForTimedNotes,
                    areSubstanceHeightsIndependent = areSubstanceHeightsIndependent
                )
                return@combine TimelineDisplayOption.Shown(model)
            } else {
                return@combine TimelineDisplayOption.NotWorthDrawing
            }
        }
    }.stateIn(
        initialValue = TimelineDisplayOption.Loading,
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