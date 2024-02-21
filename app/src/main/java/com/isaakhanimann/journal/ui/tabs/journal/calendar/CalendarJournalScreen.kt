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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.room.experiences.relations.ExperienceWithIngestionsCompanionsAndRatings
import com.isaakhanimann.journal.ui.tabs.journal.JournalScreenPreviewProvider
import com.isaakhanimann.journal.ui.tabs.journal.JournalViewModel
import com.isaakhanimann.journal.ui.theme.JournalTheme
import com.isaakhanimann.journal.ui.theme.horizontalPadding
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kotlinx.coroutines.launch
import java.time.YearMonth

@Composable
fun CalendarJournalScreen(
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    navigateToAddIngestion: () -> Unit,
    viewModel: JournalViewModel = hiltViewModel()
) {
    val experiences = viewModel.experiences.collectAsState().value
    CalendarJournalScreen(
        navigateToExperiencePopNothing = navigateToExperiencePopNothing,
        navigateToAddIngestion = navigateToAddIngestion,
        experiences = experiences
    )
}

@Preview
@Composable
fun CalendarJournalScreenPreview(
    @PreviewParameter(
        JournalScreenPreviewProvider::class,
    ) experiences: List<ExperienceWithIngestionsCompanionsAndRatings>,
) {
    JournalTheme {
        CalendarJournalScreen(
            navigateToExperiencePopNothing = {},
            navigateToAddIngestion = {},
            experiences = experiences
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarJournalScreen(
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    navigateToAddIngestion: () -> Unit,
    experiences: List<ExperienceWithIngestionsCompanionsAndRatings>,
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusYears(20) }
    val endMonth = remember { currentMonth.plusMonths(10) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Journal") },
                actions = {
                    val coroutineScope = rememberCoroutineScope()

                    IconButton(onClick = {
                        coroutineScope.launch {
                            state.animateScrollToMonth(YearMonth.now())
                        }
                    }) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = "Scroll to current month"
                        )
                    }
                }
            )
        },
    ) { padding ->
        VerticalCalendar(
            modifier = Modifier.padding(padding),
            state = state,
            dayContent = { Day(it) },
            contentPadding = PaddingValues(horizontalPadding),
            monthHeader = {
                Text(
                    text = "${it.yearMonth.month} ${it.yearMonth.year}",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        )

    }
}

@Composable
fun Day(day: CalendarDay) {
    val viewModel: DayViewModel = hiltViewModel()
    LaunchedEffect(key1 = day.date) {
        viewModel.setExperienceInfo(day)
    }
    Box(
        modifier = Modifier
            .aspectRatio(1f), // This is important for square sizing!
        contentAlignment = Alignment.Center
    ) {
        val experienceInfos = viewModel.experienceInfosFlow.collectAsState().value
        Text(
            modifier = Modifier.alpha(if (experienceInfos.experienceIds.isEmpty()) 0.2f else 1f),
            text = day.date.dayOfMonth.toString())
    }
}