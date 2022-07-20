package com.example.healthassistant.ui.stats

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.room.experiences.entities.SubstanceColor

class ProfilePreviewProvider : PreviewParameterProvider<List<SubstanceStat>> {
    override val values: Sequence<List<SubstanceStat>> = sequenceOf(
        listOf(
            SubstanceStat(
                substanceName = "LSD",
                lastUsedText = "2 minutes",
                color = SubstanceColor.BLUE
            ),
            SubstanceStat(
                substanceName = "MDMA",
                lastUsedText = "1 day",
                color = SubstanceColor.PINK
            ),
            SubstanceStat(
                substanceName = "Cocaine",
                lastUsedText = "3 months",
                color = SubstanceColor.ORANGE
            ),
            SubstanceStat(
                substanceName = "Ketamine",
                lastUsedText = "10 months",
                color = SubstanceColor.PURPLE
            )
        ),
        listOf()
    )
}