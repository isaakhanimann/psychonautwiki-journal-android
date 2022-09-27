package com.isaakhanimann.healthassistant.ui.main.routers

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute

// argument keys
const val EXPERIENCE_ID_KEY = "experienceId"
const val INGESTION_ID_KEY = "ingestionId"
const val SUBSTANCE_NAME_KEY = "substanceName"
const val ADMINISTRATION_ROUTE_KEY = "administrationRoute"
const val DOSE_KEY = "dose"
const val IS_ESTIMATE_KEY = "isEstimate"
const val UNITS_KEY = "units"

// route starts
const val ROUTE_START_EXPERIENCES = "experiences/"
const val ROUTE_START_INGESTIONS = "ingestions/"
const val ROUTE_START_ADD_INGESTIONS = "addIngestions/"
const val ROUTE_START_ADD_CUSTOM = "addCustom/"
const val ROUTE_START_EDIT_EXPERIENCE = "editExperience/"
const val ROUTE_START_EDIT_CUSTOM = "editCustom/"
const val ROUTE_START_SUBSTANCES = "substances/"
const val ROUTE_START_CHECK_INTERACTIONS_SKIP_NOTHING = "checkInteractionsSkipNothing/"
const val ROUTE_START_CHECK_INTERACTIONS_SKIP_ROUTE = "checkInteractionsSkipRoute/"
const val ROUTE_START_CHECK_INTERACTIONS_SKIP_DOSE = "checkInteractionsSkipDose/"
const val ROUTE_START_CHOOSE_ROUTE = "chooseRoute/"
const val ROUTE_START_CHOOSE_DOSE = "chooseDose/"
const val ROUTE_START_CHOOSE_TIME = "chooseTime/"
const val ROUTE_START_SUBSTANCE_COMPANION = "substancesCompanion/"


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

    object EditCustomRouter : ArgumentRouter(
        route = "$ROUTE_START_EDIT_CUSTOM{$SUBSTANCE_NAME_KEY}",
        args = listOf(navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType })
    )

    object SubstanceRouter : ArgumentRouter(
        route = "$ROUTE_START_SUBSTANCES{$SUBSTANCE_NAME_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
        )
    )

    object SubstanceCompanionRouter : ArgumentRouter(
        route = "$ROUTE_START_SUBSTANCE_COMPANION{$SUBSTANCE_NAME_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
        )
    )

    object CheckInteractionsRouterSkipNothing : ArgumentRouter(
        route = "$ROUTE_START_CHECK_INTERACTIONS_SKIP_NOTHING{$SUBSTANCE_NAME_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
        )
    )

    object CheckInteractionsRouterSkipRoute : ArgumentRouter(
        route = "$ROUTE_START_CHECK_INTERACTIONS_SKIP_ROUTE{$SUBSTANCE_NAME_KEY}/{$ADMINISTRATION_ROUTE_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
            navArgument(ADMINISTRATION_ROUTE_KEY) { type = NavType.StringType }
        )
    )

    object CheckInteractionsRouterSkipDose : ArgumentRouter(
        route = "$ROUTE_START_CHECK_INTERACTIONS_SKIP_DOSE{$SUBSTANCE_NAME_KEY}/{$ADMINISTRATION_ROUTE_KEY}/{$IS_ESTIMATE_KEY}/?$UNITS_KEY={$UNITS_KEY}/?$DOSE_KEY={$DOSE_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
            navArgument(ADMINISTRATION_ROUTE_KEY) { type = NavType.StringType },
            navArgument(IS_ESTIMATE_KEY) { type = NavType.BoolType },
            navArgument(UNITS_KEY) { nullable = true },
            navArgument(DOSE_KEY) { nullable = true },
        )
    )

    object ChooseRouteRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_ROUTE{$SUBSTANCE_NAME_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
        )
    )

    object ChooseDoseRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_DOSE{$SUBSTANCE_NAME_KEY}/{$ADMINISTRATION_ROUTE_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
            navArgument(ADMINISTRATION_ROUTE_KEY) { type = NavType.StringType }
        )
    )

    object ChooseTimeRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_TIME{$SUBSTANCE_NAME_KEY}/{$ADMINISTRATION_ROUTE_KEY}/{$IS_ESTIMATE_KEY}/?$UNITS_KEY={$UNITS_KEY}/?$DOSE_KEY={$DOSE_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
            navArgument(ADMINISTRATION_ROUTE_KEY) { type = NavType.StringType },
            navArgument(IS_ESTIMATE_KEY) { type = NavType.BoolType },
            navArgument(UNITS_KEY) { nullable = true },
            navArgument(DOSE_KEY) { nullable = true },
        )
    )
}

fun NavController.navigateToExperiencePopNothing(experienceId: Int) {
    navigate(ROUTE_START_EXPERIENCES + experienceId)
}

fun NavController.navigateToIngestion(ingestionId: Int) {
    navigate(ROUTE_START_INGESTIONS + ingestionId)
}

fun NavController.navigateToEditExperience(experienceId: Int) {
    navigate(ROUTE_START_EDIT_EXPERIENCE + experienceId)
}

fun NavController.navigateToSubstanceScreen(substanceName: String) {
    navigate(ROUTE_START_SUBSTANCES + substanceName)
}

fun NavController.navigateToEditCustomSubstance(substanceName: String) {
    navigate(ROUTE_START_EDIT_CUSTOM + substanceName)
}

fun NavController.navigateToSubstanceCompanionScreen(substanceName: String) {
    navigate(ROUTE_START_SUBSTANCE_COMPANION + substanceName)
}

fun NavController.navigateToCheckInteractionsSkipNothing(substanceName: String) {
    navigate("$ROUTE_START_CHECK_INTERACTIONS_SKIP_NOTHING$substanceName")
}

fun NavController.navigateToChooseRoute(substanceName: String) {
    navigate("$ROUTE_START_CHOOSE_ROUTE$substanceName")
}

fun NavController.navigateToChooseDose(
    substanceName: String,
    administrationRoute: AdministrationRoute
) {
    navigate("$ROUTE_START_CHOOSE_DOSE$substanceName/${administrationRoute.name}")
}

fun NavController.navigateToCheckInteractionsSkipRoute(
    substanceName: String,
    administrationRoute: AdministrationRoute
) {
    navigate("$ROUTE_START_CHECK_INTERACTIONS_SKIP_ROUTE$substanceName/${administrationRoute.name}")
}

fun NavController.navigateToChooseTimeAndMaybeColor(
    substanceName: String,
    administrationRoute: AdministrationRoute,
    units: String?,
    isEstimate: Boolean,
    dose: Double?,
) {
    navigate("$ROUTE_START_CHOOSE_TIME$substanceName/${administrationRoute.name}/$isEstimate/?$UNITS_KEY=$units/?$DOSE_KEY=$dose")
}

fun NavController.navigateToCheckInteractionsSkipDose(
    substanceName: String,
    administrationRoute: AdministrationRoute,
    units: String?,
    isEstimate: Boolean,
    dose: Double?,
) {
    navigate("$ROUTE_START_CHECK_INTERACTIONS_SKIP_DOSE$substanceName/${administrationRoute.name}/$isEstimate/?$UNITS_KEY=$units/?$DOSE_KEY=$dose")
}