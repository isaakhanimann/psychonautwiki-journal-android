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
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDose
import kotlin.math.max

fun getStrengthRelativeToCommonDose(
    ingestion: IngestionWithCompanionAndCustomUnit,
    allIngestions: List<IngestionWithCompanionAndCustomUnit>,
    roaDose: RoaDose?,
): Double {
    val allKnownDoses = allIngestions
        .filter { it.ingestion.substanceName == ingestion.ingestion.substanceName && it.ingestion.administrationRoute == ingestion.ingestion.administrationRoute}.mapNotNull {
            it.pureDose
        }
    val sumDose = allKnownDoses.reduceOrNull { acc, d -> acc + d } ?: 0.0
    val averageDose = sumDose / max(1.0, allKnownDoses.size.toDouble())
    val commonDose = roaDose?.averageCommonDose ?: averageDose

    return ingestion.pureDose?.let { doseSnap ->
        doseSnap / commonDose
    } ?: 1.0
}