package com.example.healthassistant.ui.search.substance.roa

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.inset
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
        val color = MaterialTheme.colors.secondary
        val colorTransparent = color.copy(alpha = 0.1f)
        if ((total?.min != null) && (total.max != null)) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "total: ${total.text}",
                    textAlign = TextAlign.Center
                )
                Canvas(modifier = Modifier.fillMaxWidth()) {
                    val canvasWidth = size.width
                    val max = maxDuration ?: total.max
                    val minX = (total.min.div(max) * canvasWidth).toFloat()
                    val maxX = (total.max.div(max) * canvasWidth).toFloat()
                    val midX = (minX + maxX) / 2
                    drawLine(
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = midX, y = 0f),
                        color = color,
                        strokeWidth = 10f,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        start = Offset(x = minX, y = 0f),
                        end = Offset(x = maxX, y = 0f),
                        color = colorTransparent,
                        strokeWidth = 60f,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        roaDuration.afterglow?.let {
            Text("after effects: ${it.text}")
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
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                val strokeWidth = 8f
                val canvasWidth = size.width
                val pixelsPerSec = canvasWidth.div(maxDuration.inWholeSeconds)
                val canvasHeightOuter = size.height
                val sumDurations = allDurations.filterNotNull().reduce { acc, duration ->
                    acc + duration
                }
                val restDuration = maxDuration - sumDurations
                val divider = if (undefinedCount == 0) 1 else undefinedCount
                val dottedLineWidths = restDuration.div(divider).inWholeSeconds * pixelsPerSec
                inset(vertical = strokeWidth / 2) {
                    val canvasHeight = size.height
                    val start1 = onsetInterpol?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
                    val pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(20f, 30f)
                    )
                    drawLine(
                        start = Offset(x = 0f, y = canvasHeight),
                        end = Offset(x = start1, y = canvasHeight),
                        color = color,
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round,
                        pathEffect = if (onsetInterpol == null) pathEffect else null
                    )
                    val diff1 = comeupInterpol?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
                    val start2 = start1 + diff1
                    drawLine(
                        start = Offset(x = start1, y = canvasHeight),
                        end = Offset(x = start2, y = 0f),
                        color = color,
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round,
                        pathEffect = if (comeupInterpol == null) pathEffect else null
                    )
                    val diff2 = peakInterpol?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
                    val start3 = start2 + diff2
                    drawLine(
                        start = Offset(x = start2, y = 0f),
                        end = Offset(x = start3, y = 0f),
                        color = color,
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round,
                        pathEffect = if (peakInterpol == null) pathEffect else null
                    )
                    val diff3 = offsetInterpol?.inWholeSeconds?.times(pixelsPerSec) ?: dottedLineWidths
                    val start4 = start3 + diff3
                    drawLine(
                        start = Offset(x = start3, y = 0f),
                        end = Offset(x = start4, y = canvasHeight),
                        color = color,
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round,
                        pathEffect = if (offsetInterpol == null) pathEffect else null
                    )
                }
//                val path = Path().apply {
//                    // path over top
//                    val onsetStartMinX = onset.min.inWholeSeconds * pixelsPerSec
//                    val comeupEndMinX =
//                        onsetStartMinX + (comeup.min.inWholeSeconds * pixelsPerSec)
//                    val peakEndMaxX =
//                        (onset.max + comeup.max + peak.max).inWholeSeconds * pixelsPerSec
//                    val offsetEndMaxX =
//                        peakEndMaxX + (offset.max.inWholeSeconds * pixelsPerSec)
//                    moveTo(onsetStartMinX, canvasHeightOuter)
//                    lineTo(x = comeupEndMinX, y = 0f)
//                    lineTo(x = peakEndMaxX, y = 0f)
//                    lineTo(x = offsetEndMaxX, y = canvasHeightOuter)
//                    // path bottom back
//                    val onsetStartMaxX = onset.max.inWholeSeconds * pixelsPerSec
//                    val comeupEndMaxX =
//                        onsetStartMaxX + (comeup.max.inWholeSeconds * pixelsPerSec)
//                    val peakEndMinX =
//                        (onset.min + comeup.min + peak.min).inWholeSeconds * pixelsPerSec
//                    val offsetEndMinX =
//                        peakEndMinX + (offset.min.inWholeSeconds * pixelsPerSec)
//                    lineTo(x = offsetEndMinX, y = canvasHeightOuter)
//                    lineTo(x = peakEndMinX, y = 0f)
//                    lineTo(x = comeupEndMaxX, y = 0f)
//                    lineTo(x = onsetStartMaxX, y = canvasHeightOuter)
//                    close()
//                }
//                drawPath(
//                    path = path,
//                    color = colorTransparent
//                )
            }
        }
    }
}