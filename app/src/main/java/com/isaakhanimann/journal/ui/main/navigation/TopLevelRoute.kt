package com.isaakhanimann.journal.ui.main.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.isaakhanimann.journal.R
import kotlinx.serialization.Serializable

data class TopLevelRoute<T : Any>(
    val name: String?,
    val route: T,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector
)

val topLevelRoutes
    @Composable get() = listOf(
        TopLevelRoute(stringResource(R.string.stats), StatsTopLevelRoute, Icons.Filled.BarChart, Icons.Outlined.BarChart),
        TopLevelRoute(stringResource(R.string.journal), JournalTopLevelRoute, Icons.Filled.Book, Icons.Outlined.Book),
        TopLevelRoute(stringResource(R.string.drugs), DrugsTopLevelRoute, Icons.Filled.Medication, Icons.Outlined.Medication),
        TopLevelRoute(stringResource(R.string.safer), SaferUseTopLevelRoute, Icons.Filled.HealthAndSafety, Icons.Outlined.HealthAndSafety),
        TopLevelRoute(stringResource(R.string.settings), SettingsTopLevelRoute, Icons.Filled.Settings, Icons.Outlined.Settings)
    )

val topLevelRoutesData
    get() = listOf(
        TopLevelRoute(null, StatsTopLevelRoute, Icons.Filled.BarChart, Icons.Outlined.BarChart),
        TopLevelRoute(null, JournalTopLevelRoute, Icons.Filled.Book, Icons.Outlined.Book),
        TopLevelRoute(null, DrugsTopLevelRoute, Icons.Filled.Medication, Icons.Outlined.Medication),
        TopLevelRoute(null, SaferUseTopLevelRoute, Icons.Filled.HealthAndSafety, Icons.Outlined.HealthAndSafety),
        TopLevelRoute(null, SettingsTopLevelRoute, Icons.Filled.Settings, Icons.Outlined.Settings)
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