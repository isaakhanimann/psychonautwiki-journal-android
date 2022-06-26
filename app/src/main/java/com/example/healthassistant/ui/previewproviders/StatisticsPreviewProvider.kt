package com.example.healthassistant.ui.previewproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.ui.stats.SubstanceStat

class StatisticsPreviewProvider : PreviewParameterProvider<List<SubstanceStat>> {
    override val values: Sequence<List<SubstanceStat>> = sequenceOf(
        listOf(
            SubstanceStat(
                substanceName = "LSD",
                lastUsedText = "2 minutes"
            ),
            SubstanceStat(
                substanceName = "MDMA",
                lastUsedText = "1 day"
            ),
            SubstanceStat(
                substanceName = "Cocaine",
                lastUsedText = "3 months"
            ),
            SubstanceStat(
                substanceName = "Ketamine",
                lastUsedText = "10 months"
            )
        ),
        listOf()
    )
}