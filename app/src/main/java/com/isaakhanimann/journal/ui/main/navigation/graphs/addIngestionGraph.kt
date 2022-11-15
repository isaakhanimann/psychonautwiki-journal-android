/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.main.navigation.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.navigation
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.addingestion.dose.ChooseDoseScreen
import com.isaakhanimann.journal.ui.addingestion.dose.custom.CustomChooseDose
import com.isaakhanimann.journal.ui.addingestion.interactions.CheckInteractionsScreen
import com.isaakhanimann.journal.ui.addingestion.route.ChooseRouteScreen
import com.isaakhanimann.journal.ui.addingestion.route.CustomChooseRouteScreen
import com.isaakhanimann.journal.ui.addingestion.search.AddIngestionSearchScreen
import com.isaakhanimann.journal.ui.addingestion.time.ChooseTimeScreen
import com.isaakhanimann.journal.ui.main.navigation.routers.*
import com.isaakhanimann.journal.ui.main.navigation.transitions.regularComposableWithTransitions

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addIngestionGraph(navController: NavController) {
    navigation(
        startDestination = NoArgumentRouter.AddIngestionSearchRouter.route,
        route = NoArgumentRouter.AddIngestionRouter.route, // todo add animation
    ) {
        regularComposableWithTransitions(NoArgumentRouter.AddIngestionSearchRouter.route) {
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
        regularComposableWithTransitions(
            ArgumentRouter.CheckInteractionsRouterSkipNothing.route,
            arguments = ArgumentRouter.CheckInteractionsRouterSkipNothing.args
        ) { backStackEntry ->
            CheckInteractionsScreen(
                navigateToNext = {
                    val args = backStackEntry.arguments!!
                    val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
                    navController.navigateToChooseRoute(substanceName = substanceName)
                },
                navigateToURL = navController::navigateToURLScreen
            )
        }
        regularComposableWithTransitions(
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
                navigateToURL = navController::navigateToURLScreen
            )
        }
        regularComposableWithTransitions(
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
                navigateToURL = navController::navigateToURLScreen
            )
        }
        regularComposableWithTransitions(
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
                navigateToURL = navController::navigateToURLScreen
            )
        }
        regularComposableWithTransitions(
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
        regularComposableWithTransitions(
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
                navigateToSaferSniffingScreen = navController::navigateToSaferSniffing,
                navigateToURL = navController::navigateToURLScreen
            )
        }
        regularComposableWithTransitions(
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
                navigateToVolumetricDosingScreen = navController::navigateToVolumetricDosingScreen,
                navigateToSaferSniffingScreen = navController::navigateToSaferSniffing,
                navigateToURL = navController::navigateToURLScreen
            )
        }
        regularComposableWithTransitions(
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
