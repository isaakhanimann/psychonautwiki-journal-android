package com.isaakhanimann.healthassistant.ui.main.routers

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.ui.graphics.vector.ImageVector
import com.isaakhanimann.healthassistant.R

sealed class TabRouter(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Journal : TabRouter("journal", R.string.journal, Icons.Filled.Timeline)
    object Statistics: TabRouter("statistics", R.string.stats, Icons.Filled.BarChart)
    object Search : TabRouter("search", R.string.search, Icons.Filled.Search)
    object SaferUse : TabRouter("saferUse", R.string.safer, Icons.Filled.HealthAndSafety)
}