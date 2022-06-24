package com.example.healthassistant.ui.ingestions.ingestion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.substances.RoaDuration
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

    private val _ingestionDurationPair =
        MutableStateFlow<Pair<Ingestion, RoaDuration?>?>(null)
    val ingestionDurationPair = _ingestionDurationPair.asStateFlow()

    init {
        val id = state.get<Int>(INGESTION_ID_KEY)!!
        viewModelScope.launch {
            val ingestion = experienceRepo.getIngestion(id)!!
            val roaDuration = substanceRepo.getSubstance(ingestion.substanceName)
                ?.getRoa(ingestion.administrationRoute)?.roaDuration
            _ingestionDurationPair.value = Pair(first = ingestion, second = roaDuration)
        }
    }
}