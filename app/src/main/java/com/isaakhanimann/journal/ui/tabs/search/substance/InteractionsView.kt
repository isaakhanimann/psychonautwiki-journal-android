/*
 * Copyright (c) 2022. Free Software Foundation, Inc.
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

package com.isaakhanimann.journal.ui.tabs.search.substance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.data.substances.classes.InteractionType
import com.isaakhanimann.journal.data.substances.classes.Interactions
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Preview
@Composable
fun InteractionsPreview(@PreviewParameter(InteractionsPreviewProvider::class) interactions: Interactions) {
    InteractionsView(interactions, navigateToURL = {}, substanceURL = "")
}

@Composable
fun InteractionsView(
    interactions: Interactions,
    substanceURL: String,
    navigateToURL: (url: String) -> Unit
) {
    Column {
        if (interactions.dangerous.isNotEmpty()) {
            interactions.dangerous.forEach {
                InteractionRowSubstanceScreen(
                    text = it,
                    interactionType = InteractionType.DANGEROUS
                )
            }
        }
        if (interactions.unsafe.isNotEmpty()) {
            interactions.unsafe.forEach {
                InteractionRowSubstanceScreen(text = it, interactionType = InteractionType.UNSAFE)
            }
        }
        if (interactions.uncertain.isNotEmpty()) {
            interactions.uncertain.forEach {
                InteractionRowSubstanceScreen(
                    text = it,
                    interactionType = InteractionType.UNCERTAIN
                )
            }
        }
        InteractionExplanationButton(substanceURL = substanceURL, navigateToURL = navigateToURL)
    }
}

@Composable
fun InteractionExplanationButton(substanceURL: String, navigateToURL: (url: String) -> Unit) {
    TextButton(onClick = {
        val interactionURL = "$substanceURL#Dangerous_interactions"
        navigateToURL(interactionURL)
    }) {
        Icon(
            Icons.Outlined.Info,
            contentDescription = "Open Link"
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("Interaction Explanations")
    }
}

@Composable
fun InteractionRowSubstanceScreen(
    text: String,
    interactionType: InteractionType,
    verticalPaddingInside: Dp = 2.dp
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 1.dp),
        shape = RectangleShape,
        color = interactionType.color
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = horizontalPadding,
                vertical = verticalPaddingInside
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            LazyRow {
                items(interactionType.dangerCount) {
                    Icon(
                        imageVector = Icons.Outlined.WarningAmber,
                        contentDescription = "Warning",
                        tint = Color.Black,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
        }
    }
}