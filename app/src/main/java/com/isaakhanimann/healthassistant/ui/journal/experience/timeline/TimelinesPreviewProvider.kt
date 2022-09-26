package com.isaakhanimann.healthassistant.ui.journal.experience.timeline

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import com.isaakhanimann.healthassistant.data.substances.classes.roa.DurationRange
import com.isaakhanimann.healthassistant.data.substances.classes.roa.DurationUnits
import com.isaakhanimann.healthassistant.data.substances.classes.roa.RoaDuration
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
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "Substance 1",
                        color = SubstanceColor.BLUE
                    )
                ),
                RoaDuration(
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
                        min = 2f,
                        max = 4f,
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
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "Substance 1",
                        color = SubstanceColor.BLUE
                    )
                ),
                RoaDuration(
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
                        min = 2f,
                        max = 4f,
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
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "Substance 2",
                        color = SubstanceColor.RED
                    )
                ),
                RoaDuration(
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
                        min = 4f,
                        max = 6f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 3f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 6f,
                        max = 11f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
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
                        min = 3.toDuration(DurationUnit.HOURS).inWholeSeconds.toFloat(),
                        max = 5.toDuration(DurationUnit.HOURS).inWholeSeconds.toFloat(),
                        units = DurationUnits.HOURS
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
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "Substance 1",
                        color = SubstanceColor.BLUE
                    )
                ),
                RoaDuration(
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
                        min = 4f,
                        max = 6f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 4f,
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
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "Substance 2",
                        color = SubstanceColor.ORANGE
                    )
                ),
                RoaDuration(
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
                        min = 4f,
                        max = 6f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 3f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 6f,
                        max = 11f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
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
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "Substance 3",
                        color = SubstanceColor.GREEN
                    )
                ),
                RoaDuration(
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
                        min = 4f,
                        max = 6f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 3f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 6f,
                        max = 11f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
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
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "Substance 1",
                        color = SubstanceColor.BLUE
                    )
                ),

                RoaDuration(
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
                        min = 2f,
                        max = 4f,
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
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "Substance 2",
                        color = SubstanceColor.GREEN
                    )
                ),
                RoaDuration(
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
                        min = 2f,
                        max = 4f,
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
                )
            ),
        )
    )
}