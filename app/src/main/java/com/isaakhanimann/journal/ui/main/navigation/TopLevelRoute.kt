package com.isaakhanimann.journal.ui.main.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

data class TopLevelRoute<T : Any>(
    val name: String,
    val route: T,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector
)

val topLevelRoutes = listOf(
    TopLevelRoute("Stats", StatsTopLevelRoute, Icons.Filled.BarChart, Icons.Outlined.BarChart),
    TopLevelRoute("Journal", JournalTopLevelRoute, Icons.Filled.Book, Icons.Outlined.Book),
    TopLevelRoute("Drugs", DrugsTopLevelRoute, Icons.Filled.Medication, Icons.Outlined.Medication),
    TopLevelRoute("Safer", SaferUseTopLevelRoute, Icons.Filled.HealthAndSafety, Icons.Outlined.HealthAndSafety),
    TopLevelRoute("Gamification", GamificationTopLevelRoute, Icons.Filled.EmojiEvents, Icons.Outlined.EmojiEvents),
    TopLevelRoute("Settings", SettingsTopLevelRoute, Icons.Filled.Settings, Icons.Outlined.Settings)
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
object GamificationTopLevelRoute

@Serializable
object SettingsTopLevelRoute