package com.example.healthassistant.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    private val substanceRepo: SubstanceRepository
): ViewModel() {

    val recentlyUsedSubstances: StateFlow<List<Substance>> =
        experienceRepo.getLastUsedSubstanceNamesFlow(limit = 10).map { lastUsedSubstanceNames ->
            lastUsedSubstanceNames.mapNotNull { substanceRepo.getSubstance(substanceName = it) }
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    var searchText by mutableStateOf("")
    var substancesToShow: Flow<List<Substance>> = substanceRepo.getAllSubstances()

    fun filterSubstances() {
        viewModelScope.launch {
            substancesToShow = substanceRepo.getSubstances(searchText = searchText)
        }
    }
}