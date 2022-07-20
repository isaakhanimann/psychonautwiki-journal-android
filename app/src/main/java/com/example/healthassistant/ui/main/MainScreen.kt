package com.example.healthassistant.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.*
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.ui.addingestion.dose.ChooseDoseScreen
import com.example.healthassistant.ui.addingestion.dose.DoseGuideScreen
import com.example.healthassistant.ui.addingestion.dose.VolumetricDosingScreen
import com.example.healthassistant.ui.addingestion.interactions.CheckInteractionsScreen
import com.example.healthassistant.ui.addingestion.route.ChooseRouteScreen
import com.example.healthassistant.ui.addingestion.route.RouteExplanationScreen
import com.example.healthassistant.ui.addingestion.search.AddIngestionSearchScreen
import com.example.healthassistant.ui.addingestion.search.DrugTestingScreen
import com.example.healthassistant.ui.addingestion.time.ChooseTimeScreen
import com.example.healthassistant.ui.experiences.ExperiencesScreen
import com.example.healthassistant.ui.experiences.addExperience.AddExperienceScreen
import com.example.healthassistant.ui.experiences.experience.ExperienceScreen
import com.example.healthassistant.ui.experiences.experience.edit.EditExperienceScreen
import com.example.healthassistant.ui.ingestions.IngestionsScreen
import com.example.healthassistant.ui.ingestions.ingestion.OneIngestionScreen
import com.example.healthassistant.ui.ingestions.ingestion.edit.membership.EditIngestionMembershipScreen
import com.example.healthassistant.ui.ingestions.ingestion.edit.note.EditIngestionNoteScreen
import com.example.healthassistant.ui.main.routers.*
import com.example.healthassistant.ui.search.SearchScreen
import com.example.healthassistant.ui.search.substance.SaferHallucinogensScreen
import com.example.healthassistant.ui.search.substance.SaferSniffingScreen
import com.example.healthassistant.ui.search.substance.SaferStimulantsScreen
import com.example.healthassistant.ui.search.substance.SubstanceScreen
import com.example.healthassistant.ui.search.substance.roa.DoseExplanationScreen
import com.example.healthassistant.ui.search.substance.roa.DurationExplanationScreen
import com.example.healthassistant.ui.settings.SettingsScreen
import com.example.healthassistant.ui.settings.faq.FAQScreen
import com.example.healthassistant.ui.stats.ProfileScreen
import com.example.healthassistant.ui.stats.substancecompanion.SubstanceCompanionScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isShowingBottomBar = navBackStackEntry?.destination?.route in setOf(
        TabRouter.Experiences.route,
        TabRouter.Ingestions.route,
        TabRouter.Search.route,
        TabRouter.Stats.route
    )
    bottomBarState.value = isShowingBottomBar
    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                bottomBarState = bottomBarState
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        floatingActionButton = {
            if (isShowingBottomBar) {
                FloatingActionButton(
                    shape = CircleShape,
                    onClick = {
                        navController.navigateToAddIngestion(null)
                    },
                    contentColor = Color.White
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add icon")
                }
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
}

fun NavGraphBuilder.noArgumentGraph(navController: NavController) {
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
            navigateToSaferHallucinogensScreen = navController::navigateToSaferHallucinogensScreen,
            navigateToSaferSniffingScreen = navController::navigateToSaferSniffingScreen,
            navigateToSaferStimulantsScreen = navController::navigateToSaferStimulantsScreen,
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
            }
        )
    }
    composable(TabRouter.Search.route) {
        SearchScreen(
            onSubstanceTap = {
                navController.navigateToSubstanceScreen(substanceName = it.name)
            }
        )
    }
    composable(TabRouter.Stats.route) {
        ProfileScreen(
            navigateToSettings = navController::navigateToSettings,
            navigateToSubstanceCompanion = {
                navController.navigateToSubstanceCompanionScreen(it)
            }
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
                },
                navigateToDrugTestingScreen = navController::navigateToDrugTestingScreen
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
                navigateToSaferHallucinogensScreen = navController::navigateToSaferHallucinogensScreen,
                navigateToSaferStimulantsScreen = navController::navigateToSaferStimulantsScreen
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
                navigateToSaferSniffingScreen = navController::navigateToSaferSniffingScreen
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
