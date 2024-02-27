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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.models

import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.CustomUnit
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.toReadableString
import java.time.Instant

data class SubstanceRouteSuggestion(
    val color: AdaptiveColor,
    val route: AdministrationRoute,
    val substanceName: String,
    val isCustomSubstance: Boolean,
    val dosesAndUnit: List<DoseAndUnit>,
    val customUnitDoses: List<CustomUnitDose>,
    val customUnits: List<CustomUnit>,
    val lastUsed: Instant
)

data class DoseAndUnit(
    val dose: Double?,
    val unit: String,
    val isEstimate: Boolean,
    val estimatedDoseStandardDeviation: Double?
)

data class CustomUnitDose(
    val dose: Double,
    val isEstimate: Boolean,
    val estimatedDoseStandardDeviation: Double?,
    val customUnit: CustomUnit
) {
    val calculatedDose: Double?
        get() {
            return customUnit.dose?.let { dosePerUnit ->
                return customUnit.estimatedDoseStandardDeviation?.let { customDoseDeviation ->
                    if (isEstimate && estimatedDoseStandardDeviation != null) {
                        val minDose = dose - estimatedDoseStandardDeviation
                        val maxDose = dose + estimatedDoseStandardDeviation
                        val minCustomDose = dosePerUnit - customDoseDeviation
                        val maxCustomDose = dosePerUnit + customDoseDeviation
                        val minResult = minDose * minCustomDose
                        val maxResult = maxDose * maxCustomDose
                        return (minResult + maxResult) / 2
                    } else {
                        return dose * dosePerUnit
                    }
                } ?: (dose * dosePerUnit)
            }
        }

    val calculatedDoseStandardDeviation: Double?
        get() {
            return customUnit.dose?.let { dosePerUnit ->
                return customUnit.estimatedDoseStandardDeviation?.let { customDoseDeviation ->
                    if (isEstimate && estimatedDoseStandardDeviation != null) {
                        val minDose = dose - estimatedDoseStandardDeviation
                        val maxDose = dose + estimatedDoseStandardDeviation
                        val minCustomDose = dosePerUnit - customDoseDeviation
                        val maxCustomDose = dosePerUnit + customDoseDeviation
                        val minResult = minDose * minCustomDose
                        val maxResult = maxDose * maxCustomDose
                        val result = (minResult + maxResult) / 2
                        return maxResult - result
                    } else {
                        return dose * customDoseDeviation
                    }
                } ?: run {
                    if (estimatedDoseStandardDeviation != null && isEstimate) {
                        return estimatedDoseStandardDeviation * dosePerUnit
                    } else {
                        return null
                    }
                }
            }
        }

    // 20 mg or 20±2 mg
    val calculatedDoseDescription: String? get()
    {
        return calculatedDose?.let { calculatedDoseUnwrapped ->
            calculatedDoseStandardDeviation?.let {
                return "${calculatedDoseUnwrapped.toReadableString()}±${it.toReadableString()} ${customUnit.originalUnit}"
            } ?: run {
                val description = "${calculatedDoseUnwrapped.toReadableString()} ${customUnit.originalUnit}"
                if (isEstimate || customUnit.isEstimate) {
                    return "~$description"
                } else {
                    return description
                }
            }
        }
    }

    // 2 pills
    val doseDescription: String get()
    {
        val description = dose.toStringWith(unit = customUnit.unit)
        return if (isEstimate) {
            if (estimatedDoseStandardDeviation != null) {
                "${dose.toReadableString()}±${estimatedDoseStandardDeviation.toStringWith(unit = customUnit.unit)}"
            } else {
                "~$description"
            }
        } else {
            description
        }
    }
}

fun Double.toStringWith(unit: String): String {
    return if (this > 1 && !unit.endsWith("s")) {
        "${this.toReadableString()} ${unit}s"
    } else {
        "${this.toReadableString()} $unit"
    }
}

