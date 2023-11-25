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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubstanceColorsScreen(
    viewModel: SubstanceColorsViewModel = hiltViewModel(),
) {
    SubstanceColorsScreenContent(substanceCompanions = viewModel.substanceCompanionsFlow.collectAsState().value)
}

@Preview
@Composable
fun SubstanceColorsScreenPreview() {
    SubstanceColorsScreenContent(
        substanceCompanions = listOf(
            SubstanceCompanion(substanceName = "Substance 1", color = AdaptiveColor.AUBURN),
            SubstanceCompanion(substanceName = "Substance 2", color = AdaptiveColor.BLUE),
            SubstanceCompanion(
                substanceName = "Substance with longer 3",
                color = AdaptiveColor.YELLOW
            ),
            SubstanceCompanion(substanceName = "Substance 4", color = AdaptiveColor.OLIVE),
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubstanceColorsScreenContent(
    substanceCompanions: List<SubstanceCompanion>
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Substance Colors") })
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
                    modifier = Modifier.padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = substanceCompanion.substanceName)
//                    ColorPicker(
//                        selectedColor = substanceCompanion.color,
//                        onChangeOfColor = ,
//                        alreadyUsedColors = ,
//                        otherColors =
//                    )
                }
                Divider()
            }
        }
    }
}