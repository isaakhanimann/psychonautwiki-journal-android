/*
 * Copyright (c) 2024. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.dose.customunit

import androidx.compose.runtime.Composable
import com.isaakhanimann.journal.data.room.experiences.entities.CustomUnit
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDose
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.dose.DoseClassificationRow
import kotlin.math.round

@Composable
fun CustomUnitRoaDoseView(roaDose: RoaDose, customUnit: CustomUnit) {
    fun convertToNewUnit(oldDose: Double?): Double? {
        return customUnit.dose?.let { dosePerUnit ->
            if (oldDose !=null) {
                return@let roundToOneDecimalPlace(oldDose/dosePerUnit)
            } else {
                return@let null
            }
        }
    }
    DoseClassificationRow(
        lightMin = convertToNewUnit(roaDose.lightMin),
        commonMin = convertToNewUnit(roaDose.commonMin),
        strongMin = convertToNewUnit(roaDose.strongMin),
        heavyMin = convertToNewUnit(roaDose.heavyMin),
        unit = customUnit.unit
    )
}

fun roundToOneDecimalPlace(num: Double): Double {
    return round(num * 10) / 10
}