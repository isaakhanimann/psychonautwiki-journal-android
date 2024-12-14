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
import androidx.navigation.toRoute
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.main.navigation.composableWithTransitions
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.route.CustomSubstanceChooseRouteScreen
import com.isaakhanimann.journal.ui.tabs.settings.customunits.add.ChooseRouteDuringAddCustomUnitScreen
import com.isaakhanimann.journal.ui.tabs.settings.customunits.add.ChooseSubstanceScreen
import com.isaakhanimann.journal.ui.tabs.settings.customunits.add.FinishAddCustomUnitScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.addCustomUnitGraph(navController: NavController) {
    navigation<AddCustomUnitsParentRoute>(
        startDestination = AddCustomUnitsChooseSubstanceScreenRoute,
    ) {
        composableWithTransitions<AddCustomUnitsChooseSubstanceScreenRoute> {
            ChooseSubstanceScreen(
                navigateToChooseRoute = { substanceName ->
                    navController.navigate(ChooseRouteOfAddCustomUnitRoute(substanceName))
                },
                navigateToCustomSubstanceChooseRoute = { customSubstanceName ->
                    navController.navigate(CustomSubstanceChooseRouteRoute(customSubstanceName))
                }
            )
        }
        composableWithTransitions<CustomSubstanceChooseRouteRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<CustomSubstanceChooseRouteRoute>()
            CustomSubstanceChooseRouteScreen(
                onRouteTap = { administrationRoute ->
                    navController.navigate(
                        FinishAddCustomUnitRoute(
                            administrationRoute = administrationRoute,
                            substanceName = null,
                            customSubstanceId = route.customSubstanceId,
                        )
                    )
                }
            )
        }
        composableWithTransitions<ChooseRouteOfAddCustomUnitRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ChooseRouteOfAddCustomUnitRoute>()
            ChooseRouteDuringAddCustomUnitScreen(
                onRouteChosen = { administrationRoute ->
                    navController.navigate(
                        FinishAddCustomUnitRoute(
                            administrationRoute = administrationRoute,
                            substanceName = route.substanceName,
                            customSubstanceId = null
                        )
                    )
                }
            )
        }
        composableWithTransitions<FinishAddCustomUnitRoute> {
            FinishAddCustomUnitScreen(
                dismissAddCustomUnit = {
                    navController.popBackStack(
                        route = AddCustomUnitsChooseSubstanceScreenRoute,
                        inclusive = true
                    )
                },
            )
        }
    }
}

@Serializable
object AddCustomUnitsParentRoute

@Serializable
object AddCustomUnitsChooseSubstanceScreenRoute

@Serializable
data class ChooseRouteOfAddCustomUnitRoute(val substanceName: String)

@Serializable
data class FinishAddCustomUnitRoute(
    val administrationRoute: AdministrationRoute,
    val substanceName: String?,
    val customSubstanceId: Int?
)
