package com.example.healthassistant.ui.stats.substancecompanion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.room.experiences.entities.SubstanceColor
import com.example.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.example.healthassistant.ui.main.routers.SUBSTANCE_NAME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubstanceCompanionViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {

    private val substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!

    val thisCompanionFlow: StateFlow<SubstanceCompanion?> =
        experienceRepo.getSubstanceCompanionFlow(substanceName).stateIn(
            initialValue = null,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    private val companionsFlow = experienceRepo.getAllSubstanceCompanionsFlow()

    val alreadyUsedColorsFlow: StateFlow<List<SubstanceColor>> =
        companionsFlow.map { companions ->
            companions.map { it.color }.distinct()
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    val otherColorsFlow: StateFlow<List<SubstanceColor>> =
        alreadyUsedColorsFlow.map { alreadyUsedColors ->
            SubstanceColor.values().filter {
                !alreadyUsedColors.contains(it)
            }
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )


    fun updateColor(color: SubstanceColor) {
        viewModelScope.launch {
            thisCompanionFlow.value?.let {
                it.color = color
                experienceRepo.update(substanceCompanion = it)
            }
        }
    }
}