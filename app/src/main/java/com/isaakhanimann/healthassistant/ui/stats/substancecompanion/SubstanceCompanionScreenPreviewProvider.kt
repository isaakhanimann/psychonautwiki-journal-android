package com.isaakhanimann.healthassistant.ui.stats.substancecompanion

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import com.isaakhanimann.healthassistant.ui.utils.getInstant

class SubstanceCompanionScreenPreviewProvider :
    PreviewParameterProvider<Pair<SubstanceCompanion, List<IngestionsBurst>>> {
    override val values: Sequence<Pair<SubstanceCompanion, List<IngestionsBurst>>> = sequenceOf(
        Pair(
            first = SubstanceCompanion(
                substanceName = "Cocaine",
                color = AdaptiveColor.BLUE
            ),
            second = listOf(
                IngestionsBurst(
                    timeUntil = "35 min",
                    ingestions = listOf(
                        Ingestion(
                            substanceName = "Cocaine",
                            time = getInstant(
                                year = 2022,
                                month = 7,
                                day = 20,
                                hourOfDay = 14,
                                minute = 20
                            )!!,
                            administrationRoute = AdministrationRoute.INSUFFLATED,
                            dose = 20.0,
                            isDoseAnEstimate = false,
                            units = "mg",
                            experienceId = 0,
                            notes = "This is one note",
                        ),
                        Ingestion(
                            substanceName = "Cocaine",
                            time = getInstant(
                                year = 2022,
                                month = 7,
                                day = 20,
                                hourOfDay = 13,
                                minute = 40
                            )!!,
                            administrationRoute = AdministrationRoute.INSUFFLATED,
                            dose = 30.0,
                            isDoseAnEstimate = false,
                            units = "mg",
                            experienceId = 0,
                            notes = "This is one note",
                        )
                    )
                ),
                IngestionsBurst(
                    timeUntil = "2 weeks",
                    ingestions = listOf(
                        Ingestion(
                            substanceName = "Cocaine",
                            time = getInstant(
                                year = 2022,
                                month = 7,
                                day = 4,
                                hourOfDay = 14,
                                minute = 20
                            )!!,
                            administrationRoute = AdministrationRoute.INSUFFLATED,
                            dose = 10.0,
                            isDoseAnEstimate = false,
                            units = "mg",
                            experienceId = 0,
                            notes = "This is one note",
                        ),
                        Ingestion(
                            substanceName = "Cocaine",
                            time = getInstant(
                                year = 2022,
                                month = 7,
                                day = 4,
                                hourOfDay = 13,
                                minute = 40
                            )!!,
                            administrationRoute = AdministrationRoute.INSUFFLATED,
                            dose = 20.0,
                            isDoseAnEstimate = false,
                            units = "mg",
                            experienceId = 0,
                            notes = "This is one note",
                        )
                    )
                )
            )
        )
    )
}