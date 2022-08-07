package com.isaakhanimann.healthassistant.ui.experiences.experience.timeline.ingestion

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.healthassistant.data.substances.classes.roa.DurationRange
import com.isaakhanimann.healthassistant.data.substances.classes.roa.DurationUnits
import com.isaakhanimann.healthassistant.data.substances.classes.roa.RoaDuration

class RoaDurationPreviewProvider : PreviewParameterProvider<RoaDuration> {
    override val values: Sequence<RoaDuration> = sequenceOf(
        RoaDuration(
            onset = DurationRange(
                min = 20f,
                max = 60f,
                units = DurationUnits.MINUTES
            ),
            comeup = DurationRange(
                min = 45f,
                max = 120f,
                units = DurationUnits.MINUTES
            ),
            peak = DurationRange(
                min = 3f,
                max = 5f,
                units = DurationUnits.HOURS
            ),
            offset = DurationRange(
                min = 3f,
                max = 5f,
                units = DurationUnits.HOURS
            ),
            total = DurationRange(
                min = 8f,
                max = 12f,
                units = DurationUnits.HOURS
            ),
            afterglow = DurationRange(
                min = 6f,
                max = 24f,
                units = DurationUnits.HOURS
            )
        ),
        RoaDuration(
            onset = DurationRange(
                min = 20f,
                max = 40f,
                units = DurationUnits.MINUTES
            ),
            comeup = DurationRange(
                min = 15f,
                max = 30f,
                units = DurationUnits.MINUTES
            ),
            peak = DurationRange(
                min = 1.5f,
                max = 2.5f,
                units = DurationUnits.HOURS
            ),
            offset = DurationRange(
                min = 1f,
                max = 1.5f,
                units = DurationUnits.HOURS
            ),
            total = DurationRange(
                min = 3f,
                max = 5f,
                units = DurationUnits.HOURS
            ),
            afterglow = DurationRange(
                min = 12f,
                max = 48f,
                units = DurationUnits.HOURS
            )
        ),
        RoaDuration(
            onset = DurationRange(
                min = 20f,
                max = 40f,
                units = DurationUnits.MINUTES
            ),
            comeup = null,
            peak = DurationRange(
                min = 1.5f,
                max = 2.5f,
                units = DurationUnits.HOURS
            ),
            offset = null,
            total = DurationRange(
                min = 3f,
                max = 5f,
                units = DurationUnits.HOURS
            ),
            afterglow = null
        )
    )
}