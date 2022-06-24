package com.example.healthassistant.ui.experiences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.room.experiences.entities.Experience
import com.example.healthassistant.data.room.experiences.entities.ExperienceWithIngestions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class ExperiencesViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository
) : ViewModel() {

    private val _experiencesGrouped =
        MutableStateFlow<Map<String, List<ExperienceWithIngestions>>>(emptyMap())
    val experiencesGrouped = _experiencesGrouped.asStateFlow()

    init {
        viewModelScope.launch {
            experienceRepo.getSortedExperiencesWithIngestions()
                .collect {
                    _experiencesGrouped.value =
                        groupExperiencesByYear(experiencesWithIngestions = it)
                }
        }
    }

    fun deleteExperience(experience: Experience) {
        viewModelScope.launch {
            experienceRepo.deleteExperience(experience = experience)
        }
    }

    companion object {
        fun groupExperiencesByYear(experiencesWithIngestions: List<ExperienceWithIngestions>): Map<String, List<ExperienceWithIngestions>> {
            val cal = Calendar.getInstance(TimeZone.getDefault())
            return experiencesWithIngestions.groupBy { cal.get(Calendar.YEAR).toString() }
        }
    }

}