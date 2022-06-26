package com.example.healthassistant.ui.experiences.experience

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import com.example.healthassistant.ui.main.routers.EXPERIENCE_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ExperienceViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    private val substanceRepo: SubstanceRepository,
    state: SavedStateHandle
) : ViewModel() {


    var isShowingDeleteDialog by mutableStateOf(false)

    val experienceWithIngestionsFlow =
        experienceRepo.getExperienceWithIngestions(experienceId = state.get<Int>(EXPERIENCE_ID_KEY)!!)
            .stateIn(
                initialValue = null,
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000)
            )

    private val ingestionsFlow = experienceWithIngestionsFlow.map {
        it?.ingestions ?: emptyList()
    }

    val ingestionDurationPairsFlow = ingestionsFlow.map {
        it.map { ing ->
            val roaDuration = substanceRepo.getSubstance(ing.substanceName)
                ?.getRoa(ing.administrationRoute)?.roaDuration
            Pair(first = ing, second = roaDuration)
        }
    }.stateIn(
        initialValue = emptyList(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    val ingestionElementsFlow = ingestionsFlow.map {
        getIngestionElements(it)
    }.stateIn(
        initialValue = emptyList(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    val cumulativeDosesFlow = ingestionsFlow.map {
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
        val isEstimate: Boolean
    )

    data class IngestionElement(
        val dateText: String?,
        val ingestion: Ingestion
    )

    private fun getIngestionElements(sortedIngestions: List<Ingestion>): List<IngestionElement> {
        return sortedIngestions.mapIndexed { index, ingestion ->
            val ingestionTimeText = getDateText(ingestion.time)
            if (index == 0) {
                IngestionElement(dateText = ingestionTimeText, ingestion = ingestion)
            } else {
                val lastIngestionDateText = getDateText(sortedIngestions[index - 1].time)
                if (lastIngestionDateText != ingestionTimeText) {
                    IngestionElement(dateText = ingestionTimeText, ingestion = ingestion)
                } else {
                    IngestionElement(dateText = null, ingestion = ingestion)
                }
            }
        }
    }

    private fun getDateText(date: Date): String {
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    fun deleteExperience() {
        viewModelScope.launch {
            experienceWithIngestionsFlow.value?.experience?.let {
                experienceRepo.deleteExperience(it)
            }
        }
    }

    companion object {
        fun getCumulativeDoses(ingestions: List<Ingestion>): List<CumulativeDose> {
            return ingestions.groupBy { it.substanceName }.mapNotNull { grouped ->
                val groupedIngestions = grouped.value
                if (groupedIngestions.size <= 1) return@mapNotNull null
                if (groupedIngestions.any { it.dose == null }) return@mapNotNull null
                val units = groupedIngestions.first().units
                if (groupedIngestions.any { it.units != units }) return@mapNotNull null
                val isEstimate = groupedIngestions.any { it.isDoseAnEstimate }
                val cumulativeDose = groupedIngestions.mapNotNull { it.dose }.sum()
                CumulativeDose(
                    substanceName = grouped.key,
                    cumulativeDose = cumulativeDose,
                    units = units,
                    isEstimate = isEstimate
                )
            }
        }
    }
}