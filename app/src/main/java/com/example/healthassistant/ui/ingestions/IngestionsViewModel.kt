package com.example.healthassistant.ui.ingestions


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.example.healthassistant.data.room.filter.FilterRepository
import com.example.healthassistant.data.room.filter.SubstanceFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class IngestionsViewModel @Inject constructor(
    experienceRepo: ExperienceRepository,
    private val filterRepo: FilterRepository
) : ViewModel() {

    private val lastUsedSubstancesFlow = experienceRepo.getLastUsedSubstanceNamesFlow(100)
    private val sortedIngestionsWithCompanionsFlow:  Flow<List<IngestionWithCompanion>> = experienceRepo.getSortedIngestionsWithSubstanceCompanionsFlow()
    private val filtersFlow = filterRepo.getFilters()

    val ingestionsGrouped: StateFlow<Map<String, List<IngestionWithCompanion>>> =
        filtersFlow.combine(sortedIngestionsWithCompanionsFlow) { filters, ingestionsWithCompanions ->
            val filteredIngestions = ingestionsWithCompanions.filter { ingWithComp ->
                !filters.any { filter ->
                    filter.substanceName == ingWithComp.ingestion.substanceName
                }
            }.toList()
            groupIngestionsByYear(ingestions = filteredIngestions)
        }.stateIn(
            initialValue = emptyMap(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    val filterOptions: StateFlow<List<FilterOption>> =
        lastUsedSubstancesFlow.combine(filtersFlow) { lastUsedSubstances, filters ->
            getFilterOptions(
                substanceFilters = filters,
                allDistinctSubstanceNames = lastUsedSubstances
            )
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    var numberOfActiveFilters: StateFlow<Int> = filtersFlow.map { it.size }.stateIn(
        initialValue = 0,
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    data class FilterOption(
        val name: String,
        val hasCheck: Boolean,
        val isEnabled: Boolean,
        val onTap: () -> Unit
    )

    private fun addFilter(substanceName: String) {
        val filter = SubstanceFilter(substanceName = substanceName)
        viewModelScope.launch {
            filterRepo.insert(filter)
        }
    }

    private fun deleteFilter(substanceName: String) {
        val filter = SubstanceFilter(substanceName = substanceName)
        viewModelScope.launch {
            filterRepo.deleteFilter(filter)
        }
    }

    private fun getFilterOptions(
        substanceFilters: List<SubstanceFilter>,
        allDistinctSubstanceNames: List<String>
    ): List<FilterOption> {
        val firstOption = FilterOption(
            name = "Show All",
            hasCheck = substanceFilters.isEmpty(),
            isEnabled = substanceFilters.isNotEmpty(),
            onTap = {
                if (substanceFilters.isNotEmpty()) {
                    deleteAllFilters()
                }
            }
        )
        val options = mutableListOf(firstOption)
        options.addAll(
            allDistinctSubstanceNames.map {
                val hasCheck = !substanceFilters.any { filter -> filter.substanceName == it }
                FilterOption(
                    name = it,
                    hasCheck = hasCheck,
                    isEnabled = true,
                    onTap = {
                        if (hasCheck) {
                            addFilter(substanceName = it)
                        } else {
                            deleteFilter(substanceName = it)
                        }
                    }
                )
            }
        )
        return options
    }

    private fun deleteAllFilters() {
        viewModelScope.launch {
            filterRepo.deleteAll()
        }
    }

    companion object {
        fun groupIngestionsByYear(ingestions: List<IngestionWithCompanion>): Map<String, List<IngestionWithCompanion>> {
            val cal = Calendar.getInstance(TimeZone.getDefault())
            return ingestions.groupBy { cal.get(Calendar.YEAR).toString() }
        }
    }

}