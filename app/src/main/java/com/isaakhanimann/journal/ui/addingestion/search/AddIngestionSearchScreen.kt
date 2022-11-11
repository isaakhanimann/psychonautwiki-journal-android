/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

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
    navigateToChooseRoute: (substanceName: String) -> Unit,
    navigateToCheckInteractionsSkipRoute: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToCustomDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToCheckInteractionsSkipDose: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean) -> Unit,
    navigateToChooseTime: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean) -> Unit,
    navigateToCustomSubstanceChooseRoute: (substanceName: String) -> Unit,
    navigateToAddCustomSubstanceScreen: () -> Unit,
    viewModel: AddIngestionSearchViewModel = hiltViewModel()
) {
    AddIngestionSearchScreen(
        navigateToCheckInteractionsSkipNothing = navigateToCheckInteractionsSkipNothing,
        navigateToChooseRoute = navigateToChooseRoute,
        navigateToCheckInteractionsSkipRoute = navigateToCheckInteractionsSkipRoute,
        navigateToCustomDose = navigateToCustomDose,
        navigateToCheckInteractionsSkipDose = navigateToCheckInteractionsSkipDose,
        navigateToCustomSubstanceChooseRoute = navigateToCustomSubstanceChooseRoute,
        navigateToChooseTime = navigateToChooseTime,
        navigateToDose = navigateToDose,
        navigateToAddCustomSubstanceScreen = navigateToAddCustomSubstanceScreen,
        shouldSkipInteractions = viewModel.shouldSkipInteractionsFlow.collectAsState().value,
        previousSubstances = viewModel.previousSubstanceRows.collectAsState().value,
    )
}

@Composable
fun AddIngestionSearchScreen(
    navigateToCheckInteractionsSkipNothing: (substanceName: String) -> Unit,
    navigateToChooseRoute: (substanceName: String) -> Unit,
    navigateToCheckInteractionsSkipRoute: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToCustomDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToCheckInteractionsSkipDose: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean) -> Unit,
    navigateToChooseTime: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean) -> Unit,
    navigateToCustomSubstanceChooseRoute: (substanceName: String) -> Unit,
    navigateToAddCustomSubstanceScreen: () -> Unit,
    shouldSkipInteractions: Boolean,
    previousSubstances: List<PreviousSubstance>
) {
    Column {
        LinearProgressIndicator(progress = 0.17f, modifier = Modifier.fillMaxWidth())
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        if (previousSubstances.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            if (shouldSkipInteractions) {
                SuggestionsSection(
                    previousSubstances = previousSubstances,
                    modifier = Modifier.heightIn(0.dp, screenHeight / 2),
                    onRouteChosen = navigateToDose,
                    onDoseChosen = navigateToChooseTime,
                    onDoseOfCustomSubstanceChosen = navigateToChooseTime,
                    onRouteOfCustomSubstanceChosen = navigateToCustomDose
                )
            } else {
                SuggestionsSection(
                    previousSubstances = previousSubstances,
                    modifier = Modifier.heightIn(0.dp, screenHeight / 2),
                    onRouteChosen = navigateToCheckInteractionsSkipRoute,
                    onDoseChosen = navigateToCheckInteractionsSkipDose,
                    onDoseOfCustomSubstanceChosen = navigateToChooseTime,
                    onRouteOfCustomSubstanceChosen = navigateToCustomDose
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = horizontalPadding)
        ) {
            SearchScreen(
                onSubstanceTap = {
                    if (shouldSkipInteractions) {
                        navigateToChooseRoute(it)
                    } else {
                        navigateToCheckInteractionsSkipNothing(it)
                    }
                },
                navigateToAddCustomSubstanceScreen = navigateToAddCustomSubstanceScreen,
                onCustomSubstanceTap = navigateToCustomSubstanceChooseRoute,
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
            val screenHeight = LocalConfiguration.current.screenHeightDp.dp
            SuggestionsSection(
                previousSubstances = previousSubstances,
                onDoseChosen = { _: String, _: AdministrationRoute, _: Double?, _: String?, _: Boolean -> },
                onRouteChosen = { _: String, _: AdministrationRoute -> },
                onDoseOfCustomSubstanceChosen = { _: String, _: AdministrationRoute, _: Double?, _: String?, _: Boolean -> },
                onRouteOfCustomSubstanceChosen = { _: String, _: AdministrationRoute -> },
                modifier = Modifier.heightIn(0.dp, screenHeight / 2)
            )
            Surface(
                color = Color.Blue, modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(text = "Search Screen")
            }
        }
    }

}

@Composable
fun SuggestionsSection(
    previousSubstances: List<PreviousSubstance>,
    onRouteChosen: (substanceName: String, route: AdministrationRoute) -> Unit,
    onDoseChosen: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean) -> Unit,
    onDoseOfCustomSubstanceChosen: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean) -> Unit,
    onRouteOfCustomSubstanceChosen: (substanceName: String, route: AdministrationRoute) -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = modifier.padding(horizontal = horizontalPadding),
    ) {
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
                                    onRouteOfCustomSubstanceChosen(
                                        substanceRow.substanceName,
                                        routeWithDoses.route
                                    )
                                } else {
                                    onRouteChosen(
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
                                            onDoseOfCustomSubstanceChosen(
                                                substanceRow.substanceName,
                                                routeWithDoses.route,
                                                previousDose.dose,
                                                previousDose.unit,
                                                previousDose.isEstimate
                                            )
                                        } else {
                                            onDoseChosen(
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