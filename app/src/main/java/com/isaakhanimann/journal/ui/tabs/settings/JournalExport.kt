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

import com.isaakhanimann.journal.data.room.experiences.entities.CustomSubstance
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
    val ingestions: List<IngestionSerializable>,
    val location: LocationSerializable?
)

@Serializable
data class IngestionSerializable(
    val substanceName: String,
    @Serializable(with= InstantSerializer::class) var time: Instant,
    @Serializable(with= InstantSerializer::class) var creationDate: Instant? = Instant.now(),
    val administrationRoute: AdministrationRoute,
    var dose: Double?,
    var isDoseAnEstimate: Boolean,
    var units: String?,
    var notes: String?,
    var stomachFullness: StomachFullness?
)

@Serializable
data class LocationSerializable(
    val name: String,
    val latitude: Double?,
    val longitude: Double?
)