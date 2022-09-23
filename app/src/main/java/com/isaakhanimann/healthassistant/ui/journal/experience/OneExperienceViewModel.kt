package com.isaakhanimann.healthassistant.ui.journal.experience

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Sentiment
import com.isaakhanimann.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.healthassistant.data.substances.classes.roa.DoseClass
import com.isaakhanimann.healthassistant.data.substances.classes.roa.RoaDose
import com.isaakhanimann.healthassistant.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.healthassistant.data.substances.repositories.SubstanceRepository
import com.isaakhanimann.healthassistant.ui.main.routers.EXPERIENCE_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OneExperienceViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    private val substanceRepo: SubstanceRepository,
    state: SavedStateHandle
) : ViewModel() {


    var isShowingDeleteDialog by mutableStateOf(false)

    var isShowingSentimentMenu by mutableStateOf(false)

    fun showEditSentimentMenu() {
        isShowingSentimentMenu = true
    }

    fun dismissEditSentimentMenu() {
        isShowingSentimentMenu = false
    }

    fun saveSentiment(sentiment: Sentiment?) {
        val experience = experienceWithIngestionsFlow.value?.experience
        if (experience != null) {
            viewModelScope.launch {
                experience.sentiment = sentiment
                experienceRepo.update(experience = experience)
            }
        }
    }

    val experienceWithIngestionsFlow =
        experienceRepo.getExperienceWithIngestionsAndCompanionsFlow(experienceId = state.get<Int>(EXPERIENCE_ID_KEY)!!)
            .stateIn(
                initialValue = null,
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000)
            )

    private val sortedIngestionsWithCompanionsFlow = experienceWithIngestionsFlow.map { experience ->
        experience?.ingestionsWithCompanions?.sortedBy { it.ingestion.time } ?: emptyList()
    }

    private data class IngestionWithAssociatedData(
        val ingestionWithCompanion: IngestionWithCompanion,
        val roaDuration: RoaDuration?,
        val roaDose: RoaDose?
    )

    private val ingestionsWithAssociatedDataFlow: Flow<List<IngestionWithAssociatedData>> =
        sortedIngestionsWithCompanionsFlow.map { ingestionsWithComps ->
            ingestionsWithComps.map { oneIngestionWithComp ->
                val ingestion = oneIngestionWithComp.ingestion
                val roa = substanceRepo.getSubstance(oneIngestionWithComp.ingestion.substanceName)
                    ?.getRoa(ingestion.administrationRoute)
                val roaDuration = roa?.roaDuration
                IngestionWithAssociatedData(
                    ingestionWithCompanion = oneIngestionWithComp,
                    roaDuration = roaDuration,
                    roaDose = roa?.roaDose
                )
            }
        }

    val ingestionElementsFlow = ingestionsWithAssociatedDataFlow.map {
        getIngestionElements(it)
    }.stateIn(
        initialValue = emptyList(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    val cumulativeDosesFlow = ingestionsWithAssociatedDataFlow.map {
        getCumulativeDoses(it)
    }.stateIn(
        initialValue = emptyList(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    data class CumulativeDose(
        val substanceName: String,
        val cumulativeDose: Double,
        val units: String?,
        val isEstimate: Boolean,
        val doseClass: DoseClass?
    )

    data class IngestionElement(
        val dateText: String?,
        val ingestionWithCompanion: IngestionWithCompanion,
        val roaDuration: RoaDuration?,
        val doseClass: DoseClass?
    )

    private fun getIngestionElements(sortedIngestionsWith: List<IngestionWithAssociatedData>): List<IngestionElement> {
        return sortedIngestionsWith.mapIndexed { index, ingestionWith ->
            val currentIngestionTime = ingestionWith.ingestionWithCompanion.ingestion.time
            val currentIngestionTimeText = getFullDateText(currentIngestionTime)
            val timeText = if (index == 0) {
                currentIngestionTimeText
            } else {
                val previousIngestionTimeText =
                    getFullDateText(sortedIngestionsWith[index - 1].ingestionWithCompanion.ingestion.time)
                val isCurrentSameAsPrevious = previousIngestionTimeText == currentIngestionTimeText
                if (isCurrentSameAsPrevious) {
                    null
                } else {
                    currentIngestionTimeText
                }
            }
            val doseClass =
                ingestionWith.roaDose?.getDoseClass(
                    ingestionWith.ingestionWithCompanion.ingestion.dose,
                    ingestionUnits = ingestionWith.ingestionWithCompanion.ingestion.units
                )
            IngestionElement(
                dateText = timeText,
                ingestionWithCompanion = ingestionWith.ingestionWithCompanion,
                roaDuration = ingestionWith.roaDuration,
                doseClass = doseClass
            )
        }
    }

    private fun getFullDateText(date: Date): String {
        val formatter = SimpleDateFormat("EEE, dd MMMM yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    fun deleteExperience() {
        viewModelScope.launch {
            experienceWithIngestionsFlow.firstOrNull()?.let {
                experienceRepo.delete(it)
            }
        }
    }

    private companion object {
        fun getCumulativeDoses(ingestions: List<IngestionWithAssociatedData>): List<CumulativeDose> {
            return ingestions.groupBy { it.ingestionWithCompanion.ingestion.substanceName }
                .mapNotNull { grouped ->
                    val groupedIngestions = grouped.value
                    if (groupedIngestions.size <= 1) return@mapNotNull null
                    if (groupedIngestions.any { it.ingestionWithCompanion.ingestion.dose == null }) return@mapNotNull null
                    val units = groupedIngestions.first().ingestionWithCompanion.ingestion.units
                    if (groupedIngestions.any { it.ingestionWithCompanion.ingestion.units != units }) return@mapNotNull null
                    val isEstimate =
                        groupedIngestions.any { it.ingestionWithCompanion.ingestion.isDoseAnEstimate }
                    val cumulativeDose =
                        groupedIngestions.mapNotNull { it.ingestionWithCompanion.ingestion.dose }
                            .sum()
                    val doseClass = groupedIngestions.first().roaDose?.getDoseClass(
                        ingestionDose = cumulativeDose,
                        ingestionUnits = units
                    )
                    CumulativeDose(
                        substanceName = grouped.key,
                        cumulativeDose = cumulativeDose,
                        units = units,
                        isEstimate = isEstimate,
                        doseClass = doseClass
                    )
                }
        }
    }
}