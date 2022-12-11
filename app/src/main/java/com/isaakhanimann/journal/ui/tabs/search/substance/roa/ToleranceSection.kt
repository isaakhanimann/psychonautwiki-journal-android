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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.data.substances.classes.Tolerance
import com.isaakhanimann.journal.ui.tabs.search.substance.ArrowDown

@Preview(showBackground = true)
@Composable
fun ToleranceSectionPreview() {
    ToleranceSection(
        tolerance = Tolerance(
            full = "with prolonged use",
            half = "two weeks",
            zero = "1 month"
        ),
        crossTolerances = listOf(
            "dopamine",
            "stimulant"
        ),
    )
}

@Composable
fun ToleranceSection(
    tolerance: Tolerance?,
    crossTolerances: List<String>,
    modifier: Modifier = Modifier
) {
    if (tolerance != null || crossTolerances.isNotEmpty()) {
        Column(modifier) {
            if (tolerance != null) {
                val labelWidth = 40.dp
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ArrowDown(
                        strokeWidth = 2f,
                        fractionWhenHeadStarts = 9f/10,
                        width = 5.dp,
                        height = 30.dp
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (tolerance.full != null) {
                                Text(
                                    text = "full:",
                                    modifier = Modifier.width(labelWidth)
                                )
                                Text(text = tolerance.full)
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (tolerance.half != null) {
                                Text(
                                    text = "half:",
                                    modifier = Modifier.width(labelWidth)
                                )
                                Text(text = tolerance.half)
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (tolerance.zero != null) {
                                Text(
                                    text = "zero:",
                                    modifier = Modifier.width(labelWidth)
                                )
                                Text(text = tolerance.zero)
                            }
                        }
                    }
                }
                Text(text = "* zero is the time until tolerance is like the first time.")
            }
            if (crossTolerances.isNotEmpty()) {
                val names = crossTolerances.map { it }.distinct()
                    .joinToString(separator = ", ")
                Text(text = "Cross tolerance with $names.")
            }
        }

    }

}