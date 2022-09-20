package com.isaakhanimann.healthassistant.ui.ingestions.stats

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor

class StatsPreviewProvider : PreviewParameterProvider<List<SubstanceStat>> {
    override val values: Sequence<List<SubstanceStat>> = sequenceOf(
        listOf(
            SubstanceStat(
                substanceName = "LSD",
                color = SubstanceColor.BLUE,
                ingestionCount = 3
            ),
            SubstanceStat(
                substanceName = "MDMA",
                color = SubstanceColor.PINK,
                ingestionCount = 8
            ),
            SubstanceStat(
                substanceName = "Cocaine",
                color = SubstanceColor.ORANGE,
                ingestionCount = 20
            ),
            SubstanceStat(
                substanceName = "Ketamine",
                color = SubstanceColor.PURPLE,
                ingestionCount = 1
            )
        ),
    )
}