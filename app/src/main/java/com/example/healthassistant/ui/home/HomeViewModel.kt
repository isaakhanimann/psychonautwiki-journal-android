package com.example.healthassistant.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.experiences.entities.Experience
import com.example.healthassistant.data.experiences.repositories.ExperienceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ExperienceRepository) :
    ViewModel() {

    private val _experiencesGrouped = MutableStateFlow<Map<String, List<Experience>>>(emptyMap())
    val experiencesGrouped = _experiencesGrouped.asStateFlow()
    var isShowingDialog by mutableStateOf(false)
    var enteredTitle by mutableStateOf("")
    val isEnteredTitleOk get() = enteredTitle.isNotEmpty()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllExperiences()
                .collect { es ->
                    val cal = Calendar.getInstance(TimeZone.getDefault())
                    _experiencesGrouped.value = es.groupBy { cal.get(Calendar.YEAR).toString() ?: "Unknown Year" }
                }

        }
    }

    fun addButtonTapped() {
        val formatter  = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        enteredTitle = formatter.format(Date()) ?: ""
        isShowingDialog = true
    }

    fun dialogConfirmTapped(onSuccess: () -> Unit) {
        if (enteredTitle.isNotEmpty()) {
            val newExperience = Experience(title = enteredTitle, creationDate = Date(), text = "")
            viewModelScope.launch { repository.addExperience(newExperience) }
            isShowingDialog = false
            onSuccess()
        }
    }

}