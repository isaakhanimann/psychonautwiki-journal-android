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

package com.isaakhanimann.journal.ui.tabs.journal.experience.models

import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.toReadableString

data class CumulativeDose(
    val substanceName: String,
    val cumulativeDose: Double,
    val units: String,
    val isEstimate: Boolean,
    val cumulativeDoseVariance: Double?,
    val numDots: Int?,
    val route: AdministrationRoute
) {
    val doseDescription: String get()
    {
        val description = cumulativeDose.toReadableString() + " $units"
        return if (isEstimate) {
            if (cumulativeDoseVariance != null) {
                "${cumulativeDose.toReadableString()}±${cumulativeDoseVariance.toReadableString()} $units"
            } else {
                "~$description"
            }
        } else {
            description
        }
    }
}