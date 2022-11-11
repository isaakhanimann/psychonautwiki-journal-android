/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.search.substance.roa

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
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
        Divider()
    }
}