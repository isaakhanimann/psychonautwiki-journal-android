package com.isaakhanimann.healthassistant.ui.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val experienceRepository: ExperienceRepository,
    private val fileSystemConnection: FileSystemConnection
    ) : ViewModel() {

    fun importFile(uri: Uri?) {
        val text = fileSystemConnection.getTextFromUri(uri)
        println(text)
    }

    fun exportFile(uri: Uri?) {
        viewModelScope.launch {
            val ingestions = experienceRepository.getAllIngestions()
            val jsonList = Json.encodeToString(ingestions)
            fileSystemConnection.saveTextInUri(uri, text = jsonList)
        }
    }

    fun deleteEverything() {
        viewModelScope.launch {
            experienceRepository.deleteEverything()
        }
    }
}