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

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.navigation
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.main.navigation.composableWithTransitions
import com.isaakhanimann.journal.ui.main.navigation.routers.*
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.dose.ChooseDoseScreen
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.dose.custom.CustomChooseDose
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.interactions.CheckInteractionsScreen
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.route.ChooseRouteScreen
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.route.CustomChooseRouteScreen
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.AddIngestionSearchScreen
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.time.ChooseTimeScreen
import com.isaakhanimann.journal.ui.tabs.search.substance.UrlScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addIngestionGraph(navController: NavController) {
    navigation(
        startDestination = NoArgumentRouter.AddIngestionSearchRouter.route,
        route = NoArgumentRouter.AddIngestionRouter.route,
    ) {
        composableWithTransitions(NoArgumentRouter.AddIngestionSearchRouter.route) {
            AddIngestionSearchScreen(
                navigateToCheckInteractionsSkipNothing = {
                    navController.navigateToCheckInteractionsSkipNothing(substanceName = it)
                },
                navigateToCheckInteractionsSkipRoute = { substanceName, route ->
                    navController.navigateToCheckInteractionsSkipRoute(
                        substanceName = substanceName,
                        administrationRoute = route
                    )
                },
                navigateToCheckInteractionsSkipDose = { substanceName, route, dose, units, isEstimate ->
                    navController.navigateToCheckInteractionsSkipDose(
                        substanceName = substanceName,
                        administrationRoute = route,
                        units = units,
                        isEstimate = isEstimate,
                        dose = dose
                    )
                },
                navigateToCustomSubstanceChooseRoute = navController::navigateToChooseCustomRoute,
                navigateToChooseTime = { substanceName, route, dose, units, isEstimate ->
                    navController.navigateToChooseTimeAndMaybeColor(
                        substanceName = substanceName,
                        administrationRoute = route,
                        units = units,
                        isEstimate = isEstimate,
                        dose = dose
                    )
                },
                navigateToCustomDose = { substanceName, route ->
                    navController.navigateToChooseDoseCustom(substanceName, route)
                },
                navigateToDose = { substanceName, route ->
                    navController.navigateToChooseDose(substanceName, route)
                },
                navigateToChooseRoute = { substanceName ->
                    navController.navigateToChooseRoute(substanceName)
                },
                navigateToAddCustomSubstanceScreen = navController::navigateToAddCustom
            )
        }
        composableWithTransitions(
            ArgumentRouter.CheckInteractionsRouterSkipNothing.route,
            arguments = ArgumentRouter.CheckInteractionsRouterSkipNothing.args
        ) { backStackEntry ->
            CheckInteractionsScreen(
                navigateToNext = {
                    val args = backStackEntry.arguments!!
                    val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
                    navController.navigateToChooseRoute(substanceName = substanceName)
                },
                navigateToURL = navController::navigateToURLInJournalTab
            )
        }
        composableWithTransitions(
            ArgumentRouter.CheckInteractionsRouterSkipRoute.route,
            arguments = ArgumentRouter.CheckInteractionsRouterSkipRoute.args
        ) { backStackEntry ->
            CheckInteractionsScreen(
                navigateToNext = {
                    val args = backStackEntry.arguments!!
                    val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
                    val route =
                        AdministrationRoute.valueOf(args.getString(ADMINISTRATION_ROUTE_KEY)!!)
                    navController.navigateToChooseDose(substanceName, route)
                },
                navigateToURL = navController::navigateToURLInJournalTab
            )
        }
        composableWithTransitions(
            ArgumentRouter.JournalTabURLRouter.route,
            arguments = ArgumentRouter.JournalTabURLRouter.args
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val url = args.getString(URL_KEY)!!
            UrlScreen(url = url)
        }
        composableWithTransitions(
            ArgumentRouter.CheckInteractionsRouterSkipDose.route,
            arguments = ArgumentRouter.CheckInteractionsRouterSkipDose.args
        ) { backStackEntry ->
            CheckInteractionsScreen(
                navigateToNext = {
                    val args = backStackEntry.arguments!!
                    val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
                    val route =
                        AdministrationRoute.valueOf(args.getString(ADMINISTRATION_ROUTE_KEY)!!)
                    val dose = args.getString(DOSE_KEY)?.toDoubleOrNull()
                    val units = args.getString(UNITS_KEY)?.let {
                        if (it == "null") {
                            null
                        } else {
                            it
                        }
                    }
                    val isEstimate = args.getBoolean(IS_ESTIMATE_KEY)
                    navController.navigateToChooseTimeAndMaybeColor(
                        substanceName,
                        route,
                        units,
                        isEstimate = isEstimate,
                        dose = dose
                    )
                },
                navigateToURL = navController::navigateToURLInJournalTab
            )
        }
        composableWithTransitions(
            ArgumentRouter.ChooseRouteRouter.route,
            arguments = ArgumentRouter.ChooseRouteRouter.args
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
            ChooseRouteScreen(
                navigateToChooseDose = { administrationRoute ->
                    navController.navigateToChooseDose(
                        substanceName = substanceName,
                        administrationRoute = administrationRoute,
                    )
                },
                navigateToRouteExplanationScreen = navController::navigateToAdministrationRouteExplanationScreen,
                navigateToURL = navController::navigateToURLInJournalTab
            )
        }
        composableWithTransitions(
            ArgumentRouter.CustomChooseRouteRouter.route,
            arguments = ArgumentRouter.CustomChooseRouteRouter.args
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
            CustomChooseRouteScreen(
                onRouteTap = { administrationRoute ->
                    navController.navigateToChooseDoseCustom(
                        substanceName = substanceName,
                        administrationRoute = administrationRoute,
                    )
                },
                substanceName = substanceName
            )
        }
        composableWithTransitions(
            ArgumentRouter.CustomChooseDoseRouter.route,
            arguments = ArgumentRouter.CustomChooseDoseRouter.args
        ) { backStackEntry ->
            CustomChooseDose(
                navigateToChooseTimeAndMaybeColor = { units, isEstimate, dose ->
                    val args = backStackEntry.arguments!!
                    val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
                    val route =
                        AdministrationRoute.valueOf(args.getString(ADMINISTRATION_ROUTE_KEY)!!)
                    navController.navigateToChooseTimeAndMaybeColor(
                        substanceName = substanceName,
                        administrationRoute = route,
                        units = units,
                        isEstimate = isEstimate,
                        dose = dose,
                    )
                },
                navigateToSaferSniffingScreen = navController::navigateToSaferSniffingOnJournalTab,
                navigateToURL = navController::navigateToURLInJournalTab
            )
        }
        composableWithTransitions(
            ArgumentRouter.ChooseDoseRouter.route,
            arguments = ArgumentRouter.ChooseDoseRouter.args
        ) { backStackEntry ->
            ChooseDoseScreen(
                navigateToChooseTimeAndMaybeColor = { units, isEstimate, dose ->
                    val args = backStackEntry.arguments!!
                    val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
                    val route =
                        AdministrationRoute.valueOf(args.getString(ADMINISTRATION_ROUTE_KEY)!!)
                    navController.navigateToChooseTimeAndMaybeColor(
                        substanceName = substanceName,
                        administrationRoute = route,
                        units = units,
                        isEstimate = isEstimate,
                        dose = dose,
                    )
                },
                navigateToVolumetricDosingScreenOnJournalTab = navController::navigateToVolumetricDosingScreenOnJournalTab,
                navigateToSaferSniffingScreen = navController::navigateToSaferSniffingOnJournalTab,
                navigateToURL = navController::navigateToURLInJournalTab
            )
        }
        composableWithTransitions(
            ArgumentRouter.ChooseTimeRouter.route,
            arguments = ArgumentRouter.ChooseTimeRouter.args
        ) {
            ChooseTimeScreen(
                dismissAddIngestionScreens = navController::dismissAddIngestionScreens,
            )
        }
    }
}

fun NavController.navigateToAddIngestion() {
    navigate(NoArgumentRouter.AddIngestionRouter.route)
}

fun NavController.dismissAddIngestionScreens() {
    popBackStack(route = NoArgumentRouter.AddIngestionRouter.route, inclusive = true)
}
