/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.main.navigation.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.isaakhanimann.journal.ui.journal.JournalScreen
import com.isaakhanimann.journal.ui.journal.experience.ExperienceScreen
import com.isaakhanimann.journal.ui.journal.experience.edit.EditExperienceScreen
import com.isaakhanimann.journal.ui.journal.experience.editingestion.EditIngestionScreen
import com.isaakhanimann.journal.ui.main.navigation.routers.*
import com.isaakhanimann.journal.ui.main.navigation.transitions.regularComposableWithTransitions
import com.isaakhanimann.journal.ui.main.navigation.transitions.tabNavigationWithTransitions


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.journalGraph(navController: NavController) {
    tabNavigationWithTransitions(
        startDestination = NoArgumentRouter.JournalRouter.route,
        route = TabRouter.Journal.route, // todo add animation
    ) {
        composable(
            route = NoArgumentRouter.JournalRouter.route,
        ) {
            JournalScreen(
                navigateToExperiencePopNothing = {
                    navController.navigateToExperiencePopNothing(experienceId = it)
                },
                navigateToAddIngestion = navController::navigateToAddIngestion
            )
        }
        regularComposableWithTransitions(
            ArgumentRouter.EditExperienceRouter.route,
            arguments = ArgumentRouter.EditExperienceRouter.args
        ) {
            EditExperienceScreen(navigateBack = navController::popBackStack)
        }
        regularComposableWithTransitions(
            ArgumentRouter.ExperienceRouter.route,
            arguments = ArgumentRouter.ExperienceRouter.args
        ) {
            val experienceId = it.arguments!!.getInt(EXPERIENCE_ID_KEY)
            ExperienceScreen(
                navigateToAddIngestionSearch = navController::navigateToAddIngestion,
                navigateToExplainTimeline = navController::navigateToExplainTimeline,
                navigateToEditExperienceScreen = {
                    navController.navigateToEditExperience(experienceId)
                },
                navigateToIngestionScreen = { ingestionId ->
                    navController.navigateToIngestion(ingestionId)
                },
                navigateBack = navController::popBackStack
            )
        }
        regularComposableWithTransitions(
            ArgumentRouter.IngestionRouter.route,
            arguments = ArgumentRouter.IngestionRouter.args
        ) {
            EditIngestionScreen(navigateBack = navController::popBackStack)
        }
        addIngestionGraph(navController)
    }
}