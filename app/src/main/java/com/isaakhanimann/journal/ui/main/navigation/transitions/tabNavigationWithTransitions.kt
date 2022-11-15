/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.main.navigation.transitions

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.tabNavigationWithTransitions(
    startDestination: String,
    route: String,
    builder: NavGraphBuilder.() -> Unit
) {
    val tabSwitchTimeInMs = 200
    navigation(
        startDestination = startDestination,
        route = route,
        enterTransition = { fadeIn(animationSpec = tween(tabSwitchTimeInMs)) },
        exitTransition = { fadeOut(animationSpec = tween(tabSwitchTimeInMs)) },
        popEnterTransition = { fadeIn(animationSpec = tween(tabSwitchTimeInMs)) },
        popExitTransition = { fadeOut(animationSpec = tween(tabSwitchTimeInMs)) },
        builder = builder
    )
}