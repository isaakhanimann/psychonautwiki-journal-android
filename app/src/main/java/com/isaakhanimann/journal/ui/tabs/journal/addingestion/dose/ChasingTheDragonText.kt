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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.dose

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ChasingTheDragonText(
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium,
) {
    Column(
        modifier = modifier
    ) {
        Text(text = "Chasing the dragon (foiling)", style = titleStyle)
        Text(text = """
            This is likely the least clinically delivered route of administration. An overdose caused by chasing the dragon is hard to predict because this technique doesn't deliver a standardized dosage. It's virtually impossible even for skilled users to know how much of the substance that has been evaporated, burned, and inhaled.
            These combined factors may create a false sense of security when a given dose seems safe to repeat, but may cause an overdose when all the factors are randomly excluded.
                    """.trimIndent())
    }
}

