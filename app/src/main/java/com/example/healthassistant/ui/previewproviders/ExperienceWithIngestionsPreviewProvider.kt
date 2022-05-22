package com.example.healthassistant.ui.previewproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.experiences.entities.Experience
import com.example.healthassistant.data.experiences.entities.ExperienceWithIngestions
import com.example.healthassistant.data.experiences.entities.Ingestion
import com.example.healthassistant.data.experiences.entities.IngestionColor
import com.example.healthassistant.data.substances.AdministrationRoute
import java.util.*

class ExperienceWithIngestionsPreviewProvider : PreviewParameterProvider<ExperienceWithIngestions> {
    override val values: Sequence<ExperienceWithIngestions> = sequenceOf(
        ExperienceWithIngestions(
            experience = Experience(id = 0, title = "Day at Lake Geneva", date = Date(), text = "Some notes"),
            ingestions = listOf(
                Ingestion(
                    substanceName = "MDMA",
                    time = Date(),
                    administrationRoute = AdministrationRoute.ORAL,
                    dose = 90.0,
                    isDoseAnEstimate = false,
                    units = "mg",
                    color = IngestionColor.INDIGO,
                    experienceId = 0
                ),
                Ingestion(
                    substanceName = "Cocaine",
                    time = Date(),
                    administrationRoute = AdministrationRoute.INSUFFLATED,
                    dose = 20.0,
                    isDoseAnEstimate = false,
                    units = "mg",
                    color = IngestionColor.ORANGE,
                    experienceId = 0
                )
            )
        ),
        ExperienceWithIngestions(
            experience = Experience(id = 0, title = "This one has a very very very long title in case somebody wants to be creative with the naming.", date = Date(), text = "Some notes"),
            ingestions = listOf(
                Ingestion(
                    substanceName = "MDMA",
                    time = Date(),
                    administrationRoute = AdministrationRoute.ORAL,
                    dose = 90.0,
                    isDoseAnEstimate = false,
                    units = "mg",
                    color = IngestionColor.INDIGO,
                    experienceId = 0
                ),
                Ingestion(
                    substanceName = "Cocaine",
                    time = Date(),
                    administrationRoute = AdministrationRoute.INSUFFLATED,
                    dose = 20.0,
                    isDoseAnEstimate = false,
                    units = "mg",
                    color = IngestionColor.ORANGE,
                    experienceId = 0
                )
            )
        )

    )
}