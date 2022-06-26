package com.example.healthassistant.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.room.experiences.entities.SubstanceLastUsed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository
) : ViewModel() {

    private val _substanceStats =
        MutableStateFlow<List<SubstanceLastUsed>>(emptyList())
    val substanceStats = _substanceStats.asStateFlow()

    init {
        viewModelScope.launch {
            experienceRepo.getSubstanceWithLastDateDescendingFlow()
                .collect {
                    _substanceStats.value = it
                }
        }
    }
}