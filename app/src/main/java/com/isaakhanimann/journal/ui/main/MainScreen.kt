/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

@file:OptIn(ExperimentalAnimationApi::class)

package com.isaakhanimann.journal.ui.main

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
        val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val isShowingBottomBar = navBackStackEntry?.destination?.route in setOf(
            TabRouter.Journal.route,
            TabRouter.Statistics.route,
            TabRouter.Search.route,
            TabRouter.SaferUse.route
        ) && isKeyboardOpen().value.not()
        bottomBarState.value = isShowingBottomBar
        Scaffold(
            bottomBar = {
                Column {
                    val currentExperience = viewModel.currentExperienceFlow.collectAsState().value
                    if (isShowingBottomBar) {
                        if (currentExperience != null) {
                            CurrentExperienceRow(
                                experienceWithIngestionsAndCompanions = currentExperience,
                                navigateToExperienceScreen = {
                                    navController.navigateToExperiencePopNothing(experienceId = currentExperience.experience.id)
                                }
                            )
                        }
                        val items = listOf(
                            TabRouter.Journal,
                            TabRouter.Statistics,
                            TabRouter.Search,
                            TabRouter.SaferUse
                        )
                        NavigationBar {
                            val currentRoute = navBackStackEntry?.destination?.route
                            items.forEach { item ->
                                NavigationBarItem(
                                    icon = { Icon(item.icon, contentDescription = null) },
                                    label = { Text(stringResource(item.resourceId)) },
                                    selected = currentRoute == item.route,
                                    onClick = {
                                        navController.navigate(item.route) {
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
            }
        ) { innerPadding ->
            AnimatedNavHost(
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
    } else {
        AcceptConditionsScreen(onTapAccept = viewModel::accept)
    }
}