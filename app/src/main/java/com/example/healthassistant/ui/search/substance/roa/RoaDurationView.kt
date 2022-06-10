package com.example.healthassistant.ui.search.substance.roa

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.ui.previewproviders.RoaDurationPreviewProviderForRoaDurationView
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Preview(showBackground = true)
@Composable
fun RoaDurationPreview(
    @PreviewParameter(RoaDurationPreviewProviderForRoaDurationView::class) roaDuration: RoaDuration
) {
    RoaDurationView(roaDuration = roaDuration, maxDuration = 13.toDuration(DurationUnit.HOURS))
}

@Composable
fun RoaDurationView(
    roaDuration: RoaDuration,
    maxDuration: Duration?
) {
    Column {
        val total = roaDuration.total
        val colorTimeLine = MaterialTheme.colors.secondary
        val colorTransparent = colorTimeLine.copy(alpha = 0.1f)
        val strokeWidth = 8f
        val strokeWidthThick = 40f
        if ((total?.min != null) && (total.max != null)) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "total: ${total.text}",
                    textAlign = TextAlign.Center
                )
                Canvas(modifier = Modifier.fillMaxWidth().height(with(LocalDensity.current) { strokeWidthThick.toDp() })) {
                    val canvasWidth = size.width
                    val midHeight = size.height/2
                    val max = maxDuration ?: total.max
                    val minX = (total.min.div(max) * canvasWidth).toFloat()
                    val maxX = (total.max.div(max) * canvasWidth).toFloat()
                    val midX = (minX + maxX) / 2
                    drawLine(
                        start = Offset(x = 0f, y = midHeight),
                        end = Offset(x = midX, y = midHeight),
                        color = colorTimeLine,
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        start = Offset(x = minX, y = midHeight),
                        end = Offset(x = maxX, y = midHeight),
                        color = colorTransparent,
                        strokeWidth = strokeWidthThick,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
        if (roaDuration.afterglow != null) {
            Text("after effects: ${roaDuration.afterglow.text}")
        } else {
            Spacer(modifier = Modifier.height(8.dp))
        }
        val onset = roaDuration.onset
        val comeup = roaDuration.comeup
        val peak = roaDuration.peak
        val offset = roaDuration.offset
        val onsetInterpol = roaDuration.onset?.interpolateAt(0.5)
        val comeupInterpol = roaDuration.comeup?.interpolateAt(0.5)
        val peakInterpol = roaDuration.peak?.interpolateAt(0.5)
        val offsetInterpol = roaDuration.offset?.interpolateAt(0.5)
        val allDurations = listOf(onsetInterpol, comeupInterpol, peakInterpol, offsetInterpol)
        val undefinedCount = allDurations.count { it == null }
        if (maxDuration != null && undefinedCount < 4) {
            val isDarkTheme = isSystemInDarkTheme()
            val density = LocalDensity.current
            val textColor = if (isDarkTheme) android.graphics.Color.WHITE else android.graphics.Color.BLACK
            val textSizeDen = density.run { 30f }
            val textPaintAlignCenter = remember(density) {
                Paint().apply {
                    color = textColor
                    textAlign = Paint.Align.CENTER
                    textSize = textSizeDen
                }
            }
            val textPaintAlignLeft = remember(density) {
                Paint().apply {
                    color = textColor
                    textAlign = Paint.Align.LEFT
                    textSize = textSizeDen
                }
            }
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                val canvasWidth = size.width
                val pixelsPerSec = canvasWidth.div(maxDuration.inWholeSeconds)
                val sumDurations = allDurations.filterNotNull().reduce { acc, duration ->
                    acc + duration
                }
                val wholeDuration = roaDuration.total?.interpolateAt(0.5) ?: maxDuration
                val restDuration = wholeDuration - sumDurations
                val divider = if (undefinedCount == 0) 1 else undefinedCount
                val dottedLineWidths = restDuration.div(divider).inWholeSeconds * pixelsPerSec
                inset(vertical = strokeWidthThick / 2) {
                    val canvasHeight = size.height
                    val start1 =
                        onsetInterpol?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
                    val pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(20f, 30f)
                    )
                    drawLine(
                        start = Offset(x = 0f, y = canvasHeight),
                        end = Offset(x = start1, y = canvasHeight),
                        color = colorTimeLine,
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round,
                        pathEffect = if (onsetInterpol == null) pathEffect else null
                    )
                    if (onset?.max!=null && onset.min!=null) {
                        val diff = (onset.max - onset.min).inWholeSeconds.times(pixelsPerSec)/2
                        drawLine(
                            start = Offset(x = start1 - diff, y = canvasHeight),
                            end = Offset(x = start1 + diff, y = canvasHeight),
                            color = colorTransparent,
                            strokeWidth = strokeWidthThick,
                            cap = StrokeCap.Round,
                        )
                    }
                    val diff1 =
                        comeupInterpol?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
                    val start2 = start1 + diff1
                    drawLine(
                        start = Offset(x = start1, y = canvasHeight),
                        end = Offset(x = start2, y = 0f),
                        color = colorTimeLine,
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round,
                        pathEffect = if (comeupInterpol == null) pathEffect else null
                    )
                    if (comeup?.max!=null && comeup.min!=null) {
                        val diff = (comeup.max - comeup.min).inWholeSeconds.times(pixelsPerSec)/2
                        drawLine(
                            start = Offset(x = start2 - diff, y = 0f),
                            end = Offset(x = start2 + diff, y = 0f),
                            color = colorTransparent,
                            strokeWidth = strokeWidthThick,
                            cap = StrokeCap.Round,
                        )
                    }
                    if (onset != null) {
                        drawContext.canvas.nativeCanvas.apply {
                            drawText(
                                onset.text,
                                0f,
                                canvasHeight-11f,
                                textPaintAlignLeft
                            )
                        }
                    }
                    if (comeup != null) {
                        drawContext.canvas.nativeCanvas.apply {
                            drawText(
                                comeup.text,
                                (start1 + start2)/2 + 15f,
                                canvasHeight/2,
                                textPaintAlignLeft
                            )
                        }
                    }
                    val diff2 =
                        peakInterpol?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
                    val start3 = start2 + diff2
                    drawLine(
                        start = Offset(x = start2, y = 0f),
                        end = Offset(x = start3, y = 0f),
                        color = colorTimeLine,
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round,
                        pathEffect = if (peakInterpol == null) pathEffect else null
                    )
                    if (peak?.max!=null && peak.min!=null) {
                        val diff = (peak.max - peak.min).inWholeSeconds.times(pixelsPerSec)/2
                        drawLine(
                            start = Offset(x = start3 - diff, y = 0f),
                            end = Offset(x = start3 + diff, y = 0f),
                            color = colorTransparent,
                            strokeWidth = strokeWidthThick,
                            cap = StrokeCap.Round,
                        )
                    }
                    if (peak != null) {
                        drawContext.canvas.nativeCanvas.apply {
                            drawText(
                                peak.text,
                                (start2 + start3)/2,
                                35f,
                                textPaintAlignCenter
                            )
                        }
                    }
                    val diff3 =
                        offsetInterpol?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
                    val start4 = start3 + diff3
                    drawLine(
                        start = Offset(x = start3, y = 0f),
                        end = Offset(x = start4, y = canvasHeight),
                        color = colorTimeLine,
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round,
                        pathEffect = if (offsetInterpol == null) pathEffect else null
                    )
                    if (offset?.max!=null && offset.min!=null) {
                        val diff = (offset.max - offset.min).inWholeSeconds.times(pixelsPerSec)/2
                        drawLine(
                            start = Offset(x = start4 - diff, y = canvasHeight),
                            end = Offset(x = start4 + diff, y = canvasHeight),
                            color = colorTransparent,
                            strokeWidth = strokeWidthThick,
                            cap = StrokeCap.Round,
                        )
                    }
                    if (offset != null) {
                        drawContext.canvas.nativeCanvas.apply {
                            drawText(
                                offset.text,
                                (start3 + start4)/2 + 15f,
                                canvasHeight/2,
                                textPaintAlignLeft
                            )
                        }
                    }
                }
            }
        }
    }
}