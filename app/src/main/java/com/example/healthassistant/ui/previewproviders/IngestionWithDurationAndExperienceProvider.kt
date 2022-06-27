package com.example.healthassistant.ui.previewproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.room.experiences.entities.SubstanceColor
import com.example.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.DurationRange
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.ui.ingestions.ingestion.IngestionWithCompanionDurationAndExperience
import java.util.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class IngestionWithDurationAndExperienceProvider :
    PreviewParameterProvider<IngestionWithCompanionDurationAndExperience> {
    override val values: Sequence<IngestionWithCompanionDurationAndExperience> = sequenceOf(
        IngestionWithCompanionDurationAndExperience(
            ingestion = Ingestion(
                substanceName = "Substance 1",
                time = Date(Date().time - 4 * 60 * 60 * 1000),
                administrationRoute = AdministrationRoute.ORAL,
                dose = 90.0,
                isDoseAnEstimate = false,
                units = "mg",
                experienceId = 0,
                notes = "This is my note"
            ),
            substanceCompanion = SubstanceCompanion(
                substanceName = "Substance 1",
                color = SubstanceColor.MINT
            ),
            roaDuration = RoaDuration(
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
            ),
            experience = null
        ),
    )
}