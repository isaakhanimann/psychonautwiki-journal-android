/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.main.navigation.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.isaakhanimann.journal.ui.main.navigation.routers.*
import com.isaakhanimann.journal.ui.main.navigation.transitions.regularComposableWithTransitions
import com.isaakhanimann.journal.ui.main.navigation.transitions.tabNavigationWithTransitions
import com.isaakhanimann.journal.ui.stats.StatsScreen
import com.isaakhanimann.journal.ui.stats.substancecompanion.SubstanceCompanionScreen


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.statsGraph(navController: NavController) {
    tabNavigationWithTransitions(
        startDestination = NoArgumentRouter.StatsRouter.route,
        route = TabRouter.Statistics.route, // todo add animation
    ) {
        composable(
            route = NoArgumentRouter.StatsRouter.route,
        ) {
            StatsScreen(
                navigateToSubstanceCompanion = {
                    navController.navigateToSubstanceCompanionScreen(substanceName = it)
                }
            )
        }
        regularComposableWithTransitions(
            ArgumentRouter.SubstanceCompanionRouter.route,
            arguments = ArgumentRouter.SubstanceCompanionRouter.args
        ) {
            SubstanceCompanionScreen()
        }
    }
}