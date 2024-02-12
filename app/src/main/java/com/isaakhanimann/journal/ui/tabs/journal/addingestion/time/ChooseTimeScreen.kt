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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.time

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.CardWithTitle
import com.isaakhanimann.journal.ui.tabs.journal.experience.rating.FloatingDoneButton
import com.isaakhanimann.journal.ui.theme.horizontalPadding
import com.isaakhanimann.journal.ui.utils.getStringOfPattern
import kotlinx.coroutines.launch
import java.time.LocalDateTime


@Composable
fun ChooseTimeScreen(
    dismissAddIngestionScreens: () -> Unit,
    viewModel: ChooseTimeViewModel = hiltViewModel()
) {
    val localDateTime = viewModel.localDateTimeFlow.collectAsState().value
    ChooseTimeScreen(
        createSaveAndDismissAfter = {
            viewModel.createSaveAndDismissAfter(dismiss = dismissAddIngestionScreens)
        },
        onChangeDateOrTime = viewModel::onChangeDateOrTime,
        localDateTime = localDateTime,
        isLoadingColor = viewModel.isLoadingColor,
        isShowingColorPicker = viewModel.isShowingColorPicker,
        selectedColor = viewModel.selectedColor,
        onChangeColor = { viewModel.selectedColor = it },
        alreadyUsedColors = viewModel.alreadyUsedColorsFlow.collectAsState().value,
        otherColors = viewModel.otherColorsFlow.collectAsState().value,
        previousNotes = viewModel.previousNotesFlow.collectAsState().value,
        note = viewModel.note,
        onNoteChange = {
            viewModel.note = it
        },
        experienceTitleToAddTo = viewModel.experienceTitleToAddToFlow.collectAsState().value,
        check = viewModel::toggleCheck,
        isChecked = viewModel.userWantsToContinueSameExperienceFlow.collectAsState().value,
        substanceName = viewModel.substanceName,
        enteredTitle = viewModel.enteredTitle,
        onChangeOfEnteredTitle = viewModel::changeTitle,
        isEnteredTitleOk = viewModel.isEnteredTitleOk,
        consumerName = viewModel.consumerName,
        onChangeOfConsumerName = viewModel::changeConsumerName,
        consumerNamesSorted = viewModel.sortedConsumerNamesFlow.collectAsState().value
    )
}

@Preview
@Composable
fun ChooseTimeScreenPreview() {
    val alreadyUsedColors = listOf(AdaptiveColor.BLUE, AdaptiveColor.PINK)
    val otherColors = AdaptiveColor.values().filter { color ->
        !alreadyUsedColors.contains(color)
    }
    ChooseTimeScreen(
        createSaveAndDismissAfter = {},
        onChangeDateOrTime = {},
        localDateTime = LocalDateTime.now(),
        isLoadingColor = false,
        isShowingColorPicker = true,
        selectedColor = AdaptiveColor.BLUE,
        onChangeColor = {},
        alreadyUsedColors = alreadyUsedColors,
        otherColors = otherColors,
        previousNotes = listOf(
            "My previous note where I make some remarks",
            "Another previous note and this one is very long, such that it doesn't fit on one line"
        ),
        note = "",
        onNoteChange = {},
        experienceTitleToAddTo = "New Years Eve",
        check = {},
        isChecked = false,
        substanceName = "LSD",
        enteredTitle = "This is my title",
        onChangeOfEnteredTitle = {},
        isEnteredTitleOk = true,
        consumerName = "",
        onChangeOfConsumerName = {},
        consumerNamesSorted = listOf("Isaak", "Marc", "Eve")
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseTimeScreen(
    createSaveAndDismissAfter: () -> Unit,
    onChangeDateOrTime: (LocalDateTime) -> Unit,
    localDateTime: LocalDateTime,
    isLoadingColor: Boolean,
    isShowingColorPicker: Boolean,
    selectedColor: AdaptiveColor,
    onChangeColor: (AdaptiveColor) -> Unit,
    alreadyUsedColors: List<AdaptiveColor>,
    otherColors: List<AdaptiveColor>,
    previousNotes: List<String>,
    note: String,
    onNoteChange: (String) -> Unit,
    experienceTitleToAddTo: String?,
    check: (Boolean) -> Unit,
    isChecked: Boolean,
    substanceName: String,
    enteredTitle: String,
    onChangeOfEnteredTitle: (String) -> Unit,
    isEnteredTitleOk: Boolean,
    consumerName: String,
    onChangeOfConsumerName: (String) -> Unit,
    consumerNamesSorted: List<String>
) {
    var isPresentingBottomSheet by rememberSaveable { mutableStateOf(false) }
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
    val focusManager = LocalFocusManager.current
    Scaffold(
        topBar = { TopAppBar(title = { Text("$substanceName Ingestion") }) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !isLoadingColor,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FloatingDoneButton {
                    createSaveAndDismissAfter()
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            LinearProgressIndicator(progress = 0.9f, modifier = Modifier.fillMaxWidth())
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = horizontalPadding)
            ) {
                Spacer(modifier = Modifier.height(3.dp))
                CardWithTitle(title = "Time") {
                    DatePickerButton(
                        localDateTime = localDateTime,
                        onChange = onChangeDateOrTime,
                        dateString = localDateTime.getStringOfPattern("EEE dd MMM yyyy"),
                        modifier = Modifier.fillMaxWidth()
                    )
                    TimePickerButton(
                        localDateTime = localDateTime,
                        onChange = onChangeDateOrTime,
                        timeString = localDateTime.getStringOfPattern("HH:mm"),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                CardWithTitle(title = "Experience", modifier = Modifier.fillMaxWidth()) {
                    val isCloseToExperience = experienceTitleToAddTo != null
                    AnimatedVisibility(visible = isCloseToExperience) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { check(isChecked.not()) }) {
                            Checkbox(checked = isChecked, onCheckedChange = check)
                            Text(text = "Add to $experienceTitleToAddTo")
                        }
                    }
                    AnimatedVisibility(visible = !isCloseToExperience || !isChecked) {
                        OutlinedTextField(
                            value = enteredTitle,
                            onValueChange = onChangeOfEnteredTitle,
                            singleLine = true,
                            label = { Text(text = "New Experience Title") },
                            isError = !isEnteredTitleOk,
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done,
                                capitalization = KeyboardCapitalization.Words
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 3.dp)
                        )
                    }
                }
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 3.dp)) {
                        Text(
                            text = "Consumed by: ${consumerName.ifBlank { "Me" }}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (consumerNamesSorted.isNotEmpty() || consumerName.isNotBlank()) {
                            TextButton(onClick = { isPresentingBottomSheet = !isPresentingBottomSheet }) {
                                Text(text = "Choose other consumer")
                            }
                        }
                        var showNewConsumerTextField by remember { mutableStateOf(false) }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Switch(checked = showNewConsumerTextField, onCheckedChange = {showNewConsumerTextField = !showNewConsumerTextField})
                            Text("Enter new consumer")
                        }
                        AnimatedVisibility(visible = showNewConsumerTextField) {
                            OutlinedTextField(
                                value = consumerName,
                                onValueChange = onChangeOfConsumerName,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Consumer"
                                    )
                                },
                                keyboardActions = KeyboardActions(onDone = {
                                    focusManager.clearFocus()
                                }),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Done,
                                    capitalization = KeyboardCapitalization.Words
                                ),
                                placeholder = { Text("New consumer name") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
                CardWithTitle(title = "Notes") {
                    NoteSection(
                        previousNotes,
                        note,
                        onNoteChange
                    )
                }
                if (isShowingColorPicker) {
                    CardWithTitle(title = "$substanceName Color", modifier = Modifier.fillMaxWidth()) {
                        ColorPicker(
                            selectedColor = selectedColor,
                            onChangeOfColor = onChangeColor,
                            alreadyUsedColors = alreadyUsedColors,
                            otherColors = otherColors
                        )
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
            }
        }
    }
    if (isPresentingBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { isPresentingBottomSheet = false },
            sheetState = bottomSheetState,
            windowInsets = BottomSheetDefaults.windowInsets
        ) {
            LazyColumn {
                item {
                    ListItem(
                        headlineContent = { Text("Me") },
                        leadingContent = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Consumer"
                            )
                        },
                        modifier = Modifier.clickable {
                            onChangeOfConsumerName("")
                            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                if (!bottomSheetState.isVisible) {
                                    isPresentingBottomSheet = false
                                }
                            }
                        }
                    )
                }
                items(consumerNamesSorted) { consumerName ->
                    ListItem(
                        headlineContent = { Text(consumerName) },
                        leadingContent = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Consumer"
                            )
                        },
                        modifier = Modifier.clickable {
                            onChangeOfConsumerName(consumerName)
                            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                if (!bottomSheetState.isVisible) {
                                    isPresentingBottomSheet = false
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NoteSection(
    previousNotes: List<String>,
    note: String,
    onNoteChange: (String) -> Unit
) {
    var isShowingSuggestions by remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current
    Column {
        OutlinedTextField(
            value = note,
            onValueChange = onNoteChange,
            label = { Text(text = "Notes") },
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                isShowingSuggestions = false
            }),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        isShowingSuggestions = true
                    }
                }
        )
        if (previousNotes.isNotEmpty() && isShowingSuggestions) {
            Column {
                previousNotes.forEach {
                    Row(
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .clickable {
                                focusManager.clearFocus()
                                isShowingSuggestions = false
                                onNoteChange(it)
                            }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.ContentCopy,
                                contentDescription = "Copy",
                                Modifier.size(15.dp)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(
                                text = it,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
