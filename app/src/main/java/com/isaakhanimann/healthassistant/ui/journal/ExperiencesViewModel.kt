package com.isaakhanimann.healthassistant.ui.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import com.isaakhanimann.healthassistant.data.room.experiences.relations.ExperienceWithIngestionsAndCompanions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.ZoneId
import javax.inject.Inject


@HiltViewModel
class ExperiencesViewModel @Inject constructor(
    experienceRepo: ExperienceRepository
) : ViewModel() {

    val experiencesGrouped = experienceRepo.getSortedExperiencesWithIngestionsAndCompanionsFlow()
        .map { groupExperiencesByYear(experiencesWithIngestions = it) }
        .stateIn(
            initialValue = emptyMap(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    companion object {
        fun groupExperiencesByYear(experiencesWithIngestions: List<ExperienceWithIngestionsAndCompanions>): Map<String, List<ExperienceWithIngestionsAndCompanions>> {
            return experiencesWithIngestions.groupBy { out ->
                out.sortInstant.atZone(ZoneId.systemDefault()).year.toString()

            }
        }
    }

}