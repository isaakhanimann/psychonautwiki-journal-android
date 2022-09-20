package com.isaakhanimann.healthassistant.ui.ingestions.stats

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute

class StatsPreviewProvider : PreviewParameterProvider<List<SubstanceStat>> {
    override val values: Sequence<List<SubstanceStat>> = sequenceOf(
        listOf(
            SubstanceStat(
                substanceName = "LSD",
                color = SubstanceColor.BLUE,
                ingestionCount = 3,
                routeCounts = listOf(
                    RouteCount(
                        administrationRoute = AdministrationRoute.SUBLINGUAL,
                        count = 3
                    )
                ),
                cumulativeDose = CumulativeDose(
                    dose = 500.0,
                    units = "ug",
                    isEstimate = false
                )
            ),
            SubstanceStat(
                substanceName = "MDMA",
                color = SubstanceColor.PINK,
                ingestionCount = 8,
                routeCounts = listOf(
                    RouteCount(
                        administrationRoute = AdministrationRoute.ORAL,
                        count = 6
                    ),
                    RouteCount(
                        administrationRoute = AdministrationRoute.INSUFFLATED,
                        count = 2
                    )
                ),
                cumulativeDose = CumulativeDose(
                    dose = 950.0,
                    units = "mg",
                    isEstimate = true
                )
            ),
            SubstanceStat(
                substanceName = "Cocaine",
                color = SubstanceColor.ORANGE,
                ingestionCount = 20,
                routeCounts = listOf(
                    RouteCount(
                        administrationRoute = AdministrationRoute.INSUFFLATED,
                        count = 20
                    )
                ),
                cumulativeDose = null
            ),
            SubstanceStat(
                substanceName = "Ketamine",
                color = SubstanceColor.PURPLE,
                ingestionCount = 1,
                routeCounts = listOf(
                    RouteCount(
                        administrationRoute = AdministrationRoute.INSUFFLATED,
                        count = 1
                    )
                ),
                cumulativeDose = CumulativeDose(
                    dose = 30.0,
                    units = "mg",
                    isEstimate = true
                )
            )
        ),
    )
}