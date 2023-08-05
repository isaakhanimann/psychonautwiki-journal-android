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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.ui.tabs.search.substance.SectionText
import com.isaakhanimann.journal.ui.tabs.search.substance.VerticalSpace
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Preview
@Composable
fun ReagentTestingPreview() {
    ReagentTestingScreen(navigateToReagentTestingArticle = {})
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReagentTestingScreen(
    navigateToReagentTestingArticle: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Reagent Testing") })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = navigateToReagentTestingArticle,
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
                .padding(horizontal = horizontalPadding)
        ) {
            ElevatedCard(modifier = Modifier.padding(vertical = 3.dp)) {
                Column(modifier = Modifier.padding(horizontal = horizontalPadding)) {
                    SectionText(
                        text = """Reagent testing kits is a drug testing method that uses chemical solutions that change in color when applied to a chemical compound. They can help determine what chemical might be present in a given sample. In many cases they do not rule out the possibility of another similar compound being present in addition to or instead of the one suspected.
Although very few substances are effective at dosages that allow the use of paper blotters, LSD is not the only one: It's essential to test for its presence to avoid substances of the NBOMe class. Additionally, it's becoming increasingly important to test for possible Fentanyl contamination, since this substance is effective at dosages that make it possible to put very high quantities on a single blotter.
Reagents can only determine the presence, not the quantity or purity, of a particular substance. Dark color reactions will tend to override reactions to other substances also in the pill. A positive or negative reaction for a substance does not indicate that a drug is safe. No drug use is 100% safe. Make wise decisions and take responsibility for your health and well-being; no one else can."""
                    )
                    VerticalSpace()
                }
            }
            ElevatedCard(modifier = Modifier.padding(vertical = 5.dp)) {
                Column(
                    Modifier.padding(vertical = 5.dp)
                ) {
                    Text(
                        text = "Kit Sellers",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Divider()
                    val uriHandler = LocalUriHandler.current
                    TextButton(onClick = { uriHandler.openUri("https://dancesafe.org/testing-kit-instructions/") }) {
                        Text(text = "DanceSafe")
                    }
                    Divider()
                    TextButton(onClick = { uriHandler.openUri("https://bunkpolice.com") }) {
                        Text(text = "Bunk Police")
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}