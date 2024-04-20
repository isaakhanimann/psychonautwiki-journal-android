/*
 * Copyright (c) 2024. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.settings.customunits.add

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.route.AdministrationRoutePicker
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.route.ChooseRouteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseRouteDuringAddCustomUnitScreen(
    onRouteChosen: (administrationRoute: AdministrationRoute) -> Unit,
    viewModel: ChooseRouteViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${viewModel.substanceName} route") },
                navigationIcon = {
                    if (viewModel.showOtherRoutes && viewModel.pwRoutes.isNotEmpty()) {
                        IconButton(onClick = { viewModel.showOtherRoutes = false }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            AdministrationRoutePicker(
                showOtherRoutes = viewModel.showOtherRoutes,
                onChangeOfShowOtherRoutes = {
                    viewModel.showOtherRoutes = it
                },
                pwRoutes = viewModel.pwRoutes,
                otherRoutesChunked = viewModel.otherRoutesChunked,
                onRouteTapped = {
                    onRouteChosen(it)
                }
            )
        }
    }
}