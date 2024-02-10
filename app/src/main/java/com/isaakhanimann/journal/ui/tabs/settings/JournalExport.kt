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

package com.isaakhanimann.journal.ui.tabs.settings

import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.CustomSubstance
import com.isaakhanimann.journal.data.room.experiences.entities.ShulginRatingOption
import com.isaakhanimann.journal.data.room.experiences.entities.StomachFullness
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class JournalExport(
    val experiences: List<ExperienceSerializable>,
    val substanceCompanions: List<SubstanceCompanion>,
    val customSubstances: List<CustomSubstance>
)

@Serializable
data class ExperienceSerializable(
    val title: String,
    val text: String,
    @Serializable(with= InstantSerializer::class) val creationDate: Instant = Instant.now(),
    @Serializable(with= InstantSerializer::class) val sortDate: Instant,
    val isFavorite: Boolean = false,
    val ingestions: List<IngestionSerializable> = emptyList(),
    val location: LocationSerializable? = null,
    val ratings: List<RatingSerializable> = emptyList(),
    val timedNotes: List<TimedNoteSerializable> = emptyList()
)

@Serializable
data class RatingSerializable(
    @Serializable(with= ShulginRatingOptionSerializer::class) val option: ShulginRatingOption,
    @Serializable(with= InstantSerializer::class) var time: Instant? = null,
    @Serializable(with= InstantSerializer::class) var creationDate: Instant? = Instant.now()
)

@Serializable
data class IngestionSerializable(
    val substanceName: String,
    @Serializable(with= InstantSerializer::class) var time: Instant,
    @Serializable(with= InstantSerializer::class) var creationDate: Instant? = Instant.now(),
    val administrationRoute: AdministrationRoute,
    var dose: Double? = null,
    var isDoseAnEstimate: Boolean,
    var estimatedDoseVariance: Double? = null,
    var units: String? = null,
    var notes: String? = null,
    var stomachFullness: StomachFullness? = null,
    var consumerName: String? = null,
    var customUnitId: Int? = null
)

@Serializable
data class LocationSerializable(
    val name: String,
    val latitude: Double? = null,
    val longitude: Double? = null
)

@Serializable
data class TimedNoteSerializable(
    @Serializable(with= InstantSerializer::class) var creationDate: Instant,
    @Serializable(with= InstantSerializer::class) var time: Instant,
    var note: String,
    var color: AdaptiveColor,
    var isPartOfTimeline: Boolean
)