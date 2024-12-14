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

package com.isaakhanimann.journal.ui.tabs.settings.customunits.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.CustomUnit
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.data.substances.classes.roa.DoseClass
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDose
import com.isaakhanimann.journal.data.substances.repositories.SubstanceRepository
import com.isaakhanimann.journal.ui.main.navigation.graphs.EditCustomUnitRoute
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.toReadableString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditCustomUnitViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    substanceRepository: SubstanceRepository,
    state: SavedStateHandle
) : ViewModel() {

    private var customUnit: CustomUnit? = null
    init {
        val customUnitRoute: EditCustomUnitRoute = state.toRoute()
        val customUnitId = customUnitRoute.customUnitId
        viewModelScope.launch {
            val customUnit = experienceRepo.getCustomUnit(customUnitId)
            this@EditCustomUnitViewModel.customUnit = customUnit
            if (customUnit != null) {
                substanceName = customUnit.substanceName
                administrationRoute = customUnit.administrationRoute
                val substance = substanceRepository.getSubstance(customUnit.substanceName)
                roaDose = substance?.getRoa(customUnit.administrationRoute)?.roaDose
                originalUnit = customUnit.originalUnit
                name = customUnit.name
                val pluralizableUnit = customUnit.getPluralizableUnit()
                unit = customUnit.unit
                unitPlural = pluralizableUnit.plural
                doseText = customUnit.dose?.toReadableString() ?: ""
                estimatedDoseDeviationText = customUnit.estimatedDoseStandardDeviation?.toReadableString() ?: ""
                isEstimate = customUnit.isEstimate
                isArchived = customUnit.isArchived
                note = customUnit.note
            }
        }
    }

    var roaDose: RoaDose? = null

    var name by mutableStateOf("")

    var substanceName by mutableStateOf("")
    var administrationRoute by mutableStateOf(AdministrationRoute.ORAL)

    val currentDoseClass: DoseClass? get() = roaDose?.getDoseClass(ingestionDose = dose)

    fun onChangeOfName(newName: String) {
        name = newName
    }

    var unit by mutableStateOf("")

    fun onChangeOfUnit(newUnit: String) {
        unit = newUnit
    }

    var unitPlural by mutableStateOf("")

    fun onChangeOfUnitPlural(newUnit: String) {
        unitPlural = newUnit
    }

    var originalUnit by mutableStateOf("")

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
    val estimatedDoseStandardDeviation: Double? get() = estimatedDoseDeviationText.toDoubleOrNull()


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

    fun updateAndDismissAfter(dismiss: () -> Unit) {
        viewModelScope.launch {
            customUnit?.let {
                it.dose = dose
                it.name = name
                it.isEstimate = isEstimate
                it.originalUnit = originalUnit
                it.estimatedDoseStandardDeviation = if (isEstimate) estimatedDoseStandardDeviation else null
                it.isArchived = isArchived
                it.unit = unit
                it.note = note
                experienceRepo.update(it)
            }
            withContext(Dispatchers.Main) {
                dismiss()
            }
        }
    }

    fun deleteCustomUnit(dismiss: () -> Unit) {
        viewModelScope.launch {
            customUnit?.let {
                experienceRepo.delete(customUnit = it)
            }
            withContext(Dispatchers.Main) {
                dismiss()
            }
        }
    }
}