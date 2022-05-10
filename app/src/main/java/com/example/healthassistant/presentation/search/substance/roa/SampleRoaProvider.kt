package com.example.healthassistant.presentation.search.substance.roa

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.substances.*

class SampleRoaProvider : PreviewParameterProvider<Roa> {
    override val values: Sequence<Roa> = sequenceOf(
        Roa(
            name = "oral",
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
                    min = 20.0,
                    max = 40.0,
                    units = "minutes"
                ),
                comeup = DurationRange(
                    min = 15.0,
                    max = 30.0,
                    units = "minutes"
                ),
                peak = DurationRange(
                    min = 1.5,
                    max = 2.5,
                    units = "hours"
                ),
                offset = DurationRange(
                    min = 1.0,
                    max = 1.5,
                    units = "hours"
                ),
                total = DurationRange(
                    min = 3.0,
                    max = 5.0,
                    units = "hours"
                ),
                afterglow = DurationRange(
                    min = 12.0,
                    max = 48.0,
                    units = "hours"
                )
            ),
            bioavailability = Bioavailability(
                min = 70.0,
                max = 75.0
            )
        )
    )
}