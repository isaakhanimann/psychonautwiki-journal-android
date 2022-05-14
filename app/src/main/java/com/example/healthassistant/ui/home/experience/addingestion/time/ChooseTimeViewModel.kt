package com.example.healthassistant.ui.home.experience.addingestion.time

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.healthassistant.data.experiences.entities.Experience
import com.example.healthassistant.data.experiences.repositories.ExperienceRepository
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import com.example.healthassistant.ui.main.routers.*
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChooseTimeViewModel @Inject constructor(
    substanceRepo: SubstanceRepository,
    experienceRepo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {
    val experienceId: Int?
    val latestExperience: Experience?
    val substance: Substance?
    val calendar = Calendar.getInstance()
    val year = mutableStateOf(calendar.get(Calendar.YEAR))
    val month = mutableStateOf(calendar.get(Calendar.MONTH))
    val day = mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH))
    val hour = mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY))
    val minute = mutableStateOf(calendar.get(Calendar.MINUTE))

    private val administrationRoute: AdministrationRoute?
    private val dose: Double?
    private val units: String?
    private val isEstimate: Boolean?

    init {
        substance = substanceRepo.getSubstance(state.get<String>(SUBSTANCE_NAME_KEY) ?: "LSD")
        experienceId = state.get<String>(EXPERIENCE_ID_KEY)?.toInt()
        latestExperience = if (experienceId==null) {
            experienceRepo.getLastExperiences(limit = 1).firstOrNull()
        } else {
            null
        }
        val routeString = state.get<String>(ADMINISTRATION_ROUTE_KEY)
        administrationRoute = try {
            AdministrationRoute.valueOf(routeString ?: "")
        } catch (e: Exception) {
            null
        }
        val doseText = state.get<String>(DOSE_KEY)
        dose = doseText?.toDouble()
        units = state.get<String>(UNITS_KEY)
        isEstimate = state.get<Boolean>(IS_ESTIMATE_KEY)
        assert(substance != null)
        assert(administrationRoute != null)
        assert(units != null)
        assert(isEstimate != null)
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
        //Todo: create ingestion and add it to repo

    }

    fun addIngestionToNewExperience() {

    }
}