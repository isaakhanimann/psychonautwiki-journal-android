package com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables

import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration

data class TimeRangeDrawable(
    val color: AdaptiveColor,
    val ingestionStartInSeconds: Float,
    val ingestionEndInSeconds: Float,
    val intersectionCountWithPreviousRanges: Int,
    val convolutionResultModel: ConvolutionResultModel?,
) {
    data class IntermediateRepresentation(
        val color: AdaptiveColor,
        val rangeInSeconds: ClosedFloatingPointRange<Float>,
        val fullTimelineDurations: FullTimelineDurations?,
        val height: Float
    )
}

fun FullTimelineDurations.getConvolutionResultModel(
    ingestionStartInSeconds: Float,
    ingestionEndInSeconds: Float,
    heightOfDoseAtOnePoint: Float
): ConvolutionResultModel {
    return ConvolutionResultModel(
        ingestionStartInSeconds = ingestionStartInSeconds,
        ingestionEndInSeconds = ingestionEndInSeconds,
        height = heightOfDoseAtOnePoint / 2f,
        comeupStartXInSeconds = ingestionStartInSeconds + comeupInSeconds,
        peakStartXInSeconds = ingestionStartInSeconds + onsetInSeconds + comeupInSeconds,
        offsetStartXInSeconds = ingestionEndInSeconds + onsetInSeconds + comeupInSeconds + peakInSeconds,
        offsetEndXInSeconds = ingestionEndInSeconds + onsetInSeconds + comeupInSeconds + peakInSeconds + offsetInSeconds
    )
}

data class ConvolutionResultModel(
    val ingestionStartInSeconds: Float,
    val ingestionEndInSeconds: Float,
    var height: Float,
    val comeupStartXInSeconds: Float,
    val peakStartXInSeconds: Float,
    val offsetStartXInSeconds: Float,
    val offsetEndXInSeconds: Float
) {
    fun normaliseHeight(overallHeight: Float) {
        if (overallHeight > 0) {
            height /= overallHeight
        }
    }
}

data class FullTimelineDurations(
    val onsetInSeconds: Float,
    val comeupInSeconds: Float,
    val peakInSeconds: Float,
    val offsetInSeconds: Float,
)

fun RoaDuration.toFullTimelineDurations(): FullTimelineDurations? {
    val onsetInSeconds = onset?.interpolateAtValueInSeconds(0.5f)
    val comeupInSeconds = comeup?.interpolateAtValueInSeconds(0.5f)
    val peakInSeconds = peak?.interpolateAtValueInSeconds(0.5f)
    val offsetInSeconds = offset?.interpolateAtValueInSeconds(0.5f)
    return if (onsetInSeconds != null && comeupInSeconds != null && peakInSeconds != null && offsetInSeconds != null) {
        FullTimelineDurations(
            onsetInSeconds = onsetInSeconds,
            comeupInSeconds = comeupInSeconds,
            peakInSeconds = peakInSeconds,
            offsetInSeconds = offsetInSeconds
        )
    } else {
        null
    }
}