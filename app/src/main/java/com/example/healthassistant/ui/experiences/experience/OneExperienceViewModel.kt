package com.example.healthassistant.ui.experiences.experience

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.room.experiences.entities.Sentiment
import com.example.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.example.healthassistant.data.substances.DoseClass
import com.example.healthassistant.data.substances.RoaDose
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import com.example.healthassistant.ui.main.routers.EXPERIENCE_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    var isShowingSentimentDialog by mutableStateOf(false)

    fun showEditSentimentDialog() {
        isShowingSentimentDialog = true
    }

    fun dismissEditSentimentDialog() {
        isShowingSentimentDialog = false
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
        experienceRepo.getExperienceWithIngestions(experienceId = state.get<Int>(EXPERIENCE_ID_KEY)!!)
            .stateIn(
                initialValue = null,
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000)
            )

    private val sortedIngestionsWithCompanionsFlow =
        experienceWithIngestionsFlow.map { experience ->
            experience?.ingestionsWithCompanions?.sortedBy { it.ingestion.time } ?: emptyList()
        }

    data class IngestionWithAssociatedData(
        val ingestionWithCompanion: IngestionWithCompanion,
        val roaDuration: RoaDuration?,
        val roaDose: RoaDose?,
        val doseClass: DoseClass?
    )

    val ingestionsWithAssociatedDataFlow: StateFlow<List<IngestionWithAssociatedData>> =
        sortedIngestionsWithCompanionsFlow.map { ingestionsWithComps ->
            ingestionsWithComps.map { oneIngestionWithComp ->
                val ingestion = oneIngestionWithComp.ingestion
                val roa = substanceRepo.getSubstance(oneIngestionWithComp.ingestion.substanceName)
                    ?.getRoa(ingestion.administrationRoute)
                val roaDuration = roa?.roaDuration
                val roaDose = roa?.roaDose
                IngestionWithAssociatedData(
                    ingestionWithCompanion = oneIngestionWithComp,
                    roaDuration = roaDuration,
                    roaDose = roaDose,
                    doseClass = roaDose?.getDoseClass(
                        ingestionDose = ingestion.dose,
                        ingestionUnits = ingestion.units
                    )
                )
            }
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    val firstDateTextFlow: StateFlow<String?> = sortedIngestionsWithCompanionsFlow.map {
        val date = it.firstOrNull()?.ingestion?.time ?: return@map null
        getDateText(date)
    }.stateIn(
        initialValue = null,
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

    fun deleteExperience() {
        viewModelScope.launch {
            experienceWithIngestionsFlow.value?.experience?.let {
                experienceRepo.delete(it)
            }
        }
    }

    companion object {
        private fun getCumulativeDoses(ingestions: List<IngestionWithAssociatedData>): List<CumulativeDose> {
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

        fun getDateText(date: Date): String {
            val formatter = SimpleDateFormat("EEE, dd MMMM yyyy", Locale.getDefault())
            return formatter.format(date)
        }
    }
}