package com.isaakhanimann.healthassistant.ui.journal

import androidx.compose.runtime.mutableStateOf
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

    val isFavoriteEnabledFlow = MutableStateFlow(false)

    val isSearchEnabled = mutableStateOf(false)

    fun onChangeOfIsSearchEnabled(newValue: Boolean) {
        if (newValue) {
            isSearchEnabled.value = true
        } else {
            isSearchEnabled.value = false
            viewModelScope.launch {
                searchTextFlow.emit("")
            }
        }
    }

    fun onChangeFavorite(isFavorite: Boolean) {
        viewModelScope.launch {
            isFavoriteEnabledFlow.emit(isFavorite)
        }
    }

    val searchTextFlow = MutableStateFlow("")

    fun search(newSearchText: String) {
        viewModelScope.launch {
            searchTextFlow.emit(newSearchText)
        }
    }

    val experiencesGrouped = experienceRepo.getSortedExperiencesWithIngestionsAndCompanionsFlow()
        .combine(searchTextFlow) { experiencesWithIngestions, searchText ->
            Pair(first = experiencesWithIngestions, second = searchText)
        }
        .combine(isFavoriteEnabledFlow) { pair, isFavoriteEnabled ->
            val experiencesWithIngestions = pair.first
            val searchText = pair.second
            val filtered = if (searchText.isEmpty() && !isFavoriteEnabled) {
                if (isFavoriteEnabled) {
                    experiencesWithIngestions.filter { it.experience.isFavorite }
                } else {
                    experiencesWithIngestions
                }
            } else {
                if (isFavoriteEnabled) {
                    experiencesWithIngestions.filter {
                        it.experience.isFavorite && it.experience.title.contains(
                            other = searchText,
                            ignoreCase = true
                        )
                    }
                } else {
                    experiencesWithIngestions.filter {
                        it.experience.title.contains(
                            other = searchText,
                            ignoreCase = true
                        )
                    }
                }
            }
            return@combine groupExperiencesByYear(experiencesWithIngestions = filtered)
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