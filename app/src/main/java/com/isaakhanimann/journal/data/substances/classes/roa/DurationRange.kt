/*
 * Copyright (c) 2022. Free Software Foundation, Inc.
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

data class DurationRange(
    val min: Float?,
    val max: Float?,
    val units: DurationUnits?
) {
    val text
        get() = "${min.toString().removeSuffix(".0")}-${
            max.toString().removeSuffix(".0")
        }${units?.shortText ?: ""}"

    val minInSec: Float? = if (units != null) min?.times(units.inSecondsMultiplier) else null
    val maxInSec: Float? = if (units != null) max?.times(units.inSecondsMultiplier) else null

    fun interpolateAtValueInSeconds(value: Float): Float? {
        if (min == null || max == null || units == null) return null
        val diff = max - min
        val withUnit = min + diff.times(value)
        return withUnit.times(units.inSecondsMultiplier)
    }
}