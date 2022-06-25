package com.example.healthassistant.ui.main.routers

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MultilineChart
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.healthassistant.R

sealed class TabRouter(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Experiences : TabRouter("experiences", R.string.experiences, Icons.Filled.MultilineChart)
    object Ingestions : TabRouter("ingestions", R.string.ingestions, Icons.Filled.History)
    object Search : TabRouter("search", R.string.search, Icons.Filled.Search)
    object Stats : TabRouter("stats", R.string.stats, Icons.Filled.BarChart)
}