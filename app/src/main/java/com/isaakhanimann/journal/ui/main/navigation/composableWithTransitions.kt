/*
 * Copyright (c) 2022. Isaak Hanimann.
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

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

inline fun <reified T : Any> NavGraphBuilder.composableWithTransitions(
    noinline content: @Composable (AnimatedVisibilityScope.(NavBackStackEntry) -> Unit)
) {
    val withinTabTransitionTimeInMs = 300
    val tabSwitchTimeInMs = 200
    composable<T>(
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

fun AnimatedContentTransitionScope<NavBackStackEntry>.isChangingTab(): Boolean {
    val initialTopLevel = initialState.destination.hierarchy.firstOrNull { navDestination ->
        topLevelRoutesData.any { navDestination.hasRoute(it.route::class) } }
    val targetTopLevel = targetState.destination.hierarchy.firstOrNull { navDestination ->
        topLevelRoutesData.any { navDestination.hasRoute(it.route::class) } }
    return initialTopLevel != targetTopLevel
}
