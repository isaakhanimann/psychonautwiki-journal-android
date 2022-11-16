/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.journal.experience

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.journal.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.data.substances.classes.roa.DoseClass
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