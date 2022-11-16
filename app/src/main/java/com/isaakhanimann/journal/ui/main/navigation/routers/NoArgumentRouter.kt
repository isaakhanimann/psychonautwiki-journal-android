/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.main.navigation.routers

import androidx.navigation.NavController


sealed class NoArgumentRouter(val route: String) {
    object StatsRouter : NoArgumentRouter(route = "stats")
    object CombinationSettingsRouter : NoArgumentRouter(route = "combinationSettings")
    object FAQRouter : NoArgumentRouter(route = "faqRoute")
    object ExplainTimelineOnSearchTabRouter : NoArgumentRouter(route = "explainTimelineOnSearchTab")
    object ExplainTimelineOnJournalTabRouter :
        NoArgumentRouter(route = "explainTimelineOnJournalTab")

    object SettingsRouter : NoArgumentRouter(route = "settings")
    object DosageExplanationRouter : NoArgumentRouter(route = "dosageExplanation")
    object AdministrationRouteExplanationRouter :
        NoArgumentRouter(route = "administrationRouteExplanation")

    object DosageGuideRouter : NoArgumentRouter(route = "dosageGuide")
    object VolumetricDosingRouter : NoArgumentRouter(route = "volumetricDosing")
    object DrugTestingRouter : NoArgumentRouter(route = "drugTesting")
    object SaferHallucinogens : NoArgumentRouter(route = "saferHallucinogens")
    object SaferSniffing : NoArgumentRouter(route = "saferSniffing")
    object SaferStimulants : NoArgumentRouter(route = "saferStimulants")
    object AddIngestionRouter : NoArgumentRouter(route = "addIngestions")
    object AddIngestionSearchRouter : NoArgumentRouter(route = "addIngestionsSearch")
    object AddCustomRouter : NoArgumentRouter(route = "addCustom")
    object SearchRouter :
        NoArgumentRouter(route = "searchChild") // child because there is already a route for the tabs

    object JournalRouter : NoArgumentRouter(route = "journalChild")
    object SaferRouter : NoArgumentRouter(route = "saferChild")
}

fun NavController.navigateToComboSettings() {
    navigate(NoArgumentRouter.CombinationSettingsRouter.route)
}

fun NavController.navigateToAddCustom() {
    navigate(NoArgumentRouter.AddCustomRouter.route)
}

fun NavController.navigateToExplainTimelineOnSearchTab() {
    navigate(NoArgumentRouter.ExplainTimelineOnSearchTabRouter.route)
}

fun NavController.navigateToExplainTimelineOnJournalTab() {
    navigate(NoArgumentRouter.ExplainTimelineOnJournalTabRouter.route)
}

fun NavController.navigateToSaferStimulants() {
    navigate(NoArgumentRouter.SaferStimulants.route)
}

fun NavController.navigateToSaferHallucinogens() {
    navigate(NoArgumentRouter.SaferHallucinogens.route)
}

fun NavController.navigateToSaferSniffing() {
    navigate(NoArgumentRouter.SaferSniffing.route)
}

fun NavController.navigateToFAQ() {
    navigate(NoArgumentRouter.FAQRouter.route)
}

fun NavController.navigateToSettings() {
    navigate(NoArgumentRouter.SettingsRouter.route)
}

fun NavController.navigateToDosageExplanationScreen() {
    navigate(NoArgumentRouter.DosageExplanationRouter.route)
}

fun NavController.navigateToAdministrationRouteExplanationScreen() {
    navigate(NoArgumentRouter.AdministrationRouteExplanationRouter.route)
}

fun NavController.navigateToDosageGuideScreen() {
    navigate(NoArgumentRouter.DosageGuideRouter.route)
}

fun NavController.navigateToVolumetricDosingScreen() {
    navigate(NoArgumentRouter.VolumetricDosingRouter.route)
}

fun NavController.navigateToDrugTestingScreen() {
    navigate(NoArgumentRouter.DrugTestingRouter.route)
}