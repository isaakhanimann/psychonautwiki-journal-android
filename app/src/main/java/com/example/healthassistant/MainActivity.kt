package com.example.healthassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.healthassistant.home.NoteScreenWithModel
import com.example.healthassistant.home.NoteViewModel
import com.example.healthassistant.search.Search
import com.example.healthassistant.search.SubstanceScreen
import com.example.healthassistant.ui.theme.HealthAssistantTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthAssistantTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

val items = listOf(
    Screen.Home,
    Screen.Search,
    Screen.Stats,
    Screen.Settings
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id)
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        val noteViewModel = viewModel<NoteViewModel>()
        NavHost(
            navController,
            startDestination = Screen.Home.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { NoteScreenWithModel(noteViewModel = noteViewModel) }
            composable(Screen.Search.route) { Search(navController) }
            composable(
                Screen.Search.route + "/" + "{substanceName}",
                arguments = listOf(navArgument("substanceName") { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("substanceName")?.let { countryName ->
                    SubstanceScreen(navHostController = navController, substanceName = countryName)
                }
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


sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Home : Screen("home", R.string.home, Icons.Filled.Home)
    object Search : Screen("search", R.string.search, Icons.Filled.Search)
    object Stats : Screen("stats", R.string.stats, Icons.Filled.Info)
    object Settings : Screen("settings", R.string.settings, Icons.Filled.Settings)
}

