package com.example.healthassistant.ui.experiences.experience.timeline

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.room.experiences.entities.Sentiment
import com.example.healthassistant.data.room.experiences.entities.SubstanceColor
import com.example.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.example.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.DurationRange
import com.example.healthassistant.data.substances.RoaDuration
import java.util.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class TimelinesPreviewProvider :
    PreviewParameterProvider<List<Pair<IngestionWithCompanion, RoaDuration>>> {
    override val values: Sequence<List<Pair<IngestionWithCompanion, RoaDuration>>> = sequenceOf(
        listOf(
            Pair(
                IngestionWithCompanion(
                    ingestion = Ingestion(
                        substanceName = "Substance 1",
                        time = Date(Date().time - 4 * 60 * 60 * 1000),
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = 90.0,
                        isDoseAnEstimate = false,
                        units = "mg",
                        experienceId = 0,
                        notes = null,
                        sentiment = Sentiment.SATISFIED
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "Substance 1",
                        color = SubstanceColor.BLUE
                    )
                ),

                RoaDuration(
                    onset = DurationRange(
                        min = 20.toDuration(DurationUnit.MINUTES),
                        max = 40.toDuration(DurationUnit.MINUTES),
                    ),
                    comeup = DurationRange(
                        min = 15.toDuration(DurationUnit.MINUTES),
                        max = 30.toDuration(DurationUnit.MINUTES),
                    ),
                    peak = DurationRange(
                        min = 1.5.toDuration(DurationUnit.HOURS),
                        max = 2.5.toDuration(DurationUnit.HOURS),
                    ),
                    offset = DurationRange(
                        min = 2.toDuration(DurationUnit.HOURS),
                        max = 4.toDuration(DurationUnit.HOURS),
                    ),
                    total = DurationRange(
                        min = 3.toDuration(DurationUnit.HOURS),
                        max = 5.toDuration(DurationUnit.HOURS),
                    ),
                    afterglow = DurationRange(
                        min = 12.toDuration(DurationUnit.HOURS),
                        max = 48.toDuration(DurationUnit.HOURS),
                    )
                )
            ),
            Pair(
                IngestionWithCompanion(
                    ingestion = Ingestion(
                        substanceName = "Substance 1",
                        time = Date(Date().time - 3 * 60 * 60 * 1000),
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = 45.0,
                        isDoseAnEstimate = false,
                        units = "mg",
                        experienceId = 0,
                        notes = null,
                        sentiment = Sentiment.SATISFIED
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "Substance 1",
                        color = SubstanceColor.BLUE
                    )
                ),
                RoaDuration(
                    onset = DurationRange(
                        min = 20.toDuration(DurationUnit.MINUTES),
                        max = 40.toDuration(DurationUnit.MINUTES),
                    ),
                    comeup = DurationRange(
                        min = 15.toDuration(DurationUnit.MINUTES),
                        max = 30.toDuration(DurationUnit.MINUTES),
                    ),
                    peak = DurationRange(
                        min = 1.5.toDuration(DurationUnit.HOURS),
                        max = 2.5.toDuration(DurationUnit.HOURS),
                    ),
                    offset = DurationRange(
                        min = 2.toDuration(DurationUnit.HOURS),
                        max = 4.toDuration(DurationUnit.HOURS),
                    ),
                    total = DurationRange(
                        min = 3.toDuration(DurationUnit.HOURS),
                        max = 5.toDuration(DurationUnit.HOURS),
                    ),
                    afterglow = DurationRange(
                        min = 12.toDuration(DurationUnit.HOURS),
                        max = 48.toDuration(DurationUnit.HOURS),
                    )
                )
            ),
            Pair(
                IngestionWithCompanion(
                    ingestion = Ingestion(
                        substanceName = "Substance 2",
                        time = Date(Date().time - 60 * 60 * 1000),
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = 90.0,
                        isDoseAnEstimate = false,
                        units = "mg",
                        experienceId = 0,
                        notes = null,
                        sentiment = Sentiment.SATISFIED
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "Substance 2",
                        color = SubstanceColor.RED
                    )
                ),
                RoaDuration(
                    onset = DurationRange(
                        min = 20.toDuration(DurationUnit.MINUTES),
                        max = 40.toDuration(DurationUnit.MINUTES),
                    ),
                    comeup = DurationRange(
                        min = 15.toDuration(DurationUnit.MINUTES),
                        max = 30.toDuration(DurationUnit.MINUTES),
                    ),
                    peak = DurationRange(
                        min = 4.toDuration(DurationUnit.HOURS),
                        max = 6.toDuration(DurationUnit.HOURS),
                    ),
                    offset = DurationRange(
                        min = 2.toDuration(DurationUnit.HOURS),
                        max = 3.toDuration(DurationUnit.HOURS),
                    ),
                    total = DurationRange(
                        min = 6.toDuration(DurationUnit.HOURS),
                        max = 11.toDuration(DurationUnit.HOURS),
                    ),
                    afterglow = DurationRange(
                        min = 12.toDuration(DurationUnit.HOURS),
                        max = 48.toDuration(DurationUnit.HOURS),
                    )
                )
            ),
            Pair(
                IngestionWithCompanion(
                    ingestion = Ingestion(
                        substanceName = "Substance 3",
                        time = Date(),
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = 90.0,
                        isDoseAnEstimate = false,
                        units = "mg",
                        experienceId = 0,
                        notes = null,
                        sentiment = Sentiment.SATISFIED
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "Substance 3",
                        color = SubstanceColor.ORANGE
                    )
                ),
                RoaDuration(
                    onset = null,
                    comeup = null,
                    peak = null,
                    offset = null,
                    total = DurationRange(
                        min = 3.toDuration(DurationUnit.HOURS),
                        max = 5.toDuration(DurationUnit.HOURS),
                    ),
                    afterglow = null
                )
            )
        ),
        listOf(
            Pair(
                IngestionWithCompanion(
                    ingestion = Ingestion(
                        substanceName = "Substance 1",
                        time = Date(Date().time - 3 * 60 * 60 * 1000),
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = 45.0,
                        isDoseAnEstimate = false,
                        units = "mg",
                        experienceId = 0,
                        notes = null,
                        sentiment = Sentiment.SATISFIED
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "Substance 1",
                        color = SubstanceColor.BLUE
                    )
                ),
                RoaDuration(
                    onset = DurationRange(
                        min = 20.toDuration(DurationUnit.MINUTES),
                        max = 40.toDuration(DurationUnit.MINUTES),
                    ),
                    comeup = DurationRange(
                        min = 15.toDuration(DurationUnit.MINUTES),
                        max = 30.toDuration(DurationUnit.MINUTES),
                    ),
                    peak = DurationRange(
                        min = 4.toDuration(DurationUnit.HOURS),
                        max = 6.toDuration(DurationUnit.HOURS),
                    ),
                    offset = DurationRange(
                        min = 2.toDuration(DurationUnit.HOURS),
                        max = 4.toDuration(DurationUnit.HOURS),
                    ),
                    total = DurationRange(
                        min = 3.toDuration(DurationUnit.HOURS),
                        max = 5.toDuration(DurationUnit.HOURS),
                    ),
                    afterglow = DurationRange(
                        min = 12.toDuration(DurationUnit.HOURS),
                        max = 48.toDuration(DurationUnit.HOURS),
                    )
                )
            ),
            Pair(
                IngestionWithCompanion(
                    ingestion = Ingestion(
                        substanceName = "Substance 2",
                        time = Date(Date().time - 60 * 60 * 1000),
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = 90.0,
                        isDoseAnEstimate = false,
                        units = "mg",
                        experienceId = 0,
                        notes = null,
                        sentiment = Sentiment.SATISFIED
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "Substance 2",
                        color = SubstanceColor.ORANGE
                    )
                ),
                RoaDuration(
                    onset = DurationRange(
                        min = 20.toDuration(DurationUnit.MINUTES),
                        max = 40.toDuration(DurationUnit.MINUTES),
                    ),
                    comeup = DurationRange(
                        min = 15.toDuration(DurationUnit.MINUTES),
                        max = 30.toDuration(DurationUnit.MINUTES),
                    ),
                    peak = DurationRange(
                        min = 4.toDuration(DurationUnit.HOURS),
                        max = 6.toDuration(DurationUnit.HOURS),
                    ),
                    offset = DurationRange(
                        min = 2.toDuration(DurationUnit.HOURS),
                        max = 3.toDuration(DurationUnit.HOURS),
                    ),
                    total = DurationRange(
                        min = 6.toDuration(DurationUnit.HOURS),
                        max = 11.toDuration(DurationUnit.HOURS),
                    ),
                    afterglow = DurationRange(
                        min = 12.toDuration(DurationUnit.HOURS),
                        max = 48.toDuration(DurationUnit.HOURS),
                    )
                )
            ),
            Pair(
                IngestionWithCompanion(
                    ingestion = Ingestion(
                        substanceName = "Substance 3",
                        time = Date(),
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = 90.0,
                        isDoseAnEstimate = false,
                        units = "mg",
                        experienceId = 0,
                        notes = null,
                        sentiment = Sentiment.SATISFIED
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "Substance 3",
                        color = SubstanceColor.GREEN
                    )
                ),
                RoaDuration(
                    onset = DurationRange(
                        min = 20.toDuration(DurationUnit.MINUTES),
                        max = 40.toDuration(DurationUnit.MINUTES),
                    ),
                    comeup = DurationRange(
                        min = 15.toDuration(DurationUnit.MINUTES),
                        max = 30.toDuration(DurationUnit.MINUTES),
                    ),
                    peak = DurationRange(
                        min = 4.toDuration(DurationUnit.HOURS),
                        max = 6.toDuration(DurationUnit.HOURS),
                    ),
                    offset = DurationRange(
                        min = 2.toDuration(DurationUnit.HOURS),
                        max = 3.toDuration(DurationUnit.HOURS),
                    ),
                    total = DurationRange(
                        min = 6.toDuration(DurationUnit.HOURS),
                        max = 11.toDuration(DurationUnit.HOURS),
                    ),
                    afterglow = DurationRange(
                        min = 12.toDuration(DurationUnit.HOURS),
                        max = 48.toDuration(DurationUnit.HOURS),
                    )
                )
            )
        ),
        listOf(
            Pair(
                IngestionWithCompanion(
                    ingestion = Ingestion(
                        substanceName = "Substance 1",
                        time = Date(Date().time - 20 * 60 * 60 * 1000),
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = 90.0,
                        isDoseAnEstimate = false,
                        units = "mg",
                        experienceId = 0,
                        notes = null,
                        sentiment = Sentiment.SATISFIED
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "Substance 1",
                        color = SubstanceColor.BLUE
                    )
                ),

                RoaDuration(
                    onset = DurationRange(
                        min = 20.toDuration(DurationUnit.MINUTES),
                        max = 40.toDuration(DurationUnit.MINUTES),
                    ),
                    comeup = DurationRange(
                        min = 15.toDuration(DurationUnit.MINUTES),
                        max = 30.toDuration(DurationUnit.MINUTES),
                    ),
                    peak = DurationRange(
                        min = 1.5.toDuration(DurationUnit.HOURS),
                        max = 2.5.toDuration(DurationUnit.HOURS),
                    ),
                    offset = DurationRange(
                        min = 2.toDuration(DurationUnit.HOURS),
                        max = 4.toDuration(DurationUnit.HOURS),
                    ),
                    total = DurationRange(
                        min = 3.toDuration(DurationUnit.HOURS),
                        max = 5.toDuration(DurationUnit.HOURS),
                    ),
                    afterglow = DurationRange(
                        min = 12.toDuration(DurationUnit.HOURS),
                        max = 48.toDuration(DurationUnit.HOURS),
                    )
                )
            ),
            Pair(
                IngestionWithCompanion(
                    ingestion = Ingestion(
                        substanceName = "Substance 2",
                        time = Date(),
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = 90.0,
                        isDoseAnEstimate = false,
                        units = "mg",
                        experienceId = 0,
                        notes = null,
                        sentiment = Sentiment.SATISFIED
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "Substance 2",
                        color = SubstanceColor.GREEN
                    )
                ),
                RoaDuration(
                    onset = DurationRange(
                        min = 20.toDuration(DurationUnit.MINUTES),
                        max = 40.toDuration(DurationUnit.MINUTES),
                    ),
                    comeup = DurationRange(
                        min = 15.toDuration(DurationUnit.MINUTES),
                        max = 30.toDuration(DurationUnit.MINUTES),
                    ),
                    peak = DurationRange(
                        min = 1.5.toDuration(DurationUnit.HOURS),
                        max = 2.5.toDuration(DurationUnit.HOURS),
                    ),
                    offset = DurationRange(
                        min = 2.toDuration(DurationUnit.HOURS),
                        max = 4.toDuration(DurationUnit.HOURS),
                    ),
                    total = DurationRange(
                        min = 3.toDuration(DurationUnit.HOURS),
                        max = 5.toDuration(DurationUnit.HOURS),
                    ),
                    afterglow = DurationRange(
                        min = 12.toDuration(DurationUnit.HOURS),
                        max = 48.toDuration(DurationUnit.HOURS),
                    )
                )
            ),
        )
    )
}