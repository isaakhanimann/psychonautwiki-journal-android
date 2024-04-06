/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.settings

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.ui.tabs.search.substance.SectionWithTitle
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun FAQScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("FAQ") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            QuestionAnswerRow(
                question = "Where does the information come from?",
                answer = "Dosages, durations, interactions, tolerance, common names, psychoactive classes, toxicity and addiction potential come from PsychonautWiki. Substance summaries and additional categories come from Tripsit. Effects, dosage remarks, general & long term risks and safer use come from the DIZ (drug information center of Zurich)."
            )
            QuestionAnswerRow(
                question = "When does the app detect interactions?",
                answer = "If there is an interaction with a substance you ingested less than 2 days ago. It checks interaction both ways and checks if a substance is indirectly mentioned through its psychoactive class."
            )
            QuestionAnswerRow(
                question = "How can the information be changed or added?",
                answer = "Edit the article in PsychonautWiki so your edit might get approved by one of the PsychonautWiki moderators, especially if you can reference good sources. The changed information will come in the next update of the app. If the information to be changed comes from another source than PsychonautWiki contact support."
            )
            QuestionAnswerRow(
                question = "Why does the timeline treat every ingestion independently?",
                answer = "Because the information on tolerance and cross tolerance does not yet allow to do this reliably."
            )
            QuestionAnswerRow(
                question = "What do the dots next to the dose of the ingestion mean?",
                answer = "0 dots means that the dose is less than threshold, 1: light, 2: common, 3: strong, 4: heavy. More than 4 dots means that the heavy dose is subtracted and the remainder is again classified in terms of light, common and strong."
            )
        }
    }
}

@Composable
fun QuestionAnswerRow(question: String, answer: String) {
    SectionWithTitle(title = question) {
        Text(
            text = answer,
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .padding(top = 5.dp, bottom = 8.dp)
        )
    }
}


