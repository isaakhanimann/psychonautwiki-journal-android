package com.example.healthassistant.ui.ingestions.ingestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import com.example.healthassistant.ui.main.routers.INGESTION_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OneIngestionViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    private val substanceRepo: SubstanceRepository,
    state: SavedStateHandle
) : ViewModel() {

    var isShowingDeleteDialog by mutableStateOf(false)

    private val _ingestionWithDurationAndExperience =
        MutableStateFlow<IngestionWithDurationAndExperience?>(null)
    val ingestionWithDurationAndExperience = _ingestionWithDurationAndExperience.asStateFlow()

    init {
        val id = state.get<Int>(INGESTION_ID_KEY)!!
        viewModelScope.launch {
            experienceRepo.getIngestionFlow(id).collect { ingestion ->
                val experience = ingestion?.experienceId.let {
                    if (it == null) {
                        null
                    } else {
                        experienceRepo.getExperience(
                            it
                        )
                    }
                }
                ingestion?.let {
                    _ingestionWithDurationAndExperience.value = IngestionWithDurationAndExperience(
                        ingestion,
                        roaDuration = substanceRepo.getSubstance(ingestion.substanceName)
                            ?.getRoa(ingestion.administrationRoute)?.roaDuration,
                        experience = experience
                    )
                }
            }
        }
    }

    fun deleteIngestion() {
        viewModelScope.launch {
            ingestionWithDurationAndExperience.value?.ingestion.let {
                assert(it!=null)
                if (it != null) {
                    experienceRepo.deleteIngestion(ingestion = it)
                }
            }
        }
    }
}