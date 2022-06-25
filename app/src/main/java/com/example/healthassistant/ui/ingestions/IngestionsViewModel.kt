package com.example.healthassistant.ui.ingestions


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.room.filter.FilterRepository
import com.example.healthassistant.data.room.filter.SubstanceFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class IngestionsViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    private val filterRepo: FilterRepository
) : ViewModel() {

    private val _ingestionsGrouped =
        MutableStateFlow<Map<String, List<Ingestion>>>(emptyMap())
    val ingestionsGrouped = _ingestionsGrouped.asStateFlow()

    private val _filterOptions =
        MutableStateFlow<List<FilterOption>>(emptyList())
    val filterOptions = _filterOptions.asStateFlow()

    var numberOfActiveFilters by mutableStateOf(0)

    data class FilterOption(
        val name: String,
        val hasCheck: Boolean,
        val isEnabled: Boolean,
        val onTap: () -> Unit
    )

    init {
        viewModelScope.launch {
            filterRepo.getFilters()
                .combine(experienceRepo.getLastUsedSubstanceNamesFlow(100)) { filters, names ->
                    Pair(first = filters, second = names)
                }
                .combine(experienceRepo.getSortedIngestionsFlow()) { filtersAndNames, experiencesWithIngestions ->
                    Pair(first = filtersAndNames, second = experiencesWithIngestions)
                }
                .collect {
                    val substanceFilters = it.first.first
                    numberOfActiveFilters = substanceFilters.size
                    _filterOptions.value = getFilterOptions(
                        substanceFilters = substanceFilters,
                        allDistinctSubstanceNames = it.first.second
                    )
                    val filteredIngestions = it.second.filter { ingestion ->
                        !substanceFilters.any { filter ->
                            filter.substanceName == ingestion.substanceName
                        }
                    }
                    _ingestionsGrouped.value =
                        groupIngestionsByYear(ingestions = filteredIngestions)
                }
        }
    }

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
        fun groupIngestionsByYear(ingestions: List<Ingestion>): Map<String, List<Ingestion>> {
            val cal = Calendar.getInstance(TimeZone.getDefault())
            return ingestions.groupBy { cal.get(Calendar.YEAR).toString() }
        }
    }

}