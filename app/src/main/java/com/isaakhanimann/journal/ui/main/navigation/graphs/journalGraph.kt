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
import com.isaakhanimann.journal.ui.tabs.journal.experience.rating.add.AddRatingScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.rating.edit.EditRatingScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.timednote.add.AddTimedNoteScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.timednote.edit.EditTimedNoteScreen
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.ExplainTimelineScreen
import com.isaakhanimann.journal.ui.tabs.safer.DoseExplanationScreen
import com.isaakhanimann.journal.ui.tabs.safer.VolumetricDosingScreen
import com.isaakhanimann.journal.ui.tabs.search.substance.SaferSniffingScreen
import com.isaakhanimann.journal.ui.tabs.settings.FAQScreen
import com.isaakhanimann.journal.ui.tabs.settings.SettingsScreen
import com.isaakhanimann.journal.ui.tabs.settings.combinations.CombinationSettingsScreen


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
                navigateToAddIngestion = navController::navigateToAddIngestion,
                navigateToSettings = navController::navigateToSettings
            )
        }
        composableWithTransitions(NoArgumentRouter.SettingsRouter.route) {
            SettingsScreen(
                navigateToFAQ = navController::navigateToFAQ,
                navigateToComboSettings = navController::navigateToComboSettings,
            )
        }
        composableWithTransitions(NoArgumentRouter.FAQRouter.route) { FAQScreen() }
        composableWithTransitions(NoArgumentRouter.CombinationSettingsRouter.route) { CombinationSettingsScreen() }
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
        composableWithTransitions(NoArgumentRouter.VolumetricDosingOnJournalTabRouter.route) {
            VolumetricDosingScreen(
                navigateToVolumetricLiquidDosingArticle = {
                    navController.navigateToURLInJournalTab(url = VOLUMETRIC_DOSE_ARTICLE_URL)
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
                navigateToAddRatingScreen = {
                    navController.navigateToAddRating(experienceId)
                },
                navigateToAddTimedNoteScreen = {
                    navController.navigateToAddTimedNote(experienceId)
                },
                navigateToURL = navController::navigateToURLInJournalTab,
                navigateToEditRatingScreen = navController::navigateToEditRating,
                navigateToEditTimedNoteScreen = {
                    navController.navigateToEditTimedNote(timedNoteId = it, experienceId = experienceId)
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
    }
}