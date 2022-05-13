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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.healthassistant.EXPERIENCE_ID
import com.example.healthassistant.SUBSTANCE_NAME
import com.example.healthassistant.ui.home.HomeScreen
import com.example.healthassistant.ui.home.experience.ExperienceScreen
import com.example.healthassistant.ui.search.SearchScreen
import com.example.healthassistant.ui.search.substance.SubstanceScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    when (navBackStackEntry?.destination?.route) {
        Screen.Home.route, Screen.Search.route, Screen.Stats.route, Screen.Settings.route -> {
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
            startDestination = Screen.Home.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController = navController) }
            composable(
                Screen.Home.route + "/" + "{$EXPERIENCE_ID}",
                arguments = listOf(navArgument(EXPERIENCE_ID) { type = NavType.IntType })
            ) {
                ExperienceScreen(navController = navController)
            }
            composable(Screen.Search.route) { SearchScreen(navController) }
            composable(
                Screen.Search.route + "/" + "{$SUBSTANCE_NAME}",
                arguments = listOf(navArgument(SUBSTANCE_NAME) { type = NavType.StringType })
            ) {
                SubstanceScreen(navController = navController)
            }
            composable(Screen.Stats.route) { Stats() }
            composable(Screen.Settings.route) { Settings() }
        }
    }
}

fun NavController.navigateToExperienceScreen(experienceId: Int) {
    navigate(Screen.Home.route + "/" + experienceId)
}

fun NavController.navigateToSubstanceScreen(substanceName: String) {
    navigate(Screen.Search.route + "/" + substanceName)
}

@Composable
fun Stats() {
    Text(text = "Stats")
}

@Composable
fun Settings() {
    Text(text = "Settings")
}
