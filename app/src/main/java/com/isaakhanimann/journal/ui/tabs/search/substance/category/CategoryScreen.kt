/*
 * Copyright (c) 2022. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.search.substance.category

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.substances.classes.Category
import com.isaakhanimann.journal.ui.tabs.stats.EmptyScreenDisclaimer
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel = hiltViewModel()
) {
    CategoryScreen(category = viewModel.category)
}

@Preview
@Composable
fun CategoryPreview() {
    CategoryScreen(
        category = Category(
            name = "psychedelic",
            description = "Psychedelics are drugs which alter the perception, causing a number of mental effects which manifest in many forms including altered states of consciousness, visual or tactile effects.",
            url = "https://psychonautwiki.org/wiki/Psychedelics",
            color = Color.Red
        ),
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(category: Category?) {
    if (category == null) {
        EmptyScreenDisclaimer(
            title = "Category not found",
            description = "An error happened, please navigate back."
        )
    } else {
        val uriHandler = LocalUriHandler.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(category.name.replaceFirstChar { it.uppercase() }) },
                    actions = {
                        if (category.url != null) {
                            TextButton(
                                onClick = { uriHandler.openUri(category.url) },
                            ) {
                                Text("Article")
                            }
                        }
                    }
                )
            },
        ) { padding ->
            Text(
                text = category.description,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = horizontalPadding, vertical = 10.dp)
            )
        }
    }
}