package com.isaakhanimann.healthassistant.ui.search.substancerow

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.healthassistant.ui.search.CategoryModel
import com.isaakhanimann.healthassistant.ui.search.SubstanceModel

class SubstanceModelPreviewProvider : PreviewParameterProvider<SubstanceModel> {
    override val values: Sequence<SubstanceModel> = sequenceOf(
        SubstanceModel(
            name = "Example Substance",
            commonNames = listOf("Hat", "Boot", "Hoodie", "Shirt", "Blouse"),
            categories = listOf(
                CategoryModel(
                    name = "common",
                    color = Color.Blue
                ),
                CategoryModel(
                    name = "psychedelic",
                    color = Color.Magenta
                )
            )
        )
    )
}