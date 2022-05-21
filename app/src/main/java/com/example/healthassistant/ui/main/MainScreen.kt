package com.example.healthassistant.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.*
import com.example.healthassistant.ui.home.AddExperienceScreen
import com.example.healthassistant.ui.home.HomeScreen
import com.example.healthassistant.ui.home.experience.ExperienceScreen
import com.example.healthassistant.ui.home.experience.addingestion.color.ChooseColorScreen
import com.example.healthassistant.ui.home.experience.addingestion.dose.ChooseDoseScreen
import com.example.healthassistant.ui.home.experience.addingestion.interactions.CheckInteractionsScreen
import com.example.healthassistant.ui.home.experience.addingestion.route.ChooseRouteScreen
import com.example.healthassistant.ui.home.experience.addingestion.search.AddIngestionSearchScreen
import com.example.healthassistant.ui.home.experience.addingestion.time.ChooseTimeScreen
import com.example.healthassistant.ui.main.routers.*
import com.example.healthassistant.ui.search.SearchScreen
import com.example.healthassistant.ui.search.substance.SubstanceScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    when (navBackStackEntry?.destination?.route) {
        TabRouter.Home.route, TabRouter.Search.route, TabRouter.Stats.route, TabRouter.Settings.route -> {
            bottomBarState.value = true
        }
        else ->
            bottomBarState.value = false
    }
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
            startDestination = TabRouter.Home.route,
            Modifier.padding(innerPadding)
        ) {
            composable(TabRouter.Home.route) {
                HomeScreen(
                    navigateToAddExperience = navController::navigateToAddExperience,
                    navigateToExperiencePopNothing = {
                        navController.navigateToExperiencePopNothing(experienceId = it)
                    }
                )
            }
            composable(NoArgumentRouter.AddExperienceRouter.route) {
                AddExperienceScreen(
                    navigateBack = navController::popBackStack,
                    navigateToExperienceFromAddExperience = {
                        navController.navigateToExperienceFromAddExperience(it)
                    }
                )
            }
            composable(
                ArgumentRouter.ExperienceRouter.route,
                arguments = ArgumentRouter.ExperienceRouter.args
            ) {
                ExperienceScreen(
                    navigateBack = navController::popBackStack,
                    navigateToAddIngestionSearch = {
                        navController.navigateToAddIngestionSearch(experienceId = it)
                    }
                )
            }
            addIngestionGraph(navController = navController)
            composable(TabRouter.Search.route) {
                SearchScreen(onSubstanceTap = {
                    navController.navigateToSubstanceScreen(substanceName = it.name)
                })
            }
            composable(
                ArgumentRouter.SubstanceRouter.route,
                arguments = ArgumentRouter.SubstanceRouter.args
            ) {
                SubstanceScreen(
                    navigateBack = navController::popBackStack,
                    navigateToAddIngestion = {
                        navController.navigateToAddIngestion(
                            substanceName = it,
                            experienceId = null
                        )
                    }
                )
            }
            composable(TabRouter.Stats.route) { Stats() }
            composable(TabRouter.Settings.route) { Settings() }
        }
    }
}

fun NavGraphBuilder.addIngestionGraph(navController: NavController) {
    navigation(
        startDestination = ArgumentRouter.CheckInteractionsRouter.route,
        route = "is not used"
    ) {
        composable(
            ArgumentRouter.SearchRouter.route,
            arguments = ArgumentRouter.SearchRouter.args
        ) { backStackEntry ->
            AddIngestionSearchScreen(
                navigateToAddIngestionScreens = {
                    val experienceId = backStackEntry.arguments?.getInt(EXPERIENCE_ID_KEY)
                    navController.navigateToAddIngestion(
                        substanceName = it,
                        experienceId = experienceId
                    )
                }
            )
        }
        composable(
            ArgumentRouter.CheckInteractionsRouter.route,
            arguments = ArgumentRouter.CheckInteractionsRouter.args
        ) {
            CheckInteractionsScreen(
                navigateToChooseRouteScreen = { substanceName, experienceId ->
                    navController.navigateToChooseRoute(
                        substanceName = substanceName,
                        experienceId = experienceId
                    )
                }
            )
        }
        composable(
            ArgumentRouter.ChooseRouteRouter.route,
            arguments = ArgumentRouter.ChooseRouteRouter.args
        ) {
            ChooseRouteScreen(
                navigateToChooseDose = { substanceName, administrationRoute, experienceId ->
                    navController.navigateToChooseDose(
                        substanceName = substanceName,
                        administrationRoute = administrationRoute,
                        experienceId = experienceId
                    )
                }
            )
        }
        composable(
            ArgumentRouter.ChooseDoseRouter.route,
            arguments = ArgumentRouter.ChooseDoseRouter.args
        ) {
            ChooseDoseScreen(
                navigateToChooseColor = { substanceName, administrationRoute, units, isEstimate, dose, experienceId ->
                    navController.navigateToChooseColor(
                        substanceName = substanceName,
                        administrationRoute = administrationRoute,
                        units = units,
                        isEstimate = isEstimate,
                        dose = dose,
                        experienceId = experienceId
                    )
                }
            )
        }
        composable(
            ArgumentRouter.ChooseColorRouter.route,
            arguments = ArgumentRouter.ChooseColorRouter.args
        ) {
            ChooseColorScreen(
                navigateToChooseTime = { substanceName, administrationRoute, units, isEstimate, color, dose, experienceId ->
                    navController.navigateToChooseTime(
                        substanceName = substanceName,
                        administrationRoute = administrationRoute,
                        units = units,
                        isEstimate = isEstimate,
                        color = color,
                        dose = dose,
                        experienceId = experienceId
                    )
                }
            )
        }
        composable(
            ArgumentRouter.ChooseTimeRouter.route,
            arguments = ArgumentRouter.ChooseTimeRouter.args
        ) {
            ChooseTimeScreen(
                navigateToExperiencePopupToExperienceScreen = navController::navigateToExperiencePopupToExperienceScreen,
                navigateToExperiencePopupToSubstanceScreen = navController::navigateToExperiencePopupToSubstanceScreen
            )
        }
    }
}

@Composable
fun Stats() {
    Text(text = "Stats")
}

@Composable
fun Settings() {
    Text(text = "Settings")
}
