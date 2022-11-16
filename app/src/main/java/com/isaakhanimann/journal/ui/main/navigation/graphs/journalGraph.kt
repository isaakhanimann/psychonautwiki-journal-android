/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.main.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.isaakhanimann.journal.ui.main.navigation.composableWithTransitions
import com.isaakhanimann.journal.ui.main.navigation.routers.*
import com.isaakhanimann.journal.ui.tabs.journal.JournalScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.ExperienceScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.edit.EditExperienceScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.editingestion.EditIngestionScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.ExplainTimelineScreen


fun NavGraphBuilder.journalGraph(navController: NavController) {
    navigation(
        startDestination = NoArgumentRouter.JournalRouter.route,
        route = TabRouter.Journal.route,
    ) {
        composableWithTransitions(
            route = NoArgumentRouter.JournalRouter.route,
        ) {
            JournalScreen(
                navigateToExperiencePopNothing = {
                    navController.navigateToExperience(experienceId = it)
                },
                navigateToAddIngestion = navController::navigateToAddIngestion
            )
        }
        composableWithTransitions(
            ArgumentRouter.EditExperienceRouter.route,
            arguments = ArgumentRouter.EditExperienceRouter.args
        ) {
            EditExperienceScreen(navigateBack = navController::popBackStack)
        }
        composableWithTransitions(
            ArgumentRouter.ExperienceRouter.route,
            arguments = ArgumentRouter.ExperienceRouter.args
        ) {
            val experienceId = it.arguments!!.getInt(EXPERIENCE_ID_KEY)
            ExperienceScreen(
                navigateToAddIngestionSearch = navController::navigateToAddIngestion,
                navigateToExplainTimeline = navController::navigateToExplainTimelineOnJournalTab,
                navigateToEditExperienceScreen = {
                    navController.navigateToEditExperience(experienceId)
                },
                navigateToIngestionScreen = { ingestionId ->
                    navController.navigateToIngestion(ingestionId)
                },
                navigateBack = navController::popBackStack
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
    }
}