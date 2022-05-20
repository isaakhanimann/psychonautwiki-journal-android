package com.example.healthassistant.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.experiences.entities.ExperienceWithIngestions
import com.example.healthassistant.data.experiences.repositories.ExperienceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ExperienceRepository) :
    ViewModel() {

    private val _experiencesGrouped = MutableStateFlow<Map<String, List<ExperienceWithIngestions>>>(emptyMap())
    val experiencesGrouped = _experiencesGrouped.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllExperiencesWithIngestions()
                .collect { experiences ->
                    val cal = Calendar.getInstance(TimeZone.getDefault())
                    _experiencesGrouped.value = experiences.groupBy { cal.get(Calendar.YEAR).toString() }
                }

        }
    }
}