/*
 * Copyright (c) 2022. Free Software Foundation, Inc.
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.isaakhanimann.journal.data.substances.classes.roa.DoseClass
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDose
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.toReadableString

@Preview(showBackground = true)
@Composable
fun RoaDoseViewPreview(
    @PreviewParameter(RoaDosePreviewProvider::class) roaDose: RoaDose,
) {
    RoaDoseView(
        roaDose = roaDose,
    )
}

@Composable
fun RoaDoseView(
    roaDose: RoaDose,
    modifier: Modifier = Modifier
) {
    val lightMaxOrCommonMin = roaDose.light?.max ?: roaDose.common?.min
    val commonMaxOrStrongMin = roaDose.common?.max ?: roaDose.strong?.min
    val strongMaxOrHeavy = roaDose.strong?.max ?: roaDose.heavy
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
            Text(text = roaDose.threshold?.toReadableString() ?: "..")
            Text("thresh  ", style = MaterialTheme.typography.bodySmall)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("-", color = lightColor)
            Text("light", style = MaterialTheme.typography.bodySmall, color = lightColor)
        }
        Text(
            text = lightMaxOrCommonMin?.toReadableString() ?: "..", modifier = Modifier.textBrush(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        lightColor, commonColor
                    )
                )
            )
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("-", color = commonColor)
            Text("common", style = MaterialTheme.typography.bodySmall, color = commonColor)
        }
        Text(
            text = commonMaxOrStrongMin?.toReadableString() ?: "..",
            modifier = Modifier.textBrush(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        commonColor, strongColor
                    )
                )
            )
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("-", color = strongColor)
            Text("strong", style = MaterialTheme.typography.bodySmall, color = strongColor)
        }
        Text(
            text = strongMaxOrHeavy?.toReadableString() ?: "..",
            modifier = Modifier.textBrush(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        strongColor, heavyColor
                    )
                )
            )
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("-", color = heavyColor)
            Text("heavy", style = MaterialTheme.typography.bodySmall, color = heavyColor)
        }
        Text(text = roaDose.units ?: "")
    }
}