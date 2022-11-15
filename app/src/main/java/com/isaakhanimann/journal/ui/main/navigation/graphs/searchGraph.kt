/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.main.navigation.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.isaakhanimann.journal.ui.journal.experience.timeline.ExplainTimelineScreen
import com.isaakhanimann.journal.ui.main.navigation.*
import com.isaakhanimann.journal.ui.main.navigation.routers.*
import com.isaakhanimann.journal.ui.main.navigation.transitions.regularComposableWithTransitions
import com.isaakhanimann.journal.ui.main.navigation.transitions.tabNavigationWithTransitions
import com.isaakhanimann.journal.ui.search.SearchScreen
import com.isaakhanimann.journal.ui.search.custom.AddCustomSubstance
import com.isaakhanimann.journal.ui.search.custom.EditCustomSubstance
import com.isaakhanimann.journal.ui.search.substance.SubstanceScreen
import com.isaakhanimann.journal.ui.search.substance.UrlScreen
import com.isaakhanimann.journal.ui.search.substance.category.CategoryScreen


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.searchGraph(navController: NavController) {
    tabNavigationWithTransitions(
        startDestination = NoArgumentRouter.SearchRouter.route,
        route = TabRouter.Search.route, // todo add animation
    ) {
        composable(
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
        regularComposableWithTransitions(
            route = ArgumentRouter.SubstanceRouter.route,
            arguments = ArgumentRouter.SubstanceRouter.args,
        ) {
            SubstanceScreen(
                navigateToDosageExplanationScreen = navController::navigateToDosageExplanationScreen,
                navigateToSaferHallucinogensScreen = navController::navigateToSaferHallucinogens,
                navigateToSaferStimulantsScreen = navController::navigateToSaferStimulants,
                navigateToExplainTimeline = navController::navigateToExplainTimeline,
                navigateToCategoryScreen = navController::navigateToCategoryScreen,
                navigateToVolumetricDosingScreen = navController::navigateToVolumetricDosingScreen,
                navigateToArticle = navController::navigateToURLScreen
            )
        }
        regularComposableWithTransitions(
            ArgumentRouter.URLRouter.route,
            arguments = ArgumentRouter.URLRouter.args
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val url = args.getString(URL_KEY)!!
            UrlScreen(url = url)
        }
        regularComposableWithTransitions(
            ArgumentRouter.CategoryRouter.route,
            arguments = ArgumentRouter.CategoryRouter.args
        ) {
            CategoryScreen(navigateToURL = navController::navigateToURLScreen)
        }
        regularComposableWithTransitions(
            ArgumentRouter.EditCustomRouter.route,
            arguments = ArgumentRouter.EditCustomRouter.args
        ) {
            EditCustomSubstance(navigateBack = navController::popBackStack)
        }
        regularComposableWithTransitions(NoArgumentRouter.AddCustomRouter.route) {
            AddCustomSubstance(
                navigateBack = navController::popBackStack
            )
        }
        regularComposableWithTransitions(NoArgumentRouter.ExplainTimelineRouter.route) { ExplainTimelineScreen() }
    }
}