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
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.main.navigation.composableWithTransitions
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.dose.ChooseDoseScreen
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.dose.customsubstance.CustomChooseDose
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.dose.customunit.ChooseDoseCustomUnitScreen
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.interactions.CheckInteractionsScreen
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.route.ChooseRouteScreen
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.route.CustomChooseRouteScreen
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.saferuse.CheckSaferUseScreen
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.AddIngestionSearchScreen
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.time.ChooseTimeScreen
import com.isaakhanimann.journal.ui.tabs.safer.RouteExplanationScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.addIngestionGraph(navController: NavController) {
    navigation<AddIngestionRoute>(
        startDestination = AddIngestionSearchRoute,
    ) {
        composableWithTransitions<AddIngestionSearchRoute> {
            AddIngestionSearchScreen(
                navigateToCheckInteractions = { substanceName ->
                    navController.navigate(CheckInteractionsRoute(substanceName))
                },
                navigateToCheckSaferUse = { substanceName ->
                    navController.navigate(CheckSaferUseRoute(substanceName))
                },
                navigateToCustomSubstanceChooseRoute = { customSubstanceId ->
                    navController.navigate(CustomChooseRouteRoute(customSubstanceId = customSubstanceId))
                },
                navigateToChooseTime = { substanceName, administrationRoute, dose, units, isEstimate, estimatedDoseStandardDeviation, customUnitId ->
                    navController.navigate(
                        ChooseTimeRoute(
                            administrationRoute = administrationRoute.name,
                            units = units,
                            isEstimate = isEstimate,
                            dose = dose,
                            estimatedDoseStandardDeviation = estimatedDoseStandardDeviation,
                            substanceName = substanceName,
                            customUnitId = customUnitId,
                            customSubstanceId = null
                        )
                    )
                },
                navigateToCustomDose = { customSubstanceId, administrationRoute ->
                    navController.navigate(
                        CustomChooseDoseRoute(
                            customSubstanceId = customSubstanceId,
                            administrationRoute = administrationRoute.name

                        )
                    )
                },
                navigateToDose = { substanceName, route ->
                    navController.navigate(
                        ChooseDoseRoute(
                            substanceName = substanceName,
                            administrationRoute = route.name
                        )
                    )
                },
                navigateToChooseRoute = { substanceName ->
                    navController.navigate(ChooseRouteOfAddIngestionRoute(substanceName = substanceName))
                },
                navigateToAddCustomSubstanceScreen = {
                    navController.navigate(AddCustomSubstanceRouteOnAddIngestionGraph)
                },
                navigateToCustomUnitChooseDose = { customUnitId ->
                    navController.navigate(ChooseDoseCustomUnitRoute(customUnitId = customUnitId))
                }
            )
        }
        composableWithTransitions<CheckInteractionsRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<CheckInteractionsRoute>()
            CheckInteractionsScreen(
                navigateToNext = {
                    navController.navigate(ChooseRouteOfAddIngestionRoute(substanceName = route.substanceName))
                },
            )
        }
        composableWithTransitions<CheckSaferUseRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<CheckSaferUseRoute>()
            CheckSaferUseScreen(
                navigateToNext = {
                    navController.navigate(CheckInteractionsRoute(substanceName = route.substanceName))
                },
            )
        }
        composableWithTransitions<ChooseDoseCustomUnitRoute> {
            ChooseDoseCustomUnitScreen(
                navigateToChooseTimeAndMaybeColor = { administrationRoute: AdministrationRoute,
                                                      units: String?,
                                                      isEstimate: Boolean,
                                                      dose: Double?,
                                                      estimatedDoseStandardDeviation: Double?,
                                                      substanceName: String,
                                                      customUnitId: Int? ->
                    navController.navigate(
                        ChooseTimeRoute(
                            administrationRoute = administrationRoute.name,
                            isEstimate = isEstimate,
                            units = units,
                            dose = dose,
                            estimatedDoseStandardDeviation = estimatedDoseStandardDeviation,
                            substanceName = substanceName,
                            customUnitId = customUnitId,
                            customSubstanceId = null
                        )
                    )
                })
        }
        composableWithTransitions<ChooseRouteOfAddIngestionRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ChooseRouteOfAddIngestionRoute>()
            ChooseRouteScreen(
                navigateToChooseDose = { administrationRoute ->
                    navController.navigate(
                        ChooseDoseRoute(
                            substanceName = route.substanceName,
                            administrationRoute = administrationRoute.name
                        )
                    )
                },
                navigateToRouteExplanationScreen = {
                    navController.navigate(AdministrationRouteExplanationRouteOnJournalTab)
                }
            )
        }
        composableWithTransitions<CustomChooseRouteRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<CustomChooseRouteRoute>()
            CustomChooseRouteScreen(
                onRouteTap = { administrationRoute ->
                    navController.navigate(
                        CustomChooseDoseRoute(
                            customSubstanceId = route.customSubstanceId,
                            administrationRoute = administrationRoute.name
                        )
                    )
                }
            )
        }
        composableWithTransitions<CustomChooseDoseRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<CustomChooseDoseRoute>()
            CustomChooseDose(
                navigateToChooseTimeAndMaybeColor = { units, isEstimate, dose, estimatedDoseStandardDeviation ->
                    navController.navigate(
                        ChooseTimeRoute(
                            administrationRoute = route.administrationRoute,
                            units = units,
                            isEstimate = isEstimate,
                            dose = dose,
                            estimatedDoseStandardDeviation = estimatedDoseStandardDeviation,
                            substanceName = null,
                            customSubstanceId = route.customSubstanceId,
                            customUnitId = null
                        )
                    )
                },
                navigateToSaferSniffingScreen = {
                    navController.navigate(SaferSniffingRouteOnJournalTab)
                },
            )
        }
        composableWithTransitions<ChooseDoseRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ChooseDoseRoute>()
            ChooseDoseScreen(
                navigateToChooseTimeAndMaybeColor = { units, isEstimate, dose, estimatedDoseStandardDeviation ->
                    navController.navigate(
                        ChooseTimeRoute(
                            administrationRoute = route.administrationRoute,
                            units = units,
                            isEstimate = isEstimate,
                            dose = dose,
                            estimatedDoseStandardDeviation = estimatedDoseStandardDeviation,
                            substanceName = route.substanceName,
                            customUnitId = null,
                            customSubstanceId = null
                        )
                    )
                },
                navigateToVolumetricDosingScreenOnJournalTab = {
                    navController.navigate(VolumetricDosingOnJournalTabRoute)
                },
                navigateToSaferSniffingScreen = {
                    navController.navigate(SaferSniffingRouteOnJournalTab)
                },
            )
        }
        composableWithTransitions<ChooseTimeRoute> {
            ChooseTimeScreen(
                dismissAddIngestionScreens = {
                    navController.popBackStack(route = AddIngestionRoute, inclusive = true)
                },
            )
        }
        composableWithTransitions<AdministrationRouteExplanationRouteOnJournalTab> {
            RouteExplanationScreen()
        }
    }
}

@Serializable
object AddIngestionRoute

@Serializable
object AddIngestionSearchRoute

@Serializable
data class CheckInteractionsRoute(val substanceName: String)

@Serializable
data class CheckSaferUseRoute(val substanceName: String)

@Serializable
data class ChooseDoseCustomUnitRoute(val customUnitId: Int)

@Serializable
data class ChooseRouteOfAddIngestionRoute(val substanceName: String)

@Serializable
data class CustomChooseRouteRoute(val customSubstanceId: Int)

@Serializable
data class CustomChooseDoseRoute(val customSubstanceId: Int, val administrationRoute: String)

@Serializable
data class ChooseDoseRoute(val substanceName: String, val administrationRoute: String)

@Serializable
data class ChooseTimeRoute(
    val administrationRoute: String,
    val isEstimate: Boolean,
    val units: String?,
    val dose: Double?,
    val estimatedDoseStandardDeviation: Double?,
    val substanceName: String?,
    val customUnitId: Int?,
    val customSubstanceId: Int?
)

@Serializable
object AdministrationRouteExplanationRouteOnJournalTab

@Serializable
object AddCustomSubstanceRouteOnAddIngestionGraph
