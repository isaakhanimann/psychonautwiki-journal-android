/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.search

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