package com.example.healthassistant.ui.main.routers

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.healthassistant.data.room.experiences.entities.IngestionColor
import com.example.healthassistant.data.substances.AdministrationRoute

// argument keys
const val EXPERIENCE_ID_KEY = "experienceId"
const val INGESTION_ID_KEY = "ingestionId"
const val SUBSTANCE_NAME_KEY = "substanceName"
const val ADMINISTRATION_ROUTE_KEY = "administrationRoute"
const val DOSE_KEY = "dose"
const val IS_ESTIMATE_KEY = "isEstimate"
const val UNITS_KEY = "units"
const val COLOR_KEY = "color"

// route starts
const val ROUTE_START_EXPERIENCES = "experiences/"
const val ROUTE_START_INGESTIONS = "ingestions/"
const val ROUTE_START_ADD_INGESTION_SEARCH = "addIngestionSearch/"
const val ROUTE_START_EDIT_EXPERIENCE = "editExperience/"
const val ROUTE_START_SUBSTANCES = "substances/"
const val ROUTE_START_CHECK_INTERACTIONS = "checkInteractions/"
const val ROUTE_START_CHOOSE_ROUTE = "chooseRoute/"
const val ROUTE_START_CHOOSE_DOSE = "chooseDose/"
const val ROUTE_START_CHOOSE_COLOR = "chooseColor/"
const val ROUTE_START_CHOOSE_TIME = "chooseTime/"

sealed class ArgumentRouter(val route: String, val args: List<NamedNavArgument>) {
    object ExperienceRouter : ArgumentRouter(
        route = "$ROUTE_START_EXPERIENCES{$EXPERIENCE_ID_KEY}",
        args = listOf(navArgument(EXPERIENCE_ID_KEY) { type = NavType.IntType })
    )

    object IngestionRouter : ArgumentRouter(
        route = "$ROUTE_START_INGESTIONS{$INGESTION_ID_KEY}",
        args = listOf(navArgument(INGESTION_ID_KEY) { type = NavType.IntType })
    )

    object EditExperienceRouter : ArgumentRouter(
        route = "$ROUTE_START_EDIT_EXPERIENCE{$EXPERIENCE_ID_KEY}",
        args = listOf(navArgument(EXPERIENCE_ID_KEY) { type = NavType.IntType })
    )

    object SearchRouter : ArgumentRouter(
        route = "$ROUTE_START_ADD_INGESTION_SEARCH{$EXPERIENCE_ID_KEY}",
        args = listOf(navArgument(EXPERIENCE_ID_KEY) { type = NavType.IntType })
    )

    object SubstanceRouter : ArgumentRouter(
        route = "$ROUTE_START_SUBSTANCES{$SUBSTANCE_NAME_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
        )
    )

    object CheckInteractionsRouter : ArgumentRouter(
        route = "$ROUTE_START_CHECK_INTERACTIONS{$SUBSTANCE_NAME_KEY}/?$EXPERIENCE_ID_KEY={$EXPERIENCE_ID_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
            navArgument(EXPERIENCE_ID_KEY) { nullable = true }

        )
    )

    object ChooseRouteRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_ROUTE{$SUBSTANCE_NAME_KEY}/?$EXPERIENCE_ID_KEY={$EXPERIENCE_ID_KEY}",
        args = listOf(navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
            navArgument(EXPERIENCE_ID_KEY) { nullable = true }
        )
    )

    object ChooseDoseRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_DOSE{$SUBSTANCE_NAME_KEY}/{$ADMINISTRATION_ROUTE_KEY}/?$EXPERIENCE_ID_KEY={$EXPERIENCE_ID_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
            navArgument(ADMINISTRATION_ROUTE_KEY) { type = NavType.StringType },
            navArgument(EXPERIENCE_ID_KEY) { nullable = true }

        )
    )

    object ChooseColorRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_COLOR{$SUBSTANCE_NAME_KEY}/{$ADMINISTRATION_ROUTE_KEY}/{$IS_ESTIMATE_KEY}/?$UNITS_KEY={$UNITS_KEY}/?$DOSE_KEY={$DOSE_KEY}/?$EXPERIENCE_ID_KEY={$EXPERIENCE_ID_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
            navArgument(ADMINISTRATION_ROUTE_KEY) { type = NavType.StringType },
            navArgument(IS_ESTIMATE_KEY) { type = NavType.BoolType },
            navArgument(UNITS_KEY) { nullable = true },
            navArgument(DOSE_KEY) { nullable = true },
            navArgument(EXPERIENCE_ID_KEY) { nullable = true }
        )
    )

    object ChooseTimeRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_TIME{$SUBSTANCE_NAME_KEY}/{$ADMINISTRATION_ROUTE_KEY}/{$IS_ESTIMATE_KEY}/{$COLOR_KEY}/?$UNITS_KEY={$UNITS_KEY}/?$DOSE_KEY={$DOSE_KEY}/?$EXPERIENCE_ID_KEY={$EXPERIENCE_ID_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
            navArgument(ADMINISTRATION_ROUTE_KEY) { type = NavType.StringType },
            navArgument(IS_ESTIMATE_KEY) { type = NavType.BoolType },
            navArgument(COLOR_KEY) { type = NavType.StringType },
            navArgument(UNITS_KEY) { nullable = true },
            navArgument(DOSE_KEY) { nullable = true },
            navArgument(EXPERIENCE_ID_KEY) { nullable = true }
        )
    )
}

