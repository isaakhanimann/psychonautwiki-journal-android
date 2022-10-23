package com.isaakhanimann.healthassistant.ui.search.substance.roa.duration

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.data.substances.classes.roa.Roa

@Preview
@Composable
fun RoaDurationChartPreview(
    @PreviewParameter(RoaDurationChartPreviewProvider::class) roas: List<Roa>
) {
    RoaDurationsChart(roas = roas)
}

@Composable
fun RoaDurationsChart(roas: List<Roa>) {
    val maxDuration = remember(roas) {
        roas.mapNotNull {
            val duration = it.roaDuration ?: return@mapNotNull null
            val maxTotal = duration.total?.maxInSec
            val maxOnset = duration.onset?.maxInSec
            val maxComeup = duration.comeup?.maxInSec
            val maxPeak = duration.peak?.maxInSec
            val maxOffset = duration.offset?.maxInSec
            val maxTimeline =
                if (maxOnset != null && maxComeup != null && maxPeak != null && maxOffset != null) {
                    maxOnset + maxComeup + maxPeak + maxOffset
                } else {
                    val partialSum = ((maxOnset ?: 0f) + (maxComeup ?: 0f) + (maxPeak
                        ?: 0f) + (maxOffset ?: 0f)).times(1.1f)
                    if (partialSum == 0f) {
                        null
                    } else {
                        partialSum
                    }
                }
            if (maxTotal == null && maxTimeline == null) {
                return@mapNotNull null
            } else if (maxTotal != null && maxTimeline != null) {
                if (maxTotal > maxTimeline) {
                    return@mapNotNull maxTotal
                } else {
                    return@mapNotNull maxTimeline
                }
            } else {
                return@mapNotNull maxTotal ?: maxTimeline
            }
        }.maxOrNull()
    }
    Canvas(
        modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
    ) {

    }
}