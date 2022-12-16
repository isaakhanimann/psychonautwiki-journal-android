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

import kotlin.math.floor
import kotlin.math.roundToInt

data class RoaDose(
    val units: String,
    val lightMin: Double?,
    val commonMin: Double?,
    val strongMin: Double?,
    val heavyMin: Double?,
) {
    fun getDoseClass(ingestionDose: Double?, ingestionUnits: String? = units): DoseClass? {
        if (ingestionUnits != units || ingestionDose == null) return null
        return if (lightMin != null && ingestionDose < lightMin) {
            DoseClass.THRESHOLD
        } else if (lightMin != null && commonMin != null && lightMin <= ingestionDose && ingestionDose < commonMin) {
            DoseClass.LIGHT
        } else if (commonMin != null && strongMin != null && commonMin <= ingestionDose && ingestionDose < strongMin) {
            DoseClass.COMMON
        } else if (strongMin != null && heavyMin != null && strongMin <= ingestionDose && ingestionDose < heavyMin) {
            DoseClass.STRONG
        } else if (heavyMin != null && ingestionDose > heavyMin) {
            DoseClass.HEAVY
        } else {
            null
        }
    }

    fun getNumDots(ingestionDose: Double?, ingestionUnits: String? = units): Int? {
        if (ingestionUnits != units || ingestionDose == null) return null
        if (lightMin != null && ingestionDose < lightMin) {
            return 0
        } else if (lightMin != null && commonMin != null && lightMin <= ingestionDose && ingestionDose < commonMin) {
            return 1
        } else if (commonMin != null && strongMin != null && commonMin <= ingestionDose && ingestionDose < strongMin) {
            return 2
        } else if (strongMin != null && heavyMin != null && strongMin <= ingestionDose && ingestionDose < heavyMin) {
            return 3
        } else if (heavyMin != null) {
            return if (ingestionDose > heavyMin) {
                val timesHeavy = floor(ingestionDose.div(heavyMin)).roundToInt()
                val rest = ingestionDose.rem(heavyMin)
                return (timesHeavy * 4) + getNumDotsUpTo4(dose = rest)
            } else {
                floor(ingestionDose / heavyMin).roundToInt()
            }
        } else {
            return null
        }
    }

    private fun getNumDotsUpTo4(dose: Double): Int {
        return if (lightMin != null && dose < lightMin) {
            0
        } else if (lightMin != null && commonMin != null && lightMin <= dose && dose < commonMin) {
            1
        } else if (commonMin != null && strongMin != null && commonMin <= dose && dose < strongMin) {
            2
        } else if (strongMin != null && heavyMin != null && strongMin <= dose && dose < heavyMin) {
            3
        } else if (heavyMin != null) {
            floor(dose / heavyMin).roundToInt()
        } else {
            0
        }
    }

    val shouldUseVolumetricDosing: Boolean
        get() {
            if (units == "µg") return true
            return if (units == "mg") {
                val sample = commonMin ?: strongMin
                sample != null && sample < 15
            } else {
                false
            }
        }
}