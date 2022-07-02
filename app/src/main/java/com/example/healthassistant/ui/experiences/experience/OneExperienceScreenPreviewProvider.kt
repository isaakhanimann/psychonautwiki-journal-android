package com.example.healthassistant.ui.experiences.experience

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.room.experiences.entities.*
import com.example.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.DoseClass
import java.util.*

class OneExperienceScreenPreviewProvider :
    PreviewParameterProvider<OneExperienceScreenPreviewProvider.AllThatIsNeeded> {

    data class AllThatIsNeeded(
        val experience: Experience,
        val firstDateText: String,
        val ingestionElements: List<OneExperienceViewModel.IngestionWithAssociatedData>,
        val cumulativeDoses: List<OneExperienceViewModel.CumulativeDose>
    )

    private val firstDate = Date(Date().time - 2 * 60 * 60 * 1000)

    override val values: Sequence<AllThatIsNeeded> = sequenceOf(
        AllThatIsNeeded(
            experience = Experience(
                id = 0,
                title = "Day at Lake Geneva",
                text = "Some notes",
                sentiment = Sentiment.SATISFIED
            ),
            firstDateText = OneExperienceViewModel.getDateText(firstDate),
            ingestionElements = listOf(
                OneExperienceViewModel.IngestionWithAssociatedData(
                    ingestionWithCompanion = IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "MDMA",
                            time = firstDate,
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
                    roaDose = null,
                    doseClass = DoseClass.COMMON
                ),
                OneExperienceViewModel.IngestionWithAssociatedData(
                    ingestionWithCompanion = IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "Cocaine",
                            time = Date(Date().time - 60 * 60 * 1000),
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
                    roaDose = null,
                    doseClass = DoseClass.COMMON
                ),
                OneExperienceViewModel.IngestionWithAssociatedData(
                    ingestionWithCompanion = IngestionWithCompanion(
                        ingestion = Ingestion(
                            substanceName = "Cocaine",
                            time = Date(Date().time - 30 * 60 * 1000),
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
                    roaDose = null,
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