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

package com.isaakhanimann.journal.ui.tabs.search.substance

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Preview(showBackground = true)
@Composable
fun ArrowDown(
    strokeWidth: Float = 4f,
    fractionWhenHeadStarts: Float = 9f / 10,
    width: Dp = 6.dp,
    height: Dp = 20.dp
) {
    val color = MaterialTheme.colorScheme.onBackground
    Canvas(
        modifier = Modifier
            .width(width)
            .height(height)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawLine(
            start = Offset(x = canvasWidth / 2, y = 0f),
            end = Offset(x = canvasWidth / 2, y = canvasHeight),
            strokeWidth = strokeWidth,
            color = color,
            cap = StrokeCap.Round,
        )
        drawLine(
            start = Offset(x = canvasWidth / 2, y = canvasHeight),
            end = Offset(x = 0f, y = fractionWhenHeadStarts * canvasHeight),
            strokeWidth = strokeWidth,
            color = color,
            cap = StrokeCap.Round,
        )
        drawLine(
            start = Offset(x = canvasWidth / 2, y = canvasHeight),
            end = Offset(x = canvasWidth, y = fractionWhenHeadStarts * canvasHeight),
            strokeWidth = strokeWidth,
            color = color,
            cap = StrokeCap.Round,
        )
    }
}