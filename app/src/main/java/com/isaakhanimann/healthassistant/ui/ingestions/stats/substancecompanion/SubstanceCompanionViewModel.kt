package com.isaakhanimann.healthassistant.ui.ingestions.stats.substancecompanion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.healthassistant.ui.main.routers.SUBSTANCE_NAME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SubstanceCompanionViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {

    private val currentTimeFlow: Flow<Date> = flow {
        while (true) {
            emit(Date())
            delay(timeMillis = 1000 * 10)
        }
    }
    // getTimeDifferenceText(fromDate = lastIngestion.time, toDate = currentTime)

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