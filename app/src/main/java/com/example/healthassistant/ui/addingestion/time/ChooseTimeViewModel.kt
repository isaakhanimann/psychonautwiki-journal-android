package com.example.healthassistant.ui.addingestion.time

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.room.experiences.entities.SubstanceColor
import com.example.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import com.example.healthassistant.ui.main.routers.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
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
    val substance: Substance?
    private val calendar: Calendar = Calendar.getInstance()
    val year = mutableStateOf(calendar.get(Calendar.YEAR))
    val month = mutableStateOf(calendar.get(Calendar.MONTH))
    val day = mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH))
    val hour = mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY))
    val minute = mutableStateOf(calendar.get(Calendar.MINUTE))
    private val currentlySelectedDate: Date
        get() {
            calendar.set(year.value, month.value, day.value, hour.value, minute.value)
            return calendar.time
        }
    val dateString: String
        get() {
            val formatter = SimpleDateFormat("EEE dd MMM yyyy", Locale.getDefault())
            return formatter.format(currentlySelectedDate) ?: "Unknown"
        }
    val timeString: String
        get() {
            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            return formatter.format(currentlySelectedDate) ?: "Unknown"
        }

    var isLoadingColor by mutableStateOf(true)
    var isShowingColorPicker by mutableStateOf(false)
    var selectedColor by mutableStateOf(SubstanceColor.BLUE)

    private val substanceName: String
    private val administrationRoute: AdministrationRoute
    private val dose: Double?
    private val units: String?
    private val isEstimate: Boolean
    private var substanceCompanion: SubstanceCompanion? = null

    init {
        substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!
        substance = substanceRepo.getSubstance(substanceName)
        experienceId = state.get<String>(EXPERIENCE_ID_KEY)?.toIntOrNull()
        val routeString = state.get<String>(ADMINISTRATION_ROUTE_KEY)!!
        administrationRoute = AdministrationRoute.valueOf(routeString)
        dose = state.get<String>(DOSE_KEY)?.toDoubleOrNull()
        units = state.get<String>(UNITS_KEY)?.let {
            if (it == "null") {
                null
            } else {
                it
            }
        }
        isEstimate = state.get<Boolean>(IS_ESTIMATE_KEY)!!
        assert(substance != null)
        viewModelScope.launch {
            val allCompanions = experienceRepo.getAllSubstanceCompanionsFlow().first()
            val thisCompanion = allCompanions.firstOrNull { it.substanceName == substanceName }
            substanceCompanion = thisCompanion
            if (thisCompanion == null) {
                isShowingColorPicker = true
                val alreadyUsedColors = allCompanions.map { it.color }
                val otherColors = SubstanceColor.values().filter { !alreadyUsedColors.contains(it) }
                selectedColor = otherColors.randomOrNull() ?: SubstanceColor.values().random()
            } else {
                selectedColor = thisCompanion.color
            }
            isLoadingColor = false
        }
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

    fun createAndSaveIngestion() {
        viewModelScope.launch {
            val newIngestion = Ingestion(
                substanceName = substanceName,
                time = currentlySelectedDate,
                administrationRoute = administrationRoute,
                dose = dose,
                isDoseAnEstimate = isEstimate,
                units = units,
                experienceId = experienceId,
                notes = null
            )
            experienceRepo.insert(newIngestion)
            substanceCompanion.let {
                if (it != null && it.color != selectedColor) {
                    it.color = selectedColor
                    experienceRepo.update(it)
                } else if (it == null) {
                    val substanceCompanion = SubstanceCompanion(
                        substanceName,
                        color = selectedColor
                    )
                    experienceRepo.insert(substanceCompanion)
                }
            }
        }
    }
}