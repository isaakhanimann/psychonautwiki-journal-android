/*
 * Copyright (c) 2022. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.search.substance.roa.dose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDose

@Preview(showBackground = true)
@Composable
fun RoaDoseViewPreview(
    @PreviewParameter(RoaDosePreviewProvider::class) roaDose: RoaDose,
) {
    RoaDoseView(
        roaDose = roaDose,
    )
}

@Composable
fun RoaDoseView(
    roaDose: RoaDose,
    modifier: Modifier = Modifier
) {
    DoseClassificationRow(
        modifier = modifier,
        lightMin = roaDose.lightMin,
        commonMin = roaDose.commonMin,
        strongMin = roaDose.strongMin,
        heavyMin = roaDose.heavyMin,
        unit = roaDose.units
    )
}