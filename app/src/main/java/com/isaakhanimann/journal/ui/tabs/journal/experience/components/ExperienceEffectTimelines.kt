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

package com.isaakhanimann.journal.ui.tabs.journal.experience.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.ui.tabs.journal.experience.models.IngestionElement
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.AllTimelines
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.DataForOneRating
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.DataForOneTimedNote

@Composable
fun ExperienceEffectTimelines(
    ingestionElements: List<IngestionElement>,
    dataForRatings: List<DataForOneRating>,
    dataForTimedNotes: List<DataForOneTimedNote>,
    modifier: Modifier
) {
    val effectTimelines = remember(ingestionElements) {
        ingestionElements.map { oneElement ->
            val horizontalWeight = if (oneElement.numDots == null) {
                0.5f
            } else if (oneElement.numDots > 4) {
                1f
            } else {
                oneElement.numDots.toFloat() / 4f
            }
            return@map DataForOneEffectLine(
                substanceName = oneElement.ingestionWithCompanionAndCustomUnit.ingestion.substanceName,
                route = oneElement.ingestionWithCompanionAndCustomUnit.ingestion.administrationRoute,
                roaDuration = oneElement.roaDuration,
                height = getHeightBetween0And1(
                    ingestion = oneElement.ingestionWithCompanionAndCustomUnit,
                    allIngestions = ingestionElements.map { it.ingestionWithCompanionAndCustomUnit }
                ),
                horizontalWeight = horizontalWeight,
                color = oneElement.ingestionWithCompanionAndCustomUnit.substanceCompanion?.color
                    ?: AdaptiveColor.RED,
                startTime = oneElement.ingestionWithCompanionAndCustomUnit.ingestion.time
            )
        }
    }
    AllTimelines(
        dataForEffectLines = effectTimelines,
        dataForRatings = dataForRatings,
        dataForTimedNotes = dataForTimedNotes,
        isShowingCurrentTime = true,
        modifier = modifier
    )
}