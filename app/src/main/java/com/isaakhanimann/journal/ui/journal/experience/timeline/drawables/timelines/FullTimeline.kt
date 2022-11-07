package com.isaakhanimann.journal.ui.journal.experience.timeline.drawables.timelines

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.journal.ui.journal.experience.timeline.AllTimelinesModel
import com.isaakhanimann.journal.ui.journal.experience.timeline.AllTimelinesModel.Companion.shapeAlpha
import com.isaakhanimann.journal.ui.journal.experience.timeline.drawables.TimelineDrawable

data class FullTimeline(
    val onset: FullDurationRange,
    val comeup: FullDurationRange,
    val peak: FullDurationRange,
    val offset: FullDurationRange,
    val peakAndOffsetWeight: Float
) : TimelineDrawable {

    override fun getPeakDurationRangeInSeconds(startDurationInSeconds: Float): ClosedRange<Float> {
        val startRange =
            startDurationInSeconds + onset.interpolateAtValueInSeconds(0.5f) + comeup.interpolateAtValueInSeconds(
                0.5f
            )
        return startRange..(startRange + peak.interpolateAtValueInSeconds(peakAndOffsetWeight))
    }

    override val widthInSeconds: Float =
        onset.maxInSeconds + comeup.maxInSeconds + peak.maxInSeconds + offset.maxInSeconds

    override fun drawTimeLine(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color,
    ) {
        drawScope.drawPath(
            path = Path().apply {
                val onsetAndComeupWeight = 0.5f
                val onsetEndX =
                    startX + (onset.interpolateAtValueInSeconds(onsetAndComeupWeight) * pixelsPerSec)
                val comeupEndX =
                    onsetEndX + (comeup.interpolateAtValueInSeconds(onsetAndComeupWeight) * pixelsPerSec)
                val peakEndX =
                    comeupEndX + (peak.interpolateAtValueInSeconds(peakAndOffsetWeight) * pixelsPerSec)
                val offsetEndX =
                    peakEndX + (offset.interpolateAtValueInSeconds(peakAndOffsetWeight) * pixelsPerSec)
                moveTo(x = startX, y = height)
                lineTo(x = onsetEndX, y = height)
                lineTo(x = comeupEndX, y = 0f)
                lineTo(x = peakEndX, y = 0f)
                lineTo(x = offsetEndX, y = height)
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
                // path over top
                val onsetStartMinX = startX + (onset.minInSeconds * pixelsPerSec)
                val comeupEndMinX = onsetStartMinX + (comeup.minInSeconds * pixelsPerSec)
                val peakEndMaxX =
                    startX + ((onset.maxInSeconds + comeup.maxInSeconds + peak.maxInSeconds) * pixelsPerSec)
                val offsetEndMaxX =
                    peakEndMaxX + (offset.maxInSeconds * pixelsPerSec)
                moveTo(onsetStartMinX, height)
                lineTo(x = comeupEndMinX, y = 0f)
                lineTo(x = peakEndMaxX, y = 0f)
                lineTo(x = offsetEndMaxX, y = height)
                // path bottom back
                val onsetStartMaxX = startX + (onset.maxInSeconds * pixelsPerSec)
                val comeupEndMaxX =
                    onsetStartMaxX + (comeup.maxInSeconds * pixelsPerSec)
                val peakEndMinX =
                    startX + ((onset.minInSeconds + comeup.minInSeconds + peak.minInSeconds) * pixelsPerSec)
                val offsetEndMinX =
                    peakEndMinX + (offset.minInSeconds * pixelsPerSec)
                lineTo(x = offsetEndMinX, y = height)
                lineTo(x = peakEndMinX, y = 0f)
                lineTo(x = comeupEndMaxX, y = 0f)
                lineTo(x = onsetStartMaxX, y = height)
                close()
            },
            color = color.copy(alpha = shapeAlpha)
        )
    }
}

fun RoaDuration.toFullTimeline(peakAndOffsetWeight: Float): FullTimeline? {
    val fullOnset = onset?.toFullDurationRange()
    val fullComeup = comeup?.toFullDurationRange()
    val fullPeak = peak?.toFullDurationRange()
    val fullOffset = offset?.toFullDurationRange()
    return if (fullOnset != null && fullComeup != null && fullPeak != null && fullOffset != null) {
        FullTimeline(
            onset = fullOnset,
            comeup = fullComeup,
            peak = fullPeak,
            offset = fullOffset,
            peakAndOffsetWeight = peakAndOffsetWeight
        )
    } else {
        null
    }
}