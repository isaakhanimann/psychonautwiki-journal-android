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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.search.suggestion.models.SubstanceRouteSuggestion
import com.isaakhanimann.journal.ui.tabs.journal.components.RelativeDateTextNew
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.toReadableString
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Preview(showBackground = true)
@Composable
fun SuggestionRowPreview(@PreviewParameter(SubstanceSuggestionProvider::class) substanceRouteSuggestion: SubstanceRouteSuggestion) {
    SuggestionRow(
        substanceRouteSuggestion = substanceRouteSuggestion,
        navigateToDose = { _: String, _: AdministrationRoute -> },
        navigateToCustomDose = { _: String, _: AdministrationRoute -> },
        navigateToChooseTime = { _: String, _: AdministrationRoute, _: Double?, _: String?, _: Boolean, _: Double?, _: Int? -> }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SuggestionRow(
    substanceRouteSuggestion: SubstanceRouteSuggestion,
    navigateToDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToCustomDose: (substanceName: String, route: AdministrationRoute) -> Unit,
    navigateToChooseTime: (substanceName: String, route: AdministrationRoute, dose: Double?, units: String?, isEstimate: Boolean, estimatedDoseVariance: Double?, customUnitId: Int?) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 5.dp)
            .padding(horizontal = horizontalPadding)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ColorCircle(adaptiveColor = substanceRouteSuggestion.color)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = substanceRouteSuggestion.substanceName + " " + substanceRouteSuggestion.route.displayText,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            RelativeDateTextNew(
                dateTime = substanceRouteSuggestion.lastUsed,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            substanceRouteSuggestion.dosesAndUnit.forEach { doseAndUnit ->
                SuggestionChip(
                    onClick = {
                        if (substanceRouteSuggestion.isCustomSubstance) {
                            navigateToChooseTime(
                                substanceRouteSuggestion.substanceName,
                                substanceRouteSuggestion.route,
                                doseAndUnit.dose,
                                doseAndUnit.unit,
                                doseAndUnit.isEstimate,
                                doseAndUnit.estimatedDoseVariance,
                                null
                            )
                        } else {
                            navigateToChooseTime(
                                substanceRouteSuggestion.substanceName,
                                substanceRouteSuggestion.route,
                                doseAndUnit.dose,
                                doseAndUnit.unit,
                                doseAndUnit.isEstimate,
                                doseAndUnit.estimatedDoseVariance,
                                null
                            )
                        }
                    },
                    label = {
                        if (doseAndUnit.dose != null) {
                            val estimate =
                                if (doseAndUnit.isEstimate) "~" else ""
                            Text(text = "$estimate${doseAndUnit.dose.toReadableString()} ${doseAndUnit.unit ?: ""}")
                        } else {
                            Text(text = "Unknown")
                        }
                    },
                )
            }
            SuggestionChip(onClick = {
                if (substanceRouteSuggestion.isCustomSubstance) {
                    navigateToCustomDose(
                        substanceRouteSuggestion.substanceName, substanceRouteSuggestion.route
                    )
                } else {
                    navigateToDose(
                        substanceRouteSuggestion.substanceName, substanceRouteSuggestion.route
                    )
                }
            }, label = { Text("Other") })
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            substanceRouteSuggestion.customUnitDoses.forEach { customUnitDose ->
                SuggestionChip(onClick = { /*TODO*/ }, label = {
                    Text(text = customUnitDose.dose.toReadableString() + " " + customUnitDose.customUnit.unit)
                })
            }
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            substanceRouteSuggestion.customUnits.forEach { customUnit ->
                SuggestionChip(onClick = { /*TODO*/ }, label = {
                    Text(text = "Enter " + customUnit.unit)
                })
            }
        }
    }
}