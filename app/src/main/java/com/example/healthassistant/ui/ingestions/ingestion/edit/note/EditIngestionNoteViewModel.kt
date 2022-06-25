package com.example.healthassistant.ui.ingestions.ingestion.edit.note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.ui.main.routers.INGESTION_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditIngestionNoteViewModel @Inject constructor(
    private val repository: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {

    var ingestion: Ingestion? = null
    var note by mutableStateOf("")

    init {
        val id = state.get<Int>(INGESTION_ID_KEY)!!
        viewModelScope.launch {
            ingestion = repository.getIngestionFlow(id = id).first()!!
            note = ingestion!!.notes ?: ""
        }
    }

    fun onDoneTap() {
        viewModelScope.launch {
            ingestion!!.notes = note
            repository.updateIngestion(ingestion!!)
        }
    }

}