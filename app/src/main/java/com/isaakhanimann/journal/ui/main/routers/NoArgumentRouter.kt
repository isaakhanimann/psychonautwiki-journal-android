/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.main.routers

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.isaakhanimann.journal.ui.addingestion.search.AddIngestionSearchScreen
import com.isaakhanimann.journal.ui.journal.experience.timeline.ExplainTimelineScreen
import com.isaakhanimann.journal.ui.main.routers.transitions.regularComposableWithTransitions
import com.isaakhanimann.journal.ui.safer.*
import com.isaakhanimann.journal.ui.search.custom.AddCustomSubstance
import com.isaakhanimann.journal.ui.search.substance.SaferSniffingScreen
import com.isaakhanimann.journal.ui.search.substance.SaferStimulantsScreen
import com.isaakhanimann.journal.ui.settings.FAQScreen
import com.isaakhanimann.journal.ui.settings.SettingsScreen
import com.isaakhanimann.journal.ui.settings.combinations.CombinationSettingsScreen
import com.isaakhanimann.journal.ui.stats.StatsScreen


const val FAQ_ROUTE = "faqRoute"
const val EXPLAIN_TIMELINE_ROUTE = "explainTimeline"
const val SETTINGS_ROUTE = "settings"
const val DOSAGE_EXPLANATION_ROUTE = "dosageExplanation"
const val ADMINISTRATION_ROUTE_EXPLANATION_ROUTE = "administrationRouteExplanation"
const val DOSAGE_GUIDE_ROUTE = "dosageGuide"
const val VOLUMETRIC_DOSING_ROUTE = "volumetricDosing"
const val DRUG_TESTING_ROUTE = "drugTesting"
const val SAFER_HALLUCINOGENS_ROUTE = "saferHallucinogens"
const val SAFER_SNIFFING_ROUTE = "saferSniffing"
const val SAFER_STIMULANTS_ROUTE = "saferStimulants"
const val STATS_ROUTE = "stats"
const val COMBO_SETTINGS_ROUTE = "combinationSettings"

sealed class NoArgumentRouter(val route: String) {
    object StatsRouter : NoArgumentRouter(route = STATS_ROUTE)
    object CombinationSettingsRouter : NoArgumentRouter(route = COMBO_SETTINGS_ROUTE)
    object FAQRouter : NoArgumentRouter(route = FAQ_ROUTE)
    object ExplainTimelineRouter : NoArgumentRouter(route = EXPLAIN_TIMELINE_ROUTE)
    object SettingsRouter : NoArgumentRouter(route = SETTINGS_ROUTE)
    object DosageExplanationRouter : NoArgumentRouter(route = DOSAGE_EXPLANATION_ROUTE)
    object AdministrationRouteExplanationRouter : NoArgumentRouter(route = ADMINISTRATION_ROUTE_EXPLANATION_ROUTE)
    object DosageGuideRouter : NoArgumentRouter(route = DOSAGE_GUIDE_ROUTE)
    object VolumetricDosingRouter : NoArgumentRouter(route = VOLUMETRIC_DOSING_ROUTE)
    object DrugTestingRouter : NoArgumentRouter(route = DRUG_TESTING_ROUTE)
    object SaferHallucinogens : NoArgumentRouter(route = SAFER_HALLUCINOGENS_ROUTE)
    object SaferSniffing : NoArgumentRouter(route = SAFER_SNIFFING_ROUTE)
    object SaferStimulants : NoArgumentRouter(route = SAFER_STIMULANTS_ROUTE)
    object AddIngestionRouter : NoArgumentRouter(route = ROUTE_START_ADD_INGESTIONS)
    object AddCustomRouter : NoArgumentRouter(route = ROUTE_START_ADD_CUSTOM)
}

fun NavController.navigateToAddIngestion() {
    navigate(ROUTE_START_ADD_INGESTIONS)
}

fun NavController.navigateToComboSettings() {
    navigate(COMBO_SETTINGS_ROUTE)
}

fun NavController.navigateToAddCustom() {
    navigate(ROUTE_START_ADD_CUSTOM)
}

fun NavController.navigateToExplainTimeline() {
    navigate(EXPLAIN_TIMELINE_ROUTE)
}

fun NavController.dismissAddIngestionScreens() {
    popBackStack(route = NoArgumentRouter.AddIngestionRouter.route, inclusive = true)
}

fun NavController.navigateToSaferStimulants() {
    navigate(SAFER_STIMULANTS_ROUTE)
}

fun NavController.navigateToSaferHallucinogens() {
    navigate(SAFER_HALLUCINOGENS_ROUTE)
}

fun NavController.navigateToSaferSniffing() {
    navigate(SAFER_SNIFFING_ROUTE)
}

fun NavController.navigateToFAQ() {
    navigate(FAQ_ROUTE)
}

fun NavController.navigateToSettings() {
    navigate(SETTINGS_ROUTE)
}

fun NavController.navigateToDosageExplanationScreen() {
    navigate(DOSAGE_EXPLANATION_ROUTE)
}

fun NavController.navigateToAdministrationRouteExplanationScreen() {
    navigate(ADMINISTRATION_ROUTE_EXPLANATION_ROUTE)
}

fun NavController.navigateToDosageGuideScreen() {
    navigate(DOSAGE_GUIDE_ROUTE)
}

fun NavController.navigateToVolumetricDosingScreen() {
    navigate(VOLUMETRIC_DOSING_ROUTE)
}

fun NavController.navigateToDrugTestingScreen() {
    navigate(DRUG_TESTING_ROUTE)
}

fun NavGraphBuilder.noArgumentGraph(navController: NavController) {
    regularComposableWithTransitions(NoArgumentRouter.StatsRouter.route) {
        StatsScreen(
            navigateToSubstanceCompanion = {
                navController.navigateToSubstanceCompanionScreen(it)
            }
        )
    }
    regularComposableWithTransitions(NoArgumentRouter.FAQRouter.route) { FAQScreen() }
    regularComposableWithTransitions(NoArgumentRouter.CombinationSettingsRouter.route) { CombinationSettingsScreen() }
    regularComposableWithTransitions(NoArgumentRouter.ExplainTimelineRouter.route) { ExplainTimelineScreen() }
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
    regularComposableWithTransitions(NoArgumentRouter.AddCustomRouter.route) {
        AddCustomSubstance(
            navigateBack = navController::popBackStack
        )
    }
    regularComposableWithTransitions(NoArgumentRouter.AddIngestionRouter.route) {
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
}