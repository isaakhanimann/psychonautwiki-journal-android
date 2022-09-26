package com.isaakhanimann.healthassistant.ui.journal.experience

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import com.isaakhanimann.healthassistant.data.substances.classes.roa.DoseClass
import java.util.*

class IngestionRowPreviewProvider : PreviewParameterProvider<OneExperienceViewModel.IngestionElement> {
    override val values: Sequence<OneExperienceViewModel.IngestionElement> = sequenceOf(
        OneExperienceViewModel.IngestionElement(
            dateText = null,
            ingestionWithCompanion = IngestionWithCompanion(
                ingestion = Ingestion(
                    substanceName = "MDMA",
                    time = Date(),
                    administrationRoute = AdministrationRoute.ORAL,
                    dose = 90.0,
                    isDoseAnEstimate = false,
                    units = "mg",
                    experienceId = 0,
                    notes = "This is a note",
                ),
                substanceCompanion = SubstanceCompanion(
                    substanceName = "MDMA",
                    color = SubstanceColor.PINK
                )
            ),
            roaDuration = null,
            doseClass = DoseClass.COMMON
        ),
        OneExperienceViewModel.IngestionElement(
            dateText = null,
            ingestionWithCompanion = IngestionWithCompanion(
                ingestion = Ingestion(
                    substanceName = "LSD",
                    time = Date(),
                    administrationRoute = AdministrationRoute.ORAL,
                    dose = null,
                    isDoseAnEstimate = false,
                    units = "µg",
                    experienceId = 0,
                    notes = null,
                ),
                substanceCompanion = SubstanceCompanion(
                    substanceName = "LSD",
                    color = SubstanceColor.BLUE
                )
            ),
            roaDuration = null,
            doseClass = DoseClass.COMMON
        )
    )
}