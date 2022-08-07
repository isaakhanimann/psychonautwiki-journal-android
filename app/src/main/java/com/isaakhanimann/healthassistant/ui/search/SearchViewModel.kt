package com.isaakhanimann.healthassistant.ui.search

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import com.isaakhanimann.healthassistant.data.substances.classes.Category
import com.isaakhanimann.healthassistant.data.substances.classes.Substance
import com.isaakhanimann.healthassistant.data.substances.repositories.SubstanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    experienceRepo: ExperienceRepository,
    substanceRepo: SubstanceRepository
) : ViewModel() {

    private val allSubstancesFlow: Flow<List<Substance>> = substanceRepo.getAllSubstances()
    private val allCategoriesFlow: Flow<List<Category>> = substanceRepo.getAllCategoriesFlow()

    private val recentlyUsedNamesFlow: Flow<List<Substance>> =
        experienceRepo.getLastUsedSubstanceNamesFlow(limit = 10).map { lastUsedSubstanceNames ->
            lastUsedSubstanceNames.mapNotNull { substanceRepo.getSubstance(substanceName = it) }
        }

    private val _searchTextFlow = MutableStateFlow("")
    val searchTextFlow = _searchTextFlow.asStateFlow()

    private val _filtersFlow = MutableStateFlow(listOf("common"))

    fun onFilterTapped(filterName: String) {
        viewModelScope.launch {
            val filters = _filtersFlow.value.toMutableList()
            if (filters.contains(filterName)) {
                filters.remove(filterName)
            } else {
                filters.add(filterName)
            }
            _filtersFlow.emit(filters)
        }
    }

    val chipCategoriesFlow: StateFlow<List<CategoryChipModel>> =
        allCategoriesFlow.combine(_filtersFlow) { categories, filters ->
            categories.map { category ->
                val isActive = filters.contains(category.name)
                CategoryChipModel(
                    chipName = category.name,
                    color = category.color,
                    isActive = isActive
                )
            }
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    val filteredRecentlyUsed: StateFlow<List<Substance>> =
        recentlyUsedNamesFlow.combine(searchTextFlow) { recents, searchText ->
            getMatchingSubstances(searchText, recents)
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    val filteredSubstances =
        allSubstancesFlow.combine(_filtersFlow) { substances, filters ->
            substances.filter { substance ->
                filters.all { substance.categories.contains(it) }
            }
        }.combine(searchTextFlow) { substances, searchText ->
            getMatchingSubstances(searchText, substances)
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    fun filterSubstances(searchText: String) {
        viewModelScope.launch {
            _searchTextFlow.emit(searchText)
        }
    }

    companion object {
        fun getMatchingSubstances(
            searchText: String,
            substances: List<Substance>
        ): List<Substance> {
            return if (searchText.isEmpty()) {
                substances
            } else {
                if (searchText.length < 3) {
                    substances.filter { substance ->
                        substance.name.startsWith(prefix = searchText, ignoreCase = true) ||
                                substance.commonNames.any { commonName ->
                                    commonName.startsWith(prefix = searchText, ignoreCase = true)
                                }
                    }
                } else {
                    val containing = substances.filter { substance ->
                        substance.name.contains(other = searchText, ignoreCase = true) ||
                                substance.commonNames.any { commonName ->
                                    commonName.contains(other = searchText, ignoreCase = true)
                                }
                    }
                    val prefixAndContainingMatches = containing.partition { substance ->
                        substance.name.startsWith(prefix = searchText, ignoreCase = true) ||
                                substance.commonNames.any { commonName ->
                                    commonName.startsWith(prefix = searchText, ignoreCase = true)
                                }
                    }
                    prefixAndContainingMatches.first + prefixAndContainingMatches.second
                }
            }
        }
    }
}

data class CategoryChipModel(
    val chipName: String,
    val color: Color,
    val isActive: Boolean
)
