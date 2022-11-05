package com.isaakhanimann.journal.ui.main.routers

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
    object Statistics: TabRouter("statistics", R.string.stats, Icons.Outlined.BarChart)
    object Search : TabRouter("search", R.string.search, Icons.Outlined.Search)
    object SaferUse : TabRouter("saferUse", R.string.safer, Icons.Outlined.HealthAndSafety)
}