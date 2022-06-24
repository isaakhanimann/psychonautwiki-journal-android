package com.example.healthassistant.ui.main.routers

import androidx.navigation.NavController


const val ADD_EXPERIENCE_ROUTE = "addExperience"
const val FAQ_ROUTE = "faqRoute"
const val SETTINGS_ROUTE = "settings"
const val ADD_INGESTION_ROUTE = "addIngestion"

sealed class NoArgumentRouter(val route: String) {
    object AddExperienceRouter : NoArgumentRouter(route = ADD_EXPERIENCE_ROUTE)
    object FAQRouter : NoArgumentRouter(route = FAQ_ROUTE)
    object SettingsRouter : NoArgumentRouter(route = SETTINGS_ROUTE)
    object AddIngestionRouter : NoArgumentRouter(route = ADD_INGESTION_ROUTE)
}

fun NavController.navigateToAddExperience() {
    navigate(ADD_EXPERIENCE_ROUTE)
}

fun NavController.navigateToFAQ() {
    navigate(FAQ_ROUTE)
}

fun NavController.navigateToAddIngestion() {
    navigate(ADD_INGESTION_ROUTE)
}

fun NavController.navigateToExperienceFromAddExperience(experienceId: Int) {
    navigate(ROUTE_START_EXPERIENCES + experienceId) {
        popUpTo(TabRouter.Experiences.route)
    }
}