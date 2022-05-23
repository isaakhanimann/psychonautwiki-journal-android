package com.example.healthassistant.ui.previewproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.experiences.entities.Ingestion
import com.example.healthassistant.data.experiences.entities.IngestionColor
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.DurationRange
import com.example.healthassistant.data.substances.RoaDuration
import java.util.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class TimelinesPreviewProvider : PreviewParameterProvider<List<Pair<Ingestion, RoaDuration>>> {
    override val values: Sequence<List<Pair<Ingestion, RoaDuration>>> = sequenceOf(
        listOf(
            Pair(
                Ingestion(
                    substanceName = "Example Substance 1",
                    time = Date(Date().time - 2*60*60),
                    administrationRoute = AdministrationRoute.ORAL,
                    dose = 90.0,
                    isDoseAnEstimate = false,
                    units = "mg",
                    color = IngestionColor.INDIGO,
                    experienceId = 0
                ),
                RoaDuration(
                    onset = DurationRange(
                        min = 20.0.toDuration(DurationUnit.MINUTES),
                        max = 40.0.toDuration(DurationUnit.MINUTES),
                    ),
                    comeup = DurationRange(
                        min = 15.0.toDuration(DurationUnit.MINUTES),
                        max = 30.0.toDuration(DurationUnit.MINUTES),
                    ),
                    peak = DurationRange(
                        min = 1.5.toDuration(DurationUnit.HOURS),
                        max = 2.5.toDuration(DurationUnit.HOURS),
                    ),
                    offset = DurationRange(
                        min = 1.0.toDuration(DurationUnit.HOURS),
                        max = 1.5.toDuration(DurationUnit.HOURS),
                    ),
                    total = DurationRange(
                        min = 3.0.toDuration(DurationUnit.HOURS),
                        max = 5.0.toDuration(DurationUnit.HOURS),
                    ),
                    afterglow = DurationRange(
                        min = 12.0.toDuration(DurationUnit.HOURS),
                        max = 48.0.toDuration(DurationUnit.HOURS),
                    )
                )
            ),
            Pair(
                Ingestion(
                    substanceName = "Example Substance 2",
                    time = Date(Date().time - 60*60),
                    administrationRoute = AdministrationRoute.ORAL,
                    dose = 90.0,
                    isDoseAnEstimate = false,
                    units = "mg",
                    color = IngestionColor.INDIGO,
                    experienceId = 0
                ),
                RoaDuration(
                    onset = DurationRange(
                        min = 20.0.toDuration(DurationUnit.MINUTES),
                        max = 40.0.toDuration(DurationUnit.MINUTES),
                    ),
                    comeup = DurationRange(
                        min = 15.0.toDuration(DurationUnit.MINUTES),
                        max = 30.0.toDuration(DurationUnit.MINUTES),
                    ),
                    peak = DurationRange(
                        min = 1.5.toDuration(DurationUnit.HOURS),
                        max = 2.5.toDuration(DurationUnit.HOURS),
                    ),
                    offset = DurationRange(
                        min = 1.0.toDuration(DurationUnit.HOURS),
                        max = 1.5.toDuration(DurationUnit.HOURS),
                    ),
                    total = DurationRange(
                        min = 3.0.toDuration(DurationUnit.HOURS),
                        max = 5.0.toDuration(DurationUnit.HOURS),
                    ),
                    afterglow = DurationRange(
                        min = 12.0.toDuration(DurationUnit.HOURS),
                        max = 48.0.toDuration(DurationUnit.HOURS),
                    )
                )
            )

        )
    )
}