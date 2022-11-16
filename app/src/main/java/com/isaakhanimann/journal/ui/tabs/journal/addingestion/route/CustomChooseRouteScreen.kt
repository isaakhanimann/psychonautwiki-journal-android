/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.route

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.data.substances.AdministrationRoute

@Preview
@Composable
fun CustomChooseRouteScreenPreview() {
    CustomChooseRouteScreen(onRouteTap = {}, substanceName = "Cookies")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomChooseRouteScreen(onRouteTap: (AdministrationRoute) -> Unit, substanceName: String) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("$substanceName Route") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LinearProgressIndicator(progress = 0.5f, modifier = Modifier.fillMaxWidth())
            val spacing = 6
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(spacing.dp)
            ) {
                val routesChunked = AdministrationRoute.values().toList().chunked(2)
                routesChunked.forEach { chunk ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(spacing.dp)
                    ) {
                        chunk.forEach { route ->
                            Card(
                                modifier = Modifier
                                    .clickable {
                                        onRouteTap(route)
                                    }
                                    .fillMaxHeight()
                                    .weight(1f)
                            ) {
                                RouteBox(
                                    route = route,
                                    titleStyle = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                        if (chunk.size == 1) {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}