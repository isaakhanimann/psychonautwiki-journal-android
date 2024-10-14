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
import com.isaakhanimann.journal.ui.main.navigation.routers.NoArgumentRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.TabRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToAdministrationRouteExplanationScreen
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToDosageExplanationScreenOnSaferTab
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToDosageGuideScreen
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToDrugTestingScreen
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToReagentTesting
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToSaferHallucinogens
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToVolumetricDosingScreenOnSaferTab
import com.isaakhanimann.journal.ui.tabs.safer.DoseExplanationScreen
import com.isaakhanimann.journal.ui.tabs.safer.DoseGuideScreen
import com.isaakhanimann.journal.ui.tabs.safer.DrugTestingScreen
import com.isaakhanimann.journal.ui.tabs.safer.ReagentTestingScreen
import com.isaakhanimann.journal.ui.tabs.safer.RouteExplanationScreen
import com.isaakhanimann.journal.ui.tabs.safer.SaferHallucinogensScreen
import com.isaakhanimann.journal.ui.tabs.safer.SaferUseScreen
import com.isaakhanimann.journal.ui.tabs.safer.VolumetricDosingScreen
import com.isaakhanimann.journal.ui.tabs.search.substance.SaferStimulantsScreen


fun NavGraphBuilder.saferGraph(navController: NavController) {
    navigation(
        startDestination = NoArgumentRouter.SaferRouter.route,
        route = TabRouter.SaferUse.route,
    ) {
        composableWithTransitions(
            route = NoArgumentRouter.SaferRouter.route,
        ) {
            SaferUseScreen(
                navigateToDrugTestingScreen = navController::navigateToDrugTestingScreen,
                navigateToSaferHallucinogensScreen = navController::navigateToSaferHallucinogens,
                navigateToVolumetricDosingScreen = navController::navigateToVolumetricDosingScreenOnSaferTab,
                navigateToDosageGuideScreen = navController::navigateToDosageGuideScreen,
                navigateToDosageClassificationScreen = navController::navigateToDosageExplanationScreenOnSaferTab,
                navigateToRouteExplanationScreen = navController::navigateToAdministrationRouteExplanationScreen,
                navigateToReagentTestingScreen = navController::navigateToReagentTesting,
            )
        }
        composableWithTransitions(NoArgumentRouter.SaferHallucinogens.route) { SaferHallucinogensScreen() }
        composableWithTransitions(NoArgumentRouter.SaferStimulants.route) { SaferStimulantsScreen() }
        composableWithTransitions(NoArgumentRouter.DosageExplanationRouterOnSaferTab.route) { DoseExplanationScreen() }
        composableWithTransitions(NoArgumentRouter.AdministrationRouteExplanationRouter.route) {
            RouteExplanationScreen()
        }
        composableWithTransitions(NoArgumentRouter.DrugTestingRouter.route) { DrugTestingScreen() }
        composableWithTransitions(NoArgumentRouter.DosageGuideRouter.route) {
            DoseGuideScreen(
                navigateToDoseClassification = navController::navigateToDosageExplanationScreenOnSaferTab,
                navigateToVolumetricDosing = navController::navigateToVolumetricDosingScreenOnSaferTab,
            )
        }
        composableWithTransitions(NoArgumentRouter.VolumetricDosingOnSaferTabRouter.route) {
            VolumetricDosingScreen()
        }
        composableWithTransitions(NoArgumentRouter.ReagentTestingRouter.route) {
            ReagentTestingScreen()
        }
    }
}