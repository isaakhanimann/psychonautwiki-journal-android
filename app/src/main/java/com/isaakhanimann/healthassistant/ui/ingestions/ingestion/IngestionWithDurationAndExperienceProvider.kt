package com.isaakhanimann.healthassistant.ui.ingestions.ingestion

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Sentiment
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.healthassistant.data.substances.*
import java.util.*

class IngestionWithDurationAndExperienceProvider :
    PreviewParameterProvider<OneIngestionViewModel.IngestionWithCompanionDurationAndExperience> {
    override val values: Sequence<OneIngestionViewModel.IngestionWithCompanionDurationAndExperience> = sequenceOf(
        OneIngestionViewModel.IngestionWithCompanionDurationAndExperience(
            ingestionWithCompanion = IngestionWithCompanion(
                ingestion = Ingestion(
                    substanceName = "Substance 1",
                    time = Date(Date().time - 4 * 60 * 60 * 1000),
                    administrationRoute = AdministrationRoute.ORAL,
                    dose = 90.0,
                    isDoseAnEstimate = false,
                    units = "mg",
                    experienceId = 0,
                    notes = "This is my note",
                    sentiment = Sentiment.SATISFIED
                ),
                substanceCompanion = SubstanceCompanion(
                    substanceName = "Substance 1",
                    color = SubstanceColor.MINT
                ),
            ),
            roaDuration = RoaDuration(
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
            ),
            roaDose = RoaDose(
                "mg",
                threshold = 20.0,
                light = DoseRange(
                    min = 20.0,
                    max = 40.0
                ),
                common = DoseRange(
                    min = 40.0,
                    max = 90.0
                ),
                strong = DoseRange(
                    min = 90.0,
                    max = 140.0
                ),
                heavy = 140.0
            ),
            experience = null
        ),
    )
}