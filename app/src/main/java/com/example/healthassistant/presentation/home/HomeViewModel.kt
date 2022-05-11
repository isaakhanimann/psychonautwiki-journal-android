package com.example.healthassistant.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.experiences.ExperienceRepository
import com.example.healthassistant.data.experiences.entities.Experience
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ExperienceRepository) :
    ViewModel() {

    private val _experiences = MutableStateFlow<List<Experience>>(emptyList())
    val experiences = _experiences.asStateFlow()
    var isShowingDialog by mutableStateOf(false)
    var enteredTitle by mutableStateOf("")
    val isEnteredTitleOk get() = enteredTitle.isNotEmpty()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllExperiences().distinctUntilChanged()
                .collect { es ->
                    if (es.isNotEmpty()) {
                        _experiences.value = es
                    }
                }

        }
    }

    fun dialogConfirmTapped(onSuccess: () -> Unit) {
        if (enteredTitle.isNotEmpty()) {
            val newExperience = Experience(title = enteredTitle, creationDate = Date(), text = "")
            enteredTitle = ""
            viewModelScope.launch { repository.addExperience(newExperience) }
            isShowingDialog = false
            onSuccess()
        }
    }

}