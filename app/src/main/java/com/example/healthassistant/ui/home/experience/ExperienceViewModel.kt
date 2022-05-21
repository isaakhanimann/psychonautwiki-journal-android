package com.example.healthassistant.ui.home.experience

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.experiences.entities.Experience
import com.example.healthassistant.data.experiences.entities.Ingestion
import com.example.healthassistant.data.experiences.repositories.ExperienceRepository
import com.example.healthassistant.ui.main.routers.EXPERIENCE_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExperienceViewModel @Inject constructor(
    private val repo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {

    var experience: Experience? = null
    private val _ingestions = MutableStateFlow<List<Ingestion>>(emptyList())
    val ingestions = _ingestions.asStateFlow()
    var isMenuExpanded by mutableStateOf(false)

    init {
        val id = state.get<Int>(EXPERIENCE_ID_KEY)!!
        viewModelScope.launch {
            experience = repo.getExperience(id)
            repo.getIngestions(experienceId = id).collect {
                _ingestions.value = it
            }
        }
    }

    fun deleteIngestion(ingestion: Ingestion) {
        viewModelScope.launch {
            repo.deleteIngestion(ingestion)
        }
    }
}