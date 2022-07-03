package com.example.healthassistant.ui.main.routers

import androidx.navigation.NavController


const val ADD_EXPERIENCE_ROUTE = "addExperience"
const val FAQ_ROUTE = "faqRoute"
const val SETTINGS_ROUTE = "settings"
const val DOSAGE_EXPLANATION_ROUTE = "dosageExplanation"
const val DURATION_EXPLANATION_ROUTE = "durationExplanation"

sealed class NoArgumentRouter(val route: String) {
    object AddExperienceRouter : NoArgumentRouter(route = ADD_EXPERIENCE_ROUTE)
    object FAQRouter : NoArgumentRouter(route = FAQ_ROUTE)
    object SettingsRouter : NoArgumentRouter(route = SETTINGS_ROUTE)
    object DosageExplanationRouter : NoArgumentRouter(route = DOSAGE_EXPLANATION_ROUTE)
    object DurationExplanationRouter : NoArgumentRouter(route = DURATION_EXPLANATION_ROUTE)
}

fun NavController.navigateToAddExperience() {
    navigate(ADD_EXPERIENCE_ROUTE)
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

fun NavController.navigateToDurationExplanationScreen() {
    navigate(DURATION_EXPLANATION_ROUTE)
}

fun NavController.navigateToExperienceFromAddExperience(experienceId: Int) {
    navigate(ROUTE_START_EXPERIENCES + experienceId) {
        popUpTo(TabRouter.Experiences.route)
    }
}