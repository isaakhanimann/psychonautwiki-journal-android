package com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables

import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration
import kotlin.math.max

data class TimeRangeDrawable(
    val color: AdaptiveColor,
    val ingestionStartInSeconds: Float,
    val ingestionEndInSeconds: Float,
    val intersectionCountWithPreviousRanges: Int,
    val convolutionResultModel: ConvolutionResultModel?,
) {
    data class IntermediateRepresentation(
        val startInSeconds: Float,
        val endInSeconds: Float,
        val fullTimelineDurations: FullTimelineDurations?,
        val height: Float
    )
}

fun FullTimelineDurations.getConvolutionResultModel(
    ingestionStartInSeconds: Float,
    ingestionEndInSeconds: Float,
    heightOfDoseAtOnePoint: Float
): ConvolutionResultModel {
    val lengthInSeconds = ingestionEndInSeconds - ingestionStartInSeconds

    val onsetSustained = onsetInSeconds
    val comeupSustained = comeupInSeconds + lengthInSeconds
    val peakSustained = max(0f, peakInSeconds - lengthInSeconds)
    val offsetSustained = offsetInSeconds + lengthInSeconds

    val heightSustained =
        heightOfDoseAtOnePoint * (peakInSeconds + (comeupInSeconds + offsetInSeconds) / 2) / (0.5f * comeupSustained + peakSustained + 0.5f * offsetSustained)

    val comeupStartXInSeconds = ingestionStartInSeconds + onsetSustained
    val peakStartXInSeconds = comeupStartXInSeconds + comeupSustained
    val offsetStartXInSeconds = peakStartXInSeconds + peakSustained
    val offsetEndXInSeconds = offsetStartXInSeconds + offsetSustained
    return ConvolutionResultModel(
        ingestionStartInSeconds = ingestionStartInSeconds,
        ingestionEndInSeconds = ingestionEndInSeconds,
        height = heightSustained,
        comeupStartXInSeconds = comeupStartXInSeconds,
        peakStartXInSeconds = peakStartXInSeconds,
        offsetStartXInSeconds = offsetStartXInSeconds,
        offsetEndXInSeconds = offsetEndXInSeconds
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