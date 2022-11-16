/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.main.navigation.routers

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.ui.graphics.vector.ImageVector
import com.isaakhanimann.journal.R

sealed class TabRouter(
    val route: String,
    val childRoute: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    object Journal : TabRouter(
        route = "journal",
        childRoute = NoArgumentRouter.JournalRouter.route,
        resourceId = R.string.journal,
        icon = Icons.Outlined.Timeline
    )

    object Statistics : TabRouter(
        route = "statistics",
        childRoute = NoArgumentRouter.StatsRouter.route,
        resourceId = R.string.stats,
        icon = Icons.Outlined.BarChart
    )

    object Search : TabRouter(
        route = "search",
        childRoute = NoArgumentRouter.SearchRouter.route,
        resourceId = R.string.search,
        icon = Icons.Outlined.Search
    )

    object SaferUse : TabRouter(
        route = "saferUse",
        childRoute = NoArgumentRouter.SaferRouter.route,
        resourceId = R.string.safer,
        icon = Icons.Outlined.HealthAndSafety
    )
}