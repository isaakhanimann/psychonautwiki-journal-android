/*
 * Copyright (c) 2024. Isaak Hanimann.
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

package com.isaakhanimann.journal.data.room.experiences.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import java.time.Instant

@Entity
data class CustomUnit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val substanceName: String,
    val name: String,
    val creationDate: Instant = Instant.now(),
    val administrationRoute: AdministrationRoute,
    var dose: Double?,
    var estimatedDoseVariance: Double?,
    var isEstimate: Boolean,
    var isArchived: Boolean,
    var unit: String,
    val originalUnit: String,
    var note: String
) {
    companion object {
        var mdmaSample = CustomUnit(
            substanceName = "MDMA",
            name = "Capsule",
            administrationRoute = AdministrationRoute.ORAL,
            dose = 40.0,
            estimatedDoseVariance = null,
            isEstimate = false,
            isArchived = false,
            unit = "capsule",
            originalUnit = "mg",
            note = "this is a note"
        )

        var twoCBSample = CustomUnit(
            substanceName = "2C-B",
            name = "Pink rocket",
            administrationRoute = AdministrationRoute.ORAL,
            dose = 14.0,
            estimatedDoseVariance = 4.0,
            isEstimate = true,
            isArchived = false,
            unit = "pill",
            originalUnit = "mg",
            note = "this is a note"
        )
    }
}
