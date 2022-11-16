/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.safer.settings

import android.net.Uri
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val experienceRepository: ExperienceRepository,
    private val fileSystemConnection: FileSystemConnection,
) : ViewModel() {

    val snackbarHostState = SnackbarHostState()

    fun importFile(uri: Uri) {
        viewModelScope.launch {
            val text = fileSystemConnection.getTextFromUri(uri)
            if (text == null) {
                snackbarHostState.showSnackbar(
                    message = "File Not Found",
                    duration = SnackbarDuration.Short
                )
            } else {
                try {
                    val journalExport = Json.decodeFromString<JournalExport>(text)
                    experienceRepository.insertEverything(journalExport)
                    snackbarHostState.showSnackbar(
                        message = "Import Successful",
                        duration = SnackbarDuration.Short
                    )
                } catch (_: Exception) {
                    snackbarHostState.showSnackbar(
                        message = "Decoding File Failed",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    fun exportFile(uri: Uri) {
        viewModelScope.launch {
            val journalExport = JournalExport(
                ingestions = experienceRepository.getAllIngestions(),
                experiences = experienceRepository.getAllExperiences(),
                substanceCompanions = experienceRepository.getAllSubstanceCompanions(),
                customSubstances = experienceRepository.getAllCustomSubstances()
            )
            try {
                val jsonList = Json.encodeToString(journalExport)
                fileSystemConnection.saveTextInUri(uri, text = jsonList)
                snackbarHostState.showSnackbar(
                    message = "Export Successful",
                    duration = SnackbarDuration.Short
                )
            } catch (_: Exception) {
                snackbarHostState.showSnackbar(
                    message = "Export Failed",
                    duration = SnackbarDuration.Short
                )
            }

        }
    }

    fun deleteEverything() {
        viewModelScope.launch {
            experienceRepository.deleteEverything()
        }
    }
}