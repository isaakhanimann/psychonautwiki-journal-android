/*
 * Copyright (c) 2024. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.settings.customunits.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.CustomUnit
import com.isaakhanimann.journal.data.substances.classes.Substance
import com.isaakhanimann.journal.data.substances.classes.roa.DoseClass
import com.isaakhanimann.journal.data.substances.repositories.SubstanceRepository
import com.isaakhanimann.journal.ui.main.navigation.graphs.FinishAddCustomUnitRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FinishAddCustomUnitViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    substanceRepository: SubstanceRepository,
    state: SavedStateHandle
) : ViewModel() {
    private val finishAddCustomUnitRoute = state.toRoute<FinishAddCustomUnitRoute>()
    var substanceName by mutableStateOf("")
    val administrationRoute = finishAddCustomUnitRoute.administrationRoute

    var substance by mutableStateOf<Substance?>(null)
    val roaDose get() = substance?.getRoa(administrationRoute)?.roaDose

    var name by mutableStateOf("")

    val currentDoseClass: DoseClass? get() = roaDose?.getDoseClass(ingestionDose = dose)

    fun onChangeOfName(newName: String) {
        name = newName
    }

    var unit by mutableStateOf("")

    fun onChangeOfUnit(newUnit: String) {
        unit = newUnit
        if (newUnit != "mg" && newUnit != "g" && newUnit.lowercase() != "ml" && newUnit.lastOrNull() != 's') {
            unitPlural = newUnit + "s"
        }
    }

    var unitPlural by mutableStateOf("")

    fun onChangeOfUnitPlural(newUnit: String) {
        unitPlural = newUnit
    }

    var originalUnit by mutableStateOf("mg")

    fun onChangeOfOriginalUnit(newUnit: String) {
        originalUnit = newUnit
    }

    var doseText by mutableStateOf("")
    fun onChangeOfDose(newDose: String) {
        doseText = newDose
    }
    val dose: Double? get() = doseText.toDoubleOrNull()

    var estimatedDoseDeviationText by mutableStateOf("")
    fun onChangeOfEstimatedDoseDeviation(newEstimatedDoseDeviation: String) {
        estimatedDoseDeviationText = newEstimatedDoseDeviation
    }
    private val estimatedDoseDeviation: Double? get() = estimatedDoseDeviationText.toDoubleOrNull()

    var isEstimate by mutableStateOf(false)
    fun onChangeOfIsEstimate(newIsEstimate: Boolean) {
        isEstimate = newIsEstimate
    }

    var isArchived by mutableStateOf(false)
    fun onChangeOfIsArchived(newIsArchived: Boolean) {
        isArchived = newIsArchived
    }

    var note by mutableStateOf("")

    fun onChangeOfNote(newNote: String) {
        note = newNote
    }

    init {
        originalUnit = roaDose?.units ?: ""
        viewModelScope.launch {
            if (finishAddCustomUnitRoute.substanceName != null) {
                substanceName = finishAddCustomUnitRoute.substanceName
                substance = substanceRepository.getSubstance(finishAddCustomUnitRoute.substanceName)
                originalUnit = roaDose?.units ?: "mg"
            } else if (finishAddCustomUnitRoute.customSubstanceId != null) {
                val customSubstance = experienceRepo.getCustomSubstanceFlow(finishAddCustomUnitRoute.customSubstanceId).first()
                if (customSubstance != null) {
                    substanceName = customSubstance.name
                    originalUnit = customSubstance.units
                }
            }
        }
    }

    fun createSaveAndDismissAfter(dismiss: (customUnitId: Int) -> Unit) {
        viewModelScope.launch {
            val customUnit = CustomUnit(
                substanceName = substanceName,
                name = name,
                administrationRoute = administrationRoute,
                dose = dose,
                isEstimate = isEstimate,
                estimatedDoseStandardDeviation = if (isEstimate) estimatedDoseDeviation else null,
                isArchived = isArchived,
                unit = unit,
                unitPlural = unitPlural,
                originalUnit = originalUnit,
                note = note
            )
            val customUnitId = experienceRepo.insert(customUnit)
            withContext(Dispatchers.Main) {
                dismiss(customUnitId)
            }
        }
    }
}