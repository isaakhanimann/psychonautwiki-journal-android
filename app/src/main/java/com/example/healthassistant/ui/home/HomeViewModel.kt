package com.example.healthassistant.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.room.experiences.entities.ExperienceWithIngestions
import com.example.healthassistant.data.room.filter.FilterRepository
import com.example.healthassistant.data.room.filter.SubstanceFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    private val filterRepo: FilterRepository
) : ViewModel() {

    private val _experiencesGrouped =
        MutableStateFlow<Map<String, List<ExperienceWithIngestions>>>(emptyMap())
    val experiencesGrouped = _experiencesGrouped.asStateFlow()

    private val _filters =
        MutableStateFlow<List<SubstanceFilter>>(emptyList())
    val filters = _filters.asStateFlow()

    init {
        viewModelScope.launch {
            filterRepo.getFilters().collect { filters ->
                _filters.value = filters
            }
        }
        viewModelScope.launch {
            experienceRepo.getAllExperiencesWithIngestions()
                .collect { experiences ->
                    val cal = Calendar.getInstance(TimeZone.getDefault())
                    _experiencesGrouped.value =
                        experiences.groupBy { cal.get(Calendar.YEAR).toString() }
                }
        }
    }

    fun addFilter(substanceName: String) {
        val filter = SubstanceFilter(substanceName = substanceName)
        viewModelScope.launch {
            filterRepo.insert(filter)
        }
    }

    fun deleteExperienceWithIngestions(experienceWithIngs: ExperienceWithIngestions) {
        viewModelScope.launch {
            experienceRepo.deleteExperienceWithIngestions(experience = experienceWithIngs.experience)
        }
    }

}