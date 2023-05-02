/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterVertically
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
import com.isaakhanimann.journal.ui.tabs.search.SearchScreenWithoutDrawerButton
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.toReadableString
import com.isaakhanimann.journal.ui.theme.horizontalPadding
import com.isaakhanimann.journal.ui.utils.keyboard.wasKeyboardOpened

@Composable
fun AddIngestionSearchScreen(
    navigateToCheckInteractions: (substanceName: String) -> Unit,
    navigateToCheckSaferUse: (substanceName: String) -> Unit,
    navigateToChooseRoute: (substanceName: String) -> Unit,
    navigateToDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToCustomDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToChooseTime: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean) -> Unit,
    navigateToCustomSubstanceChooseRoute: (substanceName: String) -> Unit,
    navigateToAddCustomSubstanceScreen: () -> Unit,
    viewModel: AddIngestionSearchViewModel = hiltViewModel()
) {
    AddIngestionSearchScreen(
        navigateToCheckInteractions = navigateToCheckInteractions,
        navigateToCheckSaferUse = navigateToCheckSaferUse,
        navigateToChooseRoute = navigateToChooseRoute,
        navigateToCustomDose = navigateToCustomDose,
        navigateToCustomSubstanceChooseRoute = navigateToCustomSubstanceChooseRoute,
        navigateToChooseTime = navigateToChooseTime,
        navigateToDose = navigateToDose,
        navigateToAddCustomSubstanceScreen = navigateToAddCustomSubstanceScreen,
        previousSubstances = viewModel.previousSubstanceRows.collectAsState().value,
    )
}

@Composable
fun AddIngestionSearchScreen(
    navigateToCheckInteractions: (substanceName: String) -> Unit,
    navigateToChooseRoute: (substanceName: String) -> Unit,
    navigateToCheckSaferUse: (substanceName: String) -> Unit,
    navigateToDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToCustomDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToChooseTime: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean) -> Unit,
    navigateToCustomSubstanceChooseRoute: (substanceName: String) -> Unit,
    navigateToAddCustomSubstanceScreen: () -> Unit,
    previousSubstances: List<PreviousSubstance>
) {
    Column {
        LinearProgressIndicator(progress = 0.17f, modifier = Modifier.fillMaxWidth())
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        val suggestionMaxHeight = screenHeight * 0.6f
        AnimatedVisibility(
            visible = previousSubstances.isNotEmpty() && !wasKeyboardOpened().value,
            enter = EnterTransition.None
        ) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                SuggestionsSection(
                    previousSubstances = previousSubstances,
                    modifier = Modifier.heightIn(0.dp, suggestionMaxHeight),
                    onRouteChosen = navigateToDose,
                    onDoseChosen = navigateToChooseTime,
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
            SearchScreenWithoutDrawerButton(
                onSubstanceTap = {
                    navigateToCheckInteractions(it)
                },
                navigateToAddCustomSubstanceScreen = navigateToAddCustomSubstanceScreen,
                onCustomSubstanceTap = navigateToCustomSubstanceChooseRoute,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
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
            val suggestionMaxHeight = screenHeight * 0.65f
            SuggestionsSection(
                previousSubstances = previousSubstances,
                onDoseChosen = { _: String, _: AdministrationRoute, _: Double?, _: String?, _: Boolean -> },
                onRouteChosen = { _: String, _: AdministrationRoute -> },
                onDoseOfCustomSubstanceChosen = { _: String, _: AdministrationRoute, _: Double?, _: String?, _: Boolean -> },
                onRouteOfCustomSubstanceChosen = { _: String, _: AdministrationRoute -> },
                modifier = Modifier.heightIn(0.dp, suggestionMaxHeight)
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

@OptIn(ExperimentalMaterial3Api::class)
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
            itemsIndexed(previousSubstances) { index, substanceRow ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding, vertical = 5.dp)
                ) {
                    Row(
                        verticalAlignment = CenterVertically,
                    ) {
                        ColorCircle(adaptiveColor = substanceRow.color)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = substanceRow.substanceName,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    substanceRow.routesWithDoses.forEach { routeWithDoses ->
                        Column {
                            Text(
                                text = routeWithDoses.route.displayText,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(top = 3.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            FlowRow(mainAxisSpacing = 5.dp) {
                                routeWithDoses.doses.forEach { previousDose ->
                                    SuggestionChip(
                                        onClick = {
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
                                        },
                                        label = {
                                            if (previousDose.dose != null) {
                                                val estimate =
                                                    if (previousDose.isEstimate) "~" else ""
                                                Text(text = "$estimate${previousDose.dose.toReadableString()} ${previousDose.unit ?: ""}")
                                            } else {
                                                Text(text = "Unknown")
                                            }
                                        },
                                    )
                                }
                                SuggestionChip(
                                    onClick = {
                                        if (substanceRow.isCustom) {
                                            onRouteOfCustomSubstanceChosen(
                                                substanceRow.substanceName, routeWithDoses.route
                                            )
                                        } else {
                                            onRouteChosen(
                                                substanceRow.substanceName, routeWithDoses.route
                                            )
                                        }
                                    },
                                    label = { Text("Other") }
                                )
                            }
                        }
                    }
                }
                if (index < previousSubstances.size - 1) {
                    Divider(Modifier.padding(horizontal = horizontalPadding))
                }
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
        modifier = Modifier.size(25.dp)
    ) {}
}