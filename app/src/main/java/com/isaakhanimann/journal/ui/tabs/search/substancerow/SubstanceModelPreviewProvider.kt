/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.search.substancerow

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.journal.ui.tabs.search.CategoryModel
import com.isaakhanimann.journal.ui.tabs.search.SubstanceModel

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
            ),
            hasSaferUse = false,
            hasInteractions = false
        )
    )
}