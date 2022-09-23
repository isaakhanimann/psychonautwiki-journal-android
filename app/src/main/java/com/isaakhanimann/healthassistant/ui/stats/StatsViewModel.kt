package com.isaakhanimann.healthassistant.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.healthassistant.data.room.experiences.relations.ExperienceWithIngestions
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

    private val _optionFlow = MutableStateFlow(TimePickerOption.WEEKS_26)
    private val optionFlow = _optionFlow.asStateFlow()

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
            TimePickerOption.YEARS_5 -> cal.add(Calendar.YEAR, -5)
        }
        return@map cal.time
    }

    private val startDateTextFlow = startDateFlow.map {
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        formatter.format(it) ?: ""
    }

    private val allExperiencesSortedFlow: Flow<List<ExperienceWithIngestions>> =
        experienceRepo.getSortedExperiencesWithIngestionsFlow()
    private val relevantExperiencesSortedFlow: Flow<List<ExperienceWithIngestions>> =
        allExperiencesSortedFlow.combine(startDateFlow) { experiences, startDate ->
            return@combine experiences.takeWhile { it.sortDate > startDate }
        }

    private val companionFlow = experienceRepo.getAllSubstanceCompanionsFlow()

    private val experienceChartBucketsFlow: Flow<List<List<ColorCount>>> =
        relevantExperiencesSortedFlow.combine(optionFlow) { sortedExperiences, option ->
            var remainingExperiences = sortedExperiences
            val cal = Calendar.getInstance(TimeZone.getDefault())
            cal.time = Date()
            val buckets = mutableListOf<List<ExperienceWithIngestions>>()
            for (i in 0 until option.bucketCount) {
                when (option) {
                    TimePickerOption.DAYS_7 -> cal.add(Calendar.DAY_OF_MONTH, -1)
                    TimePickerOption.DAYS_30 -> cal.add(Calendar.DAY_OF_MONTH, -1)
                    TimePickerOption.WEEKS_26 -> cal.add(Calendar.WEEK_OF_YEAR, -1)
                    TimePickerOption.MONTHS_12 -> cal.add(Calendar.MONTH, -1)
                    TimePickerOption.YEARS_5 -> cal.add(Calendar.YEAR, -1)
                }
                val experiencesForBucket = remainingExperiences.takeWhile { it.sortDate > cal.time }
                buckets.add(experiencesForBucket)
                val numExperiences = experiencesForBucket.size
                remainingExperiences =
                    remainingExperiences.takeLast(remainingExperiences.size - numExperiences)
            }
            return@combine buckets
        }.combine(companionFlow) { buckets, companions ->
            return@combine buckets.map { experiencesInBucket ->
                return@map getColorCountsForExperiences(experiencesInBucket, companions)
            }.reversed()
        }

    private fun getColorCountsForExperiences(
        experiences: List<ExperienceWithIngestions>,
        companions: List<SubstanceCompanion>
    ): List<ColorCount> {
        return experiences.map { experience ->
            experience.ingestions.map { it.substanceName }.toSet()
        }.flatten()
            .groupBy { it }.values.mapNotNull { sameNames ->
                val name =
                    sameNames.firstOrNull() ?: return@mapNotNull null
                val oneCompanion =
                    companions.firstOrNull { it.substanceName == name } ?: return@mapNotNull null
                return@mapNotNull ColorCount(
                    color = oneCompanion.color,
                    count = sameNames.size
                )
            }.sortedByDescending { it.count }
    }

    private val statsFlowItem: Flow<List<StatItem>> =
        relevantExperiencesSortedFlow.combine(companionFlow) { experiencesWithIngestions, companions ->
            val allIngestions = experiencesWithIngestions.flatMap { it.ingestions }
            val experienceNamesMap =
                experiencesWithIngestions.map { e -> e.ingestions.map { it.substanceName }.toSet() }
                    .flatten().groupBy { it }
            val map = allIngestions.groupBy { it.substanceName }
            return@combine map.values.mapNotNull { groupedIngestions ->
                val name = groupedIngestions.firstOrNull()?.substanceName ?: return@mapNotNull null
                val oneCompanion =
                    companions.firstOrNull { it.substanceName == name } ?: return@mapNotNull null
                val experienceCounts = experienceNamesMap[name]?.size ?: 0
                StatItem(
                    substanceName = name,
                    color = oneCompanion.color,
                    experienceCount = experienceCounts,
                    ingestionCount = groupedIngestions.size,
                    routeCounts = getRouteCounts(groupedIngestions),
                    totalDose = getTotalDose(groupedIngestions)
                )
            }.sortedByDescending { it.experienceCount }
        }

    private fun getRouteCounts(groupedIngestions: List<Ingestion>): List<RouteCount> {
        val routeMap = groupedIngestions.groupBy { it.administrationRoute }
        return routeMap.values.mapNotNull {
            val route = it.firstOrNull()?.administrationRoute ?: return@mapNotNull null
            RouteCount(administrationRoute = route, count = it.size)
        }
    }

    private fun getTotalDose(groupedIngestions: List<Ingestion>): TotalDose? {
        val units = groupedIngestions.firstOrNull()?.units ?: return null
        if (groupedIngestions.any { it.units != units || it.dose == null }) return null
        val sumDose = groupedIngestions.sumOf { it.dose ?: 0.0 }
        val isEstimate = groupedIngestions.any { it.isDoseAnEstimate }
        return TotalDose(dose = sumDose, units = units, isEstimate = isEstimate)
    }

    val statsModelFlow: StateFlow<StatsModel?> =
        optionFlow.combine(startDateTextFlow) { option, startDateText ->
            return@combine Pair(first = option, second = startDateText)
        }.combine(statsFlowItem) { pair, substanceStats ->
            return@combine Pair(first = pair, second = substanceStats)
        }.combine(experienceChartBucketsFlow) { pair, chartBuckets ->
            return@combine StatsModel(
                selectedOption = pair.first.first,
                startDateText = pair.first.second,
                statItems = pair.second,
                chartBuckets = chartBuckets
            )
        }.stateIn(
            initialValue = null,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )
}

data class StatsModel(
    val selectedOption: TimePickerOption,
    val startDateText: String,
    val statItems: List<StatItem>,
    val chartBuckets: List<List<ColorCount>>
)

data class ColorCount(
    val color: SubstanceColor,
    val count: Int
)

data class StatItem(
    val substanceName: String,
    val color: SubstanceColor,
    val experienceCount: Int,
    val ingestionCount: Int,
    val routeCounts: List<RouteCount>,
    val totalDose: TotalDose?,
)

data class RouteCount(
    val administrationRoute: AdministrationRoute,
    val count: Int
)

data class TotalDose(
    val dose: Double,
    val units: String,
    val isEstimate: Boolean
)

enum class TimePickerOption {
    DAYS_7 {
        override val displayText = "7D"
        override val tabIndex = 0
        override val bucketCount = 7
    },
    DAYS_30 {
        override val displayText = "30D"
        override val tabIndex = 1
        override val bucketCount = 30
    },
    WEEKS_26 {
        override val displayText = "26W"
        override val tabIndex = 2
        override val bucketCount = 26
    },
    MONTHS_12 {
        override val displayText = "12M"
        override val tabIndex = 3
        override val bucketCount = 12
    },
    YEARS_5 {
        override val displayText = "5Y"
        override val tabIndex = 4
        override val bucketCount = 5
    };

    abstract val displayText: String
    abstract val tabIndex: Int
    abstract val bucketCount: Int
}