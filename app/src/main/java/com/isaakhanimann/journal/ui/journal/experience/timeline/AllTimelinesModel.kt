package com.isaakhanimann.journal.ui.journal.experience.timeline

import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.isaakhanimann.journal.ui.journal.experience.DataForOneEffectLine
import com.isaakhanimann.journal.ui.journal.experience.timeline.drawables.AxisDrawable
import com.isaakhanimann.journal.ui.journal.experience.timeline.drawables.IngestionDrawable
import com.isaakhanimann.journal.ui.journal.experience.timeline.drawables.timelines.FullTimeline
import java.time.Instant
import kotlin.time.Duration.Companion.hours

class AllTimelinesModel(
    dataForLines: List<DataForOneEffectLine>
) {
    val startTime: Instant
    val widthInSeconds: Float
    val ingestionDrawables: List<IngestionDrawable>
    val axisDrawable: AxisDrawable

    init {
        startTime = dataForLines.map { it.startTime }
            .reduce { acc, date -> if (acc.isBefore(date)) acc else date }
        val ingestionDrawablesWithoutInsets = dataForLines.map { dataForOneLine ->
            IngestionDrawable(
                startTimeGraph = startTime,
                color = dataForOneLine.color,
                ingestionTime = dataForOneLine.startTime,
                roaDuration = dataForOneLine.roaDuration,
                height = dataForOneLine.height
            )
        }
        ingestionDrawables = updateInsets(ingestionDrawablesWithoutInsets)
        val max = ingestionDrawables.maxOfOrNull {
            if (it.timelineDrawable != null) {
                it.timelineDrawable.widthInSeconds + it.ingestionPointDistanceFromStartInSeconds
            } else {
                it.ingestionPointDistanceFromStartInSeconds
            }
        }
        widthInSeconds = if (max == null || max == 0f) {
            5.hours.inWholeSeconds.toFloat()
        } else {
            max
        }
        axisDrawable = AxisDrawable(startTime, widthInSeconds)
    }

    companion object {
        private fun updateInsets(ingestionDrawables: List<IngestionDrawable>): List<IngestionDrawable> {
            val results = mutableListOf<IngestionDrawable>()
            for (i in ingestionDrawables.indices) {
                val currentDrawable = ingestionDrawables[i]
                val otherDrawables = ingestionDrawables.take(i)
                val insetTimes = getInsetTimes(
                    ingestionDrawable = currentDrawable,
                    otherDrawables = otherDrawables
                )
                currentDrawable.insetTimes = insetTimes
                results.add(currentDrawable)
            }
            return results
        }

        private fun getInsetTimes(
            ingestionDrawable: IngestionDrawable,
            otherDrawables: List<IngestionDrawable>
        ): Int {
            val currentFullTimeline =
                ingestionDrawable.timelineDrawable as? FullTimeline ?: return 0
            val otherFullTimelinePeakRangesWithSameHeight: List<ClosedRange<Float>> =
                otherDrawables
                    .filter { it.height == ingestionDrawable.height }
                    .mapNotNull {
                        return@mapNotNull it.timelineDrawable?.getPeakDurationRangeInSeconds(
                            startDurationInSeconds = it.ingestionPointDistanceFromStartInSeconds
                        )
                    }
            val currentRange =
                currentFullTimeline.getPeakDurationRangeInSeconds(startDurationInSeconds = ingestionDrawable.ingestionPointDistanceFromStartInSeconds)
            var insetTimes = 0
            for (otherRange in otherFullTimelinePeakRangesWithSameHeight) {
                val isOverlap =
                    currentRange.start <= otherRange.endInclusive && otherRange.start <= currentRange.endInclusive
                if (isOverlap) insetTimes++
            }
            return insetTimes
        }

        const val shapeAlpha = 0.3f
        const val strokeWidth = 13f
        const val shapeWidth = 3 * strokeWidth
        val normalStroke = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
        )
        val dottedStroke = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 30f))
        )
    }
}
