package com.example.healthassistant.ui.main.routers

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.healthassistant.R

sealed class TabRouter(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Home : TabRouter("home", R.string.home, Icons.Filled.Home)
    object Search : TabRouter("search", R.string.search, Icons.Filled.Search)
    object Stats : TabRouter("stats", R.string.stats, Icons.Filled.Info)
    object Settings : TabRouter("settings", R.string.settings, Icons.Filled.Settings)
}