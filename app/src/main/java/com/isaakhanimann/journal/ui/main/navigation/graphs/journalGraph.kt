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

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.isaakhanimann.journal.ui.VOLUMETRIC_DOSE_ARTICLE_URL
import com.isaakhanimann.journal.ui.main.navigation.composableWithTransitions
import com.isaakhanimann.journal.ui.main.navigation.routers.*
import com.isaakhanimann.journal.ui.tabs.journal.JournalScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.OneExperienceScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.edit.EditExperienceScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.editingestion.EditIngestionScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.ExplainTimelineScreen
import com.isaakhanimann.journal.ui.tabs.safer.DoseExplanationScreen
import com.isaakhanimann.journal.ui.tabs.safer.VolumetricDosingScreen
import com.isaakhanimann.journal.ui.tabs.search.substance.SaferSniffingScreen


fun NavGraphBuilder.journalGraph(
    navController: NavController,
    openNavigationDrawer: () -> Unit
) {
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
                navigateToAddIngestion = navController::navigateToAddIngestion,
                openNavigationDrawer = openNavigationDrawer
            )
        }
        composableWithTransitions(
            ArgumentRouter.EditExperienceRouter.route,
            arguments = ArgumentRouter.EditExperienceRouter.args
        ) {
            EditExperienceScreen(navigateBack = navController::popBackStack)
        }
        composableWithTransitions(NoArgumentRouter.VolumetricDosingOnJournalTabRouter.route) {
            VolumetricDosingScreen(
                navigateToVolumetricLiquidDosingArticle = {
                    navController.navigateToURLInJournalTab(
                        VOLUMETRIC_DOSE_ARTICLE_URL
                    )
                })
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
                navigateToURL = navController::navigateToURLInJournalTab
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
    }
}