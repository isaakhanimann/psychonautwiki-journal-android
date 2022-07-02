package com.example.healthassistant.ui.experiences.experience

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.room.experiences.entities.*
import com.example.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.DoseClass
import com.example.healthassistant.ui.utils.getDate

class OneExperienceScreenPreviewProvider :
    PreviewParameterProvider<OneExperienceScreenPreviewProvider.AllThatIsNeeded> {

    data class AllThatIsNeeded(
        val experience: Experience,
        val ingestionElements: List<OneExperienceViewModel.IngestionElement>,
        val cumulativeDoses: List<OneExperienceViewModel.CumulativeDose>
    )

    override val values: Sequence<AllThatIsNeeded> = sequenceOf(
        AllThatIsNeeded(
            experience = Experience(
                id = 0,
                title = "Day at Lake Geneva",
                text = "Some notes",
                sentiment = Sentiment.SATISFIED
            ),
            ingestionElements = listOf(
                OneExperienceViewModel.IngestionElement(
                    dateText = "Sat, 19 Feb 2022",
                    ingestionWithCompanion = IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "MDMA",
                            time = getDate(year = 2022, month = 2, day = 19, hourOfDay = 20, minute = 5)!!,
                            administrationRoute = AdministrationRoute.ORAL,
                            dose = 90.0,
                            isDoseAnEstimate = false,
                            units = "mg",
                            experienceId = 0,
                            notes = null,
                            sentiment = Sentiment.SATISFIED
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
                            substanceName = "Cocaine",
                            time = getDate(year = 2022, month = 2, day = 19, hourOfDay = 23, minute = 5)!!,
                            administrationRoute = AdministrationRoute.INSUFFLATED,
                            dose = 40.0,
                            isDoseAnEstimate = false,
                            units = "mg",
                            experienceId = 0,
                            notes = null,
                            sentiment = null
                        ),
                        substanceCompanion = SubstanceCompanion(
                            substanceName = "Cocaine",
                            color = SubstanceColor.BLUE
                        )
                    ),
                    roaDuration = null,
                    doseClass = DoseClass.COMMON
                ),
                OneExperienceViewModel.IngestionElement(
                    dateText = "Sun, 20 Feb 2022",
                    ingestionWithCompanion = IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "Cocaine",
                            time = getDate(year = 2022, month = 2, day = 20, hourOfDay = 1, minute = 15)!!,
                            administrationRoute = AdministrationRoute.INSUFFLATED,
                            dose = 20.0,
                            isDoseAnEstimate = false,
                            units = "mg",
                            experienceId = 0,
                            notes = null,
                            sentiment = null
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
                OneExperienceViewModel.CumulativeDose(
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