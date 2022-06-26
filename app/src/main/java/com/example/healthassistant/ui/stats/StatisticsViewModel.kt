package com.example.healthassistant.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

data class SubstanceStat(
    val substanceName: String,
    val lastUsedText: String
)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository
) : ViewModel() {

    private val _substanceStats =
        MutableStateFlow<List<SubstanceStat>>(emptyList())
    val substanceStats = _substanceStats.asStateFlow()

    init {
        viewModelScope.launch {
            experienceRepo.getSubstanceWithLastDateDescendingFlow()
                .map { list ->
                    list.map {
                        SubstanceStat(
                            it.substanceName,
                            getTimeTextDifferenceToNow(it.lastUsed)
                        )
                    }
                }
                .collect {
                    _substanceStats.value = it
                }
        }
    }

    companion object {
        fun getTimeTextDifferenceToNow(date: Date): String {
            val diff: Long = Date().time - date.time
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