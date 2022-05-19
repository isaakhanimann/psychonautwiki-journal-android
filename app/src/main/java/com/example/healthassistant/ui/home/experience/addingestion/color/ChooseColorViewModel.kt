package com.example.healthassistant.ui.home.experience.addingestion.color

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.experiences.entities.IngestionColor
import com.example.healthassistant.data.experiences.repositories.ExperienceRepository
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.ui.main.routers.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseColorViewModel @Inject constructor(
    experienceRepository: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {
    val substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!
    val administrationRoute = AdministrationRoute.valueOf(state.get<String>(ADMINISTRATION_ROUTE_KEY)!!)
    val isEstimate = state.get<Boolean>(IS_ESTIMATE_KEY)!!
    val units = state.get<String>(UNITS_KEY)!!
    val dose = state.get<String>(DOSE_KEY)?.toDoubleOrNull()
    val experienceId = state.get<String>(EXPERIENCE_ID_KEY)?.toIntOrNull()
    var lastUsedColorForSubstance: IngestionColor? = null

    init {
        viewModelScope.launch {
            lastUsedColorForSubstance = experienceRepository.getLastIngestion(substanceName)?.color
        }
    }
}