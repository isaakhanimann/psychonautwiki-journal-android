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
import com.isaakhanimann.journal.ui.main.navigation.composableWithTransitions
import com.isaakhanimann.journal.ui.main.navigation.routers.ArgumentRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.EXPERIENCE_ID_KEY
import com.isaakhanimann.journal.ui.main.navigation.routers.NoArgumentRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.TabRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToAddRating
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToAddTimedNote
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToEditExperience
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToEditRating
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToEditTimedNote
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToExperience
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToExplainTimelineOnJournalTab
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToIngestion
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToTimelineScreen
import com.isaakhanimann.journal.ui.tabs.journal.JournalScreen
import com.isaakhanimann.journal.ui.tabs.journal.calendar.CalendarJournalScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.OneExperienceScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.edit.EditExperienceScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.editingestion.EditIngestionScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.rating.add.AddRatingScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.rating.edit.EditRatingScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.timednote.add.AddTimedNoteScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.timednote.edit.EditTimedNoteScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.ExplainTimelineScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.screen.TimelineScreen
import com.isaakhanimann.journal.ui.tabs.safer.DoseExplanationScreen
import com.isaakhanimann.journal.ui.tabs.safer.VolumetricDosingScreen
import com.isaakhanimann.journal.ui.tabs.search.substance.SaferSniffingScreen

fun NavGraphBuilder.journalGraph(navController: NavHostController) {
    navigation(
        startDestination = NoArgumentRouter.JournalRouter.route,
        route = TabRouter.Journal.route,
    ) {
        composableWithTransitions(
            route = NoArgumentRouter.JournalRouter.route,
        ) {
            JournalScreen(
                navigateToExperiencePopNothing = navController::navigateToExperience,
                navigateToAddIngestion = navController::navigateToAddIngestion,
                navigateToCalendar = navController::navigateToCalendar
            )
        }
        composableWithTransitions(
            ArgumentRouter.EditExperienceRouter.route,
            arguments = ArgumentRouter.EditExperienceRouter.args
        ) {
            EditExperienceScreen(navigateBack = navController::popBackStack)
        }
        composableWithTransitions(
            ArgumentRouter.AddRatingRouter.route,
            arguments = ArgumentRouter.AddRatingRouter.args
        ) {
            AddRatingScreen(navigateBack = navController::popBackStack)
        }
        composableWithTransitions(
            ArgumentRouter.AddTimedNoteRouter.route,
            arguments = ArgumentRouter.AddTimedNoteRouter.args
        ) {
            AddTimedNoteScreen(navigateBack = navController::popBackStack)
        }
        composableWithTransitions(
            ArgumentRouter.EditRatingRouter.route,
            arguments = ArgumentRouter.EditRatingRouter.args
        ) {
            EditRatingScreen(navigateBack = navController::popBackStack)
        }
        composableWithTransitions(
            ArgumentRouter.EditTimedNoteRouter.route,
            arguments = ArgumentRouter.EditTimedNoteRouter.args
        ) {
            EditTimedNoteScreen(navigateBack = navController::popBackStack)
        }
        composableWithTransitions(
            ArgumentRouter.TimelineScreenRouter.route,
            arguments = ArgumentRouter.TimelineScreenRouter.args
        ) {
            TimelineScreen()
        }
        composableWithTransitions(NoArgumentRouter.VolumetricDosingOnJournalTabRouter.route) {
            VolumetricDosingScreen()
        }
        composableWithTransitions(
            ArgumentRouter.ExperienceRouter.route,
            arguments = ArgumentRouter.ExperienceRouter.args
        ) {
            val experienceId = it.arguments!!.getInt(EXPERIENCE_ID_KEY)
            OneExperienceScreen(
                navigateToAddIngestionSearch = navController::navigateToAddIngestion,
                navigateToExplainTimeline = navController::navigateToExplainTimelineOnJournalTab,
                navigateToEditExperienceScreen = {
                    navController.navigateToEditExperience(experienceId)
                },
                navigateToIngestionScreen = { ingestionId ->
                    navController.navigateToIngestion(ingestionId)
                },
                navigateBack = navController::popBackStack,
                navigateToAddRatingScreen = {
                    navController.navigateToAddRating(experienceId)
                },
                navigateToAddTimedNoteScreen = {
                    navController.navigateToAddTimedNote(experienceId)
                },
                navigateToEditRatingScreen = navController::navigateToEditRating,
                navigateToTimelineScreen = { consumerName ->
                    navController.navigateToTimelineScreen(consumerName, experienceId)
                },
                navigateToEditTimedNoteScreen = { timedNoteID ->
                    navController.navigateToEditTimedNote(
                        timedNoteId = timedNoteID,
                        experienceId = experienceId
                    )
                }
            )
        }
        composableWithTransitions(
            ArgumentRouter.IngestionRouter.route,
            arguments = ArgumentRouter.IngestionRouter.args
        ) {
            EditIngestionScreen(navigateBack = navController::popBackStack)
        }
        addIngestionGraph(navController)
        composableWithTransitions(NoArgumentRouter.ExplainTimelineOnJournalTabRouter.route) { ExplainTimelineScreen() }
        composableWithTransitions(NoArgumentRouter.DosageExplanationRouterOnSearchTab.route) { DoseExplanationScreen() }
        composableWithTransitions(NoArgumentRouter.SaferSniffingOnJournalTab.route) { SaferSniffingScreen() }
        composableWithTransitions(NoArgumentRouter.CalendarRouter.route) {
            CalendarJournalScreen(
                navigateToExperiencePopNothing = navController::navigateToExperience,
            )
        }
    }
}