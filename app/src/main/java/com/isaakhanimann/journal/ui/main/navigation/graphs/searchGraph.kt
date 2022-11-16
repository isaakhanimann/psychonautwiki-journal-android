/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
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
                navigateToDosageExplanationScreen = navController::navigateToDosageExplanationScreen,
                navigateToSaferHallucinogensScreen = navController::navigateToSaferHallucinogens,
                navigateToSaferStimulantsScreen = navController::navigateToSaferStimulants,
                navigateToExplainTimeline = navController::navigateToExplainTimelineOnSearchTab,
                navigateToCategoryScreen = navController::navigateToCategoryScreen,
                navigateToVolumetricDosingScreen = navController::navigateToVolumetricDosingScreen,
                navigateToArticle = navController::navigateToURLScreen
            )
        }
        composableWithTransitions(
            ArgumentRouter.URLRouter.route,
            arguments = ArgumentRouter.URLRouter.args
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val url = args.getString(URL_KEY)!!
            UrlScreen(url = url)
        }
        composableWithTransitions(
            ArgumentRouter.CategoryRouter.route,
            arguments = ArgumentRouter.CategoryRouter.args
        ) {
            CategoryScreen(navigateToURL = navController::navigateToURLScreen)
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
        composableWithTransitions(NoArgumentRouter.ExplainTimelineOnSearchTabRouter.route) { ExplainTimelineScreen() }
    }
}