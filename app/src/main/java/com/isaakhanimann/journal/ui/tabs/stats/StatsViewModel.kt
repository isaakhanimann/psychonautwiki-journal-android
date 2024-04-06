/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.journal.data.room.experiences.relations.ExperienceWithIngestionsAndCompanions
import com.isaakhanimann.journal.data.room.experiences.relations.IngestionWithCompanionAndCustomUnit
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Period
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    experienceRepo: ExperienceRepository
) : ViewModel() {

    private val _optionFlow = MutableStateFlow(TimePickerOption.WEEKS_26)
    private val optionFlow = _optionFlow.asStateFlow()

    private val consumerFlow = MutableStateFlow<String?>(null)

    fun onTapOption(timePickerOption: TimePickerOption) {
        viewModelScope.launch {
            _optionFlow.emit(timePickerOption)
        }
    }

    fun onChangeConsumer(consumerName: String?) {
        viewModelScope.launch {
            consumerFlow.emit(consumerName)
        }
    }

    private val startDateFlow = _optionFlow.map {
        return@map Instant.now().getEndOfDay().minus(it.allBucketSizes)
    }

    private val startDateTextFlow = startDateFlow.map {
        val dateTime = LocalDateTime.ofInstant(it, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
        return@map dateTime.format(formatter)
    }

    private val allExperiencesSortedFlow: Flow<List<ExperienceWithIngestionsAndCompanions>> =
        experienceRepo.getSortedExperiencesWithIngestionsAndCustomUnitsFlow()

    private val areThereAnyIngestionsFlow =
        allExperiencesSortedFlow.combine(consumerFlow) { experiences, consumerName ->
            experiences.any { experience ->
                experience.ingestionsWithCompanionAndCustomUnit.any { it.ingestion.consumerName == consumerName }
            }
        }

    private val relevantExperiencesSortedFlow: Flow<List<ExperienceWithIngestionsAndCompanions>> =
        allExperiencesSortedFlow.combine(startDateFlow) { experiences, startDate ->
            return@combine experiences.dropWhile { it.sortInstant > Instant.now().getEndOfDay() }
                .takeWhile { it.sortInstant > startDate }
        }

    private val companionFlow = experienceRepo.getAllSubstanceCompanionsFlow()

    private val experienceChartBucketsFlow: Flow<List<List<ColorCount>>> =
        relevantExperiencesSortedFlow.combine(optionFlow) { sortedExperiences, option ->
            var remainingExperiences = sortedExperiences
            val buckets = mutableListOf<List<ExperienceWithIngestionsAndCompanions>>()
            var startInstant = Instant.now().getEndOfDay()
            for (i in 0 until option.bucketCount) {
                startInstant = startInstant.minus(option.oneBucketSize)
                val experiencesForBucket =
                    remainingExperiences.takeWhile { it.sortInstant > startInstant }
                buckets.add(experiencesForBucket)
                val numExperiences = experiencesForBucket.size
                remainingExperiences =
                    remainingExperiences.takeLast(remainingExperiences.size - numExperiences)
            }
            return@combine buckets
        }.combine(consumerFlow) { buckets, consumerName ->
            Pair(buckets, consumerName)
        }.combine(companionFlow) { pair, companions ->
            return@combine pair.first.map { experiencesInBucket ->
                return@map getColorCountsForExperiences(
                    experiencesInBucket,
                    companions,
                    pair.second
                )
            }.reversed()
        }

    private fun getColorCountsForExperiences(
        experiences: List<ExperienceWithIngestionsAndCompanions>,
        companions: List<SubstanceCompanion>,
        consumerName: String?
    ): List<ColorCount> {
        return experiences.map { experience ->
            experience.ingestionsWithCompanionAndCustomUnit.filter { it.ingestion.consumerName == consumerName }
                .map { it.ingestion.substanceName }.toSet()
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

    private val statsFlowItem: Flow<List<StatItem>> = combine(
        relevantExperiencesSortedFlow,
        consumerFlow,
        companionFlow
    ) { relevantExperiencesSorted, consumerName, companions ->
        val allIngestions =
            relevantExperiencesSorted.flatMap { experience -> experience.ingestionsWithCompanionAndCustomUnit.filter { it.ingestion.consumerName == consumerName } }
        val experienceNamesMap =
            relevantExperiencesSorted.map { experience ->
                experience.ingestionsWithCompanionAndCustomUnit.filter { it.ingestion.consumerName == consumerName }.map { it.ingestion.substanceName }
                    .toSet()
            }.flatten().groupBy { it }
        val map = allIngestions.groupBy { it.ingestion.substanceName }
        return@combine map.values.mapNotNull { groupedIngestions ->
            val name = groupedIngestions.firstOrNull()?.ingestion?.substanceName ?: return@mapNotNull null
            val oneCompanion =
                companions.firstOrNull { it.substanceName == name } ?: return@mapNotNull null
            val experienceCounts = experienceNamesMap[name]?.size ?: 0
            StatItem(
                substanceName = name,
                color = oneCompanion.color,
                experienceCount = experienceCounts,
                ingestionCount = groupedIngestions.size,
                routeCounts = getRouteCounts(groupedIngestions.map { it.ingestion }),
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

    private fun getTotalDose(groupedIngestions: List<IngestionWithCompanionAndCustomUnit>): TotalDose? {
        val units = groupedIngestions.firstOrNull()?.originalUnit ?: return null
        if (groupedIngestions.any { it.originalUnit != units || it.pureDose == null }) return null
        val sumDose = groupedIngestions.sumOf { it.pureDose ?: 0.0 }
        val sumStandardDeviations = groupedIngestions.sumOf { it.pureDoseStandardDeviation ?: 0.0 }
        val isEstimate = groupedIngestions.any { it.isEstimate }
        return TotalDose(
            dose = sumDose,
            units = units,
            isEstimate = isEstimate,
            estimatedDoseStandardDeviation = sumStandardDeviations.takeIf { it > 0 })
    }

    val statsModelFlow: StateFlow<StatsModel> =
        optionFlow.combine(startDateTextFlow) { option, startDateText ->
            return@combine Pair(first = option, second = startDateText)
        }.combine(statsFlowItem) { pair, substanceStats ->
            return@combine Pair(first = pair, second = substanceStats)
        }.combine(areThereAnyIngestionsFlow) { pair, areThere ->
            return@combine Pair(first = pair, second = areThere)
        }.combine(consumerFlow) { pair, consumerName ->
            return@combine Pair(first = pair, second = consumerName)
        }.combine(experienceChartBucketsFlow) { pair, chartBuckets ->
            return@combine StatsModel(
                areThereAnyIngestions = pair.first.second,
                selectedOption = pair.first.first.first.first,
                startDateText = pair.first.first.first.second,
                statItems = pair.first.first.second,
                chartBuckets = chartBuckets,
                consumerName = pair.second
            )
        }.stateIn(
            initialValue = StatsModel(
                selectedOption = TimePickerOption.WEEKS_26,
                areThereAnyIngestions = false,
                startDateText = "",
                statItems = emptyList(),
                chartBuckets = emptyList(),
                consumerName = null
            ),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    val sortedConsumerNamesFlow =
        experienceRepo.getSortedIngestions(limit = 200).map { ingestions ->
            return@map ingestions.mapNotNull { it.consumerName }.distinct()
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )
}

data class StatsModel(
    val selectedOption: TimePickerOption,
    val areThereAnyIngestions: Boolean,
    val startDateText: String,
    val statItems: List<StatItem>,
    val chartBuckets: List<List<ColorCount>>,
    val consumerName: String?
)

data class ColorCount(
    val color: AdaptiveColor,
    val count: Int
)

data class StatItem(
    val substanceName: String,
    val color: AdaptiveColor,
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
    val isEstimate: Boolean,
    val estimatedDoseStandardDeviation: Double?
)

enum class TimePickerOption {
    DAYS_7 {
        override val displayText = "7D"
        override val longDisplayText = "Last Week"
        override val tabIndex = 0
        override val bucketCount = 7
        override val oneBucketSize: Period = Period.ofDays(1)
        override val allBucketSizes: Period = Period.ofDays(7)
    },
    WEEKS_26 {
        override val displayText = "26W"
        override val longDisplayText = "Half Year"
        override val tabIndex = 2
        override val bucketCount = 26
        override val oneBucketSize: Period =
            Period.ofDays(7) // the max time unit that can be used for subtraction is days
        override val allBucketSizes: Period = Period.ofDays(7 * bucketCount)
    };

    abstract val displayText: String
    abstract val longDisplayText: String
    abstract val tabIndex: Int
    abstract val bucketCount: Int
    abstract val oneBucketSize: Period
    abstract val allBucketSizes: Period
}

fun Instant.getEndOfDay(): Instant {
    return this.atOffset(ZoneOffset.UTC)
        .with(LocalTime.of(23,59,59, this.nano))
        .toInstant()
}