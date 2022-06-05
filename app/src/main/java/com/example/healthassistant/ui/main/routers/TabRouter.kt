package com.example.healthassistant.ui.main.routers

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.healthassistant.R

sealed class TabRouter(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Journal : TabRouter("journal", R.string.journal, Icons.Filled.AutoStories)
    object Search : TabRouter("search", R.string.search, Icons.Filled.Search)
    object Stats : TabRouter("stats", R.string.stats, Icons.Filled.BarChart)
    object Settings : TabRouter("settings", R.string.settings, Icons.Filled.Settings)
}