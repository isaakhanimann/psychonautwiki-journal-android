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

package com.isaakhanimann.journal.ui.tabs.journal.experience

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.toReadableString
import com.isaakhanimann.journal.ui.utils.getStringOfPattern

@Preview
@Composable
fun IngestionRowPreview(@PreviewParameter(IngestionRowPreviewProvider::class) ingestionElement: IngestionElement) {
    IngestionRow(
        ingestionElement = ingestionElement,
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun IngestionRow(
    ingestionElement: IngestionElement,
    modifier: Modifier = Modifier,
) {
    val ingestionWithCompanion = ingestionElement.ingestionWithCompanion
    val ingestion = ingestionWithCompanion.ingestion
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        val isDarkTheme = isSystemInDarkTheme()
        Surface(
            shape = CircleShape,
            color = ingestionWithCompanion.substanceCompanion!!.color.getComposeColor(
                isDarkTheme
            ),
            modifier = Modifier
                .size(25.dp)
        ) {}
        Column {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = ingestion.substanceName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    val timeString = ingestion.time.getStringOfPattern("EEE HH:mm")
                    Text(text = timeString)
                }
                Column(horizontalAlignment = Alignment.End) {
                    ingestion.dose?.also {
                        val isEstimateText = if (ingestion.isDoseAnEstimate) "~" else ""
                        val doseText = it.toReadableString()
                        Text(text = "${ingestion.administrationRoute.displayText} $isEstimateText$doseText ${ingestion.units}")
                    } ?: run {
                        Text(text = "${ingestion.administrationRoute.displayText} Unknown Dose")
                    }
                    val numDots = ingestionElement.numDots
                    if (numDots != null) {
                        DotRows(numDots = numDots)
                    }
                }
            }
            val note = ingestion.notes
            if (!note.isNullOrBlank()) {
                Text(text = note, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

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