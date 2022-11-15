/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.main.navigation.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.isaakhanimann.journal.ui.main.navigation.routers.*
import com.isaakhanimann.journal.ui.main.navigation.transitions.regularComposableWithTransitions
import com.isaakhanimann.journal.ui.main.navigation.transitions.tabNavigationWithTransitions
import com.isaakhanimann.journal.ui.safer.*
import com.isaakhanimann.journal.ui.search.substance.SaferSniffingScreen
import com.isaakhanimann.journal.ui.search.substance.SaferStimulantsScreen
import com.isaakhanimann.journal.ui.settings.FAQScreen
import com.isaakhanimann.journal.ui.settings.SettingsScreen
import com.isaakhanimann.journal.ui.settings.combinations.CombinationSettingsScreen


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.saferGraph(navController: NavController) {
    tabNavigationWithTransitions(
        startDestination = NoArgumentRouter.SaferRouter.route,
        route = TabRouter.SaferUse.route, // todo add animation
    ) {
        composable(
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
        regularComposableWithTransitions(NoArgumentRouter.FAQRouter.route) { FAQScreen() }
        regularComposableWithTransitions(NoArgumentRouter.CombinationSettingsRouter.route) { CombinationSettingsScreen() }
        regularComposableWithTransitions(NoArgumentRouter.SaferHallucinogens.route) { SaferHallucinogensScreen() }
        regularComposableWithTransitions(NoArgumentRouter.SaferStimulants.route) { SaferStimulantsScreen() }
        regularComposableWithTransitions(NoArgumentRouter.SaferSniffing.route) { SaferSniffingScreen() }
        regularComposableWithTransitions(NoArgumentRouter.SettingsRouter.route) {
            SettingsScreen(
                navigateToFAQ = navController::navigateToFAQ,
                navigateToComboSettings = navController::navigateToComboSettings
            )
        }
        regularComposableWithTransitions(NoArgumentRouter.DosageExplanationRouter.route) { DoseExplanationScreen() }
        regularComposableWithTransitions(NoArgumentRouter.AdministrationRouteExplanationRouter.route) {
            RouteExplanationScreen(
                navigateToURL = navController::navigateToURLScreen
            )
        }
        regularComposableWithTransitions(NoArgumentRouter.DrugTestingRouter.route) { DrugTestingScreen() }
        regularComposableWithTransitions(NoArgumentRouter.DosageGuideRouter.route) {
            DoseGuideScreen(
                navigateToDoseClassification = navController::navigateToDosageExplanationScreen,
                navigateToVolumetricDosing = navController::navigateToVolumetricDosingScreen,
                navigateToPWDosageArticle = {
                    navController.navigateToURLScreen(url = "https://psychonautwiki.org/wiki/Dosage")
                }
            )
        }
        regularComposableWithTransitions(NoArgumentRouter.VolumetricDosingRouter.route) {
            VolumetricDosingScreen(
                navigateToVolumetricLiquidDosingArticle = { navController.navigateToURLScreen("https://psychonautwiki.org/wiki/Volumetric_liquid_dosing") })
        }
    }
}