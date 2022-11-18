/*
 * Copyright (c) 2022. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.safer.settings.combinations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun CombinationSettingsScreen(viewModel: CombinationSettingsViewModel = hiltViewModel()) {
    CombinationSettingsScreen(
        isSkippingInteractions = viewModel.skipViewModelInteraction.stateFlow.collectAsState().value,
        toggleIsSkipping = viewModel.skipViewModelInteraction::toggle,
        substanceInteractions = viewModel.substanceInteraction.map {
            SubstanceInteraction(
                name = it.name,
                isOn = it.stateFlow.collectAsState().value,
                toggle = it::toggle
            )
        }
    )
}

class SubstanceInteraction(
    val name: String,
    val isOn: Boolean,
    val toggle: () -> Unit,
)

@Preview
@Composable
fun CombinationSettingsPreview() {
    CombinationSettingsScreen(
        isSkippingInteractions = false,
        toggleIsSkipping = {},
        substanceInteractions = listOf(
            SubstanceInteraction(name = "Alcohol", toggle = {}, isOn = true),
            SubstanceInteraction(name = "Caffeine", toggle = {}, isOn = false),
            SubstanceInteraction(name = "Cannabis", toggle = {}, isOn = false),
            SubstanceInteraction(name = "Grapefruit", toggle = {}, isOn = true),
            SubstanceInteraction(name = "Hormonal birth control", toggle = {}, isOn = false),
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CombinationSettingsScreen(
    isSkippingInteractions: Boolean,
    toggleIsSkipping: () -> Unit,
    substanceInteractions: List<SubstanceInteraction>
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Combinations") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            Card(modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 4.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Skip Interactions Altogether",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Switch(
                        checked = isSkippingInteractions,
                        onCheckedChange = { toggleIsSkipping() })
                }
            }
            AnimatedVisibility(visible = isSkippingInteractions.not()) {
                Card(modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 4.dp)) {
                    Column(
                        modifier = Modifier.padding(
                            horizontal = horizontalPadding,
                            vertical = 10.dp
                        )
                    ) {
                        Text(
                            text = "Interaction Alerts",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "In addition to receiving alerts about interactions with substances that you log you can also receive alerts with the following substances, even if you haven't logged them.",
                            style = MaterialTheme.typography.bodySmall
                        )
                        substanceInteractions.forEach { substanceInteraction ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = substanceInteraction.name)
                                Switch(
                                    checked = substanceInteraction.isOn,
                                    onCheckedChange = { substanceInteraction.toggle() })
                            }
                        }
                    }
                }
            }
        }
    }
}




