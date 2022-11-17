/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
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

const val shapeAlpha = 0.3f
