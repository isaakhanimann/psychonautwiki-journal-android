package com.isaakhanimann.journal.ui.main.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.isaakhanimann.journal.ui.main.navigation.composableWithTransitions
import com.isaakhanimann.journal.ui.main.navigation.SettingsTopLevelRoute
import com.isaakhanimann.journal.ui.tabs.search.custom.AddCustomSubstanceScreen
import com.isaakhanimann.journal.ui.tabs.search.custom.EditCustomSubstanceScreen
import com.isaakhanimann.journal.ui.tabs.settings.DonateScreen
import com.isaakhanimann.journal.ui.tabs.settings.FAQScreen
import com.isaakhanimann.journal.ui.tabs.settings.SettingsScreen
import com.isaakhanimann.journal.ui.tabs.settings.colors.SubstanceColorsScreen
import com.isaakhanimann.journal.ui.tabs.settings.combinations.CombinationSettingsScreen
import com.isaakhanimann.journal.ui.tabs.settings.customsubstances.CustomSubstanceManagementScreen
import com.isaakhanimann.journal.ui.tabs.settings.customunits.CustomUnitsScreen
import com.isaakhanimann.journal.ui.tabs.settings.customunits.archive.CustomUnitArchiveScreen
import com.isaakhanimann.journal.ui.tabs.settings.customunits.edit.EditCustomUnitScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.settingsGraph(navController: NavHostController) {
    navigation<SettingsTopLevelRoute>(
        startDestination = SettingsScreenRoute,
    ) {
        composableWithTransitions<SettingsScreenRoute> {
            SettingsScreen(
                navigateToFAQ = {
                    navController.navigate(FAQRoute)
                },
                navigateToComboSettings = {
                    navController.navigate(CombinationSettingsRoute)
                },
                navigateToSubstanceColors = {
                    navController.navigate(SubstanceColorsRoute)
                },
                navigateToCustomUnits = {
                    navController.navigate(CustomUnitsRoute)
                },
                navigateToCustomSubstances = {
                    navController.navigate(CustomSubstancesRoute)
                },
                navigateToDonate = {
                    navController.navigate(DonateRoute)
                },
            )
        }
        composableWithTransitions<FAQRoute> { FAQScreen() }
        composableWithTransitions<DonateRoute> { DonateScreen() }
        composableWithTransitions<CombinationSettingsRoute> { CombinationSettingsScreen() }
        composableWithTransitions<SubstanceColorsRoute> { SubstanceColorsScreen() }
        composableWithTransitions<CustomUnitArchiveRoute> {
            CustomUnitArchiveScreen(navigateToEditCustomUnit = { customUnitId ->
                navController.navigate(EditCustomUnitRoute(customUnitId))
            })
        }
        addCustomUnitGraph(navController)
        composableWithTransitions<CustomUnitsRoute> {
            CustomUnitsScreen(
                navigateToAddCustomUnit = {
                    navController.navigate(AddCustomUnitsParentRoute)
                },
                navigateToEditCustomUnit = { customUnitId ->
                    navController.navigate(EditCustomUnitRoute(customUnitId))
                },
                navigateToCustomUnitArchive = {
                    navController.navigate(CustomUnitArchiveRoute)
                }
            )
        }
        composableWithTransitions<EditCustomUnitRoute> {
            EditCustomUnitScreen(navigateBack = navController::popBackStack)
        }
        composableWithTransitions<CustomSubstancesRoute> {
            CustomSubstanceManagementScreen(
                navigateBack = { navController.popBackStack() },
                navigateToAddCustomSubstance = { navController.navigate(AddCustomSubstanceRoute) },
                navigateToEditCustomSubstance = { id ->
                    navController.navigate(EditCustomSubstanceRoute(id))
                }
            )
        }
        composableWithTransitions<AddCustomSubstanceRoute> {
            AddCustomSubstanceScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
        composableWithTransitions<EditCustomSubstanceRoute> {
            EditCustomSubstanceScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}

@Serializable
object SettingsScreenRoute

@Serializable
object FAQRoute

@Serializable
object DonateRoute

@Serializable
object CombinationSettingsRoute

@Serializable
object SubstanceColorsRoute

@Serializable
object CustomUnitArchiveRoute

@Serializable
object CustomUnitsRoute

@Serializable
data class EditCustomUnitRoute(val customUnitId: Int)

@Serializable
object CustomSubstancesRoute

@Serializable
object AddCustomSubstanceRoute