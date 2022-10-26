package com.isaakhanimann.healthassistant.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
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
import com.isaakhanimann.healthassistant.ui.addingestion.dose.custom.CustomChooseDose
import com.isaakhanimann.healthassistant.ui.addingestion.interactions.CheckInteractionsScreen
import com.isaakhanimann.healthassistant.ui.addingestion.route.ChooseRouteScreen
import com.isaakhanimann.healthassistant.ui.addingestion.route.CustomChooseRouteScreen
import com.isaakhanimann.healthassistant.ui.addingestion.search.AddIngestionSearchScreen
import com.isaakhanimann.healthassistant.ui.addingestion.time.ChooseTimeScreen
import com.isaakhanimann.healthassistant.ui.journal.JournalScreen
import com.isaakhanimann.healthassistant.ui.journal.experience.ExperienceScreen
import com.isaakhanimann.healthassistant.ui.journal.experience.edit.EditExperienceScreen
import com.isaakhanimann.healthassistant.ui.journal.experience.editingestion.EditIngestionScreen
import com.isaakhanimann.healthassistant.ui.journal.experience.timeline.ExplainTimelineScreen
import com.isaakhanimann.healthassistant.ui.main.routers.*
import com.isaakhanimann.healthassistant.ui.safer.*
import com.isaakhanimann.healthassistant.ui.search.SearchScreen
import com.isaakhanimann.healthassistant.ui.search.custom.AddCustomSubstance
import com.isaakhanimann.healthassistant.ui.search.custom.EditCustomSubstance
import com.isaakhanimann.healthassistant.ui.search.substance.SaferSniffingScreen
import com.isaakhanimann.healthassistant.ui.search.substance.SaferStimulantsScreen
import com.isaakhanimann.healthassistant.ui.search.substance.SubstanceScreen
import com.isaakhanimann.healthassistant.ui.search.substance.category.CategoryScreen
import com.isaakhanimann.healthassistant.ui.settings.SettingsScreen
import com.isaakhanimann.healthassistant.ui.settings.faq.FAQScreen
import com.isaakhanimann.healthassistant.ui.stats.StatsScreen
import com.isaakhanimann.healthassistant.ui.stats.substancecompanion.SubstanceCompanionScreen

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    if (viewModel.isAcceptedFlow.collectAsState().value) {
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
                Column {
                    val currentExperience = viewModel.currentExperienceFlow.collectAsState().value
                    if (currentExperience != null && isShowingBottomBar) {
                        Divider()
                        CurrentExperienceRow(
                            experienceWithIngestionsAndCompanions = currentExperience,
                            navigateToExperienceScreen = {
                                navController.navigateToExperiencePopNothing(experienceId = currentExperience.experience.id)
                            }
                        )
                    }
                    BottomBar(
                        navController = navController,
                        bottomBarState = bottomBarState
                    )
                }
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
        AcceptConditionsScreen(onTapAccept = viewModel::accept)
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
    composable(NoArgumentRouter.ExplainTimelineRouter.route) { ExplainTimelineScreen() }
    composable(NoArgumentRouter.SaferHallucinogens.route) { SaferHallucinogensScreen() }
    composable(NoArgumentRouter.SaferStimulants.route) { SaferStimulantsScreen() }
    composable(NoArgumentRouter.SaferSniffing.route) { SaferSniffingScreen() }
    composable(NoArgumentRouter.SettingsRouter.route) { SettingsScreen(navigateToFAQ = navController::navigateToFAQ) }
    composable(NoArgumentRouter.DosageExplanationRouter.route) { DoseExplanationScreen() }
    composable(NoArgumentRouter.AdministrationRouteExplanationRouter.route) { RouteExplanationScreen() }
    composable(NoArgumentRouter.DrugTestingRouter.route) { DrugTestingScreen() }
    composable(NoArgumentRouter.DosageGuideRouter.route) {
        DoseGuideScreen(
            navigateToDoseClassification = navController::navigateToDosageExplanationScreen,
            navigateToVolumetricDosing = navController::navigateToVolumetricDosingScreen
        )
    }
    composable(NoArgumentRouter.VolumetricDosingRouter.route) { VolumetricDosingScreen() }
    composable(NoArgumentRouter.AddCustomRouter.route) { AddCustomSubstance(navigateBack = navController::popBackStack) }
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
            },
            navigateToCustomSubstanceChooseRoute = navController::navigateToChooseCustomRoute,
            navigateToChooseTime = { substanceName, route, dose, units, isEstimate ->
                navController.navigateToChooseTimeAndMaybeColor(
                    substanceName = substanceName,
                    administrationRoute = route,
                    units = units,
                    isEstimate = isEstimate,
                    dose = dose
                )
            },
            navigateToCustomDose = { substanceName, route ->
                navController.navigateToChooseDoseCustom(substanceName, route)
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
        ArgumentRouter.CategoryRouter.route,
        arguments = ArgumentRouter.CategoryRouter.args
    ) {
        CategoryScreen()
    }
    composable(
        ArgumentRouter.EditCustomRouter.route,
        arguments = ArgumentRouter.EditCustomRouter.args
    ) {
        EditCustomSubstance(navigateBack = navController::popBackStack)
    }
    composable(
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
            navigateToSaferHallucinogensScreen = navController::navigateToSaferHallucinogens,
            navigateToSaferStimulantsScreen = navController::navigateToSaferStimulants,
            navigateToExplainTimeline = navController::navigateToExplainTimeline,
            navigateToCategoryScreen = navController::navigateToCategoryScreen,
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
            onCustomSubstanceTap = navController::navigateToEditCustomSubstance,
            navigateToAddCustomSubstanceScreen = navController::navigateToAddCustom,
        )
    }
    composable(TabRouter.SaferUse.route) {
        SaferUseScreen(
            navigateToDrugTestingScreen = navController::navigateToDrugTestingScreen,
            navigateToSaferHallucinogensScreen = navController::navigateToSaferHallucinogens,
            navigateToVolumetricDosingScreen = navController::navigateToVolumetricDosingScreen,
            navigateToDosageGuideScreen = navController::navigateToDosageGuideScreen,
            navigateToDosageClassificationScreen = navController::navigateToDosageExplanationScreen,
            navigateToRouteExplanationScreen = navController::navigateToAdministrationRouteExplanationScreen,
            navigateToSettings = navController::navigateToSettings
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
            )
        }
        composable(
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
                }
            )
        }
        composable(
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
                navigateToSaferSniffingScreen = navController::navigateToSaferSniffing
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
