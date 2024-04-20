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

package com.isaakhanimann.journal.ui.tabs.journal.experience.rating

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.data.room.experiences.entities.ShulginRatingOption
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.CardWithTitle

@Preview
@Composable
fun RatingsExplanationSection() {
    CardWithTitle(title = "Longer explanations") {
        val ratings = ShulginRatingOption.values()
        ratings.forEach { oneRating ->
            Column(modifier = Modifier.padding(vertical = 5.dp)) {
                Text(
                    text = oneRating.sign,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = oneRating.longDescription,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
