/*
 * Copyright (c) 2022. Isaak Hanimann.
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

package com.isaakhanimann.journal.data.room.experiences.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.isaakhanimann.journal.data.room.experiences.entities.CustomUnit
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.models.CustomUnitDose
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.toReadableString

data class IngestionWithCompanionAndCustomUnit(
    @Embedded
    var ingestion: Ingestion,

    @Relation(
        parentColumn = "substanceName",
        entityColumn = "substanceName"
    )
    var substanceCompanion: SubstanceCompanion?,

    @Relation(
        parentColumn = "customUnitId",
        entityColumn = "id"
    )
    var customUnit: CustomUnit?
) {

    val originalUnit: String? get() = customUnit?.originalUnit ?: ingestion.units
    val pureDose: Double? get() = customUnitDose?.calculatedDose ?: ingestion.dose

    val isEstimate: Boolean get() = ingestion.isDoseAnEstimate || customUnit?.isEstimate ?: false

    val pureDoseVariance: Double? get() = customUnitDose?.calculatedDoseVariance ?: ingestion.estimatedDoseVariance

    val customUnitDose: CustomUnitDose? get() = ingestion.dose?.let { doseUnwrapped ->
        customUnit?.let { customUnitUnwrapped ->
            CustomUnitDose(
                dose = doseUnwrapped,
                isEstimate = ingestion.isDoseAnEstimate,
                estimatedDoseVariance = ingestion.estimatedDoseVariance,
                customUnit = customUnitUnwrapped)
        }
    }
    val doseDescription: String get() = customUnitDose?.doseDescription ?: ingestion.dose?.let {
        val isEstimateText = if (ingestion.isDoseAnEstimate) "~" else ""
        val doseText = it.toReadableString()
        return@let "$isEstimateText$doseText ${ingestion.units}"
    } ?: "Unknown Dose"
}