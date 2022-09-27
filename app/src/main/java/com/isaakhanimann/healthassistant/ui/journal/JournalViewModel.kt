package com.isaakhanimann.healthassistant.ui.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import com.isaakhanimann.healthassistant.data.room.experiences.relations.ExperienceWithIngestionsAndCompanions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.ZoneId
import javax.inject.Inject


@HiltViewModel
class JournalViewModel @Inject constructor(
    experienceRepo: ExperienceRepository
) : ViewModel() {

    val isFavoriteFlow = MutableStateFlow(false)

    fun onChangeFavorite(isFavorite: Boolean) {
        viewModelScope.launch {
            isFavoriteFlow.emit(isFavorite)
        }
    }

    val experiencesGrouped = experienceRepo.getSortedExperiencesWithIngestionsAndCompanionsFlow()
        .combine(isFavoriteFlow) { experiencesWithIngestions, isFavorite ->
            val filtered =
                if (isFavorite) experiencesWithIngestions.filter { it.experience.isFavorite } else experiencesWithIngestions
            groupExperiencesByYear(experiencesWithIngestions = filtered)
        }
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