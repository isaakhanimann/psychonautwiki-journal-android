/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.search.substance.roa.dose

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.journal.data.substances.classes.roa.DoseRange
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDose

class RoaDosePreviewProvider : PreviewParameterProvider<RoaDose> {
    override val values: Sequence<RoaDose> = sequenceOf(
        RoaDose(
            "mg",
            threshold = 20.0,
            light = DoseRange(
                min = 20.0,
                max = 40.0
            ),
            common = DoseRange(
                min = 40.0,
                max = 90.0
            ),
            strong = DoseRange(
                min = 90.0,
                max = 140.0
            ),
            heavy = 140.0
        )
    )
}