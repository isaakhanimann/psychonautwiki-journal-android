package com.example.healthassistant.ui.addingestion.interactions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.substances.InteractionType
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import com.example.healthassistant.ui.main.routers.SUBSTANCE_NAME_KEY
import com.example.healthassistant.ui.utils.getTimeDifferenceText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@HiltViewModel
class CheckInteractionsViewModel @Inject constructor(
    private val substanceRepo: SubstanceRepository,
    private val experienceRepo: ExperienceRepository,
    state: SavedStateHandle,
) : ViewModel() {
    val substanceName: String
    var isSearchingForInteractions by mutableStateOf(true)
    var dangerousInteractions: List<String> by mutableStateOf(listOf())
    var unsafeInteractions: List<String> by mutableStateOf(listOf())
    var uncertainInteractions: List<String> by mutableStateOf(listOf())
    var isShowingAlert by mutableStateOf(false)
    var alertInteractionType by mutableStateOf<InteractionType?>(null)
    var alertText by mutableStateOf("")
    private var latestIngestionsOfEverySubstanceSinceTwoDays: List<Ingestion> = emptyList()

    init {
        substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!
        val substance = substanceRepo.getSubstance(substanceName)!!
        dangerousInteractions = substance.dangerousInteractions
        unsafeInteractions = substance.unsafeInteractions
        uncertainInteractions = substance.uncertainInteractions
        viewModelScope.launch {
            dangerousInteractions = substanceRepo.getAllInteractions(
                type = InteractionType.DANGEROUS,
                substanceName = substanceName,
                originalInteractions = substance.dangerousInteractions,
                interactionsToFilterOut = emptyList(),
                psychoactiveClassNames = substance.psychoactiveClasses
            )
            unsafeInteractions = substanceRepo.getAllInteractions(
                type = InteractionType.UNSAFE,
                substanceName = substanceName,
                originalInteractions = substance.unsafeInteractions,
                interactionsToFilterOut = dangerousInteractions,
                psychoactiveClassNames = substance.psychoactiveClasses
            )
            uncertainInteractions = substanceRepo.getAllInteractions(
                type = InteractionType.UNCERTAIN,
                substanceName = substanceName,
                originalInteractions = substance.uncertainInteractions,
                interactionsToFilterOut = dangerousInteractions + unsafeInteractions,
                psychoactiveClassNames = substance.psychoactiveClasses
            )
            val twoDaysInMs = 2.toDuration(DurationUnit.DAYS).inWholeMilliseconds
            val twoDaysAgo = Date(System.currentTimeMillis() - twoDaysInMs)
            latestIngestionsOfEverySubstanceSinceTwoDays = experienceRepo.getLatestIngestionOfEverySubstanceSinceDate(twoDaysAgo)
            checkInteractionsAndMaybeShowAlert()
            isSearchingForInteractions = false
        }
    }

    private fun checkInteractionsAndMaybeShowAlert() {
        val dangerousIngestions = getIngestionsWithInteraction(
            interactions = dangerousInteractions
        ).sortedByDescending { it.time }
        val unsafeIngestions = getIngestionsWithInteraction(
            interactions = unsafeInteractions
        )
        val uncertainIngestions = getIngestionsWithInteraction(
            interactions = uncertainInteractions
        )
        alertInteractionType = if (dangerousIngestions.isNotEmpty()) {
            InteractionType.DANGEROUS
        } else if (unsafeIngestions.isNotEmpty()) {
            InteractionType.UNSAFE
        } else if (uncertainIngestions.isNotEmpty()) {
            InteractionType.UNCERTAIN
        } else {
            return
        }
        val now = Date()
        val messages = dangerousIngestions.map { ingestion ->
            "Dangerous Interaction with ${ingestion.substanceName} (taken ${
                getTimeDifferenceText(
                    fromDate = ingestion.time,
                    toDate = now
                )
            } ago)."
        }.toMutableList()
        messages += unsafeIngestions.map { ingestion ->
            "Unsafe Interaction with ${ingestion.substanceName} (taken ${
                getTimeDifferenceText(
                    fromDate = ingestion.time,
                    toDate = now
                )
            } ago)."
        }
        messages += uncertainIngestions.map { ingestion ->
            "Uncertain Interaction with ${ingestion.substanceName} (taken ${
                getTimeDifferenceText(
                    fromDate = ingestion.time,
                    toDate = now
                )
            } ago)."
        }
        alertText = messages.distinct().joinToString(separator = "\n")
        isShowingAlert = true
    }

    private fun getIngestionsWithInteraction(
        interactions: List<String>
    ): List<Ingestion> {
        return latestIngestionsOfEverySubstanceSinceTwoDays.filter { ingestion ->
            val substance = substanceRepo.getSubstance(ingestion.substanceName)
            val isSubstanceInDangerClass =
                substance?.psychoactiveClasses?.any { interactions.contains(it) } ?: false
            val isWildCardMatch = interactions.map { interaction ->
                Regex(
                    pattern = interaction.replace(
                        oldValue = "x",
                        newValue = "[\\S]*",
                        ignoreCase = true
                    ),
                    option = RegexOption.IGNORE_CASE
                ).matches(ingestion.substanceName)
            }.any { it }
            interactions.contains(ingestion.substanceName) || isSubstanceInDangerClass || isWildCardMatch
        }
    }
}