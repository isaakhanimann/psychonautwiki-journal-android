package com.isaakhanimann.healthassistant.ui.stats.substancecompanion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.healthassistant.data.substances.repositories.SubstanceRepository
import com.isaakhanimann.healthassistant.ui.main.routers.SUBSTANCE_NAME_KEY
import com.isaakhanimann.healthassistant.ui.utils.getTimeDifferenceText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SubstanceCompanionViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    substanceRepo: SubstanceRepository,
    state: SavedStateHandle
) : ViewModel() {

    private val currentTimeFlow: Flow<Date> = flow {
        while (true) {
            emit(Date())
            delay(timeMillis = 1000 * 10)
        }
    }

    private val substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!

    private val substance = substanceRepo.getSubstance(substanceName)
    val tolerance = substance?.tolerance
    val crossTolerances = substance?.crossTolerances ?: emptyList()

    val thisCompanionFlow: StateFlow<SubstanceCompanion?> =
        experienceRepo.getSubstanceCompanionFlow(substanceName).stateIn(
            initialValue = null,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    private val sortedIngestionsFlow: Flow<List<Ingestion>> =
        experienceRepo.getSortedIngestionsFlow(substanceName)

    val ingestionBurstsFlow: StateFlow<List<IngestionsBurst>> =
        sortedIngestionsFlow.combine(currentTimeFlow) { sortedIngestions, currentTime ->
            var lastDate = currentTime
            val firstIngestion =
                sortedIngestions.firstOrNull() ?: return@combine emptyList<IngestionsBurst>()
            var diffTextToAdd =
                getTimeDifferenceText(fromDate = firstIngestion.time, toDate = currentTime)
            var currentIngestions: MutableList<Ingestion> = mutableListOf(firstIngestion)
            val allIngestionBursts: MutableList<IngestionsBurst> = mutableListOf()
            if (sortedIngestions.size == 1) {
                allIngestionBursts.add(
                    IngestionsBurst(
                        timeUntil = diffTextToAdd,
                        ingestions = currentIngestions
                    )
                )
            }
            for (oneIngestion in sortedIngestions.takeLast(sortedIngestions.size - 1)) {
                if (isDifferenceBig(fromDate = oneIngestion.time, toDate = lastDate)) {
                    // finalize burst
                    allIngestionBursts.add(
                        IngestionsBurst(
                            timeUntil = diffTextToAdd,
                            ingestions = currentIngestions
                        )
                    )
                    diffTextToAdd =
                        getTimeDifferenceText(fromDate = oneIngestion.time, toDate = lastDate)
                    currentIngestions = mutableListOf(oneIngestion)
                    lastDate = oneIngestion.time
                } else {
                    // add to current burst
                    currentIngestions.add(oneIngestion)
                    lastDate = oneIngestion.time
                }
            }
            // finalize last burst
            if (currentIngestions.isNotEmpty()) {
                allIngestionBursts.add(
                    IngestionsBurst(
                        timeUntil = diffTextToAdd,
                        ingestions = currentIngestions
                    )
                )
            }
            return@combine allIngestionBursts
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    private fun isDifferenceBig(fromDate: Date, toDate: Date): Boolean {
        val diff: Long = toDate.time - fromDate.time
        val seconds = diff / 1000.0
        val minutes = seconds / 60
        val hours = minutes / 60
        return hours > 12
    }

    private val companionsFlow = experienceRepo.getAllSubstanceCompanionsFlow()

    val alreadyUsedColorsFlow: StateFlow<List<SubstanceColor>> =
        companionsFlow.map { companions ->
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


    fun updateColor(color: SubstanceColor) {
        viewModelScope.launch {
            thisCompanionFlow.value?.let {
                it.color = color
                experienceRepo.update(substanceCompanion = it)
            }
        }
    }
}

data class IngestionsBurst(
    val timeUntil: String,
    val ingestions: List<Ingestion>
)