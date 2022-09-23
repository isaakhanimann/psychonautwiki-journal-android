package com.isaakhanimann.healthassistant.ui.addingestion.time

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Experience
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.healthassistant.data.room.experiences.relations.ExperienceWithIngestions
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import com.isaakhanimann.healthassistant.data.substances.classes.Substance
import com.isaakhanimann.healthassistant.data.substances.repositories.SubstanceRepository
import com.isaakhanimann.healthassistant.ui.main.routers.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

const val hourLimitToSeparateIngestions = 12

@HiltViewModel
class ChooseTimeViewModel @Inject constructor(
    substanceRepo: SubstanceRepository,
    private val experienceRepo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {
    private val substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!
    val substance: Substance?
    private val calendar = Calendar.getInstance()
    val dateAndTimeFlow = MutableStateFlow(
        DateAndTime(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH),
            day = calendar.get(Calendar.DAY_OF_MONTH),
            hour = calendar.get(Calendar.HOUR_OF_DAY),
            minute = calendar.get(Calendar.MINUTE)
        )
    )

    private val sortedExperiencesFlow = experienceRepo.getSortedExperiencesWithIngestionsFlow()

    private val experienceWithIngestionsToAddToFlow: Flow<ExperienceWithIngestions?> =
        sortedExperiencesFlow.combine(dateAndTimeFlow) { sortedExperiences, relevantDateFields ->
            val selectedDate = relevantDateFields.currentlySelectedDate
            return@combine sortedExperiences.firstOrNull { experience ->
                val sortedIngestions = experience.ingestions.sortedBy { it.time }
                val firstIngestionTime = sortedIngestions.firstOrNull()?.time
                val lastIngestionTime = sortedIngestions.lastOrNull()?.time
                val cal = Calendar.getInstance(TimeZone.getDefault())
                cal.time = selectedDate
                cal.add(Calendar.HOUR_OF_DAY, -hourLimitToSeparateIngestions)
                val selectedDateMinusLimit = cal.time
                cal.time = selectedDate
                cal.add(Calendar.HOUR_OF_DAY, hourLimitToSeparateIngestions)
                val selectedDatePlusLimit = cal.time
                return@firstOrNull selectedDateMinusLimit < lastIngestionTime && selectedDatePlusLimit > firstIngestionTime
            }
        }

    val experienceTitleToAddToFlow: StateFlow<String?> =
        experienceWithIngestionsToAddToFlow.map { it?.experience?.title }.stateIn(
            initialValue = null,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )


    val userWantsToContinueSameExperienceFlow = MutableStateFlow(true)

    fun toggleCheck(userWantsToContinueSameExperience: Boolean) {
        viewModelScope.launch {
            userWantsToContinueSameExperienceFlow.emit(userWantsToContinueSameExperience)
        }
    }

    var isLoadingColor by mutableStateOf(true)
    var isShowingColorPicker by mutableStateOf(false)
    var selectedColor by mutableStateOf(SubstanceColor.BLUE)
    var note by mutableStateOf("")

    val previousNotesFlow: StateFlow<List<String>> =
        experienceRepo.getSortedIngestionsFlow(substanceName, limit = 10).map { list ->
            list.mapNotNull { it.notes }.filter { it.isNotBlank() }.distinct()
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    private val administrationRoute: AdministrationRoute
    private val dose: Double?
    private val units: String?
    private val isEstimate: Boolean
    private var substanceCompanion: SubstanceCompanion? = null

    private val companionFlow = experienceRepo.getAllSubstanceCompanionsFlow()

    val alreadyUsedColorsFlow: StateFlow<List<SubstanceColor>> =
        companionFlow.map { companions ->
            companions.map { it.color }.distinct()
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    val otherColorsFlow: StateFlow<List<SubstanceColor>> =
        alreadyUsedColorsFlow.map { alreadyUsedColors ->
            SubstanceColor.values().filter {
                !alreadyUsedColors.contains(it)
            }
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    private val newExperienceIdToUseFlow: Flow<Int> = sortedExperiencesFlow.map { experiences ->
        val previousMax = experiences.maxOfOrNull { it.experience.id } ?: 1
        return@map previousMax + 1
    }

    init {
        substance = substanceRepo.getSubstance(substanceName)
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
        val hour = dateAndTimeFlow.value.hour
        val minute = dateAndTimeFlow.value.minute
        viewModelScope.launch {
            dateAndTimeFlow.emit(
                DateAndTime(
                    day = newDay,
                    month = newMonth,
                    year = newYear,
                    hour = hour,
                    minute = minute
                )
            )
        }
    }

    fun onSubmitTime(newHour: Int, newMinute: Int) {
        val year = dateAndTimeFlow.value.year
        val month = dateAndTimeFlow.value.month
        val day = dateAndTimeFlow.value.day
        viewModelScope.launch {
            dateAndTimeFlow.emit(
                DateAndTime(
                    day = day,
                    month = month,
                    year = year,
                    hour = newHour,
                    minute = newMinute
                )
            )
        }
    }

    fun createAndSaveIngestion() {
        viewModelScope.launch {
            val newIdToUse = newExperienceIdToUseFlow.firstOrNull() ?: 1
            val oldIdToUse = experienceWithIngestionsToAddToFlow.firstOrNull()?.experience?.id
            val userWantsToCreateANewExperience = !(userWantsToContinueSameExperienceFlow.firstOrNull() ?: true)
            val substanceCompanion = SubstanceCompanion(
                substanceName,
                color = selectedColor
            )
            val ingestionTime = dateAndTimeFlow.first().currentlySelectedDate
            if (userWantsToCreateANewExperience || oldIdToUse == null) {
                val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                val newExperience = Experience(
                    id = newIdToUse,
                    title = formatter.format(ingestionTime),
                    text = "",
                    creationDate = Date(),
                    sentiment = null
                )
                val newIngestion = Ingestion(
                    substanceName = substanceName,
                    time = ingestionTime,
                    administrationRoute = administrationRoute,
                    dose = dose,
                    isDoseAnEstimate = isEstimate,
                    units = units,
                    experienceId = newExperience.id,
                    notes = note,
                    sentiment = null
                )
                experienceRepo.insertIngestionExperienceAndCompanion(
                    ingestion = newIngestion,
                    experience = newExperience,
                    substanceCompanion = substanceCompanion
                )

            } else {
                val newIngestion = Ingestion(
                    substanceName = substanceName,
                    time = ingestionTime,
                    administrationRoute = administrationRoute,
                    dose = dose,
                    isDoseAnEstimate = isEstimate,
                    units = units,
                    experienceId = oldIdToUse,
                    notes = note,
                    sentiment = null
                )
                experienceRepo.insertIngestionAndCompanion(
                    ingestion = newIngestion,
                    substanceCompanion = substanceCompanion
                )
            }
        }
    }
}

data class DateAndTime(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
) {
    val currentlySelectedDate: Date
        get() {
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day, hour, minute)
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
}