/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
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