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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.isaakhanimann.journal.ui.main.navigation.*
import com.isaakhanimann.journal.ui.main.navigation.graphs.*
import com.isaakhanimann.journal.ui.main.navigation.routers.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    if (viewModel.isAcceptedFlow.collectAsState().value) {
        val navController = rememberAnimatedNavController()
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val tabs = listOf(
            TabRouter.Journal,
            TabRouter.Statistics,
            TabRouter.Search,
            TabRouter.SaferUse
        )
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Spacer(Modifier.height(12.dp))
                    tabs.forEach { tab ->
                        val isSelected =
                            currentDestination?.hierarchy?.any { it.route == tab.route } == true
                        NavigationDrawerItem(
                            icon = { Icon(tab.icon, contentDescription = null) },
                            label = { Text(stringResource(tab.resourceId)) },
                            selected = isSelected,
                            onClick = {
                                scope.launch { drawerState.close() }
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
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "* Drawer opens with left swipe",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 15.dp).alpha(0.5f)
                    )
                }
            },
            content = {
                AnimatedNavHost(
                    navController,
                    startDestination = TabRouter.Journal.route
                ) {
                    journalGraph(navController, openNavigationDrawer = { scope.launch { drawerState.open() } })
                    statsGraph(navController)
                    searchGraph(navController)
                    saferGraph(navController)
                }
            }
        )
    } else {
        AcceptConditionsScreen(onTapAccept = viewModel::accept)
    }
}