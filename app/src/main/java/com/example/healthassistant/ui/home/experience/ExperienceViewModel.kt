package com.example.healthassistant.ui.home.experience

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.EXPERIENCE_ID
import com.example.healthassistant.data.experiences.entities.Experience
import com.example.healthassistant.data.experiences.repositories.ExperienceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExperienceViewModel @Inject constructor(
    repository: ExperienceRepository,
    state: SavedStateHandle
): ViewModel() {

    var experience: Experience? = null

    init {
        val id = state.get<Int>(EXPERIENCE_ID)
        id?.let {
            viewModelScope.launch {
                experience = repository.getExperience(id = it)
            }
        }
    }
}