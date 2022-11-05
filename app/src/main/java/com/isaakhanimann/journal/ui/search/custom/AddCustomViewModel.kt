package com.isaakhanimann.journal.ui.search.custom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.CustomSubstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCustomViewModel @Inject constructor(
    val experienceRepo: ExperienceRepository,
) : ViewModel() {

    var name by mutableStateOf("")
    var units by mutableStateOf("")
    var description by mutableStateOf("")

    val isValid get() = name.isNotBlank() && units.isNotBlank()

    fun onDoneTap() {
        viewModelScope.launch {
            val customSubstance = CustomSubstance(
                name = name,
                units = units,
                description = description
            )
            experienceRepo.insert(customSubstance)
        }
    }
}