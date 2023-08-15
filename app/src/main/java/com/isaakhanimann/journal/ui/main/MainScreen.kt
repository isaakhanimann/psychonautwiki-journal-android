/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

@file:OptIn(ExperimentalAnimationApi::class)

package com.isaakhanimann.journal.ui.main

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.isaakhanimann.journal.ui.main.navigation.*
import com.isaakhanimann.journal.ui.main.navigation.graphs.*
import com.isaakhanimann.journal.ui.main.navigation.routers.*
import com.isaakhanimann.journal.ui.utils.keyboard.isKeyboardOpen

@OptIn(ExperimentalAnimationApi::class)
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
                            TabRouter.Statistics,
                            TabRouter.Journal,
                            TabRouter.Search,
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
                    startDestination = TabRouter.Journal.route,
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    journalGraph(navController)
                    statsGraph(navController)
                    searchGraph(navController)
                    saferGraph(navController)
                    settingsGraph(navController)
                }
            }
    } else {
        AcceptConditionsScreen(onTapAccept = viewModel::accept)
    }
}