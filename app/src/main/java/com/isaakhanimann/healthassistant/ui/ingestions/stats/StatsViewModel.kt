package com.isaakhanimann.healthassistant.ui.ingestions.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    experienceRepo: ExperienceRepository
) : ViewModel() {

    private val _optionFlow = MutableStateFlow(TimePickerOption.DAYS_30)
    val optionFlow = _optionFlow.asStateFlow()

    fun onTapOption(timePickerOption: TimePickerOption) {
        viewModelScope.launch {
            _optionFlow.emit(timePickerOption)
        }
    }

    val substanceStats: StateFlow<List<SubstanceStat>> =
        experienceRepo.getSubstanceCompanionWithIngestionsFlow()
            .map { list ->
                return@map list.mapNotNull { companionWithIngestions ->
                    SubstanceStat(
                        substanceName = companionWithIngestions.substanceCompanion.substanceName,
                        color = companionWithIngestions.substanceCompanion.color,
                        ingestionCount = companionWithIngestions.ingestions.size
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
    val color: SubstanceColor,
    val ingestionCount: Int,
//    val cumulativeDose: Double,
//    val doseUnit: String,
)

enum class TimePickerOption {
    DAYS_7 {
        override val displayText = "7D"
        override val tabIndex = 0
    },
    DAYS_30 {
        override val displayText = "30D"
        override val tabIndex = 1
    },
    WEEKS_26 {
        override val displayText = "26W"
        override val tabIndex = 2
    },
    MONTHS_12 {
        override val displayText = "12M"
        override val tabIndex = 3
    },
    YEARS {
        override val displayText = "All"
        override val tabIndex = 4
    };

    abstract val displayText: String
    abstract val tabIndex: Int
}