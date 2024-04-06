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

package com.isaakhanimann.journal.ui.tabs.journal.experience.rating.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
fun EditRatingScreenPreview() {
    EditRatingScreen(
        onDone = {},
        selectedTime = LocalDateTime.now(),
        onTimeChange = {},
        selectedRating = ShulginRatingOption.TWO_PLUS,
        onRatingChange = {},
        onDelete = {},
        isOverallRating = false
    )
}

@Composable
fun EditRatingScreen(
    viewModel: EditRatingViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    EditRatingScreen(
        onDone = {
            viewModel.onDoneTap()
            navigateBack()
        },
        selectedTime = viewModel.localDateTimeFlow.collectAsState().value,
        onTimeChange = viewModel::onChangeTime,
        selectedRating = viewModel.selectedRatingOption,
        onRatingChange = viewModel::onChangeRating,
        onDelete = {
            viewModel.delete()
            navigateBack()
        },
        isOverallRating = viewModel.isOverallRatingFlow.collectAsState().value
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRatingScreen(
    onDone: () -> Unit,
    selectedTime: LocalDateTime,
    onTimeChange: (LocalDateTime) -> Unit,
    selectedRating: ShulginRatingOption,
    onRatingChange: (ShulginRatingOption) -> Unit,
    onDelete: () -> Unit,
    isOverallRating: Boolean
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Shulgin Rating") },
                actions = {
                    IconButton(
                        onClick = { onDelete() },
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete Rating",
                        )
                    }
                }
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
            if (!isOverallRating) {
                TimePickerSection(selectedTime = selectedTime, onTimeChange = onTimeChange)
            }
            RatingPickerSection(selectedRating = selectedRating, onRatingChange = onRatingChange)
            RatingsExplanationSection()
        }
    }
}