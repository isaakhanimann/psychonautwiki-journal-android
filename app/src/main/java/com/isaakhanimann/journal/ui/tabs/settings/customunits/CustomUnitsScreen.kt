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

package com.isaakhanimann.journal.ui.tabs.settings.customunits

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.room.experiences.entities.CustomUnit
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.toReadableString
import com.isaakhanimann.journal.ui.tabs.stats.EmptyScreenDisclaimer
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun CustomUnitsScreen(
    viewModel: CustomUnitsViewModel = hiltViewModel(),
    navigateToEditCustomUnit: (customUnitId: Int) -> Unit,
    navigateToAddCustomUnit: () -> Unit,
    navigateToCustomUnitArchive: () -> Unit,
    ) {
    CustomUnitsScreenContent(
        customUnits = viewModel.customUnitsFlow.collectAsState().value,
        navigateToEditCustomUnit = navigateToEditCustomUnit,
        navigateToAddCustomUnit = navigateToAddCustomUnit,
        navigateToCustomUnitArchive = navigateToCustomUnitArchive
    )
}

@Preview
@Composable
fun CustomUnitsScreenPreview() {
    CustomUnitsScreenContent(
        customUnits = listOf(
            CustomUnit.mdmaSample,
            CustomUnit.twoCBSample
        ),
        navigateToEditCustomUnit = { _ -> },
        navigateToAddCustomUnit = {},
        navigateToCustomUnitArchive = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomUnitsScreenContent(
    customUnits: List<CustomUnit>,
    navigateToEditCustomUnit: (customUnitId: Int) -> Unit,
    navigateToAddCustomUnit: () -> Unit,
    navigateToCustomUnitArchive: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Custom Units") },
                actions = {
                    IconButton(onClick = navigateToCustomUnitArchive) {
                        Icon(Icons.Default.Inventory, contentDescription = "Go to archive")
                    }
                })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = navigateToAddCustomUnit,
                icon = {
                    Icon(Icons.Default.Add, contentDescription = "Add custom unit")
                },
                text = {
                    Text(text = "Custom Unit")
                }
            )
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
                    title = "No Custom Units Yet",
                    description = "Add your first unit."
                )
            }
        }
    }
}

@Composable
fun CustomUnitRow(
    customUnit: CustomUnit,
    navigateToEditCustomUnit: (customUnitId: Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .clickable {
                navigateToEditCustomUnit(customUnit.id)
            }
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = horizontalPadding),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "${customUnit.substanceName}, ${customUnit.name}",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "${customUnit.getDoseOfOneUnitDescription()} per ${customUnit.unit}",
            style = MaterialTheme.typography.titleSmall
        )
        if (customUnit.note.isNotBlank()) {
            Text(
                text = customUnit.note,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

fun CustomUnit.getDoseOfOneUnitDescription(): String {
    return this.dose?.let { unwrappedDose ->
        if (this.isEstimate) {
            this.estimatedDoseStandardDeviation?.let { estimatedDoseStandardDeviationUnwrapped ->
                "${unwrappedDose.toReadableString()}±${estimatedDoseStandardDeviationUnwrapped.toReadableString()} ${this.originalUnit}"
            } ?: "~${unwrappedDose.toReadableString()} ${this.originalUnit}"
        } else {
            "${unwrappedDose.toReadableString()} ${this.originalUnit}"
        }
    } ?: "Unknown dose"
}