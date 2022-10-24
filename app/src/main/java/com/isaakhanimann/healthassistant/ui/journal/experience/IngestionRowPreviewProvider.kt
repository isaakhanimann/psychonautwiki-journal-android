package com.isaakhanimann.healthassistant.ui.journal.experience

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import com.isaakhanimann.healthassistant.data.substances.classes.roa.DoseClass
import java.time.Instant

class IngestionRowPreviewProvider : PreviewParameterProvider<IngestionElement> {
    override val values: Sequence<IngestionElement> = sequenceOf(
        IngestionElement(
            ingestionWithCompanion = IngestionWithCompanion(
                ingestion = Ingestion(
                    substanceName = "MDMA",
                    time = Instant.now(),
                    administrationRoute = AdministrationRoute.ORAL,
                    dose = 90.0,
                    isDoseAnEstimate = false,
                    units = "mg",
                    experienceId = 0,
                    notes = "This is a very long note which I wrote to see how it looks like if the note spans more than one line in the ingestion row.",
                ),
                substanceCompanion = SubstanceCompanion(
                    substanceName = "MDMA",
                    color = AdaptiveColor.PINK
                )
            ),
            roaDuration = null,
            doseClass = DoseClass.COMMON
        ),
        IngestionElement(
            ingestionWithCompanion = IngestionWithCompanion(
                ingestion = Ingestion(
                    substanceName = "LSD",
                    time = Instant.now(),
                    administrationRoute = AdministrationRoute.ORAL,
                    dose = null,
                    isDoseAnEstimate = false,
                    units = "µg",
                    experienceId = 0,
                    notes = null,
                ),
                substanceCompanion = SubstanceCompanion(
                    substanceName = "LSD",
                    color = AdaptiveColor.BLUE
                )
            ),
            roaDuration = null,
            doseClass = DoseClass.COMMON
        )
    )
}