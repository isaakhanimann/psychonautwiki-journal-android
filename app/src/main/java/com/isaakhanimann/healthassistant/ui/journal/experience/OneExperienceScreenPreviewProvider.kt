package com.isaakhanimann.healthassistant.ui.journal.experience

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import com.isaakhanimann.healthassistant.data.substances.classes.roa.DoseClass
import com.isaakhanimann.healthassistant.ui.utils.getInstant

class OneExperienceScreenPreviewProvider :
    PreviewParameterProvider<OneExperienceScreenModel> {

    override val values: Sequence<OneExperienceScreenModel> = sequenceOf(
        OneExperienceScreenModel(
            isFavorite = false,
            sentiment = null,
            title = "Day at Lake Geneva",
            firstIngestionTime = getInstant(
                year = 2022,
                month = 2,
                day = 19,
                hourOfDay = 20,
                minute = 5
            )!!,
            notes = "Some Notes",
            isShowingAddIngestionButton = true,
            ingestionElements = listOf(
                IngestionElement(
                    ingestionWithCompanion = IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "MDMA",
                            time = getInstant(
                                year = 2022,
                                month = 2,
                                day = 19,
                                hourOfDay = 20,
                                minute = 5
                            )!!,
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
                    roaDuration = null,
                    doseClass = DoseClass.COMMON
                ),
                IngestionElement(
                    ingestionWithCompanion = IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "Cocaine",
                            time = getInstant(
                                year = 2022,
                                month = 2,
                                day = 19,
                                hourOfDay = 23,
                                minute = 5
                            )!!,
                            administrationRoute = AdministrationRoute.INSUFFLATED,
                            dose = 40.0,
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
                    roaDuration = null,
                    doseClass = DoseClass.COMMON
                ),
                IngestionElement(
                    ingestionWithCompanion = IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "Cocaine",
                            time = getInstant(
                                year = 2022,
                                month = 2,
                                day = 20,
                                hourOfDay = 1,
                                minute = 15
                            )!!,
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
                    ),
                    roaDuration = null,
                    doseClass = DoseClass.LIGHT
                )
            ),
            cumulativeDoses = listOf(
                CumulativeDose(
                    substanceName = "Cocaine",
                    cumulativeDose = 60.0,
                    units = "mg",
                    isEstimate = false,
                    doseClass = DoseClass.STRONG
                )
            )
        )
    )
}