fun NavController.navigateToExperiencePopNothing(experienceId: Int) {
    navigate(ROUTE_START_EXPERIENCES + experienceId)
}

fun NavController.navigateToIngestion(ingestionId: Int) {
    navigate(ROUTE_START_INGESTIONS + ingestionId)
}

fun NavController.popUpToExperienceScreen() {
    popBackStack(route = ArgumentRouter.ExperienceRouter.route, inclusive = false)
}

fun NavController.popUpToSubstanceScreen() {
    popBackStack(route = ArgumentRouter.SubstanceRouter.route, inclusive = false)
}

fun NavController.navigateToAddIngestionSearch(experienceId: Int) {
    navigate(ROUTE_START_ADD_INGESTION_SEARCH + experienceId)
}

fun NavController.navigateToEditExperience(experienceId: Int) {
    navigate(ROUTE_START_EDIT_EXPERIENCE + experienceId)
}

fun NavController.navigateToSubstanceScreen(substanceName: String) {
    navigate(ROUTE_START_SUBSTANCES + substanceName)
}

fun NavController.navigateToAddIngestion(substanceName: String, experienceId: Int?) {
    navigate("$ROUTE_START_CHECK_INTERACTIONS$substanceName/?$EXPERIENCE_ID_KEY=$experienceId")
}

fun NavController.navigateToChooseRoute(substanceName: String, experienceId: Int?) {
    navigate("$ROUTE_START_CHOOSE_ROUTE$substanceName/?$EXPERIENCE_ID_KEY=$experienceId")
}

fun NavController.navigateToChooseDose(
    substanceName: String,
    administrationRoute: AdministrationRoute, experienceId: Int?
) {
    navigate("$ROUTE_START_CHOOSE_DOSE$substanceName/${administrationRoute.name}/?$EXPERIENCE_ID_KEY=$experienceId")
}

fun NavController.navigateToChooseColor(
    substanceName: String,
    administrationRoute: AdministrationRoute,
    units: String?,
    isEstimate: Boolean,
    dose: Double?,
    experienceId: Int?
) {
    navigate("$ROUTE_START_CHOOSE_COLOR$substanceName/${administrationRoute.name}/$isEstimate/?$UNITS_KEY=$units/?$DOSE_KEY=$dose/?$EXPERIENCE_ID_KEY=$experienceId")
}

fun NavController.navigateToChooseTime(
    substanceName: String,
    administrationRoute: AdministrationRoute,
    units: String?,
    isEstimate: Boolean,
    color: IngestionColor,
    dose: Double?,
    experienceId: Int?
) {
    navigate("$ROUTE_START_CHOOSE_TIME$substanceName/${administrationRoute.name}/$isEstimate/${color.name}/?$UNITS_KEY=$units/?$DOSE_KEY=$dose/?$EXPERIENCE_ID_KEY=$experienceId")
}