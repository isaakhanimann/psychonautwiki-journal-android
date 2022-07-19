package com.example.healthassistant.ui.addingestion.route


import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.room.experiences.entities.Sentiment
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.ui.utils.getDate

class RouteIngestionsPreviewProvider :
    PreviewParameterProvider<List<Ingestion>> {
    override val values: Sequence<List<Ingestion>> = sequenceOf(
        listOf(
            Ingestion(
                substanceName = "MDMA",
                time = getDate(
                    year = 2022,
                    month = 7,
                    day = 5,
                    hourOfDay = 14,
                    minute = 20
                )!!,
                administrationRoute = AdministrationRoute.ORAL,
                dose = 90.0,
                isDoseAnEstimate = false,
                units = "mg",
                experienceId = 0,
                notes = "This is one note",
                sentiment = Sentiment.SATISFIED
            ),
            Ingestion(
                substanceName = "MDMA",
                time = getDate(
                    year = 2022,
                    month = 7,
                    day = 5,
                    hourOfDay = 14,
                    minute = 20
                )!!,
                administrationRoute = AdministrationRoute.INSUFFLATED,
                dose = 45.0,
                isDoseAnEstimate = true,
                units = "mg",
                experienceId = 0,
                notes = "This is one note",
                sentiment = Sentiment.SATISFIED
            ),
            Ingestion(
                substanceName = "MDMA",
                time = getDate(
                    year = 2022,
                    month = 7,
                    day = 5,
                    hourOfDay = 14,
                    minute = 20
                )!!,
                administrationRoute = AdministrationRoute.ORAL,
                dose = null,
                isDoseAnEstimate = false,
                units = "mg",
                experienceId = 0,
                notes = "This is one note",
                sentiment = Sentiment.SATISFIED
            )
        )
    )
}