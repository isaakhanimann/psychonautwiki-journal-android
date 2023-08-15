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

package com.isaakhanimann.journal.ui.tabs.journal.experience.models

import com.isaakhanimann.journal.data.room.experiences.entities.ShulginRating
import com.isaakhanimann.journal.data.room.experiences.entities.TimedNote
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.interactions.Interaction
import java.time.Instant

data class OneExperienceScreenModel(
    val isFavorite: Boolean,
    val title: String,
    val firstIngestionTime: Instant,
    val notes: String,
    val locationName: String,
    val isShowingAddIngestionButton: Boolean,
    val ingestionElements: List<IngestionElement>,
    val cumulativeDoses: List<CumulativeDose>,
    val interactions: List<Interaction>,
    val interactionExplanations: List<InteractionExplanation>,
    val ratings: List<ShulginRating>,
    val timedNotes: List<TimedNote>,
    val consumersWithIngestions: List<ConsumerWithIngestions>
)