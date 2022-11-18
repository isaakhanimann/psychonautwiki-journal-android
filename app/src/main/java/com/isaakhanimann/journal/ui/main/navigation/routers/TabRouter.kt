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
        route = "journalTab",
        childRoute = NoArgumentRouter.JournalRouter.route,
        resourceId = R.string.journal,
        icon = Icons.Outlined.Timeline
    )

    object Statistics : TabRouter(
        route = "statisticsTab",
        childRoute = NoArgumentRouter.StatsRouter.route,
        resourceId = R.string.stats,
        icon = Icons.Outlined.BarChart
    )

    object Search : TabRouter(
        route = "searchTab",
        childRoute = NoArgumentRouter.SearchRouter.route,
        resourceId = R.string.search,
        icon = Icons.Outlined.Search
    )

    object SaferUse : TabRouter(
        route = "saferTab",
        childRoute = NoArgumentRouter.SaferRouter.route,
        resourceId = R.string.safer,
        icon = Icons.Outlined.HealthAndSafety
    )
}