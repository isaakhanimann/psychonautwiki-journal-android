package com.isaakhanimann.journal.ui.main.routers

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.*
import com.google.accompanist.navigation.animation.navigation
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.addingestion.dose.ChooseDoseScreen
import com.isaakhanimann.journal.ui.addingestion.dose.custom.CustomChooseDose
import com.isaakhanimann.journal.ui.addingestion.interactions.CheckInteractionsScreen
import com.isaakhanimann.journal.ui.addingestion.route.ChooseRouteScreen
import com.isaakhanimann.journal.ui.addingestion.route.CustomChooseRouteScreen
import com.isaakhanimann.journal.ui.addingestion.time.ChooseTimeScreen
import com.isaakhanimann.journal.ui.journal.experience.ExperienceScreen
import com.isaakhanimann.journal.ui.journal.experience.edit.EditExperienceScreen
import com.isaakhanimann.journal.ui.journal.experience.editingestion.EditIngestionScreen
import com.isaakhanimann.journal.ui.main.routers.transitions.regularComposableWithTransitions
import com.isaakhanimann.journal.ui.search.custom.EditCustomSubstance
import com.isaakhanimann.journal.ui.search.substance.SubstanceScreen
import com.isaakhanimann.journal.ui.search.substance.UrlScreen
import com.isaakhanimann.journal.ui.search.substance.category.CategoryScreen
import com.isaakhanimann.journal.ui.stats.substancecompanion.SubstanceCompanionScreen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// argument keys
const val EXPERIENCE_ID_KEY = "experienceId"
const val INGESTION_ID_KEY = "ingestionId"
const val SUBSTANCE_NAME_KEY = "substanceName"
const val URL_KEY = "url"
const val ADMINISTRATION_ROUTE_KEY = "administrationRoute"
const val DOSE_KEY = "dose"
const val IS_ESTIMATE_KEY = "isEstimate"
const val UNITS_KEY = "units"
const val CATEGORY_KEY = "category"

// route starts
const val ROUTE_START_EXPERIENCES = "experiences/"
const val ROUTE_START_INGESTIONS = "ingestions/"
const val ROUTE_START_ADD_INGESTIONS = "addIngestions/"
const val ROUTE_START_ADD_CUSTOM = "addCustom/"
const val ROUTE_START_EDIT_EXPERIENCE = "editExperience/"
const val ROUTE_START_EDIT_CUSTOM = "editCustom/"
const val ROUTE_START_CHOOSE_ROUTE_CUSTOM = "chooseRouteCustom/"
const val ROUTE_START_CHOOSE_DOSE_CUSTOM = "chooseDoseCustom/"
const val ROUTE_START_SUBSTANCES = "substances/"
const val ROUTE_START_CHECK_INTERACTIONS_SKIP_NOTHING = "checkInteractionsSkipNothing/"
const val ROUTE_START_CHECK_INTERACTIONS_SKIP_ROUTE = "checkInteractionsSkipRoute/"
const val ROUTE_START_CHECK_INTERACTIONS_SKIP_DOSE = "checkInteractionsSkipDose/"
const val ROUTE_START_CHOOSE_ROUTE = "chooseRoute/"
const val ROUTE_START_CHOOSE_DOSE = "chooseDose/"
const val ROUTE_START_CHOOSE_TIME = "chooseTime/"
const val ROUTE_START_SUBSTANCE_COMPANION = "substancesCompanion/"
const val ROUTE_START_CATEGORY = "category/"
const val ROUTE_START_URL = "url/"


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

    object URLRouter : ArgumentRouter(
        route = "$ROUTE_START_URL{$URL_KEY}",
        args = listOf(navArgument(URL_KEY) { type = NavType.StringType })
    )

    object CategoryRouter : ArgumentRouter(
        route = "$ROUTE_START_CATEGORY{$CATEGORY_KEY}",
        args = listOf(navArgument(CATEGORY_KEY) { type = NavType.StringType })
    )

    object CustomChooseRouteRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_ROUTE_CUSTOM{$SUBSTANCE_NAME_KEY}",
        args = listOf(navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType })
    )

    object CustomChooseDoseRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_DOSE_CUSTOM{$SUBSTANCE_NAME_KEY}/{$ADMINISTRATION_ROUTE_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
            navArgument(ADMINISTRATION_ROUTE_KEY) { type = NavType.StringType }
        )
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

fun NavController.navigateToURLScreen(url: String) {
    val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
    navigate(ROUTE_START_URL + encodedUrl)
}

fun NavController.navigateToEditCustomSubstance(substanceName: String) {
    navigate(ROUTE_START_EDIT_CUSTOM + substanceName)
}

fun NavController.navigateToChooseCustomRoute(substanceName: String) {
    navigate(ROUTE_START_CHOOSE_ROUTE_CUSTOM + substanceName)
}

fun NavController.navigateToSubstanceCompanionScreen(substanceName: String) {
    navigate(ROUTE_START_SUBSTANCE_COMPANION + substanceName)
}

fun NavController.navigateToCategoryScreen(categoryName: String) {
    navigate(ROUTE_START_CATEGORY + categoryName)
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

fun NavController.navigateToChooseDoseCustom(
    substanceName: String,
    administrationRoute: AdministrationRoute
) {
    navigate("$ROUTE_START_CHOOSE_DOSE_CUSTOM$substanceName/${administrationRoute.name}")
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

fun NavGraphBuilder.argumentGraph(navController: NavController) {
    regularComposableWithTransitions(
        ArgumentRouter.EditExperienceRouter.route,
        arguments = ArgumentRouter.EditExperienceRouter.args
    ) {
        EditExperienceScreen(navigateBack = navController::popBackStack)
    }
    regularComposableWithTransitions(
        ArgumentRouter.CategoryRouter.route,
        arguments = ArgumentRouter.CategoryRouter.args
    ) {
        CategoryScreen(navigateToURL = navController::navigateToURLScreen)
    }
    regularComposableWithTransitions(
        ArgumentRouter.EditCustomRouter.route,
        arguments = ArgumentRouter.EditCustomRouter.args
    ) {
        EditCustomSubstance(navigateBack = navController::popBackStack)
    }
    regularComposableWithTransitions(
        ArgumentRouter.ExperienceRouter.route,
        arguments = ArgumentRouter.ExperienceRouter.args
    ) {
        val experienceId = it.arguments!!.getInt(EXPERIENCE_ID_KEY)
        ExperienceScreen(
            navigateToAddIngestionSearch = navController::navigateToAddIngestion,
            navigateToExplainTimeline = navController::navigateToExplainTimeline,
            navigateToEditExperienceScreen = {
                navController.navigateToEditExperience(experienceId)
            },
            navigateToIngestionScreen = { ingestionId ->
                navController.navigateToIngestion(ingestionId)
            },
            navigateBack = navController::popBackStack
        )
    }
    regularComposableWithTransitions(
        ArgumentRouter.IngestionRouter.route,
        arguments = ArgumentRouter.IngestionRouter.args
    ) {
        EditIngestionScreen(navigateBack = navController::popBackStack)
    }
    regularComposableWithTransitions(
        route = ArgumentRouter.SubstanceRouter.route,
        arguments = ArgumentRouter.SubstanceRouter.args,
    ) {
        SubstanceScreen(
            navigateToDosageExplanationScreen = navController::navigateToDosageExplanationScreen,
            navigateToSaferHallucinogensScreen = navController::navigateToSaferHallucinogens,
            navigateToSaferStimulantsScreen = navController::navigateToSaferStimulants,
            navigateToExplainTimeline = navController::navigateToExplainTimeline,
            navigateToCategoryScreen = navController::navigateToCategoryScreen,
            navigateToVolumetricDosingScreen = navController::navigateToVolumetricDosingScreen,
            navigateToArticle = navController::navigateToURLScreen
        )
    }
    regularComposableWithTransitions(
        ArgumentRouter.URLRouter.route,
        arguments = ArgumentRouter.URLRouter.args
    ) { backStackEntry ->
        val args = backStackEntry.arguments!!
        val url = args.getString(URL_KEY)!!
        UrlScreen(url = url)
    }
    regularComposableWithTransitions(
        ArgumentRouter.SubstanceCompanionRouter.route,
        arguments = ArgumentRouter.SubstanceCompanionRouter.args
    ) {
        SubstanceCompanionScreen()
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addIngestionGraph(navController: NavController) {
    navigation(
        startDestination = ArgumentRouter.CheckInteractionsRouterSkipNothing.route,
        route = "is not used"
    ) {
        regularComposableWithTransitions(
            ArgumentRouter.CheckInteractionsRouterSkipNothing.route,
            arguments = ArgumentRouter.CheckInteractionsRouterSkipNothing.args
        ) { backStackEntry ->
            CheckInteractionsScreen(
                navigateToNext = {
                    val args = backStackEntry.arguments!!
                    val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
                    navController.navigateToChooseRoute(substanceName = substanceName)
                },
                navigateToURL = navController::navigateToURLScreen
            )
        }
        regularComposableWithTransitions(
            ArgumentRouter.CheckInteractionsRouterSkipRoute.route,
            arguments = ArgumentRouter.CheckInteractionsRouterSkipRoute.args
        ) { backStackEntry ->
            CheckInteractionsScreen(
                navigateToNext = {
                    val args = backStackEntry.arguments!!
                    val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
                    val route =
                        AdministrationRoute.valueOf(args.getString(ADMINISTRATION_ROUTE_KEY)!!)
                    navController.navigateToChooseDose(substanceName, route)
                },
                navigateToURL = navController::navigateToURLScreen
            )
        }
        regularComposableWithTransitions(
            ArgumentRouter.CheckInteractionsRouterSkipDose.route,
            arguments = ArgumentRouter.CheckInteractionsRouterSkipDose.args
        ) { backStackEntry ->
            CheckInteractionsScreen(
                navigateToNext = {
                    val args = backStackEntry.arguments!!
                    val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
                    val route =
                        AdministrationRoute.valueOf(args.getString(ADMINISTRATION_ROUTE_KEY)!!)
                    val dose = args.getString(DOSE_KEY)?.toDoubleOrNull()
                    val units = args.getString(UNITS_KEY)?.let {
                        if (it == "null") {
                            null
                        } else {
                            it
                        }
                    }
                    val isEstimate = args.getBoolean(IS_ESTIMATE_KEY)
                    navController.navigateToChooseTimeAndMaybeColor(
                        substanceName,
                        route,
                        units,
                        isEstimate = isEstimate,
                        dose = dose
                    )
                },
                navigateToURL = navController::navigateToURLScreen
            )
        }
        regularComposableWithTransitions(
            ArgumentRouter.ChooseRouteRouter.route,
            arguments = ArgumentRouter.ChooseRouteRouter.args
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
            ChooseRouteScreen(
                navigateToChooseDose = { administrationRoute ->
                    navController.navigateToChooseDose(
                        substanceName = substanceName,
                        administrationRoute = administrationRoute,
                    )
                },
                navigateToRouteExplanationScreen = navController::navigateToAdministrationRouteExplanationScreen,
                navigateToURL = navController::navigateToURLScreen
            )
        }
        regularComposableWithTransitions(
            ArgumentRouter.CustomChooseRouteRouter.route,
            arguments = ArgumentRouter.CustomChooseRouteRouter.args
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
            CustomChooseRouteScreen(
                onRouteTap = { administrationRoute ->
                    navController.navigateToChooseDoseCustom(
                        substanceName = substanceName,
                        administrationRoute = administrationRoute,
                    )
                },
                substanceName = substanceName
            )
        }
        regularComposableWithTransitions(
            ArgumentRouter.CustomChooseDoseRouter.route,
            arguments = ArgumentRouter.CustomChooseDoseRouter.args
        ) { backStackEntry ->
            CustomChooseDose(
                navigateToChooseTimeAndMaybeColor = { units, isEstimate, dose ->
                    val args = backStackEntry.arguments!!
                    val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
                    val route =
                        AdministrationRoute.valueOf(args.getString(ADMINISTRATION_ROUTE_KEY)!!)
                    navController.navigateToChooseTimeAndMaybeColor(
                        substanceName = substanceName,
                        administrationRoute = route,
                        units = units,
                        isEstimate = isEstimate,
                        dose = dose,
                    )
                },
                navigateToSaferSniffingScreen = navController::navigateToSaferSniffing,
                navigateToURL = navController::navigateToURLScreen
            )
        }
        regularComposableWithTransitions(
            ArgumentRouter.ChooseDoseRouter.route,
            arguments = ArgumentRouter.ChooseDoseRouter.args
        ) { backStackEntry ->
            ChooseDoseScreen(
                navigateToChooseTimeAndMaybeColor = { units, isEstimate, dose ->
                    val args = backStackEntry.arguments!!
                    val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
                    val route =
                        AdministrationRoute.valueOf(args.getString(ADMINISTRATION_ROUTE_KEY)!!)
                    navController.navigateToChooseTimeAndMaybeColor(
                        substanceName = substanceName,
                        administrationRoute = route,
                        units = units,
                        isEstimate = isEstimate,
                        dose = dose,
                    )
                },
                navigateToVolumetricDosingScreen = navController::navigateToVolumetricDosingScreen,
                navigateToSaferSniffingScreen = navController::navigateToSaferSniffing,
                navigateToURL = navController::navigateToURLScreen
            )
        }
        regularComposableWithTransitions(
            ArgumentRouter.ChooseTimeRouter.route,
            arguments = ArgumentRouter.ChooseTimeRouter.args
        ) {
            ChooseTimeScreen(
                dismissAddIngestionScreens = navController::dismissAddIngestionScreens,
            )
        }
    }
}

