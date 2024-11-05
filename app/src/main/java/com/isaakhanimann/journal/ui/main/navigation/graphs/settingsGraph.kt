/*
 * Copyright (c) 2023. Isaak Hanimann.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.main.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.isaakhanimann.journal.ui.main.navigation.composableWithTransitions
import com.isaakhanimann.journal.ui.main.navigation.routers.ArgumentRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.NoArgumentRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.TabRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToComboSettings
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToCustomUnits
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToDonate
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToEditCustomUnit
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToFAQ
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToSubstanceColors
import com.isaakhanimann.journal.ui.tabs.settings.DonateScreen
import com.isaakhanimann.journal.ui.tabs.settings.FAQScreen
import com.isaakhanimann.journal.ui.tabs.settings.SettingsScreen
import com.isaakhanimann.journal.ui.tabs.settings.colors.SubstanceColorsScreen
import com.isaakhanimann.journal.ui.tabs.settings.combinations.CombinationSettingsScreen
import com.isaakhanimann.journal.ui.tabs.settings.customunits.CustomUnitsScreen
import com.isaakhanimann.journal.ui.tabs.settings.customunits.archive.CustomUnitArchiveScreen
import com.isaakhanimann.journal.ui.tabs.settings.customunits.edit.EditCustomUnitScreen

fun NavGraphBuilder.settingsGraph(navController: NavHostController) {
    navigation(
        startDestination = NoArgumentRouter.SettingsRouter.route,
        route = TabRouter.Settings.route,
    ) {
        composableWithTransitions(
            route = NoArgumentRouter.SettingsRouter.route,
        ) {
            SettingsScreen(
                navigateToFAQ = navController::navigateToFAQ,
                navigateToComboSettings = navController::navigateToComboSettings,
                navigateToSubstanceColors = navController::navigateToSubstanceColors,
                navigateToCustomUnits = navController::navigateToCustomUnits,
                navigateToDonate = navController::navigateToDonate,
            )
        }
        composableWithTransitions(NoArgumentRouter.FAQRouter.route) { FAQScreen() }
        composableWithTransitions(NoArgumentRouter.DonateRouter.route) { DonateScreen() }
        composableWithTransitions(NoArgumentRouter.CombinationSettingsRouter.route) { CombinationSettingsScreen() }
        composableWithTransitions(NoArgumentRouter.SubstanceColorsRouter.route) { SubstanceColorsScreen() }
        composableWithTransitions(NoArgumentRouter.CustomUnitArchiveRouter.route) {
            CustomUnitArchiveScreen(navigateToEditCustomUnit = navController::navigateToEditCustomUnit)
        }
        addCustomUnitGraph(navController)
        composableWithTransitions(NoArgumentRouter.CustomUnitsRouter.route) {
            CustomUnitsScreen(
                navigateToAddCustomUnit = navController::navigateToAddCustomUnits,
                navigateToEditCustomUnit = navController::navigateToEditCustomUnit,
                navigateToCustomUnitArchive = navController::navigateToCustomUnitArchive
            )
        }
        composableWithTransitions(
            ArgumentRouter.EditCustomUnitRouter.route,
            arguments = ArgumentRouter.EditCustomUnitRouter.args
        ) {
            EditCustomUnitScreen(navigateBack = navController::popBackStack)
        }
    }
}