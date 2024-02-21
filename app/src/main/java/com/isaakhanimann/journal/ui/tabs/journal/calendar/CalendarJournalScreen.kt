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

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
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
                    TextButton(onClick = {
                        coroutineScope.launch {
                            state.animateScrollToMonth(YearMonth.now())
                        }
                    }) {
                       Text(text = "Today")
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
    val experienceInfo: MutableState<ExperienceInfo> = remember {
        mutableStateOf(
            ExperienceInfo(
                experienceIds = emptyList(),
                colors = emptyList()
            )
        )
    }
    LaunchedEffect(key1 = day.date) {
        experienceInfo.value = viewModel.getExperienceInfo(day)
    }
    Box(
        modifier = Modifier
            .aspectRatio(1f), // This is important for square sizing!
        contentAlignment = Alignment.Center
    ) {
        val experienceInfos = experienceInfo.value
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (experienceInfos.experienceIds.isEmpty()) {
                Text(
                    text = day.date.dayOfMonth.toString(),
                    color = Color.LightGray
                )
            } else {
                Text(
                    text = day.date.dayOfMonth.toString(),
                )
            }
            HorizontalColorRectangle(
                modifier = Modifier
                    .height(7.dp)
                    .fillMaxWidth(fraction = 0.7f)
                    .clip(RoundedCornerShape(2.dp)),
                colors = experienceInfos.colors
            )
        }
    }
}

@Composable
fun HorizontalColorRectangle(
    modifier: Modifier,
    colors: List<AdaptiveColor>
) {
    val isDarkTheme = isSystemInDarkTheme()
    if (colors.size >= 2) {
        val brush = remember(colors) {
            val composeColors = colors.map { it.getComposeColor(isDarkTheme) }
            Brush.horizontalGradient(colors = composeColors)
        }
        Box(
            modifier = modifier
                .background(brush),
        ) {}
    } else if (colors.size == 1) {
        Box(
            modifier = modifier
                .background(
                    colors.first().getComposeColor(isDarkTheme)
                ),
        ) {}
    } else {
        Box(
            modifier = modifier
        ) {}
    }
}