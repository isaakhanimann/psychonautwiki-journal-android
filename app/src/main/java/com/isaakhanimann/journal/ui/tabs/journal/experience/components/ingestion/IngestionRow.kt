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

package com.isaakhanimann.journal.ui.tabs.journal.experience.components.ingestion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.DotRows
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.TimeDisplayOption
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.TimeText
import com.isaakhanimann.journal.ui.tabs.journal.experience.models.IngestionElement
import java.time.Instant
import java.time.temporal.ChronoUnit

@Preview(showBackground = true)
@Composable
fun IngestionRowPreview(@PreviewParameter(IngestionRowPreviewProvider::class) ingestionElement: IngestionElement) {
    IngestionRow(
        ingestionElement = ingestionElement,
        timeDisplayOption = TimeDisplayOption.REGULAR,
        startTime = Instant.now().minus(3, ChronoUnit.HOURS),
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun IngestionRow(
    ingestionElement: IngestionElement,
    timeDisplayOption: TimeDisplayOption,
    startTime: Instant,
    modifier: Modifier = Modifier,
) {
    val ingestionWithCompanionAndCustomUnit = ingestionElement.ingestionWithCompanionAndCustomUnit
    val ingestion = ingestionWithCompanionAndCustomUnit.ingestion
    val customUnit = ingestionWithCompanionAndCustomUnit.customUnit
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.height(intrinsicSize = IntrinsicSize.Min)
    ) {
        VerticalLine(color = ingestionWithCompanionAndCustomUnit.substanceCompanion!!.color)
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                val customUnitName = if (customUnit != null) " (${customUnit.name})" else ""
                Text(
                    text = ingestion.substanceName + customUnitName,
                    style = MaterialTheme.typography.titleMedium
                )
                TimeText(
                    time = ingestion.time,
                    timeDisplayOption = timeDisplayOption,
                    startTime = startTime,
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = ingestionWithCompanionAndCustomUnit.doseDescription,
                        style = MaterialTheme.typography.titleSmall)
                    Text(text = " " + ingestion.administrationRoute.displayText,
                        style = MaterialTheme.typography.bodySmall)
                }
                val numDots = ingestionElement.numDots
                if (numDots != null) {
                    DotRows(numDots = numDots)
                }
            }
            val note = ingestion.notes
            if (!note.isNullOrBlank()) {
                Text(text = note, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}