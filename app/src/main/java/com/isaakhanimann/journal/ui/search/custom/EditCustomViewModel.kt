/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.search.custom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.CustomSubstance
import com.isaakhanimann.journal.ui.main.navigation.routers.SUBSTANCE_NAME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCustomViewModel @Inject constructor(
    val experienceRepo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {

    var id = 0
    var name by mutableStateOf("")
    var units by mutableStateOf("")
    var description by mutableStateOf("")

    val isValid get() = name.isNotBlank() && units.isNotBlank()

    init {
        val substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!
        viewModelScope.launch {
            val customSubstance =
                experienceRepo.getCustomSubstanceFlow(substanceName).firstOrNull() ?: return@launch
            id = customSubstance.id
            name = customSubstance.name
            units = customSubstance.units
            description = customSubstance.description
        }
    }

    fun onDoneTap() {
        viewModelScope.launch {
            val customSubstance = CustomSubstance(
                id,
                name,
                units,
                description
            )
            experienceRepo.insert(customSubstance)
        }
    }

    fun deleteCustomSubstance() {
        viewModelScope.launch {
            experienceRepo.delete(CustomSubstance(id, name, units, description))
        }
    }
}