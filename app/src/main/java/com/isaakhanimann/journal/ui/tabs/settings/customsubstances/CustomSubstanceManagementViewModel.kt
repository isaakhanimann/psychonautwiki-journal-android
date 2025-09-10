package com.isaakhanimann.journal.ui.tabs.settings.customsubstances

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.CustomSubstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStreamReader
import javax.inject.Inject

@HiltViewModel
class CustomSubstanceManagementViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository
) : ViewModel() {

    private val json = Json { prettyPrint = true }

    val allSubstances = experienceRepo.getCustomSubstancesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedSubstances = MutableStateFlow<Set<Int>>(emptySet())
    val selectedSubstances = _selectedSubstances.asStateFlow()

    val selectionMode = selectedSubstances.combine(allSubstances) { selected, all ->
        when {
            selected.isEmpty() -> SelectionMode.None
            selected.size == all.size -> SelectionMode.All
            else -> SelectionMode.Partial
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SelectionMode.None)

    fun toggleSelection(substanceId: Int) {
        val current = _selectedSubstances.value.toMutableSet()
        if (current.contains(substanceId)) {
            current.remove(substanceId)
        } else {
            current.add(substanceId)
        }
        _selectedSubstances.value = current
    }

    fun clearSelection() {
        _selectedSubstances.value = emptySet()
    }

    fun selectAll() {
        viewModelScope.launch {
            _selectedSubstances.value = allSubstances.value.map { it.id }.toSet()
        }
    }

    fun exportSelection(context: Context, uri: Uri, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val substancesToExport = allSubstances.value.filter {
                    _selectedSubstances.value.contains(it.id)
                }
                val exportData = CustomSubstancesExport(customSubstances = substancesToExport)
                val jsonString = json.encodeToString(exportData)

                context.contentResolver.openFileDescriptor(uri, "w")?.use {
                    FileOutputStream(it.fileDescriptor).use { stream ->
                        stream.write(jsonString.toByteArray())
                    }
                }
                onResult(true)
            } catch (e: Exception) {
                onResult(false)
            } finally {
                clearSelection()
            }
        }
    }

    fun importSubstances(context: Context, uri: Uri, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val jsonString = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        reader.readText()
                    }
                } ?: throw IllegalStateException("Could not read file")

                val importData = json.decodeFromString<CustomSubstancesExport>(jsonString)
                experienceRepo.importCustomSubstances(importData.customSubstances)
                onResult(true)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
}

enum class SelectionMode {
    None, Partial, All
}