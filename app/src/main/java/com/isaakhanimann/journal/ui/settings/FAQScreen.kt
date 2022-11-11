/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.ui.search.substance.CollapsibleSection
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
        }
    }
}

@Composable
fun QuestionAnswerRow(question: String, answer: String) {
    CollapsibleSection(title = question) {
        Text(
            text = answer,
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .padding(top = 5.dp, bottom = 8.dp)
        )
    }
}


