/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.stats.substancecompanion

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun TimeArrowUpPreview() {
    TimeArrowUp(timeText = "4.5 hours")
}

@Composable
fun TimeArrowUp(timeText: String) {
    val color = MaterialTheme.colorScheme.onBackground
    val strokeWidth = 4f
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(
            modifier = Modifier
                .width(6.dp)
                .height(20.dp)
                .background(Color.Red.copy(alpha = 0f)) // needed for fixing preview
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
                start = Offset(x = canvasWidth / 2, y = 0f),
                end = Offset(x = 0f, y = canvasHeight / 4),
                strokeWidth = strokeWidth,
                color = color,
                cap = StrokeCap.Round,
            )
            drawLine(
                start = Offset(x = canvasWidth / 2, y = 0f),
                end = Offset(x = canvasWidth, y = canvasHeight / 4),
                strokeWidth = strokeWidth,
                color = color,
                cap = StrokeCap.Round,
            )
        }
        Text(text = timeText)
        Canvas(
            modifier = Modifier
                .width(6.dp)
                .height(20.dp)
                .background(Color.Red.copy(alpha = 0f)) // needed for fixing preview
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
        }
    }
}