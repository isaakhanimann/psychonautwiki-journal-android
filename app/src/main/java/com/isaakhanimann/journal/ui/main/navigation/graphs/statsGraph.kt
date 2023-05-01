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
import com.isaakhanimann.journal.ui.main.navigation.composableWithTransitions
import com.isaakhanimann.journal.ui.main.navigation.routers.ArgumentRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.NoArgumentRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.TabRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToSubstanceCompanionScreen
import com.isaakhanimann.journal.ui.tabs.stats.StatsScreen
import com.isaakhanimann.journal.ui.tabs.stats.substancecompanion.SubstanceCompanionScreen


fun NavGraphBuilder.statsGraph(
    navController: NavController,
    openNavigationDrawer: () -> Unit
) {
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
                },
                openNavigationDrawer = openNavigationDrawer
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