package com.isaakhanimann.journal.ui.main.routers

import androidx.navigation.NavController


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

sealed class NoArgumentRouter(val route: String) {
    object StatsRouter : NoArgumentRouter(route = STATS_ROUTE)
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

fun NavController.navigateToExperienceFromAddExperience(experienceId: Int) {
    navigate(ROUTE_START_EXPERIENCES + experienceId) {
        popUpTo(TabRouter.Journal.route)
    }
}