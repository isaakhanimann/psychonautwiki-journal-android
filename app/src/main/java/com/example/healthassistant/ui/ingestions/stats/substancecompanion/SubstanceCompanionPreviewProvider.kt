package com.example.healthassistant.ui.ingestions.stats.substancecompanion

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.room.experiences.entities.SubstanceColor
import com.example.healthassistant.data.room.experiences.entities.SubstanceCompanion

class SubstanceCompanionPreviewProvider : PreviewParameterProvider<SubstanceCompanion> {
    override val values: Sequence<SubstanceCompanion> = sequenceOf(
        SubstanceCompanion(
            substanceName = "Cocaine",
            color = SubstanceColor.BLUE
        )
    )
}