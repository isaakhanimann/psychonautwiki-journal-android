package com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables

import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor

data class TimeRangeDrawable(
    val color: AdaptiveColor,
    val startInSeconds: Long,
    val endInSeconds: Long,
    val intersectionCountWithPreviousRanges: Int,
) {
    data class IntermediateRepresentation(
        val color: AdaptiveColor,
        val startInSeconds: Long,
        val endInSeconds: Long,
    )
}