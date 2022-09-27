package com.isaakhanimann.healthassistant.ui.journal

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.healthassistant.data.room.experiences.entities.*
import com.isaakhanimann.healthassistant.data.room.experiences.relations.ExperienceWithIngestionsAndCompanions
import com.isaakhanimann.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import java.time.Instant
import java.time.temporal.ChronoUnit

class ExperienceWithIngestionsPreviewProvider :
    PreviewParameterProvider<ExperienceWithIngestionsAndCompanions> {
    override val values: Sequence<ExperienceWithIngestionsAndCompanions> = sequenceOf(
        ExperienceWithIngestionsAndCompanions(
            experience = Experience(
                id = 0,
                title = "Day at Lake Geneva",
                text = "Some notes",
                sentiment = Sentiment.SATISFIED,
                isFavorite = true
            ),
            ingestionsWithCompanions = listOf(
                IngestionWithCompanion(
                    ingestion = Ingestion(
                        substanceName = "MDMA",
                        time = Instant.now().minus(2, ChronoUnit.HOURS),
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = 90.0,
                        isDoseAnEstimate = false,
                        units = "mg",
                        experienceId = 0,
                        notes = null,
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "MDMA",
                        color = SubstanceColor.PINK
                    )
                ),
                IngestionWithCompanion(
                    ingestion = Ingestion(
                        substanceName = "Cocaine",
                        time = Instant.now().minus(1, ChronoUnit.HOURS),
                        administrationRoute = AdministrationRoute.INSUFFLATED,
                        dose = 30.0,
                        isDoseAnEstimate = false,
                        units = "mg",
                        experienceId = 0,
                        notes = null,
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "Cocaine",
                        color = SubstanceColor.BLUE
                    )
                ),
                IngestionWithCompanion(
                    ingestion = Ingestion(
                        substanceName = "Cocaine",
                        time = Instant.now().minus(30, ChronoUnit.MINUTES),
                        administrationRoute = AdministrationRoute.INSUFFLATED,
                        dose = 20.0,
                        isDoseAnEstimate = false,
                        units = "mg",
                        experienceId = 0,
                        notes = null,
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "Cocaine",
                        color = SubstanceColor.BLUE
                    )
                )

            )
        ),
        ExperienceWithIngestionsAndCompanions(
            experience = Experience(
                id = 0,
                title = "This one has a very very very long title in case somebody wants to be creative with the naming.",
                text = "Some notes",
                sentiment = null,
                isFavorite = true
            ),
            ingestionsWithCompanions = listOf(
                IngestionWithCompanion(
                    ingestion = Ingestion(
                        substanceName = "MDMA",
                        time = Instant.now(),
                        administrationRoute = AdministrationRoute.ORAL,
                        dose = 90.0,
                        isDoseAnEstimate = false,
                        units = "mg",
                        experienceId = 0,
                        notes = null,
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "MDMA",
                        color = SubstanceColor.PINK
                    )
                ),
                IngestionWithCompanion(
                    ingestion = Ingestion(
                        substanceName = "Cocaine",
                        time = Instant.now(),
                        administrationRoute = AdministrationRoute.INSUFFLATED,
                        dose = 20.0,
                        isDoseAnEstimate = false,
                        units = "mg",
                        experienceId = 0,
                        notes = null,
                    ),
                    substanceCompanion = SubstanceCompanion(
                        substanceName = "Cocaine",
                        color = SubstanceColor.BLUE
                    )
                )
            )
        )
    )
}