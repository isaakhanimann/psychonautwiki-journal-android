package com.isaakhanimann.journal.ui.addingestion.time

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.Experience
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.journal.data.room.experiences.relations.ExperienceWithIngestions
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.main.routers.*
import com.isaakhanimann.journal.ui.utils.getInstant
import com.isaakhanimann.journal.ui.utils.getStringOfPattern
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

const val hourLimitToSeparateIngestions: Long = 12

@HiltViewModel
class ChooseTimeViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {
    private val substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!
    val localDateTimeFlow = MutableStateFlow(LocalDateTime.now())

    private val sortedExperiencesFlow = experienceRepo.getSortedExperiencesWithIngestionsFlow()

    private val experienceWithIngestionsToAddToFlow: Flow<ExperienceWithIngestions?> =
        sortedExperiencesFlow.combine(localDateTimeFlow) { sortedExperiences, localDateTime ->
            val selectedInstant = localDateTime.getInstant()
            return@combine sortedExperiences.firstOrNull { experience ->
                val sortedIngestions = experience.ingestions.sortedBy { it.time }
                val firstIngestionTime =
                    sortedIngestions.firstOrNull()?.time ?: return@firstOrNull false
                val lastIngestionTime =
                    sortedIngestions.lastOrNull()?.time ?: return@firstOrNull false
                val selectedDateMinusLimit =
                    selectedInstant.minus(hourLimitToSeparateIngestions, ChronoUnit.HOURS)
                val selectedDatePlusLimit =
                    selectedInstant.plus(hourLimitToSeparateIngestions, ChronoUnit.HOURS)
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
    var selectedColor by mutableStateOf(AdaptiveColor.BLUE)
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

    val alreadyUsedColorsFlow: StateFlow<List<AdaptiveColor>> =
        companionFlow.map { companions ->
            companions.map { it.color }.distinct()
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    val otherColorsFlow: StateFlow<List<AdaptiveColor>> =
        alreadyUsedColorsFlow.map { alreadyUsedColors ->
            AdaptiveColor.values().filter {
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
        viewModelScope.launch {
            val allCompanions = experienceRepo.getAllSubstanceCompanionsFlow().first()
            val thisCompanion = allCompanions.firstOrNull { it.substanceName == substanceName }
            substanceCompanion = thisCompanion
            if (thisCompanion == null) {
                isShowingColorPicker = true
                val alreadyUsedColors = allCompanions.map { it.color }
                val otherColors = AdaptiveColor.values().filter { !alreadyUsedColors.contains(it) }
                selectedColor = otherColors.randomOrNull() ?: AdaptiveColor.values().random()
            } else {
                selectedColor = thisCompanion.color
            }
            isLoadingColor = false
        }
    }

    fun onChangeDateOrTime(newLocalDateTime: LocalDateTime) {
        viewModelScope.launch {
            localDateTimeFlow.emit(
                newLocalDateTime
            )
        }
    }

    fun createAndSaveIngestion() {
        viewModelScope.launch {
            val newIdToUse = newExperienceIdToUseFlow.firstOrNull() ?: 1
            val oldIdToUse = experienceWithIngestionsToAddToFlow.firstOrNull()?.experience?.id
            val userWantsToCreateANewExperience =
                !(userWantsToContinueSameExperienceFlow.firstOrNull() ?: true)
            val substanceCompanion = SubstanceCompanion(
                substanceName,
                color = selectedColor
            )
            val ingestionTime = localDateTimeFlow.first().atZone(ZoneId.systemDefault()).toInstant()
            if (userWantsToCreateANewExperience || oldIdToUse == null) {
                val newExperience = Experience(
                    id = newIdToUse,
                    title = ingestionTime.getStringOfPattern("dd MMMM yyyy"),
                    text = "",
                    creationDate = Instant.now(),
                    sortDate = ingestionTime
                )
                val newIngestion = Ingestion(
                    substanceName = substanceName,
                    time = ingestionTime,
                    administrationRoute = administrationRoute,
                    dose = dose,
                    isDoseAnEstimate = isEstimate,
                    units = units,
                    experienceId = newExperience.id,
                    notes = note
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
                    notes = note
                )
                experienceRepo.insertIngestionAndCompanion(
                    ingestion = newIngestion,
                    substanceCompanion = substanceCompanion
                )
            }
        }
    }
}