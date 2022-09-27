package com.isaakhanimann.healthassistant.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.*
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import com.isaakhanimann.healthassistant.ui.AcceptConditionsScreen
import com.isaakhanimann.healthassistant.ui.addingestion.dose.ChooseDoseScreen
import com.isaakhanimann.healthassistant.ui.addingestion.dose.DoseGuideScreen
import com.isaakhanimann.healthassistant.ui.addingestion.interactions.CheckInteractionsScreen
import com.isaakhanimann.healthassistant.ui.addingestion.route.ChooseRouteScreen
import com.isaakhanimann.healthassistant.ui.addingestion.route.RouteExplanationScreen
import com.isaakhanimann.healthassistant.ui.addingestion.search.AddIngestionSearchScreen
import com.isaakhanimann.healthassistant.ui.addingestion.time.ChooseTimeScreen
import com.isaakhanimann.healthassistant.ui.journal.JournalScreen
import com.isaakhanimann.healthassistant.ui.journal.experience.ExperienceScreen
import com.isaakhanimann.healthassistant.ui.journal.experience.edit.EditExperienceScreen
import com.isaakhanimann.healthassistant.ui.journal.experience.editingestion.EditIngestionScreen
import com.isaakhanimann.healthassistant.ui.main.routers.*
import com.isaakhanimann.healthassistant.ui.safer.DrugTestingScreen
import com.isaakhanimann.healthassistant.ui.safer.SaferHallucinogensScreen
import com.isaakhanimann.healthassistant.ui.safer.SaferUseScreen
import com.isaakhanimann.healthassistant.ui.safer.VolumetricDosingScreen
import com.isaakhanimann.healthassistant.ui.search.SearchScreen
import com.isaakhanimann.healthassistant.ui.search.substance.SaferSniffingScreen
import com.isaakhanimann.healthassistant.ui.search.substance.SaferStimulantsScreen
import com.isaakhanimann.healthassistant.ui.search.substance.SubstanceScreen
import com.isaakhanimann.healthassistant.ui.search.substance.roa.DoseExplanationScreen
import com.isaakhanimann.healthassistant.ui.search.substance.roa.DurationExplanationScreen
import com.isaakhanimann.healthassistant.ui.settings.SettingsScreen
import com.isaakhanimann.healthassistant.ui.settings.faq.FAQScreen
import com.isaakhanimann.healthassistant.ui.stats.StatsScreen
import com.isaakhanimann.healthassistant.ui.stats.substancecompanion.SubstanceCompanionScreen

@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModel = hiltViewModel()
) {
    if (mainScreenViewModel.isAcceptedFlow.collectAsState().value) {
        val navController = rememberNavController()
        val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val isShowingBottomBar = navBackStackEntry?.destination?.route in setOf(
            TabRouter.Journal.route,
            TabRouter.Statistics.route,
            TabRouter.Search.route,
            TabRouter.SaferUse.route
        )
        bottomBarState.value = isShowingBottomBar
        Scaffold(
            bottomBar = {
                BottomBar(
                    navController = navController,
                    bottomBarState = bottomBarState
                )
            }
        ) { innerPadding ->
            NavHost(
                navController,
                startDestination = TabRouter.Search.route,
                Modifier.padding(innerPadding)
            ) {
                tabGraph(navController)
                noArgumentGraph(navController)
                argumentGraph(navController)
                addIngestionGraph(navController)
            }
        }
    } else {
        AcceptConditionsScreen(onTapAccept = mainScreenViewModel::accept)
    }
}

fun NavGraphBuilder.noArgumentGraph(navController: NavController) {
    composable(NoArgumentRouter.StatsRouter.route) {
        StatsScreen(
            navigateToSubstanceCompanion = {
                navController.navigateToSubstanceCompanionScreen(it)
            }
        )
    }
    composable(NoArgumentRouter.FAQRouter.route) { FAQScreen() }
    composable(NoArgumentRouter.SaferHallucinogens.route) { SaferHallucinogensScreen() }
    composable(NoArgumentRouter.SaferStimulants.route) { SaferStimulantsScreen() }
    composable(NoArgumentRouter.SaferSniffing.route) { SaferSniffingScreen() }
    composable(NoArgumentRouter.SettingsRouter.route) { SettingsScreen(navigateToFAQ = navController::navigateToFAQ) }
    composable(NoArgumentRouter.DosageExplanationRouter.route) { DoseExplanationScreen() }
    composable(NoArgumentRouter.DurationExplanationRouter.route) { DurationExplanationScreen() }
    composable(NoArgumentRouter.AdministrationRouteExplanationRouter.route) { RouteExplanationScreen() }
    composable(NoArgumentRouter.DrugTestingRouter.route) { DrugTestingScreen() }
    composable(NoArgumentRouter.DosageGuideRouter.route) {
        DoseGuideScreen(
            navigateToDoseClassification = navController::navigateToDosageExplanationScreen,
            navigateToVolumetricDosing = navController::navigateToVolumetricDosingScreen
        )
    }
    composable(NoArgumentRouter.VolumetricDosingRouter.route) { VolumetricDosingScreen() }
    composable(NoArgumentRouter.AddIngestionRouter.route) {
        AddIngestionSearchScreen(
            navigateToCheckInteractionsSkipNothing = {
                navController.navigateToCheckInteractionsSkipNothing(substanceName = it)
            },
            navigateToCheckInteractionsSkipRoute = { substanceName, route ->
                navController.navigateToCheckInteractionsSkipRoute(
                    substanceName = substanceName,
                    administrationRoute = route
                )
            },
            navigateToCheckInteractionsSkipDose = { substanceName, route, dose, units, isEstimate ->
                navController.navigateToCheckInteractionsSkipDose(
                    substanceName = substanceName,
                    administrationRoute = route,
                    units = units,
                    isEstimate = isEstimate,
                    dose = dose
                )
            }
        )
    }
}

fun NavGraphBuilder.argumentGraph(navController: NavController) {
    composable(
        ArgumentRouter.EditExperienceRouter.route,
        arguments = ArgumentRouter.EditExperienceRouter.args
    ) {
        EditExperienceScreen(navigateBack = navController::popBackStack)
    }
    composable(
        ArgumentRouter.ExperienceRouter.route,
        arguments = ArgumentRouter.ExperienceRouter.args
    ) {
        val experienceId = it.arguments!!.getInt(EXPERIENCE_ID_KEY)
        ExperienceScreen(
            navigateToAddIngestionSearch = {
                navController.navigateToAddIngestion()
            },
            navigateToEditExperienceScreen = {
                navController.navigateToEditExperience(experienceId)
            },
            navigateToIngestionScreen = { ingestionId ->
                navController.navigateToIngestion(ingestionId)
            },
            navigateBack = navController::popBackStack
        )
    }
    composable(
        ArgumentRouter.IngestionRouter.route,
        arguments = ArgumentRouter.IngestionRouter.args
    ) {
        EditIngestionScreen(navigateBack = navController::popBackStack)
    }
    composable(
        ArgumentRouter.SubstanceRouter.route,
        arguments = ArgumentRouter.SubstanceRouter.args
    ) {
        SubstanceScreen(
            navigateToDosageExplanationScreen = navController::navigateToDosageExplanationScreen,
            navigateToDurationExplanationScreen = navController::navigateToDurationExplanationScreen,
            navigateToSaferHallucinogensScreen = navController::navigateToSaferHallucinogens,
            navigateToSaferSniffingScreen = navController::navigateToSaferSniffing,
            navigateToSaferStimulantsScreen = navController::navigateToSaferStimulants,
            navigateToVolumetricDosingScreen = navController::navigateToVolumetricDosingScreen
        )
    }
    composable(
        ArgumentRouter.SubstanceCompanionRouter.route,
        arguments = ArgumentRouter.SubstanceCompanionRouter.args
    ) {
        SubstanceCompanionScreen()
    }
}

fun NavGraphBuilder.tabGraph(navController: NavController) {
    composable(TabRouter.Journal.route) {
        JournalScreen(
            navigateToExperiencePopNothing = {
                navController.navigateToExperiencePopNothing(experienceId = it)
            },
            navigateToAddIngestion = navController::navigateToAddIngestion
        )
    }
    composable(TabRouter.Statistics.route) {
        StatsScreen(
            navigateToSubstanceCompanion = {
                navController.navigateToSubstanceCompanionScreen(substanceName = it)
            }
        )
    }
    composable(TabRouter.Search.route) {
        SearchScreen(
            onSubstanceTap = {
                navController.navigateToSubstanceScreen(substanceName = it)
            },
            isShowingSettings = true,
            navigateToSettings = navController::navigateToSettings
        )
    }
    composable(TabRouter.SaferUse.route) {
        SaferUseScreen(
            navigateToDrugTestingScreen = navController::navigateToDrugTestingScreen,
            navigateToSaferHallucinogensScreen = navController::navigateToSaferHallucinogens,
            navigateToVolumetricDosingScreen = navController::navigateToVolumetricDosingScreen
        )
    }
}

fun NavGraphBuilder.addIngestionGraph(navController: NavController) {
    navigation(
        startDestination = ArgumentRouter.CheckInteractionsRouterSkipNothing.route,
        route = "is not used"
    ) {
        composable(
            ArgumentRouter.CheckInteractionsRouterSkipNothing.route,
            arguments = ArgumentRouter.CheckInteractionsRouterSkipNothing.args
        ) { backStackEntry ->
            CheckInteractionsScreen(
                navigateToNext = {
                    val args = backStackEntry.arguments!!
                    val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
                    navController.navigateToChooseRoute(substanceName = substanceName)
                }
            )
        }
        composable(
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
                }
            )
        }
        composable(
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
                }
            )
        }
        composable(
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
                navigateToChooseTimeAndMaybeColor = { administrationRoute, dose, units, isEstimate ->
                    navController.navigateToChooseTimeAndMaybeColor(
                        substanceName = substanceName,
                        administrationRoute = administrationRoute,
                        units = units,
                        isEstimate = isEstimate,
                        dose = dose
                    )
                }
            )
        }
        composable(
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
                navigateToDoseGuideScreen = navController::navigateToDosageGuideScreen,
                navigateToSaferSniffingScreen = navController::navigateToSaferSniffing
            )
        }
        composable(
            ArgumentRouter.ChooseTimeRouter.route,
            arguments = ArgumentRouter.ChooseTimeRouter.args
        ) {
            ChooseTimeScreen(
                dismissAddIngestionScreens = navController::dismissAddIngestionScreens,
            )
        }
    }
}
