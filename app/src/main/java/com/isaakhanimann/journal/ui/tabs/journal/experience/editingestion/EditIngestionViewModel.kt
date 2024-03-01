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

package com.isaakhanimann.journal.ui.tabs.journal.experience.editingestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.CustomUnit
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.ui.main.navigation.routers.INGESTION_ID_KEY
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.toReadableString
import com.isaakhanimann.journal.ui.utils.getInstant
import com.isaakhanimann.journal.ui.utils.getLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class EditIngestionViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {
    private var ingestionFlow: MutableStateFlow<Ingestion?> = MutableStateFlow(null)
    var ingestion: Ingestion? = null
    var note by mutableStateOf("")
    var isEstimate by mutableStateOf(false)
    var isKnown by mutableStateOf(true)
    var dose by mutableStateOf("")
    var estimatedDoseStandardDeviation by mutableStateOf("")
    var units by mutableStateOf("")
    var experienceId by mutableIntStateOf(1)
    var localDateTimeFlow = MutableStateFlow(LocalDateTime.now())
    var consumerName by mutableStateOf("")
    var customUnit: CustomUnit? by mutableStateOf(null)
    val otherCustomUnits = experienceRepo.getAllCustomUnitsFlow().combine(ingestionFlow) { customUnits, ing ->
        customUnits.filter {customUnit ->
            customUnit.administrationRoute == ing?.administrationRoute && customUnit.substanceName == ing.substanceName && customUnit.id != ing.customUnitId
        }
    }.stateIn(
        initialValue = emptyList(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    fun onChangeEstimatedDoseStandardDeviation(newEstimatedDoseStandardDeviation: String) {
        estimatedDoseStandardDeviation = newEstimatedDoseStandardDeviation
    }

    init {
        val id = state.get<Int>(INGESTION_ID_KEY)!!
        viewModelScope.launch {
            val ingestionAndCustomUnit =
                experienceRepo.getIngestionFlow(id = id).first() ?: return@launch
            val ing = ingestionAndCustomUnit.ingestion
            ingestionFlow.emit(ing)
            ingestion = ing
            note = ing.notes ?: ""
            isEstimate = ing.isDoseAnEstimate
            estimatedDoseStandardDeviation = ing.estimatedDoseStandardDeviation?.toReadableString() ?: ""
            experienceId = ing.experienceId
            dose = ing.dose?.toReadableString() ?: ""
            isKnown = ing.dose != null
            units = ing.units ?: ""
            consumerName = ing.consumerName ?: ""
            localDateTimeFlow.emit(ing.time.getLocalDateTime())
            customUnit = ingestionAndCustomUnit.customUnit
        }
    }

    val sortedConsumerNamesFlow =
        experienceRepo.getSortedIngestions(limit = 200).map { ingestions ->
            return@map ingestions.mapNotNull { it.consumerName }.distinct()
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    fun onChangeCustomUnit(newCustomUnit: CustomUnit?) {
        customUnit = newCustomUnit
        newCustomUnit?.unit?.let {
            units = it
        }
    }

    fun onChangeTime(newLocalDateTime: LocalDateTime) {
        viewModelScope.launch {
            localDateTimeFlow.emit(newLocalDateTime)
        }
    }

    fun onChangeConsumerName(newName: String) {
        consumerName = newName
    }

    fun toggleIsKnown() {
        isKnown = isKnown.not()
    }

    fun onChangeIsEstimate(newIsEstimate: Boolean) {
        isEstimate = newIsEstimate
    }

    val relevantExperiences: StateFlow<List<ExperienceOption>> = localDateTimeFlow.map {
        val selectedInstant = it.getInstant()
        val fromDate = selectedInstant.minus(2, ChronoUnit.DAYS)
        val toDate = selectedInstant.plus(2, ChronoUnit.DAYS)
        return@map experienceRepo.getIngestionsWithExperiencesFlow(fromDate, toDate).firstOrNull()
            ?: emptyList()
    }.map { list ->
        return@map list.map {
            ExperienceOption(id = it.experience.id, title = it.experience.title)
        }.distinct()
    }.stateIn(
        initialValue = emptyList(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    fun onDoneTap() {
        viewModelScope.launch {
            val selectedInstant = localDateTimeFlow.firstOrNull()?.getInstant() ?: return@launch
            ingestion?.let {
                it.notes = note
                it.isDoseAnEstimate = isEstimate
                it.experienceId = experienceId
                it.dose = if (isKnown) dose.toDoubleOrNull() else null
                it.estimatedDoseStandardDeviation = if (isEstimate) estimatedDoseStandardDeviation.toDoubleOrNull() else null
                it.units = units
                it.customUnitId = customUnit?.id
                it.time = selectedInstant
                it.consumerName = consumerName.ifBlank { null }
                experienceRepo.update(it)
            }
        }
    }

    fun deleteIngestion() {
        viewModelScope.launch {
            ingestion?.let {
                experienceRepo.delete(ingestion = it)
            }
        }
    }
}

data class ExperienceOption(
    val id: Int,
    val title: String
)