package com.isaakhanimann.healthassistant.ui.addingestion.route


import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute

class RouteIngestionsPreviewProvider :
    PreviewParameterProvider<List<ChooseRouteViewModel.IngestionSuggestion>> {
    override val values: Sequence<List<ChooseRouteViewModel.IngestionSuggestion>> = sequenceOf(
        listOf(
            ChooseRouteViewModel.IngestionSuggestion(
                administrationRoute = AdministrationRoute.ORAL,
                dose = 90.0,
                isDoseAnEstimate = false,
                units = "mg"
            ),
            ChooseRouteViewModel.IngestionSuggestion(
                administrationRoute = AdministrationRoute.INSUFFLATED,
                dose = 45.0,
                isDoseAnEstimate = true,
                units = "mg",
            ),
            ChooseRouteViewModel.IngestionSuggestion(
                administrationRoute = AdministrationRoute.ORAL,
                dose = null,
                isDoseAnEstimate = false,
                units = "mg",
            )
        )
    )
}