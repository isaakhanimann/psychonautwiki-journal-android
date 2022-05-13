package com.example.healthassistant.ui.main.routers

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.healthassistant.data.substances.AdministrationRoute

// argument keys
const val EXPERIENCE_ID_KEY = "experienceId"
const val SUBSTANCE_NAME_KEY = "substanceName"
const val ADMINISTRATION_ROUTE_KEY = "administrationRoute"
const val DOSE_KEY = "dose"
const val IS_ESTIMATE_KEY = "isEstimate"
const val UNITS_KEY = "units"

// route starts
const val ROUTE_START_EXPERIENCES = "experiences/"
const val ROUTE_START_SUBSTANCES = "substances/"
const val ROUTE_START_CHECK_INTERACTIONS = "checkInteractions/"
const val ROUTE_START_CHOOSE_ROUTE = "chooseRoute/"
const val ROUTE_START_CHOOSE_DOSE = "chooseDose/"
const val ROUTE_START_CHOOSE_TIME = "chooseTime/"

sealed class ArgumentRouter(val route: String, val args: List<NamedNavArgument>) {
    object ExperienceRouter : ArgumentRouter(
        route = "$ROUTE_START_EXPERIENCES{$EXPERIENCE_ID_KEY}",
        args = listOf(navArgument(EXPERIENCE_ID_KEY) { type = NavType.IntType })
    )

    object SubstanceRouter : ArgumentRouter(
        route = "$ROUTE_START_SUBSTANCES{$SUBSTANCE_NAME_KEY}",
        args = listOf(navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType })
    )

    object CheckInteractionsRouter : ArgumentRouter(
        route = "$ROUTE_START_CHECK_INTERACTIONS{$SUBSTANCE_NAME_KEY}",
        args = listOf(navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType })
    )

    object ChooseRouteRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_ROUTE{$SUBSTANCE_NAME_KEY}",
        args = listOf(navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType })
    )

    object ChooseDoseRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_DOSE{$SUBSTANCE_NAME_KEY}/{${ADMINISTRATION_ROUTE_KEY}}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
            navArgument(ADMINISTRATION_ROUTE_KEY) { type = NavType.StringType }
        )
    )

    object ChooseTimeRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_TIME{$SUBSTANCE_NAME_KEY}/{${ADMINISTRATION_ROUTE_KEY}}/{${IS_ESTIMATE_KEY}}/{${UNITS_KEY}}/?${DOSE_KEY}={${DOSE_KEY}}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
            navArgument(ADMINISTRATION_ROUTE_KEY) { type = NavType.StringType },
            navArgument(IS_ESTIMATE_KEY) { type = NavType.BoolType },
            navArgument(UNITS_KEY) { type = NavType.StringType },
            navArgument(DOSE_KEY) {
                nullable = true
            },
        )
    )
}

fun NavController.navigateToExperience(experienceId: Int) {
    navigate(ROUTE_START_EXPERIENCES + experienceId)
}

fun NavController.navigateToSubstanceScreen(substanceName: String) {
    navigate(ROUTE_START_SUBSTANCES + substanceName)
}

fun NavController.navigateToAddIngestion(substanceName: String) {
    navigate(ROUTE_START_CHECK_INTERACTIONS + substanceName)
}

fun NavController.navigateToChooseRoute(substanceName: String) {
    navigate(ROUTE_START_CHOOSE_ROUTE + substanceName)
}

fun NavController.navigateToChooseDose(
    substanceName: String,
    administrationRoute: AdministrationRoute
) {
    navigate(ROUTE_START_CHOOSE_DOSE + substanceName + "/" + administrationRoute.name)
}

fun NavController.navigateToChooseTime(
    substanceName: String,
    administrationRoute: AdministrationRoute,
    units: String,
    isEstimate: Boolean,
    dose: Double?,
) {
    navigate(ROUTE_START_CHOOSE_TIME + substanceName + "/" + administrationRoute.name + "/" + isEstimate + "/" + units + "/?" + DOSE_KEY + "=" + dose)
}