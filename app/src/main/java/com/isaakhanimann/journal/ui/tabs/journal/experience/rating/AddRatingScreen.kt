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

package com.isaakhanimann.journal.ui.tabs.journal.experience.rating

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.room.experiences.entities.ShulginRatingOption
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.time.DatePickerButton
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.time.TimePickerButton
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.CardWithTitle
import com.isaakhanimann.journal.ui.theme.JournalTheme
import com.isaakhanimann.journal.ui.theme.horizontalPadding
import com.isaakhanimann.journal.ui.utils.getStringOfPattern
import java.time.LocalDateTime


@Composable
fun AddRatingScreen(
    viewModel: AddRatingViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    AddRatingScreen(
        onDone = {
            viewModel.onDoneTap()
            navigateBack()
        },
        selectedTime = viewModel.localDateTimeFlow.collectAsState().value,
        onTimeChange = viewModel::onChangeTime,
        selectedRating = viewModel.rating,
        onRatingChange = viewModel::onChangeRating
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddRatingScreenPreview() {
    JournalTheme {
        AddRatingScreen(
            onDone = {},
            selectedTime = LocalDateTime.now(),
            onTimeChange = {},
            selectedRating = ShulginRatingOption.TWO_PLUS,
            onRatingChange = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRatingScreen(
    onDone: () -> Unit,
    selectedTime: LocalDateTime,
    onTimeChange: (LocalDateTime) -> Unit,
    selectedRating: ShulginRatingOption,
    onRatingChange: (ShulginRatingOption) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Shulgin Rating") },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onDone,
                icon = {
                    Icon(
                        Icons.Filled.Done,
                        contentDescription = "Done Icon"
                    )
                },
                text = { Text("Done") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = horizontalPadding)
        ) {
            Spacer(modifier = Modifier.height(3.dp))
            CardWithTitle(title = "Time") {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    DatePickerButton(
                        localDateTime = selectedTime,
                        onChange = onTimeChange,
                        dateString = selectedTime.getStringOfPattern("EEE dd MMM yyyy"),
                        modifier = Modifier.fillMaxWidth()
                    )
                    TimePickerButton(
                        localDateTime = selectedTime,
                        onChange = onTimeChange,
                        timeString = selectedTime.getStringOfPattern("HH:mm"),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            CardWithTitle(title = "Shulgin Rating") {
                val ratings = ShulginRatingOption.values()
                // Modifier.selectableGroup() is essential to ensure correct accessibility behavior
                Column(Modifier.selectableGroup()) {
                    ratings.forEach { oneRating ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = (oneRating == selectedRating),
                                    onClick = { onRatingChange(oneRating) },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (oneRating == selectedRating),
                                onClick = null // null recommended for accessibility with screen readers
                            )
                            Text(
                                text = oneRating.sign,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}