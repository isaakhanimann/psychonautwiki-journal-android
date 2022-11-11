/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.search

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class CategoryChipPreviewProvider : PreviewParameterProvider<CategoryChipModel> {
    override val values: Sequence<CategoryChipModel> = sequenceOf(
        CategoryChipModel(
            chipName = "common",
            color = Color.Blue,
            isActive = true,
        )
    )
}