package com.isaakhanimann.healthassistant.ui.search

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class CategoryChipPreviewProvider : PreviewParameterProvider<CategoryChipModel> {
    override val values: Sequence<CategoryChipModel> = sequenceOf(
        CategoryChipModel(
            chipName = "common",
            color = Color.Blue,
            isActive = true,
        ),
        CategoryChipModel(
            chipName = "common",
            color = Color.Blue,
            isActive = false,
        )
    )
}