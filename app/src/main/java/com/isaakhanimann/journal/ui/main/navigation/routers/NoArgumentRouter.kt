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

package com.isaakhanimann.journal.ui.main.navigation.routers

import androidx.navigation.NavController


sealed class NoArgumentRouter(val route: String) {
    data object StatsRouter : NoArgumentRouter(route = "stats")
    data object CombinationSettingsRouter : NoArgumentRouter(route = "combinationSettings")
    data object SubstanceColorsRouter : NoArgumentRouter(route = "substanceColors")
    data object CustomUnitsRouter : NoArgumentRouter(route = "customUnits")
    data object AddCustomUnitsRouter : NoArgumentRouter(route = "addCustomUnits")
    data object CustomUnitArchiveRouter : NoArgumentRouter(route = "customUnitArchive")
    data object AddCustomUnitsSearchSubstanceRouter : NoArgumentRouter(route = "addCustomUnitsSearch")
    data object FAQRouter : NoArgumentRouter(route = "faqRoute")
    data object DonateRouter : NoArgumentRouter(route = "donate")
    data object ExplainTimelineOnSearchTabRouter : NoArgumentRouter(route = "explainTimelineOnSearchTab")
    data object ExplainTimelineOnJournalTabRouter :
        NoArgumentRouter(route = "explainTimelineOnJournalTab")

    data object SettingsRouter : NoArgumentRouter(route = "settings")
    data object DosageExplanationRouterOnSaferTab :
        NoArgumentRouter(route = "dosageExplanationOnSaferTab")

    data object DosageExplanationRouterOnSearchTab :
        NoArgumentRouter(route = "dosageExplanationOnSearchTab")

    data object AdministrationRouteExplanationRouter :
        NoArgumentRouter(route = "administrationRouteExplanation")

    data object DosageGuideRouter : NoArgumentRouter(route = "dosageGuide")
    data object VolumetricDosingOnSaferTabRouter : NoArgumentRouter(route = "volumetricDosingOnSaferTab")
    data object VolumetricDosingOnJournalTabRouter :
        NoArgumentRouter(route = "volumetricDosingOnJournalTab")

    data object VolumetricDosingOnSearchTabRouter :
        NoArgumentRouter(route = "volumetricDosingOnSearchTab")

    data object DrugTestingRouter : NoArgumentRouter(route = "drugTesting")
    data object SaferHallucinogens : NoArgumentRouter(route = "saferHallucinogens")
    data object SaferSniffingOnJournalTab : NoArgumentRouter(route = "saferSniffing")
    data object SaferStimulants : NoArgumentRouter(route = "saferStimulants")
    data object ReagentTestingRouter : NoArgumentRouter(route = "reagentTesting")
    data object AddIngestionRouter : NoArgumentRouter(route = "addIngestions")
    data object CalendarRouter : NoArgumentRouter(route = "calendar")
    data object AddIngestionSearchRouter : NoArgumentRouter(route = "addIngestionsSearch")
    data object AddCustomRouter : NoArgumentRouter(route = "addCustom")
    data object SubstancesRouter :
        NoArgumentRouter(route = "searchChild") // child because there is already a route for the tabs

    data object JournalRouter : NoArgumentRouter(route = "journalChild")
    data object SaferRouter : NoArgumentRouter(route = "saferChild")
}

fun NavController.navigateToComboSettings() {
    navigate(NoArgumentRouter.CombinationSettingsRouter.route)
}
fun NavController.navigateToSubstanceColors() {
    navigate(NoArgumentRouter.SubstanceColorsRouter.route)
}

fun NavController.navigateToCustomUnits() {
    navigate(NoArgumentRouter.CustomUnitsRouter.route)
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

fun NavController.navigateToDonate() {
    navigate(NoArgumentRouter.DonateRouter.route)
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

fun NavController.navigateToVolumetricDosingScreenOnJournalTab() {
    navigate(NoArgumentRouter.VolumetricDosingOnJournalTabRouter.route)
}

fun NavController.navigateToVolumetricDosingScreenOnSearchTab() {
    navigate(NoArgumentRouter.VolumetricDosingOnSearchTabRouter.route)
}

fun NavController.navigateToDrugTestingScreen() {
    navigate(NoArgumentRouter.DrugTestingRouter.route)
}