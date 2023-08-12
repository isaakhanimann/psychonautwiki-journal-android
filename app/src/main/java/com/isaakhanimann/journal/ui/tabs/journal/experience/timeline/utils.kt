/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.journal.experience.timeline

import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

private val strokeWidthInDp = 5.dp

val Density.normalStroke: Stroke
    get() {
        val width = strokeWidthInDp.toPx()
        return Stroke(
            width = width,
            cap = StrokeCap.Round,
            pathEffect = PathEffect.cornerPathEffect(15f)
        )
    }

val Density.shapeWidth: Float
    get() = strokeWidth * 3

val Density.strokeWidth: Float get() = strokeWidthInDp.toPx()

val Density.dottedStroke: Stroke
    get() {
        val length = strokeWidth * 4
        val space = strokeWidth * 3
        return Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(space, length))
        )
    }

private val ingestionDotRadiusInDp = 7.dp

val Density.ingestionDotRadius: Float get() = ingestionDotRadiusInDp.toPx()


const val shapeAlpha = 0.3f
