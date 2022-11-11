/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.settings.combinations

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
                            text = "Opt in to see alerts when you log substances that have interactions with those substances.",
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




