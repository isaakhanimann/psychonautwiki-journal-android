/*
 * Copyright (c) 2024. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.journal.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.ui.utils.getInstant
import com.kizitonwose.calendar.core.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DayViewModel @Inject constructor(
    val experienceRepo: ExperienceRepository,
) : ViewModel() {

    val experienceInfosFlow =
        MutableStateFlow(ExperienceInfo(experienceIds = emptyList(), colors = emptyList()))

    fun setExperienceInfo(day: CalendarDay) {
        val startOfDay = day.date.atStartOfDay().getInstant()
        val endOfDay = startOfDay.plusMillis(24 * 60 * 60 * 1000)
        viewModelScope.launch {
            experienceRepo.getIngestionsWithExperiencesFlow(
                fromInstant = startOfDay,
                toInstant = endOfDay
            ).collect { ingestions ->
                val experienceInfos = ExperienceInfo(
                    experienceIds = ingestions.map { it.experience.id }.toSet().toList(),
                    colors = emptyList()
                )
                experienceInfosFlow.emit(
                    experienceInfos
                )
            }
        }
    }
}

data class ExperienceInfo(
    val experienceIds: List<Int>,
    val colors: List<AdaptiveColor>
)