package com.example.healthassistant.ui.experiences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.room.experiences.relations.ExperienceWithIngestions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.*
import javax.inject.Inject


@HiltViewModel
class ExperiencesViewModel @Inject constructor(
    experienceRepo: ExperienceRepository
) : ViewModel() {

    val experiencesGrouped = experienceRepo.getSortedExperiencesWithIngestionsFlow()
        .map { groupExperiencesByYear(experiencesWithIngestions = it) }
        .stateIn(
            initialValue = emptyMap(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    companion object {
        fun groupExperiencesByYear(experiencesWithIngestions: List<ExperienceWithIngestions>): Map<String, List<ExperienceWithIngestions>> {
            val cal = Calendar.getInstance(TimeZone.getDefault())
            return experiencesWithIngestions.groupBy { cal.get(Calendar.YEAR).toString() }
        }
    }

}