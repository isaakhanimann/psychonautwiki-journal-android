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

    private val recentlyUsedSubstancesFlow: Flow<List<Substance>> =
        experienceRepo.getLastUsedSubstanceNamesFlow(limit = 100).map { lastUsedSubstanceNames ->
            lastUsedSubstanceNames.mapNotNull { substanceRepo.getSubstance(substanceName = it) }
        }

    private val _searchTextFlow = MutableStateFlow("")
    val searchTextFlow = _searchTextFlow.asStateFlow()

    private val _filtersFlow = MutableStateFlow(listOf("common"))

    private val youUsedChipName = "you-used"

    fun onFilterTapped(filterName: String) {
        viewModelScope.launch {
            if (filterName == youUsedChipName) {
                _isShowingYouUsedFlow.emit(_isShowingYouUsedFlow.value.not())
            } else {
                val filters = _filtersFlow.value.toMutableList()
                if (filters.contains(filterName)) {
                    filters.remove(filterName)
                } else {
                    filters.add(filterName)
                }
                _filtersFlow.emit(filters)
            }
        }
    }

    private val _isShowingYouUsedFlow = MutableStateFlow(false)

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
        }.combine(_isShowingYouUsedFlow) { chips, isShowingYouUsed ->
            val newChips = chips.toMutableList()
            newChips.add(
                0, CategoryChipModel(
                    chipName = youUsedChipName,
                    color = Color.Magenta,
                    isActive = isShowingYouUsed
                )
            )
            return@combine newChips
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    private val allOrYouUsedSubstances =
        allSubstancesFlow.combine(recentlyUsedSubstancesFlow) { all, recents ->
            Pair(first = all, recents)
        }.combine(_isShowingYouUsedFlow) { pair, isShowingYouUsed ->
            if (isShowingYouUsed) {
                return@combine pair.second
            } else {
                return@combine pair.first
            }
        }

    val filteredSubstances =
        allOrYouUsedSubstances.combine(_filtersFlow) { substances, filters ->
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
