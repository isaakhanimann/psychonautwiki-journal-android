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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.ui.tabs.search.SubstanceModel
import com.isaakhanimann.journal.ui.tabs.search.substancerow.SubstanceModelPreviewProvider
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Preview(showBackground = true)
@Composable
fun SubstanceRowAddIngestionPreview(
    @PreviewParameter(SubstanceModelPreviewProvider::class) substanceModel: SubstanceModel
) {
    SubstanceRowAddIngestion(substanceModel = substanceModel, onTap = {})
}

@Composable
fun SubstanceRowAddIngestion(
    substanceModel: SubstanceModel,
    onTap: (substanceName: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onTap(substanceModel.name)
            }
            .padding(horizontal = horizontalPadding, vertical = 8.dp),
    ) {
        Text(
            text = substanceModel.name,
            style = MaterialTheme.typography.titleMedium,
        )
        if (substanceModel.commonNames.isNotEmpty()) {
            val commaSeparatedNames = substanceModel.commonNames.joinToString(separator = ", ")
            Text(text = commaSeparatedNames, style = MaterialTheme.typography.bodySmall)
        }
    }
}