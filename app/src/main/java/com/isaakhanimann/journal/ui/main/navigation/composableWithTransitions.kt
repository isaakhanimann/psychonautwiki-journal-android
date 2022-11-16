/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.main.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable

const val WITHIN_TAB_TRANSITION_TIME_IN_MS = 300

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.composableWithTransitions(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable (AnimatedVisibilityScope.(NavBackStackEntry) -> Unit)
) {
    val tabSwitchTimeInMs = 200
    composable(
        route = route,
        arguments = arguments,
        exitTransition = {
            if (isChangingTab()) {
                fadeOut(animationSpec = tween(tabSwitchTimeInMs))
            } else {
                slideOutHorizontally(
                    targetOffsetX = { -300 },
                    animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS)
                ) + fadeOut(animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS))
            }
        },
        popEnterTransition = {
            if (isChangingTab()) {
                fadeIn(animationSpec = tween(tabSwitchTimeInMs))
            } else {
                slideInHorizontally(
                    initialOffsetX = { -300 },
                    animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS)
                ) + fadeIn(animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS))
            }
        },
        enterTransition = {
            if (isChangingTab()) {
                fadeIn(animationSpec = tween(tabSwitchTimeInMs))
            } else {
                slideInHorizontally(
                    initialOffsetX = { 300 },
                    animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS)
                ) + fadeIn(animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS))
            }
        },
        popExitTransition = {
            if (isChangingTab()) {
                fadeOut(animationSpec = tween(tabSwitchTimeInMs))
            } else {
                slideOutHorizontally(
                    targetOffsetX = { 300 },
                    animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS)
                ) + fadeOut(animationSpec = tween(WITHIN_TAB_TRANSITION_TIME_IN_MS))
            }
        },
        content = content
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentScope<NavBackStackEntry>.isChangingTab(): Boolean {
    // check grandparents because in a tab graph there can be another nested graph such as addIngestion
    val initialParent = initialState.destination.parent
    val initialGrandParent = initialParent?.parent
    val targetParent = targetState.destination.parent
    val targetGrandParent = targetParent?.parent
    return (initialGrandParent?.route ?: initialParent?.route) != (targetGrandParent?.route
        ?: targetParent?.route)
}
