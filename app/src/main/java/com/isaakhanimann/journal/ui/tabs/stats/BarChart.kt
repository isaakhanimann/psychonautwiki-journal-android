/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.stats

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BarChart(buckets: List<List<ColorCount>>, startDateText: String) {
    Column {
        val isDarkTheme = isSystemInDarkTheme()
        val tickColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.20f)
        val labelHeight = with(LocalDensity.current) { 12.sp.toPx() }
        Canvas(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .height(150.dp)
        ) {
            val canvasHeightOuter = size.height
            if (canvasHeightOuter > 0) { // for unknown reason canvasHeight can be 0 and then the app crashes
                val maxCount = buckets.maxOf { bucket ->
                    bucket.sumOf { it.count }
                }
                val half = maxCount / 2
                val halfLineHeight = half.toFloat() * canvasHeightOuter / maxCount
                val halfLabelHeight = labelHeight / 2
                val numLettersInLabel = maxCount.toString().length
                val labelWidth = (numLettersInLabel + 1) * labelHeight
                val spaceBetweenLabelAndChart = labelHeight
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        half.toString(),
                        labelWidth - spaceBetweenLabelAndChart,
                        canvasHeightOuter - halfLineHeight + halfLabelHeight,
                        Paint().apply {
                            textSize = labelHeight
                            color = Color.GRAY
                            textAlign = Paint.Align.RIGHT
                        }
                    )
                    drawText(
                        maxCount.toString(),
                        labelWidth - spaceBetweenLabelAndChart,
                        halfLabelHeight,
                        Paint().apply {
                            textSize = labelHeight
                            color = Color.GRAY
                            textAlign = Paint.Align.RIGHT
                        }
                    )
                }
                inset(left = labelWidth, right = 0f, top = 0f, bottom = 0f) {
                    val horizontalLinesWidth = 4f
                    val canvasWidthWithoutLabel = size.width
                    // top line
                    drawLine(
                        color = tickColor,
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = canvasWidthWithoutLabel, y = 0f),
                        strokeWidth = horizontalLinesWidth / 2,
                        cap = StrokeCap.Round
                    )
                    // half line
                    drawLine(
                        color = tickColor,
                        start = Offset(x = 0f, y = canvasHeightOuter - halfLineHeight),
                        end = Offset(
                            x = canvasWidthWithoutLabel,
                            y = canvasHeightOuter - halfLineHeight
                        ),
                        strokeWidth = horizontalLinesWidth / 2,
                        cap = StrokeCap.Round
                    )
                    // bottom line
                    val tickHeight = 6f
                    val numBuckets = buckets.size
                    val spaceBetweenTicks = canvasWidthWithoutLabel / numBuckets
                    val numSpacers = numBuckets + 1
                    drawLine(
                        color = tickColor,
                        start = Offset(x = 0f, y = canvasHeightOuter - tickHeight),
                        end = Offset(
                            x = canvasWidthWithoutLabel,
                            y = canvasHeightOuter - tickHeight
                        ),
                        strokeWidth = horizontalLinesWidth,
                        cap = StrokeCap.Round
                    )
                    // ticks
                    for (index in 0 until numSpacers) {
                        val xSpacer = index * spaceBetweenTicks
                        drawLine(
                            color = tickColor,
                            start = Offset(x = xSpacer, y = canvasHeightOuter),
                            end = Offset(x = xSpacer, y = canvasHeightOuter - tickHeight),
                            strokeWidth = horizontalLinesWidth,
                            cap = StrokeCap.Round
                        )
                    }
                    val percentageOfBucketWidthToSpaceBetweenTicks = 0.7f
                    val bucketWidth =
                        spaceBetweenTicks * percentageOfBucketWidthToSpaceBetweenTicks
                    val spaceWidth = spaceBetweenTicks - bucketWidth
                    inset(left = 0f, top = 0f, right = 0f, bottom = tickHeight) {
                        val canvasHeightInner = size.height
                        buckets.forEachIndexed { index, colorCounts ->
                            val xBucket = (((index * 2f) + 1) / 2) * (spaceWidth + bucketWidth)
                            var yStart = canvasHeightInner
                            colorCounts.forEach { colorCount ->
                                val yLength = colorCount.count * canvasHeightInner / maxCount
                                val yEnd = yStart - yLength
                                val cornerRadius = bucketWidth / 6
                                drawRoundRect(
                                    color = colorCount.color.getComposeColor(isDarkTheme),
                                    topLeft = Offset(x = xBucket - (bucketWidth / 2), y = yEnd),
                                    size = Size(width = bucketWidth, height = yLength),
                                    cornerRadius = CornerRadius(
                                        x = cornerRadius,
                                        y = cornerRadius
                                    )
                                )
                                yStart = yEnd
                            }
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val style = MaterialTheme.typography.bodySmall
            Text(
                text = startDateText,
                style = style,
            )
            Text(
                text = "Now",
                style = style,
            )
        }
    }
}