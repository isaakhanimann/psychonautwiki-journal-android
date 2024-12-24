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
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.dose.customsubstance.CustomSubstanceChooseDoseScreen
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.dose.customunit.ChooseDoseCustomUnitScreen
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.interactions.CheckInteractionsScreen
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.route.ChooseRouteScreen
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.route.CustomSubstanceChooseRouteScreen
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.saferuse.CheckSaferUseScreen
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.AddIngestionSearchScreen
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.time.FinishIngestionScreen
import com.isaakhanimann.journal.ui.tabs.safer.RouteExplanationScreen
import com.isaakhanimann.journal.ui.tabs.search.custom.AddCustomSubstanceAndContinueScreen
import com.isaakhanimann.journal.ui.tabs.settings.customunits.add.FinishAddCustomUnitScreen
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
                    navController.navigate(CustomSubstanceChooseRouteRoute(customSubstanceId))
                },
                navigateToChooseTime = { substanceName, administrationRoute, dose, units, isEstimate, estimatedDoseStandardDeviation, customUnitId ->
                    navController.navigate(
                        FinishIngestionRoute(
                            administrationRoute = administrationRoute,
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
                navigateToChooseCustomSubstanceDose = { customSubstanceId, administrationRoute ->
                    navController.navigate(
                        ChooseCustomSubstanceDoseRoute(
                            customSubstanceId = customSubstanceId,
                            administrationRoute = administrationRoute

                        )
                    )
                },
                navigateToDose = { substanceName, administrationRoute ->
                    navController.navigate(
                        ChooseDoseRoute(
                            substanceName = substanceName,
                            administrationRoute = administrationRoute
                        )
                    )
                },
                navigateToCreateCustomUnit = { administrationRoute, substanceName, customSubstanceId ->
                    navController.navigate(
                        FinishAddCustomUnitRoute(
                            administrationRoute = administrationRoute,
                            substanceName = substanceName,
                            customSubstanceId = customSubstanceId
                        )
                    )
                },
                navigateToChooseRoute = { substanceName ->
                    navController.navigate(ChooseRouteOfAddIngestionRoute(substanceName = substanceName))
                },
                navigateToAddCustomSubstanceScreen = { searchText ->
                    navController.navigate(AddCustomSubstanceRouteOnAddIngestionGraph(searchText = searchText))
                },
                navigateToCustomUnitChooseDose = { customUnitId ->
                    navController.navigate(ChooseDoseCustomUnitRoute(customUnitId = customUnitId))
                }
            )
        }
        composableWithTransitions<AddCustomSubstanceRouteOnAddIngestionGraph> { backStackEntry ->
            val route = backStackEntry.toRoute<AddCustomSubstanceRouteOnAddIngestionGraph>()
            AddCustomSubstanceAndContinueScreen(
                navigateToChooseRoa = { customSubstanceId ->
                    navController.navigate(CustomSubstanceChooseRouteRoute(customSubstanceId)) {
                        popUpTo(AddIngestionSearchRoute)
                    }
                },
                initialName = route.searchText
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
                        FinishIngestionRoute(
                            administrationRoute = administrationRoute,
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
                            administrationRoute = administrationRoute
                        )
                    )
                },
                navigateToRouteExplanationScreen = {
                    navController.navigate(AdministrationRouteExplanationRouteOnJournalTab)
                }
            )
        }
        composableWithTransitions<CustomSubstanceChooseRouteRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<CustomSubstanceChooseRouteRoute>()
            CustomSubstanceChooseRouteScreen(
                onRouteTap = { administrationRoute ->
                    navController.navigate(
                        ChooseCustomSubstanceDoseRoute(
                            customSubstanceId = route.customSubstanceId,
                            administrationRoute = administrationRoute
                        )
                    )
                }
            )
        }
        composableWithTransitions<ChooseCustomSubstanceDoseRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ChooseCustomSubstanceDoseRoute>()
            CustomSubstanceChooseDoseScreen(
                navigateToChooseTimeAndMaybeColor = { units, isEstimate, dose, estimatedDoseStandardDeviation ->
                    navController.navigate(
                        FinishIngestionRoute(
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
                        FinishIngestionRoute(
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
                navigateToCreateCustomUnit = {
                    navController.navigate(
                        FinishAddCustomUnitRoute(
                            substanceName = route.substanceName,
                            administrationRoute = route.administrationRoute,
                            customSubstanceId = null
                        )
                    )
                }
            )
        }
        composableWithTransitions<FinishIngestionRoute> {
            FinishIngestionScreen(
                dismissAddIngestionScreens = {
                    navController.popBackStack(route = AddIngestionRoute, inclusive = true)
                },
            )
        }
        composableWithTransitions<AdministrationRouteExplanationRouteOnJournalTab> {
            RouteExplanationScreen()
        }
        composableWithTransitions<FinishAddCustomUnitRoute> {
            FinishAddCustomUnitScreen(
                dismissAddCustomUnit = { customUnitId ->
                    navController.navigate(ChooseDoseCustomUnitRoute(customUnitId = customUnitId)) {
                        popUpTo(AddIngestionSearchRoute)
                    }
                },
            )
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
data class CustomSubstanceChooseRouteRoute(val customSubstanceId: Int)

@Serializable
data class ChooseCustomSubstanceDoseRoute(
    val customSubstanceId: Int,
    val administrationRoute: AdministrationRoute,
)

@Serializable
data class ChooseDoseRoute(
    val substanceName: String,
    val administrationRoute: AdministrationRoute
)

@Serializable
data class FinishIngestionRoute(
    val administrationRoute: AdministrationRoute,
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
data class AddCustomSubstanceRouteOnAddIngestionGraph(val searchText: String)