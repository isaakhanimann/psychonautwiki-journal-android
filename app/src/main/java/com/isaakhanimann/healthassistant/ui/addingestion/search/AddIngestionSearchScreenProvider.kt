package com.isaakhanimann.healthassistant.ui.addingestion.search

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute

class AddIngestionSearchScreenProvider : PreviewParameterProvider<AddIngestionSearchModel> {
    override val values: Sequence<AddIngestionSearchModel> = sequenceOf(
        AddIngestionSearchModel(
            routeSuggestions = listOf(
                RouteSuggestionElement(
                    color = SubstanceColor.BLUE,
                    substanceName = "MDMA",
                    administrationRoute = AdministrationRoute.ORAL
                ),
                RouteSuggestionElement(
                    color = SubstanceColor.PURPLE,
                    substanceName = "Cocaine",
                    administrationRoute = AdministrationRoute.INSUFFLATED
                )
            ),
            doseSuggestions = listOf(
                DoseSuggestionElement(
                    color = SubstanceColor.BLUE,
                    substanceName = "MDMA",
                    administrationRoute = AdministrationRoute.ORAL,
                    dose = 50.0,
                    units = "mg",
                    isDoseAnEstimate = false
                ),
                DoseSuggestionElement(
                    color = SubstanceColor.BLUE,
                    substanceName = "MDMA",
                    administrationRoute = AdministrationRoute.ORAL,
                    dose = 80.0,
                    units = "mg",
                    isDoseAnEstimate = false
                ),
                DoseSuggestionElement(
                    color = SubstanceColor.PURPLE,
                    substanceName = "Cocaine",
                    administrationRoute = AdministrationRoute.INSUFFLATED,
                    dose = 20.0,
                    units = "mg",
                    isDoseAnEstimate = false
                )
            )
        )
    )
}