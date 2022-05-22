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
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.ui.home.AddExperienceScreen
import com.example.healthassistant.ui.home.HomeScreen
import com.example.healthassistant.ui.home.experience.ExperienceScreen
import com.example.healthassistant.ui.home.experience.addingestion.color.ChooseColorScreen
import com.example.healthassistant.ui.home.experience.addingestion.dose.ChooseDoseScreen
import com.example.healthassistant.ui.home.experience.addingestion.interactions.CheckInteractionsScreen
import com.example.healthassistant.ui.home.experience.addingestion.route.ChooseRouteScreen
import com.example.healthassistant.ui.home.experience.addingestion.search.AddIngestionSearchScreen
import com.example.healthassistant.ui.home.experience.addingestion.time.ChooseTimeScreen
import com.example.healthassistant.ui.home.experience.edit.EditExperienceScreen
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
                    navigateToExperienceFromAddExperience = {
                        navController.navigateToExperienceFromAddExperience(it)
                    }
                )
            }
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
                        navController.navigateToAddIngestionSearch(experienceId)
                    },
                    navigateToEditExperienceScreen = {
                        navController.navigateToEditExperience(experienceId)
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
                    val experienceId = backStackEntry.arguments!!.getInt(EXPERIENCE_ID_KEY)
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
                }
            )
        }
        composable(
            ArgumentRouter.ChooseRouteRouter.route,
            arguments = ArgumentRouter.ChooseRouteRouter.args
        ) { backStackEntry ->
            ChooseRouteScreen(
                navigateToChooseDose = { administrationRoute ->
                    val args = backStackEntry.arguments!!
                    val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
                    val experienceId = args.getString(EXPERIENCE_ID_KEY)?.toIntOrNull()
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
        ) { backStackEntry ->
            ChooseDoseScreen(
                navigateToChooseColor = { units, isEstimate, dose ->
                    val args = backStackEntry.arguments!!
                    val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
                    val experienceId = args.getString(EXPERIENCE_ID_KEY)?.toIntOrNull()
                    val route =
                        AdministrationRoute.valueOf(args.getString(ADMINISTRATION_ROUTE_KEY)!!)
                    navController.navigateToChooseColor(
                        substanceName = substanceName,
                        administrationRoute = route,
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
        ) { backStackEntry ->
            ChooseColorScreen(
                navigateToChooseTime = { color ->
                    val args = backStackEntry.arguments!!
                    val substanceName = args.getString(SUBSTANCE_NAME_KEY)!!
                    val experienceId = args.getString(EXPERIENCE_ID_KEY)?.toIntOrNull()
                    val route =
                        AdministrationRoute.valueOf(args.getString(ADMINISTRATION_ROUTE_KEY)!!)
                    val units = args.getString(UNITS_KEY)?.let {
                        if (it == "null") {
                            null
                        } else {
                            it
                        }
                    }
                    val isEstimate = args.getBoolean(IS_ESTIMATE_KEY)
                    val dose = args.getString(DOSE_KEY)?.toDoubleOrNull()
                    navController.navigateToChooseTime(
                        substanceName = substanceName,
                        administrationRoute = route,
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
