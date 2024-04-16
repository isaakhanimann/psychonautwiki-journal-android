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
import kotlin.math.pow
import kotlin.math.sqrt

data class SubstanceRouteSuggestion(
    val color: AdaptiveColor,
    val route: AdministrationRoute,
    val substanceName: String,
    val customSubstanceId: Int?,
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
    val calculatedDose: Double? get() = customUnit.dose?.let { dosePerUnit ->
        dose * dosePerUnit
    }

    // https://www.mathsisfun.com/data/standard-deviation.html
    // https://en.m.wikipedia.org/wiki/Distribution_of_the_product_of_two_random_variables
    // Var(X*Y) = (Var(X) + E(X)^2)*(Var(Y) + E(Y)^2) - E(X)^2 * E(Y)^2
    val calculatedDoseStandardDeviation: Double?
        get() {
            return customUnit.dose?.let { expectationY ->
                val standardDeviationY = if (customUnit.isEstimate) (customUnit.estimatedDoseStandardDeviation ?: 0.0) else 0.0
                val expectationX = dose
                val standardDeviationX = if (isEstimate) (estimatedDoseStandardDeviation ?: 0.0) else 0.0
                val sum1 = standardDeviationX.pow(2) + expectationX.pow(2)
                val sum2 = standardDeviationY.pow(2) + expectationY.pow(2)
                val expectations = expectationX.pow(2) * expectationY.pow(2)
                val productVariance = sum1*sum2 - expectations
                if (productVariance > 0.0000001) {
                    return sqrt(productVariance)
                } else {
                    return null
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
                "${dose.toReadableString()}±${estimatedDoseStandardDeviation.toReadableString()} ${customUnit.unit.asPlural(dose)}"
            } else {
                "~$description"
            }
        } else {
            description
        }
    }
}

fun Double.toStringWith(unit: String): String {
    return if (this != 1.0 && !unit.endsWith("s")) {
        "${this.toReadableString()} ${unit}s"
    } else {
        "${this.toReadableString()} $unit"
    }
}

fun String.asPlural(basedOn: Double): String {
    return if (basedOn != 1.0 && !this.endsWith("s")) {
        "${this}s"
    } else {
        this
    }
}

