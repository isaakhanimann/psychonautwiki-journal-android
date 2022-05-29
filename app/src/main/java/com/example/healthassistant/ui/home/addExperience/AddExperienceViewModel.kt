package com.example.healthassistant.ui.home.addExperience

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.experiences.ExperienceRepository
import com.example.healthassistant.data.experiences.entities.Experience
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class AddExperienceViewModel @Inject constructor(private val repository: ExperienceRepository) :
    ViewModel() {

    var title by mutableStateOf("")
    var notes by mutableStateOf("")
    val isTitleOk get() = title.isNotEmpty()
    private val calendar: Calendar = Calendar.getInstance()
    var year by mutableStateOf(calendar.get(Calendar.YEAR))
    var month by mutableStateOf(calendar.get(Calendar.MONTH))
    var day by mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH))
    private val currentlySelectedDate: Date
        get() {
            calendar.set(year, month, day)
            return calendar.time
        }
    val dateString: String
        get() {
            val formatter = SimpleDateFormat("EEE dd MMM yyyy", Locale.getDefault())
            return formatter.format(currentlySelectedDate) ?: "Unknown"
        }

    init {
        title = dateString
    }

    fun onSubmitDate(newDay: Int, newMonth: Int, newYear: Int) {
        day = newDay
        month = newMonth
        year = newYear
        title = dateString
    }

    fun onConfirmTap(onSuccess: (Int) -> Unit) {
        if (title.isNotEmpty()) {
            viewModelScope.launch {
                val newExperience =
                    Experience(
                        title = title,
                        date = currentlySelectedDate,
                        text = notes
                    )
                val experienceId = repository.addExperience(newExperience)
                onSuccess(experienceId.toInt())
            }
        }
    }

}