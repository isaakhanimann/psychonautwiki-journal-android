/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

@file:OptIn(ExperimentalAnimationApi::class)

package com.isaakhanimann.journal.ui.main

import androidx.compose.animation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.isaakhanimann.journal.ui.AcceptConditionsScreen
import com.isaakhanimann.journal.ui.main.navigation.*
import com.isaakhanimann.journal.ui.main.navigation.graphs.*
import com.isaakhanimann.journal.ui.main.navigation.routers.*
import com.isaakhanimann.journal.ui.safer.*
import com.isaakhanimann.journal.ui.utils.keyboard.isKeyboardOpen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    if (viewModel.isAcceptedFlow.collectAsState().value) {
        val navController = rememberAnimatedNavController()
        Scaffold(
            bottomBar = {
                val isShowingBottomBar = isKeyboardOpen().value.not()
                if (isShowingBottomBar) {
                    NavigationBar {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        val tabs = listOf(
                            TabRouter.Journal,
                            TabRouter.Statistics,
                            TabRouter.Search,
                            TabRouter.SaferUse
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
                                        navController.popBackStack(
                                            route = tab.childRoute,
                                            inclusive = false
                                        )
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
        ) { innerPadding ->
            AnimatedNavHost(
                navController,
                startDestination = TabRouter.Search.route,
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                journalGraph(navController)
                statsGraph(navController)
                searchGraph(navController)
                saferGraph(navController)
            }
        }
    } else {
        AcceptConditionsScreen(onTapAccept = viewModel::accept)
    }
}