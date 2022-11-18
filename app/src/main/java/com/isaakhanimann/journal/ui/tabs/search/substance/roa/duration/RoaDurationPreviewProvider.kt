/*
 * Copyright (c) 2022. Free Software Foundation, Inc.
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

package com.isaakhanimann.journal.ui.tabs.search.substance.roa.duration

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.journal.data.substances.classes.roa.DurationRange
import com.isaakhanimann.journal.data.substances.classes.roa.DurationUnits
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration

class RoaDurationPreviewProvider : PreviewParameterProvider<RoaDuration> {
    override val values: Sequence<RoaDuration> = sequenceOf(
        RoaDuration(
            onset = DurationRange(
                min = 20f,
                max = 60f,
                units = DurationUnits.MINUTES
            ),
            comeup = DurationRange(
                min = 45f,
                max = 120f,
                units = DurationUnits.MINUTES
            ),
            peak = DurationRange(
                min = 3f,
                max = 5f,
                units = DurationUnits.HOURS
            ),
            offset = DurationRange(
                min = 3f,
                max = 5f,
                units = DurationUnits.HOURS
            ),
            total = DurationRange(
                min = 8f,
                max = 12f,
                units = DurationUnits.HOURS
            ),
            afterglow = DurationRange(
                min = 6f,
                max = 24f,
                units = DurationUnits.HOURS
            )
        ),
        RoaDuration(
            onset = null,
            comeup = DurationRange(
                min = 45f,
                max = 120f,
                units = DurationUnits.MINUTES
            ),
            peak = DurationRange(
                min = 3f,
                max = 5f,
                units = DurationUnits.HOURS
            ),
            offset = DurationRange(
                min = 3f,
                max = 5f,
                units = DurationUnits.HOURS
            ),
            total = DurationRange(
                min = 8f,
                max = 12f,
                units = DurationUnits.HOURS
            ),
            afterglow = DurationRange(
                min = 6f,
                max = 24f,
                units = DurationUnits.HOURS
            )
        ),
        RoaDuration(
            onset = DurationRange(
                min = 20f,
                max = 60f,
                units = DurationUnits.MINUTES
            ),
            comeup = null,
            peak = DurationRange(
                min = 3f,
                max = 5f,
                units = DurationUnits.HOURS
            ),
            offset = DurationRange(
                min = 3f,
                max = 5f,
                units = DurationUnits.HOURS
            ),
            total = DurationRange(
                min = 8f,
                max = 12f,
                units = DurationUnits.HOURS
            ),
            afterglow = DurationRange(
                min = 6f,
                max = 24f,
                units = DurationUnits.HOURS
            )
        ),
        RoaDuration(
            onset = DurationRange(
                min = 20f,
                max = 60f,
                units = DurationUnits.MINUTES
            ),
            comeup = DurationRange(
                min = 45f,
                max = 120f,
                units = DurationUnits.MINUTES
            ),
            peak = null,
            offset = DurationRange(
                min = 3f,
                max = 5f,
                units = DurationUnits.HOURS
            ),
            total = DurationRange(
                min = 8f,
                max = 12f,
                units = DurationUnits.HOURS
            ),
            afterglow = DurationRange(
                min = 6f,
                max = 24f,
                units = DurationUnits.HOURS
            )
        ),
        RoaDuration(
            onset = DurationRange(
                min = 20f,
                max = 60f,
                units = DurationUnits.MINUTES
            ),
            comeup = DurationRange(
                min = 45f,
                max = 120f,
                units = DurationUnits.MINUTES
            ),
            peak = DurationRange(
                min = 3f,
                max = 5f,
                units = DurationUnits.HOURS
            ),
            offset = null,
            total = DurationRange(
                min = 8f,
                max = 12f,
                units = DurationUnits.HOURS
            ),
            afterglow = DurationRange(
                min = 6f,
                max = 24f,
                units = DurationUnits.HOURS
            )
        ),
        RoaDuration(
            onset = null,
            comeup = null,
            peak = DurationRange(
                min = 3f,
                max = 5f,
                units = DurationUnits.HOURS
            ),
            offset = DurationRange(
                min = 3f,
                max = 5f,
                units = DurationUnits.HOURS
            ),
            total = DurationRange(
                min = 8f,
                max = 12f,
                units = DurationUnits.HOURS
            ),
            afterglow = DurationRange(
                min = 6f,
                max = 24f,
                units = DurationUnits.HOURS
            )
        ),
        RoaDuration(
            onset = null,
            comeup = DurationRange(
                min = 45f,
                max = 120f,
                units = DurationUnits.MINUTES
            ),
            peak = null,
            offset = DurationRange(
                min = 3f,
                max = 5f,
                units = DurationUnits.HOURS
            ),
            total = DurationRange(
                min = 8f,
                max = 12f,
                units = DurationUnits.HOURS
            ),
            afterglow = DurationRange(
                min = 6f,
                max = 24f,
                units = DurationUnits.HOURS
            )
        ),
        RoaDuration(
            onset = null,
            comeup = DurationRange(
                min = 45f,
                max = 120f,
                units = DurationUnits.MINUTES
            ),
            peak = DurationRange(
                min = 3f,
                max = 5f,
                units = DurationUnits.HOURS
            ),
            offset = null,
            total = DurationRange(
                min = 8f,
                max = 12f,
                units = DurationUnits.HOURS
            ),
            afterglow = DurationRange(
                min = 6f,
                max = 24f,
                units = DurationUnits.HOURS
            )
        ),
        RoaDuration(
            onset = DurationRange(
                min = 20f,
                max = 60f,
                units = DurationUnits.MINUTES
            ),
            comeup = null,
            peak = null,
            offset = DurationRange(
                min = 3f,
                max = 5f,
                units = DurationUnits.HOURS
            ),
            total = DurationRange(
                min = 8f,
                max = 12f,
                units = DurationUnits.HOURS
            ),
            afterglow = DurationRange(
                min = 6f,
                max = 24f,
                units = DurationUnits.HOURS
            )
        ),
        RoaDuration(
            onset = DurationRange(
                min = 20f,
                max = 60f,
                units = DurationUnits.MINUTES
            ),
            comeup = null,
            peak = DurationRange(
                min = 3f,
                max = 5f,
                units = DurationUnits.HOURS
            ),
            offset = null,
            total = DurationRange(
                min = 8f,
                max = 12f,
                units = DurationUnits.HOURS
            ),
            afterglow = DurationRange(
                min = 6f,
                max = 24f,
                units = DurationUnits.HOURS
            )
        ),
        RoaDuration(
            onset = null,
            comeup = null,
            peak = null,
            offset = DurationRange(
                min = 3f,
                max = 5f,
                units = DurationUnits.HOURS
            ),
            total = DurationRange(
                min = 8f,
                max = 12f,
                units = DurationUnits.HOURS
            ),
            afterglow = DurationRange(
                min = 6f,
                max = 24f,
                units = DurationUnits.HOURS
            )
        ),
        RoaDuration(
            onset = DurationRange(
                min = 20f,
                max = 60f,
                units = DurationUnits.MINUTES
            ),
            comeup = null,
            peak = null,
            offset = null,
            total = DurationRange(
                min = 8f,
                max = 12f,
                units = DurationUnits.HOURS
            ),
            afterglow = DurationRange(
                min = 6f,
                max = 24f,
                units = DurationUnits.HOURS
            )
        ),
        RoaDuration(
            onset = null,
            comeup = DurationRange(
                min = 45f,
                max = 120f,
                units = DurationUnits.MINUTES
            ),
            peak = null,
            offset = null,
            total = DurationRange(
                min = 8f,
                max = 12f,
                units = DurationUnits.HOURS
            ),
            afterglow = DurationRange(
                min = 6f,
                max = 24f,
                units = DurationUnits.HOURS
            )
        ),
        RoaDuration(
            onset = null,
            comeup = null,
            peak = DurationRange(
                min = 3f,
                max = 5f,
                units = DurationUnits.HOURS
            ),
            offset = null,
            total = DurationRange(
                min = 8f,
                max = 12f,
                units = DurationUnits.HOURS
            ),
            afterglow = DurationRange(
                min = 6f,
                max = 24f,
                units = DurationUnits.HOURS
            )
        ),
    )
}