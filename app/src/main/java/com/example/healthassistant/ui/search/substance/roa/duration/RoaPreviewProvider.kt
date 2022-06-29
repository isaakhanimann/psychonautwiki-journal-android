package com.example.healthassistant.ui.search.substance.roa.duration

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.substances.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class RoaPreviewProvider : PreviewParameterProvider<Roa> {
    override val values: Sequence<Roa> = sequenceOf(
        Roa(
            route = AdministrationRoute.ORAL,
            roaDose = RoaDose(
                "mg",
                threshold = 20.0,
                light = DoseRange(
                    min = 20.0,
                    max = 40.0
                ),
                common = DoseRange(
                    min = 40.0,
                    max = 90.0
                ),
                strong = DoseRange(
                    min = 90.0,
                    max = 140.0
                ),
                heavy = 140.0
            ),
            roaDuration = RoaDuration(
                onset = DurationRange(
                    min = 20.0.toDuration(DurationUnit.MINUTES),
                    max = 40.0.toDuration(DurationUnit.MINUTES),
                ),
                comeup = DurationRange(
                    min = 15.0.toDuration(DurationUnit.MINUTES),
                    max = 30.0.toDuration(DurationUnit.MINUTES),
                ),
                peak = DurationRange(
                    min = 1.5.toDuration(DurationUnit.HOURS),
                    max = 2.5.toDuration(DurationUnit.HOURS),
                ),
                offset = DurationRange(
                    min = 1.0.toDuration(DurationUnit.HOURS),
                    max = 1.5.toDuration(DurationUnit.HOURS),
                ),
                total = DurationRange(
                    min = 3.0.toDuration(DurationUnit.HOURS),
                    max = 5.0.toDuration(DurationUnit.HOURS),
                ),
                afterglow = DurationRange(
                    min = 12.0.toDuration(DurationUnit.HOURS),
                    max = 48.0.toDuration(DurationUnit.HOURS),
                )
            ),
            bioavailability = Bioavailability(
                min = 70.0,
                max = 75.0
            )
        )
    )
}