package com.isaakhanimann.healthassistant.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
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

    private val allIngestionsSortedFlow: Flow<List<Ingestion>> = experienceRepo.getSortedIngestionsFlow()

    val substanceStats: StateFlow<List<SubstanceStat>> = allIngestionsSortedFlow.combine(startDateFlow) { ingestions, startDate ->
        return@combine ingestions.takeWhile { it.time > startDate }
    }.combine(experienceRepo.getAllSubstanceCompanionsFlow()) { ingestions, companions ->
        val map = ingestions.groupBy { it.substanceName }
        return@combine map.values.mapNotNull { groupedIngestions ->
            val name = groupedIngestions.firstOrNull()?.substanceName ?: return@mapNotNull null
            val oneCompanion =
                companions.firstOrNull { it.substanceName == name } ?: return@mapNotNull null
            SubstanceStat(
                substanceName = name,
                color = oneCompanion.color,
                ingestionCount = groupedIngestions.size,
                routeCounts = getRouteCounts(groupedIngestions),
                cumulativeDose = getCumulativeDose(groupedIngestions)
            )
        }
    }.stateIn(
        initialValue = emptyList(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    private fun getRouteCounts(groupedIngestions: List<Ingestion>): List<RouteCount> {
        val routeMap = groupedIngestions.groupBy { it.administrationRoute }
        return routeMap.values.mapNotNull {
            val route = it.firstOrNull()?.administrationRoute ?: return@mapNotNull null
            RouteCount(administrationRoute = route, count = it.size)
        }
    }

    private fun getCumulativeDose(groupedIngestions: List<Ingestion>): CumulativeDose? {
        val units = groupedIngestions.firstOrNull()?.units ?: return null
        if (groupedIngestions.any { it.units != units || it.dose == null }) return null
        val sumDose = groupedIngestions.sumOf { it.dose ?: 0.0 }
        val isEstimate = groupedIngestions.any { it.isDoseAnEstimate }
        return CumulativeDose(dose = sumDose, units = units, isEstimate = isEstimate)
    }
}

data class SubstanceStat(
    val substanceName: String,
    val color: SubstanceColor,
    val ingestionCount: Int,
    val routeCounts: List<RouteCount>,
    val cumulativeDose: CumulativeDose?
)

data class RouteCount(
    val administrationRoute: AdministrationRoute,
    val count: Int
)

data class CumulativeDose(
    val dose: Double,
    val units: String,
    val isEstimate: Boolean
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
        override val displayText = "Years"
        override val tabIndex = 4
    };

    abstract val displayText: String
    abstract val tabIndex: Int
}