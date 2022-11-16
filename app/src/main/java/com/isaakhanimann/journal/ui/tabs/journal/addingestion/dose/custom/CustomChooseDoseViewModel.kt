/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.dose.custom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.main.navigation.routers.ADMINISTRATION_ROUTE_KEY
import com.isaakhanimann.journal.ui.main.navigation.routers.SUBSTANCE_NAME_KEY
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.toReadableString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CustomChooseDoseViewModel @Inject constructor(
    experienceRepository: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {
    val substanceName: String
    val administrationRoute: AdministrationRoute
    var units by mutableStateOf("mg")
    var isEstimate by mutableStateOf(false)
    var doseText by mutableStateOf("")
    var purityText by mutableStateOf("100")
    private val purity: Double?
        get() {
            val p = purityText.toDoubleOrNull()
            return if (p != null && p > 0 && p <= 100) {
                p
            } else {
                null
            }
        }
    val isPurityValid: Boolean get() = purity != null
    val rawDoseWithUnit: String?
        get() {
            dose.let {
                if (it == null) return null
                purity.let { safePurity ->
                    if (safePurity == null) return null
                    return String
                        .format("%.2f", it.div(safePurity).times(100))
                        .toDouble()
                        .toReadableString() + " $units"
                }
            }
        }
    val dose: Double? get() = doseText.toDoubleOrNull()
    val isValidDose: Boolean get() = dose != null

    fun onDoseTextChange(newDoseText: String) {
        doseText = newDoseText.replace(oldChar = ',', newChar = '.')
    }

    init {
        substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!
        val routeString = state.get<String>(ADMINISTRATION_ROUTE_KEY)!!
        administrationRoute = AdministrationRoute.valueOf(routeString)
        viewModelScope.launch {
            val customSubstance =
                experienceRepository.getCustomSubstanceFlow(substanceName).firstOrNull()
                    ?: return@launch
            units = customSubstance.units
        }
    }

}
