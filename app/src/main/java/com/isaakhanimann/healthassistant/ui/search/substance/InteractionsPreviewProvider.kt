package com.isaakhanimann.healthassistant.ui.search.substance

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.healthassistant.data.substances.classes.Interactions

class InteractionsPreviewProvider : PreviewParameterProvider<Interactions> {
    override val values: Sequence<Interactions> = sequenceOf(
        Interactions(
            dangerous = listOf(
                "Alcohol",
                "AMT",
                "Cocaine"
            ),
            unsafe = listOf(
                "Tramadol",
                "MAOI",
                "Dissociatives"
            ),
            uncertain = listOf(
                "MDMA",
                "Stimulants",
                "Dextromethorphan"
            ),
        )
    )
}