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

package com.isaakhanimann.journal.ui.tabs.search.substance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SaferStimulantsScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Safer Stimulants Use") })
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = horizontalPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = """
                Consider how long you want to stay awake. Don't suppress your need for sleep.
                
                Drink enough non-alcoholic drinks (3 - 5 dl per hour) and take breaks in the fresh air.
                
                Eat healthy before and after consumption and do not consume on an empty stomach.
                
                People with psychological disorders, pre-existing cardiovascular conditions, asthma, liver and kidney disorders or diabetes, hyperthyroidism and pregnant women are particulary discouraged from taking stimulants.
                
                Take vitamin C and D and minerals (iron, calcium and magnesium) with frequent use.
                
                It is better not to wear headgear (danger of overheating).
            """.trimIndent()
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}