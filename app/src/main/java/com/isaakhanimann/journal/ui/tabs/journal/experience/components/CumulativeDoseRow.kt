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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.tabs.journal.experience.models.CumulativeDose

@Composable
fun CumulativeDoseRow(cumulativeDose: CumulativeDose, modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = cumulativeDose.substanceName + " ${cumulativeDose.route.displayText}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = cumulativeDose.doseDescription, style = MaterialTheme.typography.titleSmall)

        }
        val numDots = cumulativeDose.numDots
        if (numDots != null) {
            DotRows(numDots = numDots)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CumulativeDoseRowPreview() {
    CumulativeDoseRow(
        cumulativeDose = CumulativeDose(
            substanceName = "Cocaine",
            cumulativeDose = 60.0,
            units = "mg",
            isEstimate = false,
            cumulativeDoseStandardDeviation = 12.0,
            numDots = 6,
            route = AdministrationRoute.INSUFFLATED
        ), modifier = Modifier.fillMaxWidth()
    )
}