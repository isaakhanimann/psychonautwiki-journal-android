package com.isaakhanimann.journal.ui.main

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.isaakhanimann.journal.ui.main.navigation.routers.TabRouter
import com.isaakhanimann.journal.ui.utils.keyboard.isKeyboardOpen

@Composable
fun JournalBottomNavigationBar(navController: NavHostController) {
    val isShowingBottomBar = isKeyboardOpen().value.not()
    if (isShowingBottomBar) {
        NavigationBar {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val tabs = listOf(
                TabRouter.Statistics,
                TabRouter.Journal,
                TabRouter.Substances,
                TabRouter.SaferUse,
                TabRouter.Settings
            )
            tabs.forEach { tab ->
                val isSelected =
                    currentDestination?.hierarchy?.any { it.route == tab.route } == true
                NavigationBarItem(
                    icon = { Icon(tab.icon, contentDescription = null) },
                    label = { Text(stringResource(tab.resourceId)) },
                    selected = isSelected,
                    onClick = {
                        if (isSelected) {
                            val isAlreadyOnTopOfTab =
                                tabs.any { it.childRoute == currentDestination?.route }
                            if (!isAlreadyOnTopOfTab) {
                                navController.popBackStack()
                            }
                        } else {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}