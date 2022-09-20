package com.isaakhanimann.healthassistant.ui.ingestions.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
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

    private val startDateFlow = _optionFlow.map {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        cal.time = Date()
        when (it) {
            TimePickerOption.DAYS_7 -> cal.add(Calendar.WEEK_OF_YEAR, -1)
            TimePickerOption.DAYS_30 -> cal.add(Calendar.MONTH, -1)
            TimePickerOption.WEEKS_26 -> cal.add(Calendar.WEEK_OF_YEAR, -26)
            TimePickerOption.MONTHS_12 -> cal.add(Calendar.YEAR, -1)
            TimePickerOption.YEARS -> return@map null
        }
        return@map cal.time
    }

    val startDateTextFlow = startDateFlow.map {
        if (it == null) return@map "Start"
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        formatter.format(it) ?: ""
    }.stateIn(
        initialValue = "",
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    val substanceStats: StateFlow<List<SubstanceStat>> = startDateFlow.map {
        if (it == null) {
            experienceRepo.getAllIngestions()
        } else {
            experienceRepo.getIngestionsSinceDate(it)
        }
    }.combine(experienceRepo.getAllSubstanceCompanionsFlow()) { ingestions, companions ->
        val map = ingestions.groupBy { it.substanceName }
        return@combine map.values.mapNotNull { groupedIngestions ->
            val name = groupedIngestions.firstOrNull()?.substanceName ?: return@mapNotNull null
            val oneCompanion =
                companions.firstOrNull { it.substanceName == name } ?: return@mapNotNull null
            SubstanceStat(
                substanceName = name,
                color = oneCompanion.color,
                ingestionCount = groupedIngestions.size
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
        override val milliseconds: Long = 7 * 24 * 60 * 60 * 1000
    },
    DAYS_30 {
        override val displayText = "30D"
        override val tabIndex = 1
        override val milliseconds: Long = 30 * 24 * 60 * 60 * 1000
    },
    WEEKS_26 {
        override val displayText = "26W"
        override val tabIndex = 2
        override val milliseconds: Long = 26 * 7 * 24 * 60 * 60 * 1000
    },
    MONTHS_12 {
        override val displayText = "12M"
        override val tabIndex = 3
        override val milliseconds: Long = 52 * 7 * 24 * 60 * 60 * 1000
    },
    YEARS {
        override val displayText = "All"
        override val tabIndex = 4
        override val milliseconds: Long = 7 * 24 * 60 * 60 * 1000
    };

    abstract val displayText: String
    abstract val tabIndex: Int
    abstract val milliseconds: Long

}