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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.search

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.substances.AdministrationRoute

class AddIngestionSearchScreenProvider : PreviewParameterProvider<List<PreviousSubstance>> {
    override val values: Sequence<List<PreviousSubstance>> = sequenceOf(
        listOf(
            PreviousSubstance(
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
                )
            ),
            PreviousSubstance(
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
                )
            )
        )
    )
}