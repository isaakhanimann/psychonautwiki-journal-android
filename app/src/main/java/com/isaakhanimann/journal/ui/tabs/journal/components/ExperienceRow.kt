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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.data.room.experiences.relations.ExperienceWithIngestionsCompanionsAndRatings
import com.isaakhanimann.journal.data.room.experiences.relations.IngestionWithCompanionAndCustomUnit
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
            .height(IntrinsicSize.Min)
            .padding(horizontal = horizontalPadding, vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val ingestions = experienceWithIngestionsCompanionsAndRatings.ingestionsWithCompanions
        val experience = experienceWithIngestionsCompanionsAndRatings.experience
        ColorRectangle(ingestions = ingestions)
        Column {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = experience.title,
                    style = MaterialTheme.typography.titleMedium,
                )
                if (experience.isFavorite) {
                    Icon(imageVector = Icons.Filled.Star, contentDescription = "Is favorite")
                }
            }
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
            val consumerNames = remember(ingestions) {
                ingestions.mapNotNull { it.ingestion.consumerName }.distinct()
                    .joinToString(separator = ", ")
            }
            if (consumerNames.isNotEmpty()) {
                Text(text = "With: $consumerNames", style = MaterialTheme.typography.labelSmall)
            }
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
                        text = experienceWithIngestionsCompanionsAndRatings.sortInstant.getStringOfPattern(
                            "EEE, dd MMM yyyy"
                        ),
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
        }
    }
}

@Composable
fun ColorRectangle(ingestions: List<IngestionWithCompanionAndCustomUnit>) {
    val isDarkTheme = isSystemInDarkTheme()
    val width = 11.dp
    val cornerRadius = 3.dp
    if (ingestions.size >= 2) {
        val brush = remember(ingestions) {
            val colors =
                ingestions.map { it.substanceCompanion!!.color.getComposeColor(isDarkTheme) }
            Brush.verticalGradient(colors = colors)
        }
        Box(
            modifier = Modifier
                .width(width)
                .fillMaxHeight()
                .clip(RoundedCornerShape(cornerRadius))
                .background(brush),
        ) {}
    } else if (ingestions.size == 1) {
        Box(
            modifier = Modifier
                .width(width)
                .fillMaxHeight()
                .clip(RoundedCornerShape(cornerRadius))
                .background(
                    ingestions.first().substanceCompanion!!.color.getComposeColor(
                        isDarkTheme
                    )
                ),
        ) {}
    } else {
        Box(
            modifier = Modifier
                .width(width)
                .fillMaxHeight()
                .clip(RoundedCornerShape(cornerRadius))
                .background(Color.LightGray.copy(0.1f)),
        ) {}
    }
}