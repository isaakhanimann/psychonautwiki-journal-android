package com.example.healthassistant.ui.previewproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.substances.DoseRange
import com.example.healthassistant.data.substances.RoaDose

class RoaDosePreviewProvider : PreviewParameterProvider<RoaDose> {
    override val values: Sequence<RoaDose> = sequenceOf(
        RoaDose(
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
        )
    )
}