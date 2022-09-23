package com.isaakhanimann.healthassistant.ui.stats

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute

class StatsPreviewProvider : PreviewParameterProvider<StatsModel> {
    override val values: Sequence<StatsModel> = sequenceOf(
        StatsModel(
            selectedOption = TimePickerOption.DAYS_7,
            startDateText = "22. June 2022",
            ingestionStats = listOf(
                IngestionStat(
                    substanceName = "LSD",
                    color = SubstanceColor.BLUE,
                    ingestionCount = 3,
                    routeCounts = listOf(
                        RouteCount(
                            administrationRoute = AdministrationRoute.SUBLINGUAL,
                            count = 3
                        )
                    ),
                    totalDose = TotalDose(
                        dose = 500.0,
                        units = "ug",
                        isEstimate = false
                    )
                ),
                IngestionStat(
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
                    totalDose = TotalDose(
                        dose = 950.0,
                        units = "mg",
                        isEstimate = true
                    )
                ),
                IngestionStat(
                    substanceName = "Cocaine",
                    color = SubstanceColor.ORANGE,
                    ingestionCount = 11,
                    routeCounts = listOf(
                        RouteCount(
                            administrationRoute = AdministrationRoute.INSUFFLATED,
                            count = 11
                        )
                    ),
                    totalDose = null
                ),
                IngestionStat(
                    substanceName = "Ketamine",
                    color = SubstanceColor.PURPLE,
                    ingestionCount = 1,
                    routeCounts = listOf(
                        RouteCount(
                            administrationRoute = AdministrationRoute.INSUFFLATED,
                            count = 1
                        )
                    ),
                    totalDose = TotalDose(
                        dose = 30.0,
                        units = "mg",
                        isEstimate = true
                    )
                )
            ),
            ingestionChartBuckets = listOf(
                listOf(
                    ColorCount(
                        color = SubstanceColor.PINK,
                        count = 8
                    ),
                    ColorCount(
                        color = SubstanceColor.BLUE,
                        count = 3
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