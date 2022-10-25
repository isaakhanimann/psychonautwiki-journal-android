package com.isaakhanimann.healthassistant.ui.journal.experience.timeline.drawables.timelines

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.isaakhanimann.healthassistant.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.healthassistant.ui.journal.experience.timeline.AllTimelinesModel
import com.isaakhanimann.healthassistant.ui.journal.experience.timeline.drawables.TimelineDrawable

data class OnsetComeupPeakTimeline(
    val onset: FullDurationRange,
    val comeup: FullDurationRange,
    val peak: FullDurationRange,
) : TimelineDrawable {

    override fun getPeakDurationRangeInSeconds(startDurationInSeconds: Float): ClosedRange<Float> {
        val startRange =
            startDurationInSeconds + onset.interpolateAtValueInSeconds(0.5f) + comeup.interpolateAtValueInSeconds(0.5f)
        return startRange..(startRange + peak.interpolateAtValueInSeconds(0.5f))
    }

    override val widthInSeconds: Float =
        onset.maxInSeconds + comeup.maxInSeconds + peak.maxInSeconds

    override fun drawTimeLine(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color,
    ) {
        val weight = 0.5f
        val onsetEndX =
            startX + (onset.interpolateAtValueInSeconds(weight) * pixelsPerSec)
        val comeupEndX =
            onsetEndX + (comeup.interpolateAtValueInSeconds(weight) * pixelsPerSec)
        val peakEndX =
            comeupEndX + (peak.interpolateAtValueInSeconds(weight) * pixelsPerSec)
        drawScope.drawPath(
            path = Path().apply {
                moveTo(x = startX, y = height)
                lineTo(x = onsetEndX, y = height)
                lineTo(x = comeupEndX, y = 0f)
                lineTo(x = peakEndX, y = 0f)
            },
            color = color,
            style = AllTimelinesModel.normalStroke
        )
    }

    override fun drawTimeLineShape(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color,
    ) {
        drawScope.drawPath(
            path = Path().apply {
                val onsetEndMinX = startX + (onset.minInSeconds * pixelsPerSec)
                val comeupEndMinX = onsetEndMinX + (comeup.minInSeconds * pixelsPerSec)
                moveTo(onsetEndMinX, height)
                lineTo(x = comeupEndMinX, y = 0f)
                val onsetEndMaxX = startX + (onset.maxInSeconds * pixelsPerSec)
                val comeupEndMaxX =
                    onsetEndMaxX + (comeup.maxInSeconds * pixelsPerSec)
                lineTo(x = comeupEndMaxX, y = 0f)
                val peakEndMaxX = comeupEndMaxX + (peak.maxInSeconds * pixelsPerSec)
                val peakEndMinX = comeupEndMinX + (peak.minInSeconds * pixelsPerSec)
                lineTo(x = peakEndMaxX, y = 0f)
                lineTo(x = peakEndMaxX, y = AllTimelinesModel.shapeWidth)
                lineTo(x = peakEndMinX, y = AllTimelinesModel.shapeWidth)
                lineTo(x = peakEndMinX, y = 0f)
                lineTo(x = comeupEndMaxX, y = 0f)
                lineTo(x = onsetEndMaxX, y = height)
                close()
            },
            color = color.copy(alpha = AllTimelinesModel.shapeAlpha)
        )
    }
}

fun RoaDuration.toOnsetComeupPeakTimeline(): OnsetComeupPeakTimeline? {
    val fullOnset = onset?.toFullDurationRange()
    val fullComeup = comeup?.toFullDurationRange()
    val fullPeak = peak?.toFullDurationRange()
    return if (fullOnset != null && fullComeup != null && fullPeak != null) {
        OnsetComeupPeakTimeline(
            onset = fullOnset,
            comeup = fullComeup,
            peak = fullPeak,
        )
    } else {
        null
    }
}