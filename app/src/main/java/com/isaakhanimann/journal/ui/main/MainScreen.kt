/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

@file:OptIn(ExperimentalAnimationApi::class)

package com.isaakhanimann.journal.ui.main

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.isaakhanimann.journal.ui.AcceptConditionsScreen
import com.isaakhanimann.journal.ui.main.routers.*
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
                val tabs = listOf(
                    TabRouter.Journal,
                    TabRouter.Statistics,
                    TabRouter.Search,
                    TabRouter.SaferUse
                )
                val tabRoutes = tabs.map { it.route }.toSet()
                val isShowingBottomBar = isKeyboardOpen().value.not()
                if (isShowingBottomBar) {
                    NavigationBar {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        val latestTab =
                            navController.backQueue.lastOrNull { it.destination.route in tabRoutes }
                        tabs.forEach { tab ->
                            val isSelected = latestTab?.destination?.route == tab.route
                            NavigationBarItem(
                                icon = { Icon(tab.icon, contentDescription = null) },
                                label = { Text(stringResource(tab.resourceId)) },
                                selected = isSelected,
                                onClick = {
                                    navController.navigate(tab.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            val currentExperience = viewModel.currentExperienceFlow.collectAsState().value
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                AnimatedNavHost(
                    navController,
                    startDestination = TabRouter.Search.route,
                ) {
                    tabGraph(navController)
                    noArgumentGraph(navController)
                    argumentGraph(navController)
                    addIngestionGraph(navController)
                }
                if (currentExperience != null) {
                    CurrentExperienceRow(
                        experienceWithIngestionsAndCompanions = currentExperience,
                        navigateToExperienceScreen = {
                            navController.navigateToExperiencePopNothing(experienceId = currentExperience.experience.id)
                        }
                    )
                }
            }
        }
    } else {
        AcceptConditionsScreen(onTapAccept = viewModel::accept)
    }
}