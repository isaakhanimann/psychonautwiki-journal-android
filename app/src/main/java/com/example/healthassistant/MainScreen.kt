package com.example.healthassistant

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.healthassistant.presentation.home.NoteScreen
import com.example.healthassistant.presentation.search.SearchScreen
import com.example.healthassistant.presentation.search.SubstanceScreen

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
            composable(Screen.Home.route) { NoteScreen() }
            composable(Screen.Search.route) { SearchScreen(navController) }
            composable(
                Screen.Search.route + "/" + "{$SUBSTANCE_NAME}",
                arguments = listOf(navArgument(SUBSTANCE_NAME) { type = NavType.StringType })
            ) {
                SubstanceScreen(navHostController = navController)
            }
            composable(Screen.Stats.route) { Stats() }
            composable(Screen.Settings.route) { Settings() }
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
