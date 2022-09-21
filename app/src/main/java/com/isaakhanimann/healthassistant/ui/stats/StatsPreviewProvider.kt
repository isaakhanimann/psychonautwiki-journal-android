package com.isaakhanimann.healthassistant.ui.stats

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute

class StatsPreviewProvider : PreviewParameterProvider<StatsModel> {
    override val values: Sequence<StatsModel> = sequenceOf(
        StatsModel(
            selectedOption = TimePickerOption.DAYS_7,
            startDateText = "22. June 2022",
            substanceStats = listOf(
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
                    ingestionCount = 11,
                    routeCounts = listOf(
                        RouteCount(
                            administrationRoute = AdministrationRoute.INSUFFLATED,
                            count = 11
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
            chartBuckets = listOf(
                listOf(
                    ColorCount(
                        color = SubstanceColor.BLUE,
                        count = 3
                    ),
                    ColorCount(
                        color = SubstanceColor.PINK,
                        count = 8
                    ),
                ),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(
                    ColorCount(
                        color = SubstanceColor.ORANGE,
                        count = 11
                    ),
                    ColorCount(
                        color = SubstanceColor.PINK,
                        count = 8
                    ),
                ),
                listOf(
                    ColorCount(
                        color = SubstanceColor.PURPLE,
                        count = 1
                    )
                )
            )
        )
    )
}