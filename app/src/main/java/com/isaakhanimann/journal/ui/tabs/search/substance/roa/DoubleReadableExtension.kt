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

package com.isaakhanimann.journal.ui.tabs.search.substance.roa

import java.text.DecimalFormat
import java.math.BigDecimal
import java.math.RoundingMode


fun Double.toReadableString(): String {
    val numberOfSignificantDigits = if (this > 1) 3 else 2
    val roundedNumber = roundToSignificantDigits(this, numberOfSignificantDigits)
    return formatToMaximumFractionDigits(roundedNumber, 6)
}

fun roundToSignificantDigits(value: Double, significantDigits: Int): Double {
    if (value == 0.0) return 0.0
    val bigDecimal = BigDecimal(value)
    val scale = significantDigits - bigDecimal.precision() + bigDecimal.scale()
    return bigDecimal.setScale(scale, RoundingMode.HALF_UP).toDouble()
}

fun formatToMaximumFractionDigits(value: Double, maximumFractionDigits: Int): String {
    val df = DecimalFormat()
    df.isDecimalSeparatorAlwaysShown = false
    df.minimumFractionDigits = 0
    df.maximumFractionDigits = maximumFractionDigits
    df.isGroupingUsed = false

    return df.format(value)
}
