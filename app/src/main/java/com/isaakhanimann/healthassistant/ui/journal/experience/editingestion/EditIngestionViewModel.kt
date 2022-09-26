package com.isaakhanimann.healthassistant.ui.journal.experience.editingestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Experience
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.ui.main.routers.INGESTION_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditIngestionViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {
    var ingestion: Ingestion? = null
    var note by mutableStateOf("")
    var isEstimate by mutableStateOf(false)
    var experienceId by mutableStateOf(1)

    init {
        val id = state.get<Int>(INGESTION_ID_KEY)!!
        viewModelScope.launch {
            val ing = experienceRepo.getIngestionFlow(id = id).first() ?: return@launch
            ingestion = ing
            note = ing.notes ?: ""
            isEstimate = ing.isDoseAnEstimate
            experienceId = ing.experienceId
        }
    }

    val experiences: StateFlow<List<Experience>> = experienceRepo.getSortedExperiencesFlow().stateIn(
        initialValue = emptyList(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    fun onDoneTap() {
        viewModelScope.launch {
            ingestion?.let {
                it.notes = note
                it.isDoseAnEstimate = isEstimate
                it.experienceId = experienceId
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