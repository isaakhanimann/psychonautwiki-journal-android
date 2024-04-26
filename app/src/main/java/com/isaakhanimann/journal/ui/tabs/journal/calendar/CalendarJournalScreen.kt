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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.relations.ExperienceWithIngestionsCompanionsAndRatings
import com.isaakhanimann.journal.ui.tabs.journal.components.ExperienceRow
import com.isaakhanimann.journal.ui.theme.JournalTheme
import com.isaakhanimann.journal.ui.theme.horizontalPadding
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@Preview
@Composable
fun CalendarJournalScreenPreview() {
    JournalTheme {
        CalendarJournalScreen(
            navigateToExperiencePopNothing = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarJournalScreen(
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusYears(20) }
    val endMonth = remember { currentMonth.plusMonths(10) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth.minusMonths(1),
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
                            state.animateScrollToMonth(YearMonth.now().minusMonths(1))
                        }
                    }) {
                        Text(text = "Today")
                    }
                }
            )
        },
    ) { padding ->
        val sheetState = rememberModalBottomSheetState()
        var showBottomSheet by remember { mutableStateOf(false) }
        var experienceIdsToShowInSheet: List<Int> by remember {
            mutableStateOf(emptyList())
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                val scope = rememberCoroutineScope()
                SheetContent(
                    experienceIds = experienceIdsToShowInSheet,
                    navigateToExperiencePopNothing = navigateToExperiencePopNothing,
                    dismissSheet = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    })
            }
        }
        VerticalCalendar(
            modifier = Modifier.padding(padding),
            state = state,
            dayContent = { calendarDay ->
                Day(
                    calendarDay,
                    navigateToExperiencePopNothing,
                    chooseBetweenExperiences = { experienceIds ->
                        experienceIdsToShowInSheet = experienceIds
                        showBottomSheet = true
                    }
                )
            },
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
fun SheetContent(
    experienceIds: List<Int>,
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    dismissSheet: () -> Unit
) {
    LazyColumn {
        items(experienceIds) { experienceId ->
            ExperienceRowWithFetch(
                experienceId = experienceId,
                navigateToExperiencePopNothing,
                dismissSheet
            )
            HorizontalDivider()
        }
    }
}

@Composable
fun ExperienceRowWithFetch(
    experienceId: Int,
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    dismissSheet: () -> Unit
) {
    val viewModel: ExperienceFetchViewModel = hiltViewModel()
    val experienceState: MutableState<ExperienceWithIngestionsCompanionsAndRatings?> = remember {
        mutableStateOf(
            null
        )
    }
    LaunchedEffect(key1 = experienceId) {
        experienceState.value = viewModel.getExperience(experienceId)
    }
    experienceState.value?.let { experience ->
        ExperienceRow(
            experience,
            navigateToExperienceScreen = {
                dismissSheet()
                navigateToExperiencePopNothing(experienceId)
            },
            isTimeRelativeToNow = false
        )
    }
}

@Composable
fun Day(
    day: CalendarDay,
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    chooseBetweenExperiences: (experienceIds: List<Int>) -> Unit
) {
    if (day.position == DayPosition.MonthDate) {
        val viewModel: DayViewModel = hiltViewModel()
        val experienceInfoState: MutableState<ExperienceInfo> = remember {
            mutableStateOf(
                ExperienceInfo(
                    experienceIds = emptyList(),
                    colors = emptyList()
                )
            )
        }
        LaunchedEffect(Unit) {
            experienceInfoState.value = viewModel.getExperienceInfo(day)
        }
        val experienceInfos = experienceInfoState.value
        val aspectRatioModifier = Modifier.aspectRatio(1f) // This is important for square sizing!
        val modifier = if (experienceInfos.experienceIds.count() == 1) {
            aspectRatioModifier.clickable {
                navigateToExperiencePopNothing(experienceInfos.experienceIds.first())
            }
        } else if (experienceInfos.experienceIds.count() > 1) {
            aspectRatioModifier.clickable {
                chooseBetweenExperiences(experienceInfos.experienceIds)
            }
        } else {
            aspectRatioModifier
        }
        val isToday = day.date == LocalDate.now()
        Box(
            modifier = modifier
                .padding(4.dp)
                .background(
                    if (isToday) MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f) else Color.Transparent,
                    shape = RoundedCornerShape(20)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (isToday) {
                    Text(
                        text = day.date.dayOfMonth.toString(),
                    )
                } else if (experienceInfos.experienceIds.isEmpty()) {
                    Text(
                        text = day.date.dayOfMonth.toString(),
                        color = if (isSystemInDarkTheme()) Color.Gray else Color.LightGray
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