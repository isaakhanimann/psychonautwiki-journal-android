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

package com.isaakhanimann.journal.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.R

@Preview
@Composable
fun AcceptConditionsPreview() {
    AcceptConditionsScreen {}
}

@Composable
fun AcceptConditionsScreen(
    onTapAccept: () -> Unit
) {
    Scaffold { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(padding)
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            var checkedState0 by remember { mutableStateOf(false) }
            var checkedState1 by remember { mutableStateOf(false) }
            var checkedState2 by remember { mutableStateOf(false) }
            var checkedState3 by remember { mutableStateOf(false) }
            val allIsChecked =
                checkedState0 && checkedState1 && checkedState2 && checkedState3
            val painter =
                if (allIsChecked) painterResource(R.drawable.eye_open) else painterResource(R.drawable.eye_closed)
            Image(
                painter = painter,
                contentDescription = "PsychonautWiki eye",
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .clickable {
                        checkedState0 = true
                        checkedState1 = true
                        checkedState2 = true
                        checkedState3 = true
                    }
                    .fillMaxWidth(0.4f)
                    .padding(bottom = 20.dp)
            )
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                val responsibilityText =
                    "I acknowledge that I am the only one responsible for my actions, especially when deciding to use drugs."
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            checkedState0 = checkedState0.not()
                        }) {
                    Checkbox(
                        checked = checkedState0,
                        onCheckedChange = { checkedState0 = it },
                        modifier = Modifier.semantics {
                            contentDescription = responsibilityText
                        }
                    )
                    Text(text = responsibilityText)
                }
                val mitigateRisksText =
                    "I'm going to use this app for mitigating the risks of my or somebody else’s substance use."
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            checkedState1 = checkedState1.not()
                        }) {
                    Checkbox(
                        checked = checkedState1,
                        onCheckedChange = { checkedState1 = it },
                        modifier = Modifier.semantics {
                            contentDescription = mitigateRisksText
                        }
                    )
                    Text(text = mitigateRisksText)
                }
                val incompleteDataText =
                    "I acknowledge that the data in this app might be inaccurate or incomplete."
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            checkedState2 = checkedState2.not()
                        }
                ) {
                    Checkbox(
                        checked = checkedState2,
                        onCheckedChange = { checkedState2 = it },
                        modifier = Modifier.semantics {
                            contentDescription = incompleteDataText
                        }
                    )
                    Text(text = incompleteDataText)
                }
                val professionalHelpText =
                    "I’m going to seek professional help before attempting to self-medicate."
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            checkedState3 = checkedState3.not()
                        }) {
                    Checkbox(
                        checked = checkedState3,
                        onCheckedChange = { checkedState3 = it },
                        modifier = Modifier.semantics {
                            contentDescription = professionalHelpText
                        }
                    )
                    Text(text = professionalHelpText)
                }
            }
            Text(
                text = "Journaling data always stays on this device",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp)
            )
            Button(onClick = onTapAccept, enabled = allIsChecked) {
                Text(text = "Continue")
            }
        }
    }
}