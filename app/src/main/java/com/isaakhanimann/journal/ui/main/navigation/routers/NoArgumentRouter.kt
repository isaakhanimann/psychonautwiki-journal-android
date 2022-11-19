/*
 * Copyright (c) 2022. Isaak Hanimann.
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
    object DosageExplanationRouterOnSaferTab :
        NoArgumentRouter(route = "dosageExplanationOnSaferTab")

    object DosageExplanationRouterOnSearchTab :
        NoArgumentRouter(route = "dosageExplanationOnSearchTab")

    object AdministrationRouteExplanationRouter :
        NoArgumentRouter(route = "administrationRouteExplanation")

    object DosageGuideRouter : NoArgumentRouter(route = "dosageGuide")
    object VolumetricDosingOnSaferTabRouter : NoArgumentRouter(route = "volumetricDosingOnSaferTab")
    object VolumetricDosingOnSearchTabRouter :
        NoArgumentRouter(route = "volumetricDosingOnSearchTab")

    object DrugTestingRouter : NoArgumentRouter(route = "drugTesting")
    object SaferHallucinogens : NoArgumentRouter(route = "saferHallucinogens")
    object SaferSniffingOnJournalTab : NoArgumentRouter(route = "saferSniffing")
    object SaferStimulants : NoArgumentRouter(route = "saferStimulants")
    object ReagentTestingRouter : NoArgumentRouter(route = "reagentTesting")
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

fun NavController.navigateToReagentTesting() {
    navigate(NoArgumentRouter.ReagentTestingRouter.route)
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

fun NavController.navigateToSaferSniffingOnJournalTab() {
    navigate(NoArgumentRouter.SaferSniffingOnJournalTab.route)
}

fun NavController.navigateToFAQ() {
    navigate(NoArgumentRouter.FAQRouter.route)
}

fun NavController.navigateToSettings() {
    navigate(NoArgumentRouter.SettingsRouter.route)
}

fun NavController.navigateToDosageExplanationScreenOnSaferTab() {
    navigate(NoArgumentRouter.DosageExplanationRouterOnSaferTab.route)
}

fun NavController.navigateToDosageExplanationScreenOnSearchTab() {
    navigate(NoArgumentRouter.DosageExplanationRouterOnSearchTab.route)
}

fun NavController.navigateToAdministrationRouteExplanationScreen() {
    navigate(NoArgumentRouter.AdministrationRouteExplanationRouter.route)
}

fun NavController.navigateToDosageGuideScreen() {
    navigate(NoArgumentRouter.DosageGuideRouter.route)
}

fun NavController.navigateToVolumetricDosingScreenOnSaferTab() {
    navigate(NoArgumentRouter.VolumetricDosingOnSaferTabRouter.route)
}

fun NavController.navigateToVolumetricDosingScreenOnSearchTab() {
    navigate(NoArgumentRouter.VolumetricDosingOnSearchTabRouter.route)
}

fun NavController.navigateToDrugTestingScreen() {
    navigate(NoArgumentRouter.DrugTestingRouter.route)
}