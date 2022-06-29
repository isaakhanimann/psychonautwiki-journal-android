package com.example.healthassistant.ui.search.substance.roa.duration

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.substances.RoaDuration
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Preview(showBackground = true)
@Composable
fun RoaDurationPreview(
    @PreviewParameter(RoaDurationPreviewProvider::class) roaDuration: RoaDuration
) {
    RoaDurationView(
        roaDuration = roaDuration,
        maxDuration = 13.toDuration(DurationUnit.HOURS),
        isOralRoute = true
    )
}

@Composable
fun RoaDurationView(
    roaDuration: RoaDuration,
    maxDuration: Duration?,
    isOralRoute: Boolean
) {
    Column {
        val total = roaDuration.total
        val colorTimeLine = MaterialTheme.colors.primary
        val colorTransparent = colorTimeLine.copy(alpha = 0.1f)
        val strokeWidth = 8f
        val strokeWidthThick = 40f
        val ingestionDotRadius = 10f
        val onset = roaDuration.onset
        val comeup = roaDuration.comeup
        val peak = roaDuration.peak
        val offset = roaDuration.offset
        val onsetInterpol = onset?.interpolateAt(0.5)
        val comeupInterpol = comeup?.interpolateAt(0.5)
        val peakInterpol = peak?.interpolateAt(0.5)
        val offsetInterpol = offset?.interpolateAt(0.5)
        val allDurations = listOf(onsetInterpol, comeupInterpol, peakInterpol, offsetInterpol)
        val undefinedCount = allDurations.count { it == null }
        if (maxDuration != null && undefinedCount < 4) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                inset(left = ingestionDotRadius, top = 0f, right = 0f, bottom = 0f) {
                    val canvasWidth = size.width
                    val pixelsPerSec = canvasWidth.div(maxDuration.inWholeSeconds)
                    val sumDurations = allDurations.filterNotNull().reduce { acc, duration ->
                        acc + duration
                    }
                    val offsetDiff =
                        if (offset?.max != null && offset.min != null) offset.max - offset.min else null
                    val wholeDuration = total?.interpolateAt(0.5)
                        ?: if (offsetDiff != null) maxDuration.minus(offsetDiff.div(2)) else maxDuration
                    val restDuration = wholeDuration - sumDurations
                    val divider = if (undefinedCount == 0) 1 else undefinedCount
                    val dottedLineWidths = restDuration.div(divider).inWholeSeconds * pixelsPerSec
                    inset(vertical = strokeWidthThick / 2) {
                        val canvasHeight = size.height
                        drawCircle(
                            color = colorTimeLine,
                            radius = ingestionDotRadius,
                            center = Offset(x = 0f, y = canvasHeight)
                        )
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
                        if (onset?.max != null && onset.min != null) {
                            val diff =
                                (onset.max - onset.min).inWholeSeconds.times(pixelsPerSec) / 2
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
                        if (comeup?.max != null && comeup.min != null) {
                            val diff =
                                (comeup.max - comeup.min).inWholeSeconds.times(pixelsPerSec) / 2
                            drawLine(
                                start = Offset(x = start2 - diff, y = 0f),
                                end = Offset(x = start2 + diff, y = 0f),
                                color = colorTransparent,
                                strokeWidth = strokeWidthThick,
                                cap = StrokeCap.Round,
                            )
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
                        if (peak?.max != null && peak.min != null) {
                            val diff = (peak.max - peak.min).inWholeSeconds.times(pixelsPerSec) / 2
                            drawLine(
                                start = Offset(x = start3 - diff, y = 0f),
                                end = Offset(x = start3 + diff, y = 0f),
                                color = colorTransparent,
                                strokeWidth = strokeWidthThick,
                                cap = StrokeCap.Round,
                            )
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
                        if (offset?.max != null && offset.min != null) {
                            val diff =
                                (offset.max - offset.min).inWholeSeconds.times(pixelsPerSec) / 2
                            drawLine(
                                start = Offset(x = start4 - diff, y = canvasHeight),
                                end = Offset(x = start4 + diff, y = canvasHeight),
                                color = colorTransparent,
                                strokeWidth = strokeWidthThick,
                                cap = StrokeCap.Round,
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
        if ((total?.min != null) && (total.max != null)) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(with(LocalDensity.current) { strokeWidthThick.toDp() })
            ) {
                inset(left = ingestionDotRadius, top = 0f, right = 0f, bottom = 0f) {
                    val canvasWidth = size.width
                    val midHeight = size.height / 2
                    drawCircle(
                        color = colorTimeLine,
                        radius = ingestionDotRadius,
                        center = Offset(x = 0f, y = midHeight)
                    )
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
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            val durationTextStyle = MaterialTheme.typography.caption
            Column {
                if (total != null) {
                    Text(
                        text = "total",
                        style = durationTextStyle
                    )
                }
                if (onset != null) {
                    Text(
                        "onset",
                        style = durationTextStyle
                    )
                }
                if (comeup != null) {
                    Text(
                        "comeup",
                        style = durationTextStyle
                    )
                }
                if (peak != null) {
                    Text(
                        "peak",
                        style = durationTextStyle
                    )
                }
                if (offset != null) {
                    Text(
                        "offset",
                        style = durationTextStyle
                    )
                }
                if (roaDuration.afterglow != null) {
                    Text(
                        "after effects",
                        style = durationTextStyle
                    )
                }
            }
            Column {
                if (total != null) {
                    Text(
                        text = total.text,
                        style = durationTextStyle
                    )
                }
                if (onset != null) {
                    Row {
                        Text(
                            roaDuration.onset.text,
                            style = durationTextStyle
                        )
                        if (isOralRoute) {
                            Text(
                                text = " * a full stomach can delay the onset by hours",
                                style = durationTextStyle
                            )
                        }
                    }
                }
                if (comeup != null) {
                    Text(
                        roaDuration.comeup.text,
                        style = durationTextStyle
                    )
                }
                if (peak != null) {
                    Text(
                        roaDuration.peak.text,
                        style = durationTextStyle
                    )
                }
                if (offset != null) {
                    Text(
                        roaDuration.offset.text,
                        style = durationTextStyle
                    )
                }
                if (roaDuration.afterglow != null) {
                    Text(
                        roaDuration.afterglow.text,
                        style = durationTextStyle
                    )
                }
            }
        }
    }
}