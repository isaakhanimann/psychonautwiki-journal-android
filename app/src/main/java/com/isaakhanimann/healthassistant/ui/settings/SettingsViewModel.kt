package com.isaakhanimann.healthassistant.ui.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import com.isaakhanimann.healthassistant.data.room.experiences.entities.CustomSubstance
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Experience
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceCompanion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val experienceRepository: ExperienceRepository,
    private val fileSystemConnection: FileSystemConnection
    ) : ViewModel() {

    fun importFile(uri: Uri?) {
        viewModelScope.launch {
            val text = fileSystemConnection.getTextFromUri(uri) ?: return@launch
            val journalExport = Json.decodeFromString<JournalExport>(text)
            experienceRepository.insertEverything(journalExport)
        }
    }

    fun exportFile(uri: Uri?) {
        viewModelScope.launch {
            val journalExport = JournalExport(
                ingestions = experienceRepository.getAllIngestions(),
                experiences = experienceRepository.getAllExperiences(),
                substanceCompanions = experienceRepository.getAllSubstanceCompanions(),
                customSubstances = experienceRepository.getAllCustomSubstances()
            )
            val jsonList = Json.encodeToString(journalExport)
            fileSystemConnection.saveTextInUri(uri, text = jsonList)
        }
    }

    fun deleteEverything() {
        viewModelScope.launch {
            experienceRepository.deleteEverything()
        }
    }
}