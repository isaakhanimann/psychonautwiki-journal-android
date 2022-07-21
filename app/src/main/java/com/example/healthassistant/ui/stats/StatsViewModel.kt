package com.example.healthassistant.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.room.experiences.entities.SubstanceColor
import com.example.healthassistant.ui.utils.getTimeDifferenceText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    experienceRepo: ExperienceRepository
) : ViewModel() {

    private val currentTimeFlow: Flow<Date> = flow {
        while (true) {
            emit(Date())
            delay(timeMillis = 1000 * 10)
        }
    }

    val substanceStats: StateFlow<List<SubstanceStat>> =
        experienceRepo.getSubstanceWithLastDateDescendingFlow()
            .combine(currentTimeFlow) { list, currentTime ->
                list.map {
                    SubstanceStat(
                        substanceName = it.substanceName,
                        lastUsedText = getTimeDifferenceText(fromDate = it.lastUsed, toDate = currentTime),
                        color = it.color
                    )
                }
            }.stateIn(
                initialValue = emptyList(),
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000)
            )
}

data class SubstanceStat(
    val substanceName: String,
    val lastUsedText: String,
    val color: SubstanceColor
)