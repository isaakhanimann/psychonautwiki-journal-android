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

package com.isaakhanimann.journal.ui.tabs.safer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.tabs.search.substance.CollapsibleSection
import com.isaakhanimann.journal.ui.tabs.search.substance.SectionText
import com.isaakhanimann.journal.ui.tabs.search.substance.VerticalSpace
import com.isaakhanimann.journal.ui.theme.horizontalPadding


@Preview
@Composable
fun RouteExplanationPreview() {
    RouteExplanationScreen(navigateToURL = {})
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteExplanationScreen(navigateToURL: (url: String) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Routes of Administration") })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navigateToURL(AdministrationRoute.psychonautWikiArticleURL) },
                icon = {
                    Icon(
                        Icons.Outlined.Article,
                        contentDescription = "Open Link"
                    )
                },
                text = { Text("Article") },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            ElevatedCard(modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 3.dp)) {
                Column(modifier = Modifier.padding(horizontal = horizontalPadding)) {
                    SectionText(
                        text = """A route of administration is the method in which a psychoactive substance is delivered into the body.
The route through which a substance is administered can greatly impact its potency, duration, and subjective effects. For example, many substances are more effective when consumed using particular routes of administration, while some substances are completely inactive with certain routes.
Determining an optimal route of administration is highly dependent on the substance consumed, its desired duration and potency and side effects, and one's personal comfort level."""
                    )
                    VerticalSpace()
                }
            }
            AdministrationRoute.values().filter { !it.isInjectionMethod }.forEach {
                CollapsibleSection(title = it.displayText) {
                    Text(
                        text = it.articleText,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .padding(horizontal = horizontalPadding)
                            .padding(bottom = 10.dp)
                    )
                    if (it == AdministrationRoute.RECTAL) {
                        Button(
                            onClick = { navigateToURL(AdministrationRoute.saferPluggingArticleURL) },
                            modifier = Modifier.padding(horizontal = 5.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Article,
                                contentDescription = "Open Link",
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text("Safer Plugging")
                        }
                    }
                }
            }
            AdministrationRoute.values().filter { it.isInjectionMethod }.forEach {
                CollapsibleSection(title = it.displayText) {
                    Text(
                        text = it.articleText,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .padding(horizontal = horizontalPadding)
                            .padding(bottom = 10.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}