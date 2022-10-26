package com.isaakhanimann.healthassistant.ui.journal

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

    val experiences = experienceRepo.getSortedExperiencesWithIngestionsAndCompanionsFlow()
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
            return@combine filtered
        }
        .stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

}