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

package com.isaakhanimann.journal.ui.tabs.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun CategoryChipDeletePreview(
    @PreviewParameter(CategoryChipPreviewProvider::class) categoryChipModel: CategoryChipModel
) {
    CategoryChipDelete(
        categoryChipModel = categoryChipModel,
        onClick = {}
    )
}

@Composable
fun CategoryChipDelete(
    categoryChipModel: CategoryChipModel,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(shape = CircleShape)
            .background(color = categoryChipModel.color.copy(alpha = 0.2f))
            .clickable(onClick = onClick)
            .padding(vertical = 3.dp, horizontal = 8.dp)

    ) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = "Remove",
            Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.width(3.dp))
        Text(text = categoryChipModel.chipName)
    }
}