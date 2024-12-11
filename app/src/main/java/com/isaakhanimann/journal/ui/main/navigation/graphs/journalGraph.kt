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

package com.isaakhanimann.journal.ui.main.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.isaakhanimann.journal.ui.main.navigation.JournalTopLevelRoute
import com.isaakhanimann.journal.ui.main.navigation.composableWithTransitions
import com.isaakhanimann.journal.ui.tabs.journal.JournalScreen
import com.isaakhanimann.journal.ui.tabs.journal.calendar.CalendarJournalScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.ExperienceScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.edit.EditExperienceScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.editingestion.EditIngestionScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.rating.add.AddRatingScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.rating.edit.EditRatingScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.timednote.add.AddTimedNoteScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.timednote.edit.EditTimedNoteScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.ExplainTimelineScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.screen.TimelineScreen
import com.isaakhanimann.journal.ui.tabs.safer.VolumetricDosingScreen
import com.isaakhanimann.journal.ui.tabs.search.substance.SaferSniffingScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.journalGraph(navController: NavHostController) {
    navigation<JournalTopLevelRoute>(
        startDestination = JournalScreenRoute,
    ) {
        composableWithTransitions<JournalScreenRoute> {
            JournalScreen(
                navigateToExperiencePopNothing = { experienceId ->
                    navController.navigate(ExperienceRoute(experienceId))
                },
                navigateToAddIngestion = {
                    navController.navigate(AddIngestionRoute)
                },
                navigateToCalendar = {
                    navController.navigate(CalendarRoute)
                }
            )
        }
        composableWithTransitions<EditExperienceRoute> {
            EditExperienceScreen(navigateBack = navController::popBackStack)
        }
        composableWithTransitions<AddRatingRoute> {
            AddRatingScreen(navigateBack = navController::popBackStack)
        }
        composableWithTransitions<AddTimedNoteRoute> {
            AddTimedNoteScreen(navigateBack = navController::popBackStack)
        }
        composableWithTransitions<EditRatingRoute> {
            EditRatingScreen(navigateBack = navController::popBackStack)
        }
        composableWithTransitions<EditTimedNoteRoute> {
            EditTimedNoteScreen(navigateBack = navController::popBackStack)
        }
        composableWithTransitions<TimelineScreenRoute> {
            TimelineScreen()
        }
        composableWithTransitions<VolumetricDosingOnJournalTabRoute> {
            VolumetricDosingScreen()
        }
        composableWithTransitions<ExperienceRoute> { backStackEntry ->
            val experienceRoute: ExperienceRoute = backStackEntry.toRoute()
            val experienceId = experienceRoute.experienceId
            ExperienceScreen(
                navigateToAddIngestionSearch = {
                    navController.navigate(AddIngestionRoute)
                },
                navigateToExplainTimeline = {
                    navController.navigate(ExplainTimelineOnJournalTabRoute)
                },
                navigateToEditExperienceScreen = {
                    navController.navigate(EditExperienceRoute(experienceId))
                },
                navigateToIngestionScreen = { ingestionId ->
                    navController.navigate(EditIngestionRoute(ingestionId))
                },
                navigateBack = navController::popBackStack,
                navigateToAddRatingScreen = {
                    navController.navigate(AddRatingRoute(experienceId))
                },
                navigateToAddTimedNoteScreen = {
                    navController.navigate(AddTimedNoteRoute(experienceId))
                },
                navigateToEditRatingScreen = { ratingId ->
                    navController.navigate(EditRatingRoute(ratingId))
                },
                navigateToTimelineScreen = { consumerName ->
                    navController.navigate(
                        TimelineScreenRoute(
                            consumerName = consumerName,
                            experienceId = experienceId
                        )
                    )
                },
                navigateToEditTimedNoteScreen = { timedNoteID ->
                    navController.navigate(
                        EditTimedNoteRoute(
                            timedNoteId = timedNoteID,
                            experienceId = experienceId
                        )
                    )
                }
            )
        }
        composableWithTransitions<EditIngestionRoute> {
            EditIngestionScreen(
                navigateBack = navController::popBackStack,
                navigateToAddIngestion = {
                    navController.navigate(AddIngestionRoute)
                }
            )
        }
        addIngestionGraph(navController)
        composableWithTransitions<ExplainTimelineOnJournalTabRoute> { ExplainTimelineScreen() }
        composableWithTransitions<SaferSniffingRouteOnJournalTab> { SaferSniffingScreen() }
        composableWithTransitions<CalendarRoute> {
            CalendarJournalScreen(
                navigateToExperiencePopNothing = { experienceId ->
                    navController.navigate(ExperienceRoute(experienceId))
                },
            )
        }
    }
}

@Serializable
object JournalScreenRoute

@Serializable
data class EditExperienceRoute(val experienceId: Int)

@Serializable
data class AddRatingRoute(val experienceId: Int)

@Serializable
data class AddTimedNoteRoute(val experienceId: Int)

@Serializable
data class EditRatingRoute(val ratingId: Int)

@Serializable
data class EditTimedNoteRoute(val timedNoteId: Int, val experienceId: Int)

@Serializable
data class TimelineScreenRoute(val consumerName: String, val experienceId: Int)

@Serializable
object VolumetricDosingOnJournalTabRoute

@Serializable
data class ExperienceRoute(val experienceId: Int)

@Serializable
data class EditIngestionRoute(val ingestionId: Int)

@Serializable
object ExplainTimelineOnJournalTabRoute

@Serializable
object SaferSniffingRouteOnJournalTab

@Serializable
object CalendarRoute