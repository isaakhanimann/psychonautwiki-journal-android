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
    private val calendar: Calendar = Calendar.getInstance()
    var year by mutableStateOf(calendar.get(Calendar.YEAR))
    var month by mutableStateOf(calendar.get(Calendar.MONTH))
    var day by mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH))
    private val currentlySelectedDate: Date get() {
        calendar.set(year, month, day)
        return calendar.time
    }
    val dateString: String get() {
        val formatter = SimpleDateFormat("EEE dd MMM yyyy", Locale.getDefault())
        return formatter.format(currentlySelectedDate) ?: "Unknown"
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllExperiences()
                .collect { experiences ->
                    val cal = Calendar.getInstance(TimeZone.getDefault())
                    _experiencesGrouped.value = experiences.groupBy { cal.get(Calendar.YEAR).toString() }
                }

        }
    }

    fun onSubmitDate(newDay: Int, newMonth: Int, newYear: Int) {
        day = newDay
        month = newMonth
        year = newYear
        enteredTitle = dateString
    }

    fun addButtonTapped() {
        enteredTitle = dateString
        isShowingDialog = true
    }

    fun dialogConfirmTapped(onSuccess: () -> Unit) {
        if (enteredTitle.isNotEmpty()) {
            val newExperience = Experience(title = enteredTitle, creationDate = currentlySelectedDate, text = "")
            viewModelScope.launch { repository.addExperience(newExperience) }
            isShowingDialog = false
            onSuccess()
        }
    }

}