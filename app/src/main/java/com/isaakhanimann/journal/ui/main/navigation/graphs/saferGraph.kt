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
import com.isaakhanimann.journal.ui.VOLUMETRIC_DOSE_ARTICLE_URL
import com.isaakhanimann.journal.ui.main.navigation.composableWithTransitions
import com.isaakhanimann.journal.ui.main.navigation.routers.*
import com.isaakhanimann.journal.ui.tabs.safer.*
import com.isaakhanimann.journal.ui.tabs.search.substance.SaferStimulantsScreen
import com.isaakhanimann.journal.ui.tabs.search.substance.UrlScreen


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
                navigateToURL = navController::navigateToURLInSaferTab,
                navigateToReagentTestingScreen = navController::navigateToReagentTesting,
            )
        }
        composableWithTransitions(NoArgumentRouter.SaferHallucinogens.route) { SaferHallucinogensScreen() }
        composableWithTransitions(NoArgumentRouter.SaferStimulants.route) { SaferStimulantsScreen() }
        composableWithTransitions(NoArgumentRouter.DosageExplanationRouterOnSaferTab.route) { DoseExplanationScreen() }
        composableWithTransitions(NoArgumentRouter.AdministrationRouteExplanationRouter.route) {
            RouteExplanationScreen(
                navigateToURL = navController::navigateToURLInSaferTab
            )
        }
        composableWithTransitions(
            ArgumentRouter.URLRouterOnSaferTab.route,
            arguments = ArgumentRouter.URLRouterOnSaferTab.args
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val url = args.getString(URL_KEY)!!
            UrlScreen(url = url)
        }
        composableWithTransitions(NoArgumentRouter.DrugTestingRouter.route) { DrugTestingScreen() }
        composableWithTransitions(NoArgumentRouter.DosageGuideRouter.route) {
            DoseGuideScreen(
                navigateToDoseClassification = navController::navigateToDosageExplanationScreenOnSaferTab,
                navigateToVolumetricDosing = navController::navigateToVolumetricDosingScreenOnSaferTab,
                navigateToPWDosageArticle = {
                    navController.navigateToURLInSaferTab(url = "https://psychonautwiki.org/wiki/Dosage")
                }
            )
        }
        composableWithTransitions(NoArgumentRouter.VolumetricDosingOnSaferTabRouter.route) {
            VolumetricDosingScreen(
                navigateToVolumetricLiquidDosingArticle = {
                    navController.navigateToURLInSaferTab(
                        VOLUMETRIC_DOSE_ARTICLE_URL
                    )
                })
        }
        composableWithTransitions(NoArgumentRouter.ReagentTestingRouter.route) {
            ReagentTestingScreen(
                navigateToReagentTestingArticle = {
                    navController.navigateToURLInSaferTab(
                        "https://psychonautwiki.org/wiki/Reagent_testing_kits"
                    )
                }
            )
        }
    }
}