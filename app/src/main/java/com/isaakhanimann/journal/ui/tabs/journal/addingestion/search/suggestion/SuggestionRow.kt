/*
 * Copyright (c) 2023. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.ColorCircle
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.models.Suggestion
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.toReadableString
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Preview(showBackground = true)
@Composable
fun SuggestionRowPreview(@PreviewParameter(SubstanceSuggestionProvider::class) suggestion: Suggestion) {
    SuggestionRow(
        suggestion = suggestion,
        navigateToDose = { _: String, _: AdministrationRoute -> },
        navigateToCustomUnitChooseDose = {},
        navigateToCustomDose = { _: String, _: AdministrationRoute -> },
        navigateToChooseTime = { _: String, _: AdministrationRoute, _: Double?, _: String?, _: Boolean, _: Double?, _: Int? -> },
    )
}

@Composable
fun SuggestionRow(
    suggestion: Suggestion,
    navigateToDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToCustomUnitChooseDose: (customUnitId: Int) -> Unit,
    navigateToCustomDose: (customSubstanceName: String, route: AdministrationRoute) -> Unit,
    navigateToChooseTime: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean, estimatedDoseStandardDeviation: Double?, customUnitId: Int?) -> Unit,
) {
    when(suggestion) {
        is Suggestion.CustomSubstanceSuggestion -> CustomSubstanceSuggestionRow(
            customSubstanceSuggestion = suggestion,
            navigateToCustomDose = navigateToCustomDose,
            navigateToChooseTime = navigateToChooseTime
        )
        is Suggestion.CustomUnitSuggestion -> CustomUnitSuggestionRow(
            customUnitSuggestion = suggestion,
            navigateToCustomUnitChooseDose = navigateToCustomUnitChooseDose,
            navigateToChooseTime = navigateToChooseTime
        )
        is Suggestion.PureSubstanceSuggestion -> PureSubstanceSuggestionRow(
            pureSubstanceSuggestion = suggestion,
            navigateToDose = navigateToDose,
            navigateToChooseTime = navigateToChooseTime
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PureSubstanceSuggestionRow(
    pureSubstanceSuggestion: Suggestion.PureSubstanceSuggestion,
    navigateToDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToChooseTime: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean, estimatedDoseStandardDeviation: Double?, customUnitId: Int?) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 5.dp)
            .padding(horizontal = horizontalPadding)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ColorCircle(adaptiveColor = pureSubstanceSuggestion.adaptiveColor)
            Text(
                text = pureSubstanceSuggestion.substanceName + " " + pureSubstanceSuggestion.administrationRoute.displayText.lowercase(),
                style = MaterialTheme.typography.titleMedium
            )
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            pureSubstanceSuggestion.dosesAndUnit.forEach { doseAndUnit ->
                SuggestionChip(
                    onClick = {
                        navigateToChooseTime(
                            pureSubstanceSuggestion.substanceName,
                            pureSubstanceSuggestion.administrationRoute,
                            doseAndUnit.dose,
                            doseAndUnit.unit,
                            doseAndUnit.isEstimate,
                            doseAndUnit.estimatedDoseStandardDeviation,
                            null
                        )
                    },
                    label = {
                        if (doseAndUnit.dose != null) {
                            val description =
                                "${doseAndUnit.dose.toReadableString()} ${doseAndUnit.unit}"
                            if (doseAndUnit.isEstimate) {
                                if (doseAndUnit.estimatedDoseStandardDeviation != null) {
                                    Text(
                                        text = "${doseAndUnit.dose.toReadableString()}±${
                                            doseAndUnit.estimatedDoseStandardDeviation.toReadableString()
                                        } ${doseAndUnit.unit}"
                                    )
                                } else {
                                    Text(text = "~$description")
                                }
                            } else {
                                Text(text = description)
                            }
                        } else {
                            Text(text = "Unknown")
                        }
                    },
                )
            }
            SuggestionChip(onClick = {
                navigateToDose(
                    pureSubstanceSuggestion.substanceName,
                    pureSubstanceSuggestion.administrationRoute
                )
            }, label = {
                Text("Other dose")
            }, icon = {
                Icon(
                    imageVector = Icons.Default.Keyboard,
                    contentDescription = "Keyboard"
                )
            })
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomUnitSuggestionRow(
    customUnitSuggestion: Suggestion.CustomUnitSuggestion,
    navigateToCustomUnitChooseDose: (customUnitId: Int) -> Unit,
    navigateToChooseTime: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean, estimatedDoseStandardDeviation: Double?, customUnitId: Int?) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 5.dp)
            .padding(horizontal = horizontalPadding)
    ) {
        val customUnit = customUnitSuggestion.customUnit
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ColorCircle(adaptiveColor = customUnitSuggestion.adaptiveColor)
            Text(
                text = customUnit.substanceName + " " + customUnit.administrationRoute.displayText.lowercase() + ", " + customUnit.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            customUnitSuggestion.dosesAndUnit.forEach { customUnitDose ->
                SuggestionChip(onClick = {
                    navigateToChooseTime(
                        customUnit.substanceName,
                        customUnit.administrationRoute,
                        customUnitDose.dose,
                        customUnit.unit,
                        customUnitDose.isEstimate,
                        customUnitDose.estimatedDoseStandardDeviation,
                        customUnit.id
                    )
                }, label = {
                    Text(text = customUnitDose.getDoseDescription(customUnit.getPluralizableUnit()))
                })
            }
            SuggestionChip(onClick = {
                navigateToCustomUnitChooseDose(customUnit.id)
            }, label = {
                Text(text = "Other dose")
            }, icon = {
                Icon(
                    imageVector = Icons.Default.Keyboard,
                    contentDescription = "Keyboard"
                )
            })
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomSubstanceSuggestionRow(
    customSubstanceSuggestion: Suggestion.CustomSubstanceSuggestion,
    navigateToCustomDose: (customSubstanceName: String, route: AdministrationRoute) -> Unit,
    navigateToChooseTime: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean, estimatedDoseStandardDeviation: Double?, customUnitId: Int?) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 5.dp)
            .padding(horizontal = horizontalPadding)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ColorCircle(adaptiveColor = customSubstanceSuggestion.adaptiveColor)
            Text(
                text = customSubstanceSuggestion.customSubstance.name + " " + customSubstanceSuggestion.administrationRoute.displayText.lowercase(),
                style = MaterialTheme.typography.titleMedium
            )
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            customSubstanceSuggestion.dosesAndUnit.forEach { doseAndUnit ->
                SuggestionChip(
                    onClick = {
                        navigateToChooseTime(
                            customSubstanceSuggestion.customSubstance.name,
                            customSubstanceSuggestion.administrationRoute,
                            doseAndUnit.dose,
                            doseAndUnit.unit,
                            doseAndUnit.isEstimate,
                            doseAndUnit.estimatedDoseStandardDeviation,
                            null
                        )
                    },
                    label = {
                        if (doseAndUnit.dose != null) {
                            val description =
                                "${doseAndUnit.dose.toReadableString()} ${doseAndUnit.unit}"
                            if (doseAndUnit.isEstimate) {
                                if (doseAndUnit.estimatedDoseStandardDeviation != null) {
                                    Text(
                                        text = "${doseAndUnit.dose.toReadableString()}±${
                                            doseAndUnit.estimatedDoseStandardDeviation.toReadableString()
                                        } ${doseAndUnit.unit}"
                                    )
                                } else {
                                    Text(text = "~$description")
                                }
                            } else {
                                Text(text = description)
                            }
                        } else {
                            Text(text = "Unknown")
                        }
                    },
                )
            }
            SuggestionChip(onClick = {
                navigateToCustomDose(
                    customSubstanceSuggestion.customSubstance.name,
                    customSubstanceSuggestion.administrationRoute
                )
            }, label = {
                Text("Other dose")
            }, icon = {
                Icon(
                    imageVector = Icons.Default.Keyboard,
                    contentDescription = "Keyboard"
                )
            })
        }
    }
}
