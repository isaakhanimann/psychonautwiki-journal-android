package com.isaakhanimann.journal.ui.addingestion.search

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.search.SearchScreen
import com.isaakhanimann.journal.ui.search.substance.roa.toReadableString
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun AddIngestionSearchScreen(
    navigateToCheckInteractionsSkipNothing: (substanceName: String) -> Unit,
    navigateToCheckInteractionsSkipRoute: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToCustomDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToCheckInteractionsSkipDose: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean) -> Unit,
    navigateToChooseTime: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean) -> Unit,
    navigateToCustomSubstanceChooseRoute: (substanceName: String) -> Unit,
    viewModel: AddIngestionSearchViewModel = hiltViewModel()
) {
    AddIngestionSearchScreen(
        navigateToCheckInteractionsSkipNothing = navigateToCheckInteractionsSkipNothing,
        navigateToCheckInteractionsSkipRoute = navigateToCheckInteractionsSkipRoute,
        navigateToCustomDose = navigateToCustomDose,
        navigateToCheckInteractionsSkipDose = navigateToCheckInteractionsSkipDose,
        navigateToCustomSubstanceChooseRoute = navigateToCustomSubstanceChooseRoute,
        navigateToChooseTime = navigateToChooseTime,
        previousSubstances = viewModel.previousSubstanceRows.collectAsState().value,
    )
}

@Composable
fun AddIngestionSearchScreen(
    navigateToCheckInteractionsSkipNothing: (substanceName: String) -> Unit,
    navigateToCheckInteractionsSkipRoute: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToCustomDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToCheckInteractionsSkipDose: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean) -> Unit,
    navigateToChooseTime: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean) -> Unit,
    navigateToCustomSubstanceChooseRoute: (substanceName: String) -> Unit,
    previousSubstances: List<PreviousSubstance>
) {
    Column {
        LinearProgressIndicator(progress = 0.17f, modifier = Modifier.fillMaxWidth())
        SearchScreen(
            onSubstanceTap = {
                navigateToCheckInteractionsSkipNothing(it)
            },
            modifier = Modifier.weight(1f),
            navigateToAddCustomSubstanceScreen = {},
            onCustomSubstanceTap = navigateToCustomSubstanceChooseRoute,
        )
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        if (previousSubstances.isNotEmpty()) {
            SuggestionsSection(
                previousSubstances = previousSubstances,
                modifier = Modifier.heightIn(0.dp, screenHeight / 2),
                navigateToCheckInteractionsSkipRoute = navigateToCheckInteractionsSkipRoute,
                navigateToCheckInteractionsSkipDose = navigateToCheckInteractionsSkipDose,
                navigateToChooseTime = navigateToChooseTime,
                navigateToCustomDose = navigateToCustomDose
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SuggestionSectionPreview(@PreviewParameter(AddIngestionSearchScreenProvider::class) previousSubstances: List<PreviousSubstance>) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Surface(
                color = Color.Blue, modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(text = "Search Screen")
            }
            val screenHeight = LocalConfiguration.current.screenHeightDp.dp
            SuggestionsSection(
                previousSubstances = previousSubstances,
                navigateToCheckInteractionsSkipDose = { _: String, _: AdministrationRoute, _: Double?, _: String?, _: Boolean -> },
                navigateToCheckInteractionsSkipRoute = { _: String, _: AdministrationRoute -> },
                navigateToChooseTime = { _: String, _: AdministrationRoute, _: Double?, _: String?, _: Boolean -> },
                navigateToCustomDose = { _: String, _: AdministrationRoute -> },
                modifier = Modifier.heightIn(0.dp, screenHeight / 2)
            )
        }
    }

}

@Composable
fun SuggestionsSection(
    previousSubstances: List<PreviousSubstance>,
    navigateToCheckInteractionsSkipRoute: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToCheckInteractionsSkipDose: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean) -> Unit,
    navigateToChooseTime: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean) -> Unit,
    navigateToCustomDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    modifier: Modifier
) {
    Card(modifier = modifier, shape = RectangleShape) {
        Text(
            text = "Quick Logging",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 5.dp)
        )
        Divider()
        LazyColumn {
            items(previousSubstances) { substanceRow ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding, vertical = 5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ColorCircle(adaptiveColor = substanceRow.color)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = substanceRow.substanceName,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    substanceRow.routesWithDoses.forEach { routeWithDoses ->
                        Row {
                            OutlinedButton(onClick = {
                                if (substanceRow.isCustom) {
                                    navigateToCustomDose(
                                        substanceRow.substanceName,
                                        routeWithDoses.route
                                    )
                                } else {
                                    navigateToCheckInteractionsSkipRoute(
                                        substanceRow.substanceName,
                                        routeWithDoses.route
                                    )
                                }
                            }) {
                                Text(text = routeWithDoses.route.displayText)
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            FlowRow(mainAxisSpacing = 5.dp) {
                                routeWithDoses.doses.forEach { previousDose ->
                                    OutlinedButton(onClick = {
                                        if (substanceRow.isCustom) {
                                            navigateToChooseTime(
                                                substanceRow.substanceName,
                                                routeWithDoses.route,
                                                previousDose.dose,
                                                previousDose.unit,
                                                previousDose.isEstimate
                                            )
                                        } else {
                                            navigateToCheckInteractionsSkipDose(
                                                substanceRow.substanceName,
                                                routeWithDoses.route,
                                                previousDose.dose,
                                                previousDose.unit,
                                                previousDose.isEstimate
                                            )
                                        }
                                    }) {
                                        if (previousDose.dose != null) {
                                            val estimate =
                                                if (previousDose.isEstimate) "~" else ""
                                            Text(text = "$estimate${previousDose.dose.toReadableString()} ${previousDose.unit ?: ""}")
                                        } else {
                                            Text(text = "Unknown Dose")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Divider()
            }
        }
    }
}

@Composable
fun ColorCircle(adaptiveColor: AdaptiveColor) {
    val isDarkTheme = isSystemInDarkTheme()
    Surface(
        shape = CircleShape,
        color = adaptiveColor.getComposeColor(isDarkTheme),
        modifier = Modifier
            .size(25.dp)
    ) {}
}