package com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables

import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration

data class TimeRangeDrawable(
    val color: AdaptiveColor,
    val ingestionStartInSeconds: Float,
    val ingestionEndInSeconds: Float,
    val intersectionCountWithPreviousRanges: Int,
) {
    data class IntermediateRepresentation(
        val startInSeconds: Float,
        val endInSeconds: Float,
        val fullTimelineDurations: FullTimelineDurations?,
        val height: Float
    )
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