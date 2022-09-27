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
import java.time.Instant
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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
        return@map Instant.now().minus(it.allBucketSizes)
    }

    private val startDateTextFlow = startDateFlow.map {
        val dateTime = LocalDateTime.ofInstant(it, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        return@map dateTime.format(formatter)
    }

    private val allExperiencesSortedFlow: Flow<List<ExperienceWithIngestions>> =
        experienceRepo.getSortedExperiencesWithIngestionsFlow()
    private val relevantExperiencesSortedFlow: Flow<List<ExperienceWithIngestions>> =
        allExperiencesSortedFlow.combine(startDateFlow) { experiences, startDate ->
            return@combine experiences.takeWhile { it.sortInstant > startDate }
        }

    private val companionFlow = experienceRepo.getAllSubstanceCompanionsFlow()

    private val experienceChartBucketsFlow: Flow<List<List<ColorCount>>> =
        relevantExperiencesSortedFlow.combine(optionFlow) { sortedExperiences, option ->
            var remainingExperiences = sortedExperiences
            val buckets = mutableListOf<List<ExperienceWithIngestions>>()
            for (i in 0 until option.bucketCount) {
                val startInstant = Instant.now().minus(option.oneBucketSize)
                val experiencesForBucket = remainingExperiences.takeWhile { it.sortInstant > startInstant }
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
        override val oneBucketSize: Period = Period.ofDays(1)
        override val allBucketSizes: Period = Period.ofDays(7)
    },
    DAYS_30 {
        override val displayText = "30D"
        override val tabIndex = 1
        override val bucketCount = 30
        override val oneBucketSize: Period = Period.ofDays(1)
        override val allBucketSizes: Period = Period.ofDays(30)
    },
    WEEKS_26 {
        override val displayText = "26W"
        override val tabIndex = 2
        override val bucketCount = 26
        override val oneBucketSize: Period = Period.ofWeeks(1)
        override val allBucketSizes: Period = Period.ofWeeks(26)
    },
    MONTHS_12 {
        override val displayText = "12M"
        override val tabIndex = 3
        override val bucketCount = 12
        override val oneBucketSize: Period = Period.ofMonths(1)
        override val allBucketSizes: Period = Period.ofMonths(12)
    },
    YEARS_10 {
        override val displayText = "10Y"
        override val tabIndex = 4
        override val bucketCount = 10
        override val oneBucketSize: Period = Period.ofYears(1)
        override val allBucketSizes: Period = Period.ofYears(10)
    };

    abstract val displayText: String
    abstract val tabIndex: Int
    abstract val bucketCount: Int
    abstract val oneBucketSize: Period
    abstract val allBucketSizes: Period
}