package com.isaakhanimann.healthassistant.ui.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
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
    private val toastManager: ToastManager
) : ViewModel() {

    fun importFile(uri: Uri) {
        viewModelScope.launch {
            val text = fileSystemConnection.getTextFromUri(uri)
            if (text == null) {
                toastManager.showToast("File Not Found")
            } else {
                try {
                    val journalExport = Json.decodeFromString<JournalExport>(text)
                    experienceRepository.insertEverything(journalExport)
                    toastManager.showToast("Import Successful")
                } catch (_: Exception) {
                    toastManager.showToast("Decoding File Failed")
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
                toastManager.showToast("Export Successful")
            } catch (_: Exception) {
                toastManager.showToast("Export Failed")
            }

        }
    }

    fun deleteEverything() {
        viewModelScope.launch {
            experienceRepository.deleteEverything()
        }
    }
}