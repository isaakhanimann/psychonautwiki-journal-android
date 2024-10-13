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

package com.isaakhanimann.journal.ui.tabs.journal.experience.components.rating

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.isaakhanimann.journal.data.room.experiences.entities.ShulginRating

@Composable
fun TimedRatingRow(
    modifier: Modifier = Modifier,
    ratingSign: String,
    timeText: @Composable () -> Unit
) {
    RatingRow(
        modifier = modifier,
        ratingSign = ratingSign,
        content = timeText
    )
}

@Composable
fun OverallRatingRow(
    modifier: Modifier = Modifier,
    ratingSign: String
) {
    RatingRow(
        modifier = modifier,
        ratingSign = ratingSign
    ) {
        Text(
            text = "Overall rating",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Composable
private fun RatingRow(
    modifier: Modifier = Modifier,
    ratingSign: String,
    content: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        content()
        Text(
            text = ratingSign,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview
@Composable
private fun RatingRowPreview(@PreviewParameter(RatingPreviewProvider::class) rating: ShulginRating) {
    RatingRow(
        ratingSign = rating.option.sign,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Sat 7:34")
    }
}