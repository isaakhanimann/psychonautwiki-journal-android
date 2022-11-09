@file:OptIn(ExperimentalAnimationApi::class)

package com.isaakhanimann.journal.ui.main

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.AcceptConditionsScreen
import com.isaakhanimann.journal.ui.addingestion.dose.ChooseDoseScreen
import com.isaakhanimann.journal.ui.addingestion.dose.custom.CustomChooseDose
import com.isaakhanimann.journal.ui.addingestion.interactions.CheckInteractionsScreen
import com.isaakhanimann.journal.ui.addingestion.route.ChooseRouteScreen
import com.isaakhanimann.journal.ui.addingestion.route.CustomChooseRouteScreen
import com.isaakhanimann.journal.ui.addingestion.search.AddIngestionSearchScreen
import com.isaakhanimann.journal.ui.addingestion.time.ChooseTimeScreen
import com.isaakhanimann.journal.ui.journal.JournalScreen
import com.isaakhanimann.journal.ui.journal.experience.ExperienceScreen
import com.isaakhanimann.journal.ui.journal.experience.edit.EditExperienceScreen
import com.isaakhanimann.journal.ui.journal.experience.editingestion.EditIngestionScreen
import com.isaakhanimann.journal.ui.journal.experience.timeline.ExplainTimelineScreen
import com.isaakhanimann.journal.ui.main.routers.*
import com.isaakhanimann.journal.ui.safer.*
import com.isaakhanimann.journal.ui.search.SearchScreen
import com.isaakhanimann.journal.ui.search.custom.AddCustomSubstance
import com.isaakhanimann.journal.ui.search.custom.EditCustomSubstance
import com.isaakhanimann.journal.ui.search.substance.SaferSniffingScreen
import com.isaakhanimann.journal.ui.search.substance.SaferStimulantsScreen
import com.isaakhanimann.journal.ui.search.substance.SubstanceScreen
import com.isaakhanimann.journal.ui.search.substance.UrlScreen
import com.isaakhanimann.journal.ui.search.substance.category.CategoryScreen
import com.isaakhanimann.journal.ui.settings.FAQScreen
import com.isaakhanimann.journal.ui.settings.SettingsScreen
import com.isaakhanimann.journal.ui.settings.combinations.CombinationSettingsScreen
import com.isaakhanimann.journal.ui.stats.StatsScreen
import com.isaakhanimann.journal.ui.stats.substancecompanion.SubstanceCompanionScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    if (viewModel.isAcceptedFlow.collectAsState().value) {
        val navController = rememberAnimatedNavController()
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
                    if (isShowingBottomBar) {
                        if (currentExperience != null) {
                            CurrentExperienceRow(
                                experienceWithIngestionsAndCompanions = currentExperience,
                                navigateToExperienceScreen = {
                                    navController.navigateToExperiencePopNothing(experienceId = currentExperience.experience.id)
                                }
                            )
                        }
                        val items = listOf(
                            TabRouter.Journal,
                            TabRouter.Statistics,
                            TabRouter.Search,
                            TabRouter.SaferUse
                        )
                        NavigationBar {
                            val currentRoute = navBackStackEntry?.destination?.route
                            items.forEach { item ->
                                NavigationBarItem(
                                    icon = { Icon(item.icon, contentDescription = null) },
                                    label = { Text(stringResource(item.resourceId)) },
                                    selected = currentRoute == item.route,
                                    onClick = {
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            AnimatedNavHost(
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
    horizontalTransitionComposable(NoArgumentRouter.StatsRouter.route) {
        StatsScreen(
            navigateToSubstanceCompanion = {
                navController.navigateToSubstanceCompanionScreen(it)
            }
        )
    }
    horizontalTransitionComposable(NoArgumentRouter.FAQRouter.route) { FAQScreen() }
    horizontalTransitionComposable(NoArgumentRouter.CombinationSettingsRouter.route) { CombinationSettingsScreen() }
    horizontalTransitionComposable(NoArgumentRouter.ExplainTimelineRouter.route) { ExplainTimelineScreen() }
    horizontalTransitionComposable(NoArgumentRouter.SaferHallucinogens.route) { SaferHallucinogensScreen() }
    horizontalTransitionComposable(NoArgumentRouter.SaferStimulants.route) { SaferStimulantsScreen() }
    horizontalTransitionComposable(NoArgumentRouter.SaferSniffing.route) { SaferSniffingScreen() }
    horizontalTransitionComposable(NoArgumentRouter.SettingsRouter.route) {
        SettingsScreen(
            navigateToFAQ = navController::navigateToFAQ,
            navigateToComboSettings = navController::navigateToComboSettings
        )
    }
    horizontalTransitionComposable(NoArgumentRouter.DosageExplanationRouter.route) { DoseExplanationScreen() }
    horizontalTransitionComposable(NoArgumentRouter.AdministrationRouteExplanationRouter.route) {
        RouteExplanationScreen(
            navigateToURL = navController::navigateToURLScreen
        )
    }
    horizontalTransitionComposable(NoArgumentRouter.DrugTestingRouter.route) { DrugTestingScreen() }
    horizontalTransitionComposable(NoArgumentRouter.DosageGuideRouter.route) {
        DoseGuideScreen(
            navigateToDoseClassification = navController::navigateToDosageExplanationScreen,
            navigateToVolumetricDosing = navController::navigateToVolumetricDosingScreen,
            navigateToPWDosageArticle = {
                navController.navigateToURLScreen(url = "https://psychonautwiki.org/wiki/Dosage")
            }
        )
    }
    horizontalTransitionComposable(NoArgumentRouter.VolumetricDosingRouter.route) {
        VolumetricDosingScreen(
            navigateToVolumetricLiquidDosingArticle = { navController.navigateToURLScreen("https://psychonautwiki.org/wiki/Volumetric_liquid_dosing") })
    }
    horizontalTransitionComposable(NoArgumentRouter.AddCustomRouter.route) {
        AddCustomSubstance(
            navigateBack = navController::popBackStack
        )
    }
    horizontalTransitionComposable(NoArgumentRouter.AddIngestionRouter.route) {
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
            },
            navigateToDose = { substanceName, route ->
                navController.navigateToChooseDose(substanceName, route)
            },
            navigateToChooseRoute = { substanceName ->
                navController.navigateToChooseRoute(substanceName)
            }
        )
    }
}

fun NavGraphBuilder.argumentGraph(navController: NavController) {
    horizontalTransitionComposable(
        ArgumentRouter.EditExperienceRouter.route,
        arguments = ArgumentRouter.EditExperienceRouter.args
    ) {
        EditExperienceScreen(navigateBack = navController::popBackStack)
    }
    horizontalTransitionComposable(
        ArgumentRouter.CategoryRouter.route,
        arguments = ArgumentRouter.CategoryRouter.args
    ) {
        CategoryScreen(navigateToURL = navController::navigateToURLScreen)
    }
    horizontalTransitionComposable(
        ArgumentRouter.EditCustomRouter.route,
        arguments = ArgumentRouter.EditCustomRouter.args
    ) {
        EditCustomSubstance(navigateBack = navController::popBackStack)
    }
    horizontalTransitionComposable(
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
    horizontalTransitionComposable(
        ArgumentRouter.IngestionRouter.route,
        arguments = ArgumentRouter.IngestionRouter.args
    ) {
        EditIngestionScreen(navigateBack = navController::popBackStack)
    }
    horizontalTransitionComposable(
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
    horizontalTransitionComposable(
        ArgumentRouter.URLRouter.route,
        arguments = ArgumentRouter.URLRouter.args
    ) { backStackEntry ->
        val args = backStackEntry.arguments!!
        val url = args.getString(URL_KEY)!!
        UrlScreen(url = url)
    }
    horizontalTransitionComposable(
        ArgumentRouter.SubstanceCompanionRouter.route,
        arguments = ArgumentRouter.SubstanceCompanionRouter.args
    ) {
        SubstanceCompanionScreen()
    }
}

fun NavGraphBuilder.tabGraph(navController: NavController) {
    tabScreenComposable(
        route = TabRouter.Journal.route,
    ) {
        JournalScreen(
            navigateToExperiencePopNothing = {
                navController.navigateToExperiencePopNothing(experienceId = it)
            },
            navigateToAddIngestion = navController::navigateToAddIngestion
        )
    }
    tabScreenComposable(
        route = TabRouter.Statistics.route,
    ) {
        StatsScreen(
            navigateToSubstanceCompanion = {
                navController.navigateToSubstanceCompanionScreen(substanceName = it)
            }
        )
    }
    tabScreenComposable(
        route = TabRouter.Search.route,
    ) {
        SearchScreen(
            onSubstanceTap = {
                navController.navigateToSubstanceScreen(substanceName = it)
            },
            onCustomSubstanceTap = navController::navigateToEditCustomSubstance,
            navigateToAddCustomSubstanceScreen = navController::navigateToAddCustom,
        )
    }
    tabScreenComposable(
        route = TabRouter.SaferUse.route
    ) {
        SaferUseScreen(
            navigateToDrugTestingScreen = navController::navigateToDrugTestingScreen,
            navigateToSaferHallucinogensScreen = navController::navigateToSaferHallucinogens,
            navigateToVolumetricDosingScreen = navController::navigateToVolumetricDosingScreen,
            navigateToDosageGuideScreen = navController::navigateToDosageGuideScreen,
            navigateToDosageClassificationScreen = navController::navigateToDosageExplanationScreen,
            navigateToRouteExplanationScreen = navController::navigateToAdministrationRouteExplanationScreen,
            navigateToSettings = navController::navigateToSettings,
            navigateToURL = navController::navigateToURLScreen
        )
    }
}

fun NavGraphBuilder.addIngestionGraph(navController: NavController) {
    navigation(
        startDestination = ArgumentRouter.CheckInteractionsRouterSkipNothing.route,
        route = "is not used"
    ) {
        horizontalTransitionComposable(
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
        horizontalTransitionComposable(
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
        horizontalTransitionComposable(
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
        horizontalTransitionComposable(
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
        horizontalTransitionComposable(
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
        horizontalTransitionComposable(
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
        horizontalTransitionComposable(
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
        horizontalTransitionComposable(
            ArgumentRouter.ChooseTimeRouter.route,
            arguments = ArgumentRouter.ChooseTimeRouter.args
        ) {
            ChooseTimeScreen(
                dismissAddIngestionScreens = navController::dismissAddIngestionScreens,
            )
        }
    }
}


fun NavGraphBuilder.tabScreenComposable(
    route: String,
    content: @Composable (AnimatedVisibilityScope.(NavBackStackEntry) -> Unit),
) {
    val tabRoutes = setOf(
        TabRouter.Journal.route,
        TabRouter.Statistics.route,
        TabRouter.Search.route,
        TabRouter.SaferUse.route
    )
    val tabSwitchTimeInMs = 200
    composable(
        route = route,
        enterTransition = { fadeIn(animationSpec = tween(tabSwitchTimeInMs)) },
        exitTransition = {
            if (tabRoutes.contains(targetState.destination.route)) {
                fadeOut(animationSpec = tween(tabSwitchTimeInMs))
            } else {
                slideOutHorizontally(
                    targetOffsetX = { -300 },
                    animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS)
                ) + fadeOut(animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS))
            }
        },
        popEnterTransition = {
            if (tabRoutes.contains(targetState.destination.route)) {
                fadeIn(animationSpec = tween(tabSwitchTimeInMs))
            } else {
                slideInHorizontally(
                    initialOffsetX = { -300 },
                    animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS)
                ) + fadeIn(animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS))
            }
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(tabSwitchTimeInMs))
        },
        content = content
    )
}

const val WITHIN_TAB_TRANSITION_TIME_IN_MS = 300

fun NavGraphBuilder.horizontalTransitionComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable (AnimatedVisibilityScope.(NavBackStackEntry) -> Unit)
) {
    composable(
        route = route,
        arguments = arguments,
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -300 },
                animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS)
            ) + fadeOut(animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -300 },
                animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS)
            ) + fadeIn(animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS))
        },
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { 300 },
                animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS)
            ) + fadeIn(animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { 300 },
                animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS)
            ) + fadeOut(animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS))
        },
        content = content
    )
}


