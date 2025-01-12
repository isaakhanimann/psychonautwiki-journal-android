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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.data.room.experiences.entities.CustomUnit
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun CustomUnitRowAddIngestion(
    customUnit: CustomUnit,
    navigateToCustomUnitChooseDose: (customUnitId: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .clickable {
                navigateToCustomUnitChooseDose(customUnit.id)
            }
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = horizontalPadding),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "${customUnit.substanceName} ${customUnit.administrationRoute.displayText}, ${customUnit.name}",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "${customUnit.getDoseOfOneUnitDescription()} per ${customUnit.unit}",
            style = MaterialTheme.typography.titleSmall
        )
    }
}