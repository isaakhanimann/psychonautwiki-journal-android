/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
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