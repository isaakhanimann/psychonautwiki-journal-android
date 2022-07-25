package com.isaakhanimann.healthassistant.ui.ingestions.stats

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor

class StatsPreviewProvider : PreviewParameterProvider<List<SubstanceStat>> {
    override val values: Sequence<List<SubstanceStat>> = sequenceOf(
        listOf(
            SubstanceStat(
                substanceName = "LSD",
                lastUsedText = "2 minutes",
                color = SubstanceColor.BLUE,
                ingestionCount = 3
            ),
            SubstanceStat(
                substanceName = "MDMA",
                lastUsedText = "1 day",
                color = SubstanceColor.PINK,
                ingestionCount = 8
            ),
            SubstanceStat(
                substanceName = "Cocaine",
                lastUsedText = "3 months",
                color = SubstanceColor.ORANGE,
                ingestionCount = 20
            ),
            SubstanceStat(
                substanceName = "Ketamine",
                lastUsedText = "10 months",
                color = SubstanceColor.PURPLE,
                ingestionCount = 1
            )
        ),
        listOf()
    )
}