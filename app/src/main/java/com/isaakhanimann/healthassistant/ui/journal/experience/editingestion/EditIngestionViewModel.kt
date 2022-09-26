package com.isaakhanimann.healthassistant.ui.journal.experience.editingestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.ui.main.routers.INGESTION_ID_KEY
import com.isaakhanimann.healthassistant.ui.search.substance.roa.toReadableString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditIngestionViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {
    var ingestion: Ingestion? = null
    var note by mutableStateOf("")
    var isEstimate by mutableStateOf(false)
    var dose by mutableStateOf("")
    var units by mutableStateOf("")
    var experienceId by mutableStateOf(1)

    private val dateFlow = MutableStateFlow(Date())

    init {
        val id = state.get<Int>(INGESTION_ID_KEY)!!
        viewModelScope.launch {
            val ing = experienceRepo.getIngestionFlow(id = id).first() ?: return@launch
            ingestion = ing
            note = ing.notes ?: ""
            isEstimate = ing.isDoseAnEstimate
            experienceId = ing.experienceId
            dose = ing.dose?.toReadableString() ?: ""
            units = ing.units ?: ""
            dateFlow.emit(ing.time)
        }
    }

    val relevantExperiences: StateFlow<List<ExperienceOption>> = dateFlow.map {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        cal.time = it
        cal.add(Calendar.DAY_OF_YEAR, -2)
        val fromDate = cal.time
        cal.time = it
        cal.add(Calendar.DAY_OF_YEAR, 2)
        val toDate = cal.time
        return@map experienceRepo.getIngestionsWithExperiencesFlow(fromDate, toDate).firstOrNull()
            ?: emptyList()
    }.map { list ->
        return@map list.map {
            ExperienceOption(id = it.experience.id, title = it.experience.title)
        }.distinct()
    }.stateIn(
        initialValue = emptyList(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    fun onDoneTap() {
        viewModelScope.launch {
            ingestion?.let {
                it.notes = note
                it.isDoseAnEstimate = isEstimate
                it.experienceId = experienceId
                it.dose = dose.toDoubleOrNull()
                it.units = units
                experienceRepo.update(it)
            }
        }
    }

    fun deleteIngestion() {
        viewModelScope.launch {
            ingestion?.let {
                experienceRepo.delete(ingestion = it)
            }
        }
    }
}

data class ExperienceOption(
    val id: Int,
    val title: String
)