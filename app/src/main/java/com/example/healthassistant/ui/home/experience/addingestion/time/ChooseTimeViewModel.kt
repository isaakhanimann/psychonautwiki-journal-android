package com.example.healthassistant.ui.home.experience.addingestion.time

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.experiences.entities.Experience
import com.example.healthassistant.data.experiences.entities.Ingestion
import com.example.healthassistant.data.experiences.entities.IngestionColor
import com.example.healthassistant.data.experiences.repositories.ExperienceRepository
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import com.example.healthassistant.ui.main.routers.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChooseTimeViewModel @Inject constructor(
    substanceRepo: SubstanceRepository,
    private val experienceRepo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {
    val experienceId: Int?
    val latestExperience: Experience?
    val substance: Substance?
    private val calendar: Calendar = Calendar.getInstance()
    val year = mutableStateOf(calendar.get(Calendar.YEAR))
    val month = mutableStateOf(calendar.get(Calendar.MONTH))
    val day = mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH))
    val hour = mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY))
    val minute = mutableStateOf(calendar.get(Calendar.MINUTE))

    private val substanceName: String
    private val administrationRoute: AdministrationRoute
    private val dose: Double?
    private val units: String
    private val isEstimate: Boolean
    private val color: IngestionColor

    init {
        substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!
        substance = substanceRepo.getSubstance(substanceName)
        experienceId = state.get<String>(EXPERIENCE_ID_KEY)?.toIntOrNull()
        latestExperience = if (experienceId == null) {
            experienceRepo.getLastExperiences(limit = 1).firstOrNull()
        } else {
            null
        }
        val routeString = state.get<String>(ADMINISTRATION_ROUTE_KEY)!!
        administrationRoute = AdministrationRoute.valueOf(routeString)
        dose = state.get<String>(DOSE_KEY)?.toDoubleOrNull()
        units = state.get<String>(UNITS_KEY)!!
        isEstimate = state.get<Boolean>(IS_ESTIMATE_KEY)!!
        val colorName = state.get<String>(COLOR_KEY)!!
        color = IngestionColor.valueOf(colorName)
        assert(substance != null)
    }

    fun onSubmitDate(newDay: Int, newMonth: Int, newYear: Int) {
        day.value = newDay
        month.value = newMonth
        year.value = newYear
    }

    fun onSubmitTime(newHour: Int, newMinute: Int) {
        hour.value = newHour
        minute.value = newMinute
    }

    fun addIngestion(experienceIdToAddTo: Int) {
        viewModelScope.launch {
            val newIngestion = createNewIngestion(experienceIdToAddTo)
            experienceRepo.addIngestion(newIngestion)
        }
    }

    private fun createNewIngestion(experienceId: Int): Ingestion {
        calendar.set(year.value, month.value, day.value, hour.value, minute.value)
        val ingestionDate = calendar.time
        return Ingestion(
            substanceName = substanceName,
            time = ingestionDate,
            administrationRoute = administrationRoute,
            dose = dose,
            isDoseAnEstimate = isEstimate,
            units = units,
            color = color,
            experienceId = experienceId
        )
    }

    fun addIngestionToNewExperience() {
        viewModelScope.launch {
            val newExperience = createNewExperience()
            val newIngestion = createNewIngestion(experienceId = newExperience.id)
            experienceRepo.addExperience(newExperience)
            experienceRepo.addIngestion(newIngestion)
        }
    }

    private fun createNewExperience(): Experience {
        calendar.set(year.value, month.value, day.value, hour.value, minute.value)
        val experienceDate = calendar.time
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val title = formatter.format(experienceDate) ?: "Title"
        return Experience(title = title, creationDate = experienceDate, text = "")
    }
}