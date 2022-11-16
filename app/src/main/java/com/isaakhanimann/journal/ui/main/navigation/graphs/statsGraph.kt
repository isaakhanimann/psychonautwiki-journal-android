/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.main.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.isaakhanimann.journal.ui.main.navigation.composableWithTransitions
import com.isaakhanimann.journal.ui.main.navigation.routers.ArgumentRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.NoArgumentRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.TabRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToSubstanceCompanionScreen
import com.isaakhanimann.journal.ui.tabs.stats.StatsScreen
import com.isaakhanimann.journal.ui.tabs.stats.substancecompanion.SubstanceCompanionScreen


fun NavGraphBuilder.statsGraph(navController: NavController) {
    navigation(
        startDestination = NoArgumentRouter.StatsRouter.route,
        route = TabRouter.Statistics.route,
    ) {
        composableWithTransitions(
            route = NoArgumentRouter.StatsRouter.route,
        ) {
            StatsScreen(
                navigateToSubstanceCompanion = {
                    navController.navigateToSubstanceCompanionScreen(substanceName = it)
                }
            )
        }
        composableWithTransitions(
            ArgumentRouter.SubstanceCompanionRouter.route,
            arguments = ArgumentRouter.SubstanceCompanionRouter.args
        ) {
            SubstanceCompanionScreen()
        }
    }
}