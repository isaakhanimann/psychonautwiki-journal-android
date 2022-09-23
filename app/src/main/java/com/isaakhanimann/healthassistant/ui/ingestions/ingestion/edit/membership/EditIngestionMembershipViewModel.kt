package com.isaakhanimann.healthassistant.ui.ingestions.ingestion.edit.membership

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
class EditIngestionMembershipViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {

    var ingestion: Ingestion? = null
    var selectedExperienceId: Int? by mutableStateOf(null)

    val experiences: StateFlow<List<Experience>> = experienceRepo.getSortedExperiencesFlow().stateIn(
        initialValue = emptyList(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    init {
        val id = state.get<Int>(INGESTION_ID_KEY)!!
        viewModelScope.launch {
            ingestion = experienceRepo.getIngestionFlow(id = id).first()!!
            selectedExperienceId = ingestion?.experienceId
        }
    }

    fun onDoneTap() {
        viewModelScope.launch {
            ingestion!!.experienceId = selectedExperienceId
            experienceRepo.update(ingestion!!)
        }
    }

}