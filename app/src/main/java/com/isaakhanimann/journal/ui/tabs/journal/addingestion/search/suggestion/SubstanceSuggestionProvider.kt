/*
 * Copyright (c) 2023. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.models.PreviousDose
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.models.RouteWithDoses
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.models.SubstanceSuggestion
import com.isaakhanimann.journal.ui.utils.getInstant

class SubstanceSuggestionProvider : PreviewParameterProvider<SubstanceSuggestion> {
    override val values: Sequence<SubstanceSuggestion> = sequenceOf(
        SubstanceSuggestion(
            color = AdaptiveColor.PINK,
            substanceName = "MDMA",
            isCustom = false,
            routesWithDoses = listOf(
                RouteWithDoses(
                    route = AdministrationRoute.ORAL,
                    doses = listOf(
                        PreviousDose(
                            dose = 50.0,
                            unit = "mg",
                            isEstimate = false
                        ),
                        PreviousDose(
                            dose = 100.0,
                            unit = "mg",
                            isEstimate = false
                        ),
                        PreviousDose(
                            dose = null,
                            unit = "mg",
                            isEstimate = false
                        )
                    )
                )
            ),
            lastUsed = getInstant(year = 2023, month = 4, day = 10, hourOfDay = 5, minute = 20)!!
        ),
        SubstanceSuggestion(
            color = AdaptiveColor.BLUE,
            substanceName = "Amphetamine",
            isCustom = false,
            routesWithDoses = listOf(
                RouteWithDoses(
                    route = AdministrationRoute.INSUFFLATED,
                    doses = listOf(
                        PreviousDose(
                            dose = 10.0,
                            unit = "mg",
                            isEstimate = false
                        ),
                        PreviousDose(
                            dose = 20.0,
                            unit = "mg",
                            isEstimate = false
                        ),
                        PreviousDose(
                            dose = null,
                            unit = "mg",
                            isEstimate = false
                        )
                    )
                ),
                RouteWithDoses(
                    route = AdministrationRoute.ORAL,
                    doses = listOf(
                        PreviousDose(
                            dose = 30.0,
                            unit = "mg",
                            isEstimate = false
                        )
                    )
                )
            ),
            lastUsed = getInstant(year = 2023, month = 3, day = 10, hourOfDay = 5, minute = 20)!!
        )
    )
}