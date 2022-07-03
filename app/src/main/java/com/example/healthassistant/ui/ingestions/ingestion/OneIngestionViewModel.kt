package com.example.healthassistant.ui.ingestions.ingestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.room.experiences.entities.Experience
import com.example.healthassistant.data.room.experiences.entities.Sentiment
import com.example.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.example.healthassistant.data.substances.RoaDose
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import com.example.healthassistant.ui.main.routers.INGESTION_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OneIngestionViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    private val substanceRepo: SubstanceRepository,
    state: SavedStateHandle
) : ViewModel() {

    var isShowingDeleteDialog by mutableStateOf(false)

    var isShowingSentimentMenu by mutableStateOf(false)

    fun showEditSentimentDialog() {
        isShowingSentimentMenu = true
    }

    fun dismissEditSentimentDialog() {
        isShowingSentimentMenu = false
    }

    fun saveSentiment(sentiment: Sentiment?) {
        val ingestion =
            ingestionWithCompanionDurationAndExperience.value?.ingestionWithCompanion?.ingestion
        if (ingestion != null) {
            viewModelScope.launch {
                ingestion.sentiment = sentiment
                experienceRepo.update(ingestion)
            }
        }
    }

    data class IngestionWithCompanionDurationAndExperience(
        val ingestionWithCompanion: IngestionWithCompanion,
        val roaDuration: RoaDuration?,
        val roaDose: RoaDose?,
        val experience: Experience?
    )

    private val ingestionId = state.get<Int>(INGESTION_ID_KEY)!!
    private val ingestionFlow: Flow<IngestionWithCompanion?> =
        experienceRepo.getIngestionWithCompanionFlow(ingestionId)
    val ingestionWithCompanionDurationAndExperience: StateFlow<IngestionWithCompanionDurationAndExperience?> =
        ingestionFlow.map { ingestionWithCompanion ->
            val ingestion = ingestionWithCompanion?.ingestion
            val experience = ingestion?.experienceId.let {
                if (it == null) {
                    null
                } else {
                    experienceRepo.getExperience(
                        it
                    )
                }
            }
            if (ingestionWithCompanion != null && ingestion != null) {
                val roa = substanceRepo.getSubstance(ingestion.substanceName)
                    ?.getRoa(ingestion.administrationRoute)
                IngestionWithCompanionDurationAndExperience(
                    ingestionWithCompanion = ingestionWithCompanion,
                    roaDuration = roa?.roaDuration,
                    roaDose = roa?.roaDose,
                    experience = experience
                )
            } else {
                null
            }
        }.stateIn(
            initialValue = null,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    fun deleteIngestion() {
        viewModelScope.launch {
            ingestionWithCompanionDurationAndExperience.value?.ingestionWithCompanion.let {
                assert(it != null)
                if (it != null) {
                    experienceRepo.delete(ingestion = it.ingestion)
                }
            }
        }
    }
}