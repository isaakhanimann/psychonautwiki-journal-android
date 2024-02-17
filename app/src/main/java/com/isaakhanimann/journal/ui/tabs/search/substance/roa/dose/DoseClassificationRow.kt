/*
 * Copyright (c) 2024. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.search.substance.roa.dose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import com.isaakhanimann.journal.data.substances.classes.roa.DoseClass
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.toReadableString


@Preview
@Composable
fun DoseClassificationPreview() {
    DoseClassificationRow(
        lightMin = 20.0,
        commonMin = 50.0,
        strongMin = 90.0,
        heavyMin = 120.0,
        unit = "mg"
    )
}
@Composable
fun DoseClassificationRow(
    lightMin: Double?,
    commonMin: Double?,
    strongMin: Double?,
    heavyMin: Double?,
    unit: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val isDarkTheme = isSystemInDarkTheme()
        val threshColor = DoseClass.THRESHOLD.getComposeColor(isDarkTheme)
        val lightColor = DoseClass.LIGHT.getComposeColor(isDarkTheme)
        val commonColor = DoseClass.COMMON.getComposeColor(isDarkTheme)
        val strongColor = DoseClass.STRONG.getComposeColor(isDarkTheme)
        val heavyColor = DoseClass.HEAVY.getComposeColor(isDarkTheme)
        val labelStyle = MaterialTheme.typography.labelSmall
        val numberStyle = MaterialTheme.typography.labelLarge
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.textBrush(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        threshColor, lightColor
                    )
                )
            )
        ) {
            Text(text = lightMin?.toReadableString() ?: "..", style = numberStyle)
            Text("thresh  ", style = labelStyle)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("-", color = lightColor)
            Text("light", style = labelStyle, color = lightColor)
        }
        Text(
            text = commonMin?.toReadableString() ?: "..",
            style = numberStyle,
            modifier = Modifier.textBrush(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        lightColor, commonColor
                    )
                )
            )
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("-", color = commonColor, style = numberStyle)
            Text("common", style = labelStyle, color = commonColor)
        }
        Text(
            text = strongMin?.toReadableString() ?: "..",
            style = numberStyle,
            modifier = Modifier.textBrush(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        commonColor, strongColor
                    )
                )
            )
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("-", color = strongColor, style = numberStyle)
            Text("strong", style = labelStyle, color = strongColor)
        }
        Text(
            text = heavyMin?.toReadableString() ?: "..",
            style = numberStyle,
            modifier = Modifier.textBrush(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        strongColor, heavyColor
                    )
                )
            )
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("-", color = heavyColor, style = numberStyle)
            Text("heavy", style = labelStyle, color = heavyColor)
        }
        Text(text = unit, style = numberStyle)
    }
}