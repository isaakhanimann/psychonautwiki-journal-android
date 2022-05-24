package com.example.healthassistant.ui.home.experience

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.experiences.ExperienceRepository
import com.example.healthassistant.data.experiences.entities.ExperienceWithIngestions
import com.example.healthassistant.data.experiences.entities.Ingestion
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import com.example.healthassistant.ui.main.routers.EXPERIENCE_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExperienceViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    private val substanceRepo: SubstanceRepository,
    state: SavedStateHandle
) : ViewModel() {

    private val _experienceWithIngestions = MutableStateFlow<ExperienceWithIngestions?>(null)
    val experienceWithIngestions = _experienceWithIngestions.asStateFlow()
    private val _ingestionDurationPairs =
        MutableStateFlow<List<Pair<Ingestion, RoaDuration?>>>(listOf())
    val ingestionDurationPairs = _ingestionDurationPairs.asStateFlow()

    init {
        val id = state.get<Int>(EXPERIENCE_ID_KEY)!!
        viewModelScope.launch {
            experienceRepo.getExperienceWithIngestions(experienceId = id).collect {
                _experienceWithIngestions.value = it
                val ingestions = it?.ingestions ?: listOf()
                _ingestionDurationPairs.value = ingestions.map { ing ->
                    val roaDuration = substanceRepo.getSubstance(ing.substanceName)?.getRoa(ing.administrationRoute)?.roaDuration
                    Pair(first = ing, second = roaDuration)
                }
            }
        }
    }

    fun deleteIngestion(ingestion: Ingestion) {
        viewModelScope.launch {
            experienceRepo.deleteIngestion(ingestion)
        }
    }
}