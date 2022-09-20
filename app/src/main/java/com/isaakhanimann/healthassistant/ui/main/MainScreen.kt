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
import com.isaakhanimann.healthassistant.ui.addingestion.AddIngestionSearchScreen
import com.isaakhanimann.healthassistant.ui.addingestion.dose.ChooseDoseScreen
import com.isaakhanimann.healthassistant.ui.addingestion.dose.DoseGuideScreen
import com.isaakhanimann.healthassistant.ui.addingestion.interactions.CheckInteractionsScreen
import com.isaakhanimann.healthassistant.ui.addingestion.route.ChooseRouteScreen
import com.isaakhanimann.healthassistant.ui.addingestion.route.RouteExplanationScreen
import com.isaakhanimann.healthassistant.ui.addingestion.time.ChooseTimeScreen
import com.isaakhanimann.healthassistant.ui.experiences.ExperiencesScreen
import com.isaakhanimann.healthassistant.ui.experiences.addExperience.AddExperienceScreen
import com.isaakhanimann.healthassistant.ui.experiences.experience.ExperienceScreen
import com.isaakhanimann.healthassistant.ui.experiences.experience.edit.EditExperienceScreen
import com.isaakhanimann.healthassistant.ui.ingestions.IngestionsScreen
import com.isaakhanimann.healthassistant.ui.ingestions.ingestion.OneIngestionScreen
import com.isaakhanimann.healthassistant.ui.ingestions.ingestion.edit.membership.EditIngestionMembershipScreen
import com.isaakhanimann.healthassistant.ui.ingestions.ingestion.edit.note.EditIngestionNoteScreen
import com.isaakhanimann.healthassistant.ui.ingestions.stats.StatsScreen
import com.isaakhanimann.healthassistant.ui.ingestions.stats.substancecompanion.SubstanceCompanionScreen
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

@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModel = hiltViewModel()
) {
    if (mainScreenViewModel.isAcceptedFlow.collectAsState().value) {
        val navController = rememberNavController()
        val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val isShowingBottomBar = navBackStackEntry?.destination?.route in setOf(
            TabRouter.Experiences.route,
            TabRouter.Ingestions.route,
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
    composable(NoArgumentRouter.AddExperienceRouter.route) {
        AddExperienceScreen(
            navigateToExperienceFromAddExperience = {
                navController.navigateToExperienceFromAddExperience(it)
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
                navController.navigateToAddIngestion(experienceId = experienceId)
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
        val ingestionId = it.arguments!!.getInt(INGESTION_ID_KEY)
        OneIngestionScreen(
            navigateToEditNote = {
                navController.navigateToEditIngestionNote(ingestionId)
            },
            navigateToEditMembership = {
                navController.navigateToEditIngestionMembership(ingestionId)
            },
            navigateToSubstance = { substanceName ->
                navController.navigateToSubstanceScreen(substanceName)
            },
            navigateBack = navController::popBackStack,
            navigateToDoseExplanationScreen = navController::navigateToDosageExplanationScreen
        )
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
    composable(
        ArgumentRouter.EditIngestionNoteRouter.route,
        arguments = ArgumentRouter.EditIngestionNoteRouter.args
    ) {
        EditIngestionNoteScreen(navigateBack = navController::popBackStack)
    }
    composable(
        ArgumentRouter.EditIngestionMembershipRouter.route,
        arguments = ArgumentRouter.EditIngestionMembershipRouter.args
    ) {
        EditIngestionMembershipScreen(navigateBack = navController::popBackStack)
    }
}

fun NavGraphBuilder.tabGraph(navController: NavController) {
    composable(TabRouter.Experiences.route) {
        ExperiencesScreen(
            navigateToAddExperience = navController::navigateToAddExperience,
            navigateToExperiencePopNothing = {
                navController.navigateToExperiencePopNothing(experienceId = it)
            },
        )
    }
    composable(TabRouter.Ingestions.route) {
        IngestionsScreen(
            navigateToIngestion = {
                navController.navigateToIngestion(ingestionId = it)
            },
            navigateToAddIngestion = {
                navController.navigateToAddIngestion(experienceId = null)
            },
            navigateToStatsScreen = navController::navigateToStats
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
        startDestination = ArgumentRouter.CheckInteractionsRouter.route,
        route = "is not used"
    ) {
        composable(
            ArgumentRouter.AddIngestionRouter.route,
            arguments = ArgumentRouter.AddIngestionRouter.args
        ) { backStackEntry ->
            AddIngestionSearchScreen(
                navigateToCheckInteractions = {
                    val experienceId =
                        backStackEntry.arguments?.getString(EXPERIENCE_ID_KEY)?.toIntOrNull()
                    navController.navigateToCheckInteractions(
                        substanceName = it,
                        experienceId = experienceId
                    )
                }
            )
        }
        composable(
            ArgumentRouter.CheckInteractionsRouter.route,
            arguments = ArgumentRouter.CheckInteractionsRouter.args
        ) { backStackEntry ->
            CheckInteractionsScreen(
                navigateToChooseRouteScreen = {
                    val args = backStackEntry.arguments!!
                    val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
                    val experienceId = args.getString(EXPERIENCE_ID_KEY)?.toIntOrNull()
                    navController.navigateToChooseRoute(
                        substanceName = substanceName,
                        experienceId = experienceId
                    )
                },
                navigateToSaferHallucinogensScreen = navController::navigateToSaferHallucinogens,
                navigateToSaferStimulantsScreen = navController::navigateToSaferStimulants
            )
        }
        composable(
            ArgumentRouter.ChooseRouteRouter.route,
            arguments = ArgumentRouter.ChooseRouteRouter.args
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
            val experienceId = args.getString(EXPERIENCE_ID_KEY)?.toIntOrNull()
            ChooseRouteScreen(
                navigateToChooseDose = { administrationRoute ->
                    navController.navigateToChooseDose(
                        substanceName = substanceName,
                        administrationRoute = administrationRoute,
                        experienceId = experienceId
                    )
                },
                navigateToRouteExplanationScreen = navController::navigateToAdministrationRouteExplanationScreen,
                navigateToChooseTimeAndMaybeColor = { administrationRoute, dose, units, isEstimate ->
                    navController.navigateToChooseTimeAndMaybeColor(
                        substanceName = substanceName,
                        administrationRoute = administrationRoute,
                        experienceId = experienceId,
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
                    val experienceId = args.getString(EXPERIENCE_ID_KEY)?.toIntOrNull()
                    val route =
                        AdministrationRoute.valueOf(args.getString(ADMINISTRATION_ROUTE_KEY)!!)
                    navController.navigateToChooseTimeAndMaybeColor(
                        substanceName = substanceName,
                        administrationRoute = route,
                        units = units,
                        isEstimate = isEstimate,
                        dose = dose,
                        experienceId = experienceId
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
