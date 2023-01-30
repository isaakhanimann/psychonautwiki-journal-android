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
                    experienceRepository.deleteEverything()
                    experienceRepository.insertEverything(journalExport)
                    snackbarHostState.showSnackbar(
                        message = "Import Successful",
                        duration = SnackbarDuration.Short
                    )
                } catch (e: Exception) {
                    println("Error when decoding: ${e.message}")
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
            val experiencesWithIngestions = experienceRepository.getAllExperiencesWithIngestionsSorted()
            val experiencesSerializable = experiencesWithIngestions.map {
                ExperienceSerializable(
                    title = it.experience.title,
                    text = it.experience.text,
                    creationDate = it.experience.creationDate,
                    sortDate = it.experience.sortDate,
                    isFavorite = it.experience.isFavorite,
                    ingestions = it.ingestions.map { ingestion ->
                        IngestionSerializable(
                            substanceName = ingestion.substanceName,
                            time = ingestion.time,
                            creationDate = ingestion.creationDate,
                            administrationRoute = ingestion.administrationRoute,
                            dose = ingestion.dose,
                            isDoseAnEstimate = ingestion.isDoseAnEstimate,
                            units = ingestion.units,
                            notes = ingestion.notes
                        )
                    },
                    location = null
                )
            }
            val journalExport = JournalExport(
                experiences = experiencesSerializable,
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