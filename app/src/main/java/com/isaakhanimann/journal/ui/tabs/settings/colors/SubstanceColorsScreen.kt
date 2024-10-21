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

package com.isaakhanimann.journal.ui.tabs.settings.colors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.time.ColorPicker
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun SubstanceColorsScreen(
    viewModel: SubstanceColorsViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.deleteUnusedSubstanceCompanions()
    }
    SubstanceColorsScreenContent(
        substanceCompanions = viewModel.substanceCompanionsFlow.collectAsState().value,
        updateColor = viewModel::updateColor,
        alreadyUsedColors = viewModel.alreadyUsedColorsFlow.collectAsState().value,
        otherColors = viewModel.otherColorsFlow.collectAsState().value
    )
}

@Preview
@Composable
fun SubstanceColorsScreenPreview() {
    val alreadyUsedColors = listOf(AdaptiveColor.OLIVE, AdaptiveColor.AUBURN)
    val otherColors = AdaptiveColor.entries.filter { !alreadyUsedColors.contains(it) }
    SubstanceColorsScreenContent(
        substanceCompanions = listOf(
            SubstanceCompanion(substanceName = "Substance 1", color = AdaptiveColor.AUBURN),
            SubstanceCompanion(substanceName = "Substance 2", color = AdaptiveColor.BLUE),
            SubstanceCompanion(
                substanceName = "Substance with longer 3",
                color = AdaptiveColor.YELLOW
            ),
            SubstanceCompanion(substanceName = "Substance 4", color = AdaptiveColor.OLIVE),
        ),
        updateColor = { _, _ -> },
        alreadyUsedColors = alreadyUsedColors,
        otherColors = otherColors

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubstanceColorsScreenContent(
    substanceCompanions: List<SubstanceCompanion>,
    updateColor: (color: AdaptiveColor, substanceName: String) -> Unit,
    alreadyUsedColors: List<AdaptiveColor>,
    otherColors: List<AdaptiveColor>
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Substance colors") })
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = horizontalPadding)
                .fillMaxSize()
        ) {
            items(substanceCompanions) { substanceCompanion ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = substanceCompanion.substanceName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    ColorPicker(
                        selectedColor = substanceCompanion.color,
                        onChangeOfColor = {
                            updateColor(it, substanceCompanion.substanceName)
                        },
                        alreadyUsedColors = alreadyUsedColors,
                        otherColors = otherColors
                    )
                }
                HorizontalDivider()
            }
        }
    }
}