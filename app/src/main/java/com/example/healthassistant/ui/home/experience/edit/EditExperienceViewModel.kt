package com.example.healthassistant.ui.home.experience.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.entities.Experience
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.ui.main.routers.EXPERIENCE_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class EditExperienceViewModel @Inject constructor(
    private val repository: ExperienceRepository,
    state: SavedStateHandle
) :
    ViewModel() {

    var experience: Experience? = null
    var enteredTitle by mutableStateOf("")
    val isEnteredTitleOk get() = enteredTitle.isNotEmpty()
    var enteredText by mutableStateOf("")
    private val calendar: Calendar = Calendar.getInstance()
    var year by mutableStateOf(1)
    var month by mutableStateOf(1)
    var day by mutableStateOf(1)
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
        val id = state.get<Int>(EXPERIENCE_ID_KEY)!!
        viewModelScope.launch {
            experience = repository.getExperience(id = id)!!
            calendar.time = experience!!.date
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)
            enteredTitle = experience!!.title
            enteredText = experience!!.text
        }
    }

    fun onSubmitDate(newDay: Int, newMonth: Int, newYear: Int) {
        day = newDay
        month = newMonth
        year = newYear
    }

    fun onDoneTap() {
        if (enteredTitle.isNotEmpty()) {
            viewModelScope.launch {
                experience!!.title = enteredTitle
                experience!!.date = currentlySelectedDate
                experience!!.text = enteredText
                repository.updateExperience(experience = experience!!)
            }
        }
    }

}