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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.saferuse

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.NextFAB
import com.isaakhanimann.journal.ui.tabs.search.substance.BulletPoints
import com.isaakhanimann.journal.ui.theme.horizontalPadding


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckSaferUseScreen(
    navigateToNext: () -> Unit,
    viewModel: SaferUseViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("${viewModel.substanceName} safer use") }) },
        floatingActionButton = {
            NextFAB(navigateToNext)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            BulletPoints(
                points = viewModel.substance.saferUse,
                modifier = Modifier.padding(horizontal = horizontalPadding)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}