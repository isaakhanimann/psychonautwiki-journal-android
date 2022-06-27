package com.example.healthassistant.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.room.experiences.entities.SubstanceColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

data class SubstanceStat(
    val substanceName: String,
    val lastUsedText: String,
    val color: SubstanceColor
)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
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

    companion object {
        fun getTimeDifferenceText(fromDate: Date, toDate: Date): String {
            val diff: Long = toDate.time - fromDate.time
            val seconds = diff / 1000.0
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            val months = days / 30
            val years = months / 12
            val yearsRounded = years.roundToInt()
            val monthsRounded = months.roundToInt()
            val daysRounded = days.roundToInt()
            val hoursRounded = hours.roundToInt()
            val minutesRounded = minutes.roundToInt()
            return if (yearsRounded == 1) {
                "1 year"
            } else if (yearsRounded != 0) {
                "$yearsRounded years"
            } else if (monthsRounded == 1) {
                "1 month"
            } else if (monthsRounded != 0) {
                "$monthsRounded months"
            } else if (daysRounded == 1) {
                "1 day"
            } else if (daysRounded != 0) {
                "$daysRounded days"
            } else if (hoursRounded == 1) {
                "1 hour"
            } else if (hoursRounded != 0) {
                "$hoursRounded hours"
            } else if (minutesRounded == 1) {
                "1 minute"
            } else {
                "$minutesRounded minutes"
            }
        }
    }
}