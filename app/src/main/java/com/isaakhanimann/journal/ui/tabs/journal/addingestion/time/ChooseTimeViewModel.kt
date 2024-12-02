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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.time

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.Experience
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.journal.data.room.experiences.relations.ExperienceWithIngestions
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.main.navigation.graphs.ChooseTimeRoute
import com.isaakhanimann.journal.ui.tabs.settings.combinations.UserPreferences
import com.isaakhanimann.journal.ui.utils.getInstant
import com.isaakhanimann.journal.ui.utils.getLocalDateTime
import com.isaakhanimann.journal.ui.utils.getStringOfPattern
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

const val hourLimitToSeparateIngestions: Long = 12

@HiltViewModel
class ChooseTimeViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    userPreferences: UserPreferences,
    state: SavedStateHandle
) : ViewModel() {
    var substanceName by mutableStateOf("")
    val localDateTimeFlow = MutableStateFlow(LocalDateTime.now())
    val experiencesInRangeFlow = MutableStateFlow<List<ExperienceWithIngestions>>(emptyList())
    val selectedExperienceFlow = MutableStateFlow<ExperienceWithIngestions?>(null)
    var enteredTitle by mutableStateOf(LocalDateTime.now().getStringOfPattern("dd MMMM yyyy"))
    val isEnteredTitleOk get() = enteredTitle.isNotEmpty()
    var consumerName by mutableStateOf("")


    private val sortedExperiencesFlow = experienceRepo.getSortedExperiencesWithIngestionsFlow()

    val sortedConsumerNamesFlow =
        experienceRepo.getSortedIngestions(limit = 200).map { ingestions ->
            return@map ingestions.mapNotNull { it.consumerName }.distinct()
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    var isLoadingColor by mutableStateOf(true)
    var isShowingColorPicker by mutableStateOf(false)
    var selectedColor by mutableStateOf(AdaptiveColor.BLUE)
    var note by mutableStateOf("")
    private var hasTitleBeenChanged = false

    fun changeTitle(newTitle: String) {
        enteredTitle = newTitle
        hasTitleBeenChanged = true
    }

    fun changeConsumerName(newName: String) {
        consumerName = newName
    }

    val previousNotesFlow: StateFlow<List<String>> =
        experienceRepo.getSortedIngestionsFlow(substanceName, limit = 10).map { list ->
            list.mapNotNull { it.notes }.filter { it.isNotBlank() }.distinct()
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    private val administrationRoute: AdministrationRoute
    private val dose: Double?
    private val units: String?
    private val isEstimate: Boolean
    private val estimatedDoseStandardDeviation: Double?
    private val customUnitId: Int?
    private var substanceCompanion: SubstanceCompanion? = null

    private val companionFlow = experienceRepo.getAllSubstanceCompanionsFlow()

    val alreadyUsedColorsFlow: StateFlow<List<AdaptiveColor>> =
        companionFlow.map { companions ->
            companions.map { it.color }.distinct()
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    val otherColorsFlow: StateFlow<List<AdaptiveColor>> =
        alreadyUsedColorsFlow.map { alreadyUsedColors ->
            AdaptiveColor.entries.filter {
                !alreadyUsedColors.contains(it)
            }
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    private val newExperienceIdToUseFlow: Flow<Int> = sortedExperiencesFlow.map { experiences ->
        val previousMax = experiences.maxOfOrNull { it.experience.id } ?: 1
        return@map previousMax + 1
    }

    init {
        val chooseTimeRoute = state.toRoute<ChooseTimeRoute>()
        substanceName = chooseTimeRoute.substanceName ?: ""
        administrationRoute = chooseTimeRoute.administrationRoute
        dose = chooseTimeRoute.dose
        estimatedDoseStandardDeviation = chooseTimeRoute.estimatedDoseStandardDeviation
        customUnitId = chooseTimeRoute.customUnitId
        units = chooseTimeRoute.units?.let {
            if (it == "null") {
                null
            } else {
                it
            }
        }
        isEstimate = chooseTimeRoute.isEstimate
        val customSubstanceId = chooseTimeRoute.customSubstanceId
        viewModelScope.launch {
            val lastIngestionTimeOfExperience = userPreferences.lastIngestionTimeOfExperienceFlow.first()
            val clonedIngestionTime = userPreferences.clonedIngestionTimeFlow.first()
            if (clonedIngestionTime != null) {
                localDateTimeFlow.emit(clonedIngestionTime.getLocalDateTime())
                updateTitleBasedOnTime(clonedIngestionTime)
            } else if (lastIngestionTimeOfExperience != null) {
                val wasLastIngestionOfExperienceMoreThan20HoursAgo = lastIngestionTimeOfExperience < Instant.now().minus(20, ChronoUnit.HOURS)
                if (wasLastIngestionOfExperienceMoreThan20HoursAgo) {
                    localDateTimeFlow.emit(lastIngestionTimeOfExperience.getLocalDateTime())
                    updateTitleBasedOnTime(lastIngestionTimeOfExperience)
                }
            }
            updateExperiencesBasedOnSelectedTime()
            if (customSubstanceId != null) {
                val customSubstance =
                    experienceRepo.getCustomSubstanceFlow(customSubstanceId).firstOrNull()
                if (customSubstance != null) {
                    substanceName = customSubstance.name
                }
            }
            val allCompanions = experienceRepo.getAllSubstanceCompanionsFlow().first()
            val thisCompanion = allCompanions.firstOrNull { it.substanceName == substanceName }
            substanceCompanion = thisCompanion
            if (thisCompanion == null) {
                isShowingColorPicker = true
                val alreadyUsedColors = allCompanions.map { it.color }
                val otherColors = AdaptiveColor.entries.filter { !alreadyUsedColors.contains(it) }
                selectedColor = otherColors.filter { it.isPreferred }.randomOrNull()
                    ?: otherColors.randomOrNull() ?: AdaptiveColor.entries.random()
            } else {
                selectedColor = thisCompanion.color
            }
            isLoadingColor = false
        }
    }

    fun onChangeOfSelectedExperience(experienceWithIngestions: ExperienceWithIngestions?) = viewModelScope.launch {
        selectedExperienceFlow.emit(experienceWithIngestions)
    }

    fun onChangeDateOrTime(newLocalDateTime: LocalDateTime) {
        viewModelScope.launch {
            localDateTimeFlow.emit(newLocalDateTime)
            updateExperiencesBasedOnSelectedTime()
            val ingestionTime = newLocalDateTime.atZone(ZoneId.systemDefault()).toInstant()
            if (!hasTitleBeenChanged) {
                updateTitleBasedOnTime(ingestionTime)
            }
        }
    }

    private fun updateTitleBasedOnTime(time: Instant) {
        enteredTitle = time.getStringOfPattern("dd MMMM yyyy")
    }

    private suspend fun updateExperiencesBasedOnSelectedTime() {
        val selectedInstant = localDateTimeFlow.value.getInstant()
        val fromInstant = selectedInstant.minus(3, ChronoUnit.DAYS)
        val toInstant = selectedInstant.plus(1, ChronoUnit.DAYS)
        val experiencesInRange =
            experienceRepo.getSortedExperiencesWithIngestionsWithSortDateBetween(
                fromInstant = fromInstant,
                toInstant = toInstant
            )
        experiencesInRangeFlow.emit(experiencesInRange)
        val closestExperience = experiencesInRange.firstOrNull { experience ->
            val sortedIngestions = experience.ingestions.sortedBy { it.time }
            val firstIngestionTime =
                sortedIngestions.firstOrNull()?.time ?: return@firstOrNull false
            val upperBoundBasedOnFirstIngestion = firstIngestionTime.plus(15, ChronoUnit.HOURS)
            val lastIngestionTime = sortedIngestions.lastOrNull()?.time ?: return@firstOrNull false
            val upperBoundBasedOnLastIngestion = lastIngestionTime.plus(3, ChronoUnit.HOURS)
            val finalUpperBound = maxOf(upperBoundBasedOnFirstIngestion, upperBoundBasedOnLastIngestion)
            val lowerBound = firstIngestionTime.minus(3, ChronoUnit.HOURS)
            return@firstOrNull selectedInstant in lowerBound..finalUpperBound
        }
        selectedExperienceFlow.emit(closestExperience)
    }

    fun createSaveAndDismissAfter(dismiss: () -> Unit) {
        viewModelScope.launch {
            createAndSaveIngestion()
            withContext(Dispatchers.Main) {
                dismiss()
            }
        }
    }

    private suspend fun createAndSaveIngestion() {
        val substanceCompanion = SubstanceCompanion(
            substanceName,
            color = selectedColor
        )
        val oldIdToUse = selectedExperienceFlow.firstOrNull()?.experience?.id
        if (oldIdToUse == null) {
            val newIdToUse = newExperienceIdToUseFlow.firstOrNull() ?: 1
            val ingestionTime = localDateTimeFlow.first().atZone(ZoneId.systemDefault()).toInstant()
            val newExperience = Experience(
                id = newIdToUse,
                title = enteredTitle,
                text = "",
                creationDate = Instant.now(),
                sortDate = ingestionTime,
                location = null // todo: allow to add real location
            )
            val newIngestion = createNewIngestion(newExperience.id)
            experienceRepo.insertIngestionExperienceAndCompanion(
                ingestion = newIngestion,
                experience = newExperience,
                substanceCompanion = substanceCompanion
            )
        } else {
            val newIngestion = createNewIngestion(oldIdToUse)
            experienceRepo.insertIngestionAndCompanion(
                ingestion = newIngestion,
                substanceCompanion = substanceCompanion
            )
        }
    }

    private suspend fun createNewIngestion(experienceId: Int) = Ingestion(
        substanceName = substanceName,
        time = localDateTimeFlow.first().atZone(ZoneId.systemDefault()).toInstant(),
        administrationRoute = administrationRoute,
        dose = dose,
        isDoseAnEstimate = isEstimate,
        estimatedDoseStandardDeviation = estimatedDoseStandardDeviation,
        units = units,
        experienceId = experienceId,
        notes = note,
        stomachFullness = null, // todo: allow to add real stomach fullness
        consumerName = consumerName.ifBlank {
            null
        },
        customUnitId = customUnitId
    )
}