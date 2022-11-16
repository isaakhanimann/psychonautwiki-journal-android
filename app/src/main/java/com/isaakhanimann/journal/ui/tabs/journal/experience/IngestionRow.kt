/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
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
import com.isaakhanimann.journal.data.substances.classes.roa.DoseClass
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
                    Text(text = ingestion.substanceName, style = MaterialTheme.typography.titleMedium)
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
                    val doseClass = ingestionElement.doseClass
                    if (doseClass != null) {
                        DotRow(doseClass = doseClass)
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
fun DotRow(doseClass: DoseClass) {
    val dotSize = 10.dp
    val horizontalPadding = 1.dp
    Row(modifier = Modifier.padding(vertical = 3.dp)) {
        List(doseClass.numDots, init = { it }).forEach { _ ->
            Box(
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onBackground)
            )
        }
        val numEmpty = DoseClass.values().size - doseClass.numDots
        List(numEmpty, init = { it }).forEach { _ ->
            Box(
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
                    .size(dotSize)
                    .clip(CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
            )
        }
    }
}