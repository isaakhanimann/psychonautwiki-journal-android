package com.example.healthassistant.ui.previewproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.room.experiences.entities.Experience
import com.example.healthassistant.data.room.experiences.entities.ExperienceWithIngestions
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.room.experiences.entities.IngestionColor
import com.example.healthassistant.data.substances.AdministrationRoute
import java.util.*

class ExperienceWithIngestionsPreviewProvider : PreviewParameterProvider<ExperienceWithIngestions> {
    override val values: Sequence<ExperienceWithIngestions> = sequenceOf(
        ExperienceWithIngestions(
            experience = Experience(
                id = 0,
                title = "Day at Lake Geneva",
                text = "Some notes"
            ),
            ingestions = listOf(
                Ingestion(
                    substanceName = "MDMA",
                    time = Date(Date().time - 2*60*60*1000),
                    administrationRoute = AdministrationRoute.ORAL,
                    dose = 90.0,
                    isDoseAnEstimate = false,
                    units = "mg",
                    color = IngestionColor.INDIGO,
                    experienceId = 0,
                    notes = null
                ),
                Ingestion(
                    substanceName = "Cocaine",
                    time = Date(Date().time - 60*60*1000),
                    administrationRoute = AdministrationRoute.INSUFFLATED,
                    dose = 30.0,
                    isDoseAnEstimate = false,
                    units = "mg",
                    color = IngestionColor.ORANGE,
                    experienceId = 0,
                    notes = null
                ),
                Ingestion(
                    substanceName = "Cocaine",
                    time = Date(Date().time - 30*60*1000),
                    administrationRoute = AdministrationRoute.INSUFFLATED,
                    dose = 20.0,
                    isDoseAnEstimate = false,
                    units = "mg",
                    color = IngestionColor.ORANGE,
                    experienceId = 0,
                    notes = null
                )
            )
        ),
        ExperienceWithIngestions(
            experience = Experience(
                id = 0,
                title = "This one has a very very very long title in case somebody wants to be creative with the naming.",
                text = "Some notes"
            ),
            ingestions = listOf(
                Ingestion(
                    substanceName = "MDMA",
                    time = Date(),
                    administrationRoute = AdministrationRoute.ORAL,
                    dose = 90.0,
                    isDoseAnEstimate = false,
                    units = "mg",
                    color = IngestionColor.INDIGO,
                    experienceId = 0,
                    notes = null
                ),
                Ingestion(
                    substanceName = "Cocaine",
                    time = Date(),
                    administrationRoute = AdministrationRoute.INSUFFLATED,
                    dose = 20.0,
                    isDoseAnEstimate = false,
                    units = "mg",
                    color = IngestionColor.ORANGE,
                    experienceId = 0,
                    notes = null
                )
            )
        )

    )
}