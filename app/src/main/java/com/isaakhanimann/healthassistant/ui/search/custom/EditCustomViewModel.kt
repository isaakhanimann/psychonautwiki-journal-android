package com.isaakhanimann.healthassistant.ui.search.custom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import com.isaakhanimann.healthassistant.data.room.experiences.entities.CustomSubstance
import com.isaakhanimann.healthassistant.ui.main.routers.SUBSTANCE_NAME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCustomViewModel @Inject constructor(
    val experienceRepo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {

    var name by mutableStateOf("")
    var units by mutableStateOf("")
    var description by mutableStateOf("")

    val isValid get() = name.isNotBlank() && units.isNotBlank()

    init {
        val substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!
        viewModelScope.launch {
            val customSubstance = experienceRepo.getCustomSubstanceFlow(substanceName).firstOrNull() ?: return@launch
            name = customSubstance.name
            units = customSubstance.units
            description = customSubstance.description
        }
    }

    fun onDoneTap() {
        viewModelScope.launch {
            val customSubstance = CustomSubstance(
                name,
                units,
                description
            )
            experienceRepo.insert(customSubstance)
        }
    }
}