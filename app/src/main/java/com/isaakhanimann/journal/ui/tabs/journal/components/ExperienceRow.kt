/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.journal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.data.room.experiences.relations.ExperienceWithIngestionsCompanionsAndRatings
import com.isaakhanimann.journal.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.journal.ui.theme.horizontalPadding
import com.isaakhanimann.journal.ui.utils.getStringOfPattern

@Preview(showBackground = true)
@Composable
fun ExperienceRow(
    @PreviewParameter(ExperienceWithIngestionsCompanionsAndRatingsPreviewProvider::class) experienceWithIngestionsCompanionsAndRatings: ExperienceWithIngestionsCompanionsAndRatings,
    navigateToExperienceScreen: () -> Unit = {},
    isTimeRelativeToNow: Boolean = true
) {
    Row(
        modifier = Modifier
            .clickable {
                navigateToExperienceScreen()
            }
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(horizontal = horizontalPadding, vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val ingestions = experienceWithIngestionsCompanionsAndRatings.ingestionsWithCompanions
        val experience = experienceWithIngestionsCompanionsAndRatings.experience
        Box(contentAlignment = Alignment.Center) {
            ExperienceCircle(ingestions = ingestions)
            if (experience.isFavorite) {
                Icon(imageVector = Icons.Filled.Star, contentDescription = "Is Favorite")
            }
        }
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                val timeStyle = MaterialTheme.typography.labelMedium
                if (isTimeRelativeToNow) {
                    RelativeDateTextNew(
                        dateTime = experienceWithIngestionsCompanionsAndRatings.sortInstant,
                        style = timeStyle
                    )
                } else {
                    Text(
                        text = experienceWithIngestionsCompanionsAndRatings.sortInstant.getStringOfPattern("dd MMM yy"),
                        style = timeStyle
                    )
                }
                val location = experience.location
                if (location != null) {
                    Text(
                        text = location.name,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            Text(
                text = experience.title,
                style = MaterialTheme.typography.titleMedium,
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                val substanceNames = remember(ingestions) {
                    ingestions.map { it.ingestion.substanceName }.distinct()
                        .joinToString(separator = ", ")
                }
                if (substanceNames.isNotEmpty()) {
                    Text(text = substanceNames)
                } else {
                    Text(
                        text = "No substance yet",
                    )
                }
                val rating = experienceWithIngestionsCompanionsAndRatings.rating?.sign
                if (rating != null) {
                    Text(text = rating)
                }
            }
        }
    }
}

@Composable
fun ExperienceCircle(ingestions: List<IngestionWithCompanion>, circleSize: Dp = 35.dp) {
    val isDarkTheme = isSystemInDarkTheme()
    if (ingestions.size >= 2) {
        val brush = remember(ingestions) {
            val colors =
                ingestions.map { it.substanceCompanion!!.color.getComposeColor(isDarkTheme) }
            val twiceColors = colors.flatMap { listOf(it, it) } + colors.first()
            Brush.sweepGradient(twiceColors)
        }
        Box(
            modifier = Modifier
                .size(circleSize)
                .clip(CircleShape)
                .background(brush),
        ) {}
    } else if (ingestions.size == 1) {
        Box(
            modifier = Modifier
                .size(circleSize)
                .clip(CircleShape)
                .background(
                    ingestions.first().substanceCompanion!!.color.getComposeColor(
                        isDarkTheme
                    )
                ),
        ) {}
    } else {
        Box(
            modifier = Modifier
                .size(circleSize)
                .clip(CircleShape)
                .background(Color.LightGray.copy(0.1f)),
        ) {}
    }
}