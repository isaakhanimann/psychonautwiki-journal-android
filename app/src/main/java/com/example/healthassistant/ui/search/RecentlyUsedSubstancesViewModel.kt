package com.example.healthassistant.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.experiences.ExperienceRepository
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentlyUsedSubstancesViewModel @Inject constructor(
    private val substanceRepo: SubstanceRepository,
    private val experienceRepo: ExperienceRepository
): ViewModel() {

    private val _recentlyUsedSubstances = MutableStateFlow<List<Substance>>(emptyList())
    val recentlyUsedSubstances = _recentlyUsedSubstances.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            experienceRepo.getLastUsedSubstanceNames(limit = 10)
                .collect { names ->
                    _recentlyUsedSubstances.value = names.mapNotNull { substanceRepo.getSubstance(substanceName = it) }
                }

        }
    }
}