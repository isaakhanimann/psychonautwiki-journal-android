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

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.isaakhanimann.journal.data.room.experiences.entities.ShulginRating
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.TimeDisplayOption
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.TimeText
import java.time.Instant
import java.time.temporal.ChronoUnit


@Preview
@Composable
fun RatingRowPreview(@PreviewParameter(RatingPreviewProvider::class) rating: ShulginRating) {
    RatingRow(
        rating = rating,
        timeDisplayOption = TimeDisplayOption.REGULAR,
        startTime = Instant.now().minus(2, ChronoUnit.HOURS),
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun RatingRow(
    rating: ShulginRating,
    timeDisplayOption: TimeDisplayOption,
    startTime: Instant,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        val ratingTime = rating.time
        if (ratingTime == null) {
            Text(
                text = "Overall Rating",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            TimeText(
                time = ratingTime,
                timeDisplayOption = timeDisplayOption,
                startTime = startTime,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Text(
            text = rating.option.sign,
            style = MaterialTheme.typography.titleMedium
        )
    }
}