/*
 * Copyright (c) 2024. Isaak Hanimann.
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
import androidx.navigation.navigation
import com.isaakhanimann.journal.ui.main.navigation.composableWithTransitions
import com.isaakhanimann.journal.ui.main.navigation.routers.ArgumentRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.NoArgumentRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.SUBSTANCE_NAME_KEY
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToChooseRouteOfAddCustomUnit
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToFinishAddCustomUnit
import com.isaakhanimann.journal.ui.tabs.settings.customunits.add.ChooseRouteDuringAddCustomUnitScreen
import com.isaakhanimann.journal.ui.tabs.settings.customunits.add.ChooseSubstanceScreen
import com.isaakhanimann.journal.ui.tabs.settings.customunits.add.FinishAddCustomUnitScreen

fun NavGraphBuilder.addCustomUnitGraph(navController: NavController) {
    navigation(
        startDestination = NoArgumentRouter.AddCustomUnitsSearchSubstanceRouter.route,
        route = NoArgumentRouter.AddCustomUnitsRouter.route,
    ) {
        composableWithTransitions(NoArgumentRouter.AddCustomUnitsSearchSubstanceRouter.route) {
            ChooseSubstanceScreen(
                navigateToChooseRoute = { substanceName ->
                    navController.navigateToChooseRouteOfAddCustomUnit(substanceName)
                }
            )
        }
        composableWithTransitions(
            ArgumentRouter.ChooseRouteOfAddCustomUnitRouter.route,
            arguments = ArgumentRouter.ChooseRouteOfAddCustomUnitRouter.args
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
            ChooseRouteDuringAddCustomUnitScreen(
                onRouteChosen = { administrationRoute ->
                    navController.navigateToFinishAddCustomUnit(
                        substanceName = substanceName,
                        administrationRoute = administrationRoute,
                    )
                }
            )
        }
        composableWithTransitions(
            ArgumentRouter.FinishAddCustomUnitRouter.route,
            arguments = ArgumentRouter.FinishAddCustomUnitRouter.args
        ) {
            FinishAddCustomUnitScreen(
                dismissAddCustomUnit = navController::dismissAddCustomUnits,
            )
        }
    }
}

fun NavController.navigateToAddCustomUnits() {
    navigate(NoArgumentRouter.AddCustomUnitsRouter.route)
}
fun NavController.navigateToCustomUnitArchive() {
    navigate(NoArgumentRouter.CustomUnitArchiveRouter.route)
}

fun NavController.dismissAddCustomUnits() {
    popBackStack(route = NoArgumentRouter.AddCustomUnitsRouter.route, inclusive = true)
}