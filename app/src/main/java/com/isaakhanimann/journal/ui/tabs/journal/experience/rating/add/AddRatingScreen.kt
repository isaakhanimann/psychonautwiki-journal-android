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

package com.isaakhanimann.journal.ui.tabs.journal.experience.rating.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.room.experiences.entities.ShulginRatingOption
import com.isaakhanimann.journal.ui.tabs.journal.experience.rating.*
import com.isaakhanimann.journal.ui.theme.horizontalPadding
import java.time.LocalDateTime

@Preview
@Composable
fun AddRatingScreenPreview() {
    AddRatingScreen(
        onDone = {},
        selectedTime = LocalDateTime.now(),
        onTimeChange = {},
        selectedRating = ShulginRatingOption.TWO_PLUS,
        onRatingChange = {},
        canAddOverallRating = true,
        isOverallRating = false,
        onChangeIsOverallRating = {}
    )
}

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
        selectedRating = viewModel.selectedRating,
        onRatingChange = viewModel::onChangeRating,
        canAddOverallRating = !viewModel.isThereAlreadyAnOverallRatingFlow.collectAsState().value,
        isOverallRating = viewModel.isThisOverallRating,
        onChangeIsOverallRating = {viewModel.isThisOverallRating = it}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRatingScreen(
    onDone: () -> Unit,
    selectedTime: LocalDateTime,
    onTimeChange: (LocalDateTime) -> Unit,
    selectedRating: ShulginRatingOption,
    onRatingChange: (ShulginRatingOption) -> Unit,
    canAddOverallRating: Boolean,
    isOverallRating: Boolean,
    onChangeIsOverallRating: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Shulgin Rating") },
            )
        },
        floatingActionButton = {
            FloatingDoneButton(onDone)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = horizontalPadding)
        ) {
            Spacer(modifier = Modifier.height(3.dp))
            if (canAddOverallRating) {
                Card(modifier = Modifier
                    .padding(vertical = 5.dp)
                    .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 3.dp)
                    ) {
                        Switch(
                            checked = isOverallRating,
                            onCheckedChange = { onChangeIsOverallRating(it) }
                        )
                        Text(
                            text = "Overall Rating",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            AnimatedVisibility(visible = !isOverallRating) {
                TimePickerSection(selectedTime = selectedTime, onTimeChange = onTimeChange)
            }
            RatingPickerSection(selectedRating = selectedRating, onRatingChange = onRatingChange)
            RatingsExplanationSection()
        }
    }
}