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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.time

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
        createAndSaveIngestion = viewModel::createAndSaveIngestion,
        onChangeDateOrTime = viewModel::onChangeDateOrTime,
        localDateTime = localDateTime,
        dismissAddIngestionScreens = dismissAddIngestionScreens,
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
        isEnteredTitleOk = viewModel.isEnteredTitleOk
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
        createAndSaveIngestion = {},
        onChangeDateOrTime = {},
        localDateTime = LocalDateTime.now(),
        dismissAddIngestionScreens = {},
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
        isEnteredTitleOk = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseTimeScreen(
    createAndSaveIngestion: () -> Unit,
    onChangeDateOrTime: (LocalDateTime) -> Unit,
    localDateTime: LocalDateTime,
    dismissAddIngestionScreens: () -> Unit,
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
    isEnteredTitleOk: Boolean
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopAppBar(title = { Text("$substanceName Ingestion") }) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !isLoadingColor,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        createAndSaveIngestion()
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Ingestion Added",
                                duration = SnackbarDuration.Short
                            )
                        }
                        dismissAddIngestionScreens()
                    },
                    icon = {
                        Icon(
                            Icons.Filled.Done,
                            contentDescription = "Next"
                        )
                    },
                    text = { Text("Done") },
                )
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
                if (isShowingColorPicker) {
                    MyCard(title = "$substanceName Color") {
                        ColorPicker(
                            selectedColor = selectedColor,
                            onChangeOfColor = onChangeColor,
                            alreadyUsedColors = alreadyUsedColors,
                            otherColors = otherColors
                        )
                    }
                }
                MyCard(title = "Time") {
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
                MyCard(title = "Experience") {
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
                        val focusManager = LocalFocusManager.current
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
                MyCard(title = "Notes") {
                    NoteSection(
                        previousNotes,
                        note,
                        onNoteChange
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
            }
        }
    }
}


@Composable
fun MyCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = horizontalPadding,
                vertical = 5.dp
            )
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(3.dp))
            content()
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
