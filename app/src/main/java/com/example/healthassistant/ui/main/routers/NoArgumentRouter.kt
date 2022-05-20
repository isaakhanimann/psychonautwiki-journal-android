package com.example.healthassistant.ui.main.routers

import androidx.navigation.NavController


const val ADD_EXPERIENCE_ROUTE = "addExperience"

sealed class NoArgumentRouter(val route: String) {
    object AddExperienceRouter : NoArgumentRouter(
        route = ADD_EXPERIENCE_ROUTE
    )
}

fun NavController.navigateToAddExperience() {
    navigate(ADD_EXPERIENCE_ROUTE)
}

fun NavController.navigateToExperienceFromAddExperience(experienceId: Int) {
    navigate(ROUTE_START_EXPERIENCES + experienceId) {
        popUpTo(TabRouter.Home.route)
    }
}