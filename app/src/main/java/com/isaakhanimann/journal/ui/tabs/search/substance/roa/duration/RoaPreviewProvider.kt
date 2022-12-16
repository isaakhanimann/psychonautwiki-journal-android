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

package com.isaakhanimann.journal.ui.tabs.search.substance.roa.duration

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.data.substances.classes.roa.*

class RoaPreviewProvider : PreviewParameterProvider<Roa> {
    override val values: Sequence<Roa> = sequenceOf(
        Roa(
            route = AdministrationRoute.INSUFFLATED,
            roaDose = RoaDose(
                "mg",
                lightMin = 20.0,
                commonMin = 40.0,
                strongMin  = 90.0,
                heavyMin = 140.0
            ),
            roaDuration = RoaDuration(
                onset = DurationRange(
                    min = 20f,
                    max = 40f,
                    units = DurationUnits.MINUTES
                ),
                comeup = DurationRange(
                    min = 15f,
                    max = 30f,
                    units = DurationUnits.MINUTES
                ),
                peak = DurationRange(
                    min = 1.5f,
                    max = 2.5f,
                    units = DurationUnits.HOURS
                ),
                offset = DurationRange(
                    min = 1f,
                    max = 1.5f,
                    units = DurationUnits.HOURS
                ),
                total = DurationRange(
                    min = 3f,
                    max = 5f,
                    units = DurationUnits.HOURS
                ),
                afterglow = DurationRange(
                    min = 12f,
                    max = 48f,
                    units = DurationUnits.HOURS
                )
            ),
            bioavailability = Bioavailability(
                min = 70.0,
                max = 75.0
            )
        ),
        Roa(
            route = AdministrationRoute.INTRAVENOUS,
            roaDose = RoaDose(
                "mg",
                lightMin = 20.0,
                commonMin = 40.0,
                strongMin  = 90.0,
                heavyMin = 140.0
            ),
            roaDuration = RoaDuration(
                onset = DurationRange(
                    min = 20f,
                    max = 40f,
                    units = DurationUnits.MINUTES
                ),
                comeup = DurationRange(
                    min = 15f,
                    max = 30f,
                    units = DurationUnits.MINUTES
                ),
                peak = DurationRange(
                    min = 1.5f,
                    max = 2.5f,
                    units = DurationUnits.HOURS
                ),
                offset = DurationRange(
                    min = 1f,
                    max = 1.5f,
                    units = DurationUnits.HOURS
                ),
                total = DurationRange(
                    min = 3f,
                    max = 5f,
                    units = DurationUnits.HOURS
                ),
                afterglow = DurationRange(
                    min = 12f,
                    max = 48f,
                    units = DurationUnits.HOURS
                )
            ),
            bioavailability = Bioavailability(
                min = 70.0,
                max = 75.0
            )
        )
    )
}