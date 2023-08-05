/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.stats.substancecompanion

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.time.ColorPicker
import com.isaakhanimann.journal.ui.theme.JournalTheme

@Composable
fun SubstanceCompanionScreen(
    viewModel: SubstanceCompanionViewModel = hiltViewModel()
) {
    val companion = viewModel.thisCompanionFlow.collectAsState().value
    if (companion == null) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {}
    } else {
        SubstanceCompanionScreen(
            substanceCompanion = companion,
            onChangeColor = { viewModel.updateColor(it) },
            alreadyUsedColors = viewModel.alreadyUsedColorsFlow.collectAsState().value,
            otherColors = viewModel.otherColorsFlow.collectAsState().value,
        )
    }
}

@Preview
@Composable
fun SubstanceCompanionPreview() {
    val alreadyUsedColors = listOf(AdaptiveColor.BLUE, AdaptiveColor.PINK)
    val otherColors = AdaptiveColor.values().filter { color ->
        !alreadyUsedColors.contains(color)
    }
    JournalTheme {
        SubstanceCompanionScreen(
            substanceCompanion = SubstanceCompanion(
                substanceName = "Cocaine",
                color = AdaptiveColor.BLUE
            ),
            onChangeColor = {},
            alreadyUsedColors = alreadyUsedColors,
            otherColors = otherColors,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubstanceCompanionScreen(
    substanceCompanion: SubstanceCompanion,
    onChangeColor: (AdaptiveColor) -> Unit,
    alreadyUsedColors: List<AdaptiveColor>,
    otherColors: List<AdaptiveColor>
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(substanceCompanion.substanceName + " Color") })
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            ColorPicker(
                selectedColor = substanceCompanion.color,
                onChangeOfColor = onChangeColor,
                alreadyUsedColors = alreadyUsedColors,
                otherColors = otherColors
            )
        }
    }
}