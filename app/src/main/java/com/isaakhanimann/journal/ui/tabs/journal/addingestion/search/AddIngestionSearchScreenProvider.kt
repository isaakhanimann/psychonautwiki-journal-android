/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
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