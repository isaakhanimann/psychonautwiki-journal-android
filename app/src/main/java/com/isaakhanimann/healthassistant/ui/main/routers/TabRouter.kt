package com.isaakhanimann.healthassistant.ui.main.routers

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.isaakhanimann.healthassistant.R

sealed class TabRouter(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Experiences : TabRouter("experiences", R.string.experiences, Icons.Filled.LocalLibrary)
    object Ingestions : TabRouter("ingestions", R.string.ingestions, Icons.Filled.History)
    object Statistics: TabRouter("statistics", R.string.stats, Icons.Filled.BarChart)
    object Search : TabRouter("search", R.string.search, Icons.Filled.Search)
    object SaferUse : TabRouter("saferUse", R.string.safer, Icons.Filled.HealthAndSafety)
}