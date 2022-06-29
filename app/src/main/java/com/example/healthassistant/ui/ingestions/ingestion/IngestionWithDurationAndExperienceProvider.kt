package com.example.healthassistant.ui.ingestions.ingestion

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.room.experiences.entities.Sentiment
import com.example.healthassistant.data.room.experiences.entities.SubstanceColor
import com.example.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.example.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.DurationRange
import com.example.healthassistant.data.substances.DurationUnits
import com.example.healthassistant.data.substances.RoaDuration
import java.util.*

class IngestionWithDurationAndExperienceProvider :
    PreviewParameterProvider<IngestionWithCompanionDurationAndExperience> {
    override val values: Sequence<IngestionWithCompanionDurationAndExperience> = sequenceOf(
        IngestionWithCompanionDurationAndExperience(
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
            experience = null
        ),
    )
}