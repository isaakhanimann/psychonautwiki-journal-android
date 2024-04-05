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

package com.isaakhanimann.journal.ui.tabs.search.substance.roa

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun ClassSectionPreview() {
    ClassSection(
        psychoactiveClasses = listOf("Stimulants", "Psychedelics"),
        chemicalClasses = listOf("Substituted Phenethylamines"),
        titleStyle = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun ClassSection(
    psychoactiveClasses: List<String>,
    chemicalClasses: List<String>,
    titleStyle: TextStyle
) {
    if (psychoactiveClasses.isNotEmpty() || chemicalClasses.isNotEmpty()) {
        Column {
            Text(text = "Class Membership", style = titleStyle)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (psychoactiveClasses.isNotEmpty()) {
                    Column {
                        Text(text = "Psychoactive")
                        psychoactiveClasses.forEach {
                            Text(text = it)
                        }
                    }
                }
                if (chemicalClasses.isNotEmpty()) {
                    Column {
                        Text(text = "Chemical")
                        chemicalClasses.forEach {
                            Text(text = it)
                        }
                    }
                }
            }

        }
        HorizontalDivider()
    }
}