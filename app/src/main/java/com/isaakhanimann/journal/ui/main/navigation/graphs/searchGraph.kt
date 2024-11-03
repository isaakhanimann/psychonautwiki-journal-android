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
import com.isaakhanimann.journal.ui.main.JournalBottomNavigationBar
import com.isaakhanimann.journal.ui.main.navigation.composableWithTransitions
import com.isaakhanimann.journal.ui.main.navigation.routers.ArgumentRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.NoArgumentRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.TabRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToAddCustom
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToCategoryScreen
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToDosageExplanationScreenOnSearchTab
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToEditCustomSubstance
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToExplainTimelineOnSearchTab
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToSaferHallucinogens
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToSaferStimulants
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToSubstanceScreen
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToVolumetricDosingScreenOnSearchTab
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.ExplainTimelineScreen
import com.isaakhanimann.journal.ui.tabs.safer.VolumetricDosingScreen
import com.isaakhanimann.journal.ui.tabs.search.SearchScreen
import com.isaakhanimann.journal.ui.tabs.search.custom.AddCustomSubstance
import com.isaakhanimann.journal.ui.tabs.search.custom.EditCustomSubstance
import com.isaakhanimann.journal.ui.tabs.search.substance.SubstanceScreen
import com.isaakhanimann.journal.ui.tabs.search.substance.category.CategoryScreen

fun NavGraphBuilder.searchGraph(navController: NavHostController) {
    navigation(
        startDestination = NoArgumentRouter.SubstancesRouter.route,
        route = TabRouter.Substances.route,
    ) {
        composableWithTransitions(
            route = NoArgumentRouter.SubstancesRouter.route,
        ) {
            SearchScreen(
                onSubstanceTap = {
                    navController.navigateToSubstanceScreen(substanceName = it.name)
                },
                onCustomSubstanceTap = navController::navigateToEditCustomSubstance,
                navigateToAddCustomSubstanceScreen = navController::navigateToAddCustom,
                bottomAppBar = {
                    JournalBottomNavigationBar(navController)
                }
            )
        }
        composableWithTransitions(
            route = ArgumentRouter.SubstanceRouter.route,
            arguments = ArgumentRouter.SubstanceRouter.args,
        ) {
            SubstanceScreen(
                navigateToDosageExplanationScreen = navController::navigateToDosageExplanationScreenOnSearchTab,
                navigateToSaferHallucinogensScreen = navController::navigateToSaferHallucinogens,
                navigateToSaferStimulantsScreen = navController::navigateToSaferStimulants,
                navigateToExplainTimeline = navController::navigateToExplainTimelineOnSearchTab,
                navigateToCategoryScreen = navController::navigateToCategoryScreen,
                navigateToVolumetricDosingScreen = navController::navigateToVolumetricDosingScreenOnSearchTab,
            )
        }
        composableWithTransitions(
            ArgumentRouter.CategoryRouter.route, arguments = ArgumentRouter.CategoryRouter.args
        ) {
            CategoryScreen()
        }
        composableWithTransitions(
            ArgumentRouter.EditCustomRouter.route, arguments = ArgumentRouter.EditCustomRouter.args
        ) {
            EditCustomSubstance(navigateBack = navController::popBackStack)
        }
        composableWithTransitions(NoArgumentRouter.AddCustomRouter.route) {
            AddCustomSubstance(
                navigateBack = navController::popBackStack
            )
        }
        composableWithTransitions(NoArgumentRouter.VolumetricDosingOnSearchTabRouter.route) {
            VolumetricDosingScreen()
        }
        composableWithTransitions(NoArgumentRouter.ExplainTimelineOnSearchTabRouter.route) { ExplainTimelineScreen() }
    }
}