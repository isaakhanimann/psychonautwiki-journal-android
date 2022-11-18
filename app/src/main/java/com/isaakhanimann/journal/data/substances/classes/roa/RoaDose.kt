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

package com.isaakhanimann.journal.data.substances.classes.roa

data class RoaDose(
    val units: String?,
    val threshold: Double?,
    val light: DoseRange?,
    val common: DoseRange?,
    val strong: DoseRange?,
    val heavy: Double?
) {
    fun getDoseClass(ingestionDose: Double?, ingestionUnits: String? = units): DoseClass? {
        if (ingestionUnits != units || ingestionDose == null) return null
        return if (threshold != null && ingestionDose < threshold) {
            DoseClass.THRESHOLD
        } else if (light?.isValueInRange(ingestionDose) == true) {
            DoseClass.LIGHT
        } else if (common?.isValueInRange(ingestionDose) == true) {
            DoseClass.COMMON
        } else if (strong?.isValueInRange(ingestionDose) == true) {
            DoseClass.STRONG
        } else if (heavy != null && ingestionDose > heavy) {
            DoseClass.HEAVY
        } else {
            null
        }
    }

    val shouldDefinitelyUseVolumetricDosing: Boolean
        get() {
            if (units == "µg") return true
            return if (units == "mg") {
                val sample = common?.min
                sample != null && sample < 15
            } else {
                false
            }
        }
}