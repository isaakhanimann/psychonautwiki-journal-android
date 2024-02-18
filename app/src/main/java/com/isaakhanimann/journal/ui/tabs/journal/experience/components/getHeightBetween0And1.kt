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

import com.isaakhanimann.journal.data.room.experiences.relations.IngestionWithCompanionAndCustomUnit

fun getHeightBetween0And1(
    ingestion: IngestionWithCompanionAndCustomUnit,
    allIngestions: List<IngestionWithCompanionAndCustomUnit>
): Float {
    val max = allIngestions
        .filter { it.ingestion.substanceName == ingestion.ingestion.substanceName }
        .mapNotNull { it.pureDose }
        .maxOrNull()
    return ingestion.pureDose.let { doseSnap ->
        if (doseSnap == 0.0) {
            0.01f
        } else if (max == null || max == 0.0 || doseSnap == null) {
            1f
        } else {
            doseSnap.div(max).toFloat()
        }
    }
}