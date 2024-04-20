/*
 * Copyright (c) 2024. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.settings.customunits.archive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.room.experiences.entities.CustomUnit
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.tabs.settings.customunits.CustomUnitRow
import com.isaakhanimann.journal.ui.tabs.stats.EmptyScreenDisclaimer

@Composable
fun CustomUnitArchiveScreen(
    viewModel: CustomUnitArchiveViewModel = hiltViewModel(),
    navigateToEditCustomUnit: (customUnitId: Int) -> Unit,
) {
    CustomUnitArchiveScreenContent(
        customUnits = viewModel.customUnitsFlow.collectAsState().value,
        navigateToEditCustomUnit = navigateToEditCustomUnit,
    )
}

@Preview
@Composable
fun CustomUnitArchiveScreenPreview() {
    CustomUnitArchiveScreenContent(
        customUnits = listOf(
            CustomUnit(
                substanceName = "Substance 1",
                name = "Spoon",
                administrationRoute = AdministrationRoute.ORAL,
                dose = 10.0,
                estimatedDoseStandardDeviation = 2.0,
                isEstimate = true,
                isArchived = false,
                unit = "spoon",
                originalUnit = "mg",
                note = ""
            ),
            CustomUnit(
                substanceName = "Substance 2",
                name = "Red pill",
                administrationRoute = AdministrationRoute.ORAL,
                dose = 20.0,
                estimatedDoseStandardDeviation = 2.0,
                isEstimate = true,
                isArchived = false,
                unit = "pill",
                originalUnit = "mg",
                note = ""
            ),
        ),
        navigateToEditCustomUnit = { _ -> },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomUnitArchiveScreenContent(
    customUnits: List<CustomUnit>,
    navigateToEditCustomUnit: (customUnitId: Int) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Custom unit archive") })
        }
    ) { padding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(customUnits) { customUnit ->
                    CustomUnitRow(
                        customUnit = customUnit,
                        navigateToEditCustomUnit = navigateToEditCustomUnit
                    )
                    HorizontalDivider()
                }
            }
            if (customUnits.isEmpty()) {
                EmptyScreenDisclaimer(
                    title = "No archived units yet",
                    description = "Archived units don't show up when adding ingestions"
                )
            }
        }
    }
}
