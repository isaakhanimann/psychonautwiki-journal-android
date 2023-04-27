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

package com.isaakhanimann.journal.ui.tabs.journal.experience.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun DotRows(numDots: Int) {
    val verticalPadding = 2.dp
    Column {
        if (numDots==0) {
            Row(modifier = Modifier.padding(vertical = verticalPadding)) {
                for (dot in 1..4) {
                    Dot(isFull = false)
                }
            }
        } else {
            val numFullRows = numDots/4
            val dotsInLastRow = numDots.rem(4)
            if (numFullRows > 0) {
                for (row in 1..numFullRows) {
                    Row(modifier = Modifier.padding(vertical = verticalPadding)) {
                        for (dot in 1..4) {
                            Dot(isFull = true)
                        }
                    }
                }
            }
            if (dotsInLastRow > 0) {
                Row(modifier = Modifier.padding(vertical = verticalPadding)) {
                    for (dot in 1..dotsInLastRow) {
                        Dot(isFull = true)
                    }
                    val numEmpty = 4 - dotsInLastRow
                    for (dot in 1..numEmpty) {
                        Dot(isFull = false)
                    }
                }
            }
        }
    }
}

@Composable
fun Dot(isFull: Boolean) {
    val dotSize = 10.dp
    val horizontalPadding = 1.dp
    if (isFull) {
        Box(
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .size(dotSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onBackground)
        )
    } else {
        Box(
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .size(dotSize)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
        )
    }
}