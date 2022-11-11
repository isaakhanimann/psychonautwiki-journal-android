/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.main.routers.transitions

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.isaakhanimann.journal.ui.main.routers.TabRouter

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.tabComposableWithTransitions(
    route: String,
    content: @Composable (AnimatedVisibilityScope.(NavBackStackEntry) -> Unit),
) {
    val tabRoutes = setOf(
        TabRouter.Journal.route,
        TabRouter.Statistics.route,
        TabRouter.Search.route,
        TabRouter.SaferUse.route
    )
    val tabSwitchTimeInMs = 200
    composable(
        route = route,
        enterTransition = { fadeIn(animationSpec = tween(tabSwitchTimeInMs)) },
        exitTransition = {
            if (tabRoutes.contains(targetState.destination.route)) {
                fadeOut(animationSpec = tween(tabSwitchTimeInMs))
            } else {
                slideOutHorizontally(
                    targetOffsetX = { -300 },
                    animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS)
                ) + fadeOut(animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS))
            }
        },
        popEnterTransition = {
            if (tabRoutes.contains(initialState.destination.route)) {
                fadeIn(animationSpec = tween(tabSwitchTimeInMs))
            } else {
                slideInHorizontally(
                    initialOffsetX = { -300 },
                    animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS)
                ) + fadeIn(animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS))
            }
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(tabSwitchTimeInMs))
        },
        content = content
    )
}