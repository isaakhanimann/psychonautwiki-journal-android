/*
 * Copyright (c) 2022. Free Software Foundation, Inc.
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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        val checkedState0 = remember { mutableStateOf(false) }
        val checkedState1 = remember { mutableStateOf(false) }
        val checkedState2 = remember { mutableStateOf(false) }
        val checkedState3 = remember { mutableStateOf(false) }
        val allIsChecked =
            checkedState0.value && checkedState1.value && checkedState2.value && checkedState3.value
        val painter =
            if (allIsChecked) painterResource(R.drawable.eye_open) else painterResource(R.drawable.eye_closed)
        Image(
            painter = painter,
            contentDescription = "PsychonautWiki eye",
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .padding(bottom = 10.dp)
        )
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    checkedState0.value = checkedState0.value.not()
                }) {
                Checkbox(
                    checked = checkedState0.value,
                    onCheckedChange = { checkedState0.value = it }
                )
                Text(text = "I acknowledge that I am the only one responsible for my actions, especially when deciding to use drugs.")
            }
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    checkedState1.value = checkedState1.value.not()
                }) {
                Checkbox(
                    checked = checkedState1.value,
                    onCheckedChange = { checkedState1.value = it }
                )
                Text(text = "I'm going to use this app for mitigating the risks of my or somebody else’s substance use.")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    checkedState2.value = checkedState2.value.not()
                }
            ) {
                Checkbox(
                    checked = checkedState2.value,
                    onCheckedChange = { checkedState2.value = it }
                )
                Text(text = "I acknowledge that the data in this app might be inaccurate or incomplete.")
            }
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    checkedState3.value = checkedState3.value.not()
                }) {
                Checkbox(
                    checked = checkedState3.value,
                    onCheckedChange = { checkedState3.value = it }
                )
                Text(text = "I’m going to seek professional help before attempting to self-medicate.")
            }
        }
        Text(
            text = "Journaling data always stays on this device",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 15.dp)
        )
        Button(onClick = onTapAccept, enabled = allIsChecked) {
            Text(text = "Continue")
        }
    }
}