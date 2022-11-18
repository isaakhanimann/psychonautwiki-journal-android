/*
 * Copyright (c) 2022. Free Software Foundation, Inc.
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

package com.isaakhanimann.journal.ui.main.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.composableWithTransitions(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable (AnimatedVisibilityScope.(NavBackStackEntry) -> Unit)
) {
    val withinTabTransitionTimeInMs = 300
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
                    animationSpec = tween(withinTabTransitionTimeInMs)
                ) + fadeOut(animationSpec = tween(withinTabTransitionTimeInMs))
            }
        },
        popEnterTransition = {
            if (isChangingTab()) {
                fadeIn(animationSpec = tween(tabSwitchTimeInMs))
            } else {
                slideInHorizontally(
                    initialOffsetX = { -300 },
                    animationSpec = tween(withinTabTransitionTimeInMs)
                ) + fadeIn(animationSpec = tween(withinTabTransitionTimeInMs))
            }
        },
        enterTransition = {
            if (isChangingTab()) {
                fadeIn(animationSpec = tween(tabSwitchTimeInMs))
            } else {
                slideInHorizontally(
                    initialOffsetX = { 300 },
                    animationSpec = tween(withinTabTransitionTimeInMs)
                ) + fadeIn(animationSpec = tween(withinTabTransitionTimeInMs))
            }
        },
        popExitTransition = {
            if (isChangingTab()) {
                fadeOut(animationSpec = tween(tabSwitchTimeInMs))
            } else {
                slideOutHorizontally(
                    targetOffsetX = { 300 },
                    animationSpec = tween(withinTabTransitionTimeInMs)
                ) + fadeOut(animationSpec = tween(withinTabTransitionTimeInMs))
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
