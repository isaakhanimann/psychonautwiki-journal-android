package com.isaakhanimann.healthassistant.ui.main.routers

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.isaakhanimann.healthassistant.R

sealed class TabRouter(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Experiences : TabRouter("experiences", R.string.experiences, Icons.Filled.LocalLibrary)
    object Ingestions : TabRouter("ingestions", R.string.ingestions, Icons.Filled.History)
    object Search : TabRouter("search", R.string.search, Icons.Filled.Search)
}