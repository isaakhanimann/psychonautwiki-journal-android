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

sealed class TabRouter(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Journal : TabRouter("journal", R.string.journal, Icons.Outlined.Timeline)
    object Statistics : TabRouter("statistics", R.string.stats, Icons.Outlined.BarChart)
    object Search : TabRouter("search", R.string.search, Icons.Outlined.Search)
    object SaferUse : TabRouter("saferUse", R.string.safer, Icons.Outlined.HealthAndSafety)
}