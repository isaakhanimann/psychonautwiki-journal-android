/*
 * Copyright (c) 2022. Isaak Hanimann.
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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.isaakhanimann.journal.ui.main.navigation.composableWithTransitions
import com.isaakhanimann.journal.ui.main.navigation.routers.*
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.ExplainTimelineScreen
import com.isaakhanimann.journal.ui.tabs.safer.VolumetricDosingScreen
import com.isaakhanimann.journal.ui.tabs.search.SearchScreen
import com.isaakhanimann.journal.ui.tabs.search.custom.AddCustomSubstance
import com.isaakhanimann.journal.ui.tabs.search.custom.EditCustomSubstance
import com.isaakhanimann.journal.ui.tabs.search.substance.SubstanceScreen
import com.isaakhanimann.journal.ui.tabs.search.substance.UrlScreen
import com.isaakhanimann.journal.ui.tabs.search.substance.category.CategoryScreen


fun NavGraphBuilder.searchGraph(navController: NavController) {
    navigation(
        startDestination = NoArgumentRouter.SearchRouter.route,
        route = TabRouter.Search.route,
    ) {
        composableWithTransitions(
            route = NoArgumentRouter.SearchRouter.route,
        ) {
            SearchScreen(
                onSubstanceTap = {
                    navController.navigateToSubstanceScreen(substanceName = it)
                },
                onCustomSubstanceTap = navController::navigateToEditCustomSubstance,
                navigateToAddCustomSubstanceScreen = navController::navigateToAddCustom,
                modifier = Modifier.fillMaxSize()
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
                navigateToArticle = navController::navigateToURLScreenOnSearchTab
            )
        }
        composableWithTransitions(
            ArgumentRouter.URLRouterOnSearchTab.route,
            arguments = ArgumentRouter.URLRouterOnSearchTab.args
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val url = args.getString(URL_KEY)!!
            UrlScreen(url = url)
        }
        composableWithTransitions(
            ArgumentRouter.CategoryRouter.route,
            arguments = ArgumentRouter.CategoryRouter.args
        ) {
            CategoryScreen(navigateToURL = navController::navigateToURLScreenOnSearchTab)
        }
        composableWithTransitions(
            ArgumentRouter.EditCustomRouter.route,
            arguments = ArgumentRouter.EditCustomRouter.args
        ) {
            EditCustomSubstance(navigateBack = navController::popBackStack)
        }
        composableWithTransitions(NoArgumentRouter.AddCustomRouter.route) {
            AddCustomSubstance(
                navigateBack = navController::popBackStack
            )
        }
        composableWithTransitions(NoArgumentRouter.VolumetricDosingOnSearchTabRouter.route) {
            VolumetricDosingScreen(
                navigateToVolumetricLiquidDosingArticle = {
                    navController.navigateToURLScreenOnSearchTab(
                        "https://psychonautwiki.org/wiki/Volumetric_liquid_dosing"
                    )
                })
        }
        composableWithTransitions(NoArgumentRouter.ExplainTimelineOnSearchTabRouter.route) { ExplainTimelineScreen() }
    }
}