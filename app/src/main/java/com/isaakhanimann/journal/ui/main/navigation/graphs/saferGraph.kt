/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.main.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.isaakhanimann.journal.ui.main.navigation.composableWithTransitions
import com.isaakhanimann.journal.ui.main.navigation.routers.*
import com.isaakhanimann.journal.ui.safer.*
import com.isaakhanimann.journal.ui.search.substance.SaferSniffingScreen
import com.isaakhanimann.journal.ui.search.substance.SaferStimulantsScreen
import com.isaakhanimann.journal.ui.settings.FAQScreen
import com.isaakhanimann.journal.ui.settings.SettingsScreen
import com.isaakhanimann.journal.ui.settings.combinations.CombinationSettingsScreen


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
                navigateToVolumetricDosingScreen = navController::navigateToVolumetricDosingScreen,
                navigateToDosageGuideScreen = navController::navigateToDosageGuideScreen,
                navigateToDosageClassificationScreen = navController::navigateToDosageExplanationScreen,
                navigateToRouteExplanationScreen = navController::navigateToAdministrationRouteExplanationScreen,
                navigateToSettings = navController::navigateToSettings,
                navigateToURL = navController::navigateToURLScreen
            )
        }
        composableWithTransitions(NoArgumentRouter.FAQRouter.route) { FAQScreen() }
        composableWithTransitions(NoArgumentRouter.CombinationSettingsRouter.route) { CombinationSettingsScreen() }
        composableWithTransitions(NoArgumentRouter.SaferHallucinogens.route) { SaferHallucinogensScreen() }
        composableWithTransitions(NoArgumentRouter.SaferStimulants.route) { SaferStimulantsScreen() }
        composableWithTransitions(NoArgumentRouter.SaferSniffing.route) { SaferSniffingScreen() }
        composableWithTransitions(NoArgumentRouter.SettingsRouter.route) {
            SettingsScreen(
                navigateToFAQ = navController::navigateToFAQ,
                navigateToComboSettings = navController::navigateToComboSettings
            )
        }
        composableWithTransitions(NoArgumentRouter.DosageExplanationRouter.route) { DoseExplanationScreen() }
        composableWithTransitions(NoArgumentRouter.AdministrationRouteExplanationRouter.route) {
            RouteExplanationScreen(
                navigateToURL = navController::navigateToURLScreen
            )
        }
        composableWithTransitions(NoArgumentRouter.DrugTestingRouter.route) { DrugTestingScreen() }
        composableWithTransitions(NoArgumentRouter.DosageGuideRouter.route) {
            DoseGuideScreen(
                navigateToDoseClassification = navController::navigateToDosageExplanationScreen,
                navigateToVolumetricDosing = navController::navigateToVolumetricDosingScreen,
                navigateToPWDosageArticle = {
                    navController.navigateToURLScreen(url = "https://psychonautwiki.org/wiki/Dosage")
                }
            )
        }
        composableWithTransitions(NoArgumentRouter.VolumetricDosingRouter.route) {
            VolumetricDosingScreen(
                navigateToVolumetricLiquidDosingArticle = { navController.navigateToURLScreen("https://psychonautwiki.org/wiki/Volumetric_liquid_dosing") })
        }
    }
}