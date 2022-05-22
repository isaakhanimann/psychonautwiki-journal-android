package com.example.healthassistant.ui.home.experience.addingestion.color

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.experiences.entities.IngestionColor
import com.example.healthassistant.data.experiences.repositories.ExperienceRepository
import com.example.healthassistant.ui.main.routers.SUBSTANCE_NAME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseColorViewModel @Inject constructor(
    experienceRepository: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {
    val substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!
    var lastUsedColorForSubstance: IngestionColor? = null

    init {
        viewModelScope.launch {
            lastUsedColorForSubstance = experienceRepository.getLastIngestion(substanceName)?.color
        }
    }
}