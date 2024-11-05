package com.isaakhanimann.journal.ui.main.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)

val topLevelRoutes = listOf(
    TopLevelRoute("Stats", StatsTopLevelRoute, Icons.Filled.BarChart),
    TopLevelRoute("Journal", JournalTopLevelRoute, Icons.Filled.Book),
    TopLevelRoute("Drugs", DrugsTopLevelRoute, Icons.Filled.Medication),
    TopLevelRoute("Safer", SaferUseTopLevelRoute, Icons.Filled.HealthAndSafety),
    TopLevelRoute("Settings", SettingsTopLevelRoute, Icons.Filled.Settings)
)

@Serializable
object StatsTopLevelRoute

@Serializable
object JournalTopLevelRoute

@Serializable
object DrugsTopLevelRoute

@Serializable
object SaferUseTopLevelRoute

@Serializable
object SettingsTopLevelRoute