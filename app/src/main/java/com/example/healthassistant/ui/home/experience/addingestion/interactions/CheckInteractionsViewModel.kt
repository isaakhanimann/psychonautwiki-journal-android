package com.example.healthassistant.ui.home.experience.addingestion.interactions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.experiences.ExperienceRepository
import com.example.healthassistant.data.experiences.entities.Ingestion
import com.example.healthassistant.data.substances.InteractionType
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import com.example.healthassistant.ui.main.routers.SUBSTANCE_NAME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@HiltViewModel
class CheckInteractionsViewModel @Inject constructor(
    private val substanceRepo: SubstanceRepository,
    experienceRepo: ExperienceRepository,
    state: SavedStateHandle,
): ViewModel() {
    val substanceName: String
    var isSearchingForInteractions by mutableStateOf(true)
    var dangerousInteractions: List<String> by mutableStateOf(listOf())
    var unsafeInteractions: List<String> by mutableStateOf(listOf())
    var uncertainInteractions: List<String> by mutableStateOf(listOf())
    var latestIngestions: List<Ingestion> by mutableStateOf(listOf())
    val dangerousIngestions: List<Ingestion> get() = getIngestionsWithInteraction(interactionType = InteractionType.DANGEROUS)
    val unsafeIngestions: List<Ingestion> get() = getIngestionsWithInteraction(interactionType = InteractionType.UNSAFE)
    val uncertainIngestions: List<Ingestion> get() = getIngestionsWithInteraction(interactionType = InteractionType.UNCERTAIN)
    var isShowingAlert by mutableStateOf(false)
    var alertTitle by mutableStateOf("")
    var alertText by mutableStateOf("")

    init {
        substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!
        val substance = substanceRepo.getSubstance(substanceName)!!
        dangerousInteractions = substance.dangerousInteractions
        unsafeInteractions = substance.unsafeInteractions
        uncertainInteractions = substance.uncertainInteractions
        viewModelScope.launch(context = Dispatchers.Default) {
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
            isSearchingForInteractions = false
            val twoDaysInMs = 2.toDuration(DurationUnit.DAYS).inWholeMilliseconds
            val twoDaysAgo = Date(System.currentTimeMillis() - twoDaysInMs)
            latestIngestions = experienceRepo.getIngestionAfterDate(twoDaysAgo)
            maybeShowAlert()
        }
    }

    fun maybeShowAlert() {
        alertTitle = if (dangerousIngestions.isNotEmpty()) {
            "Dangerous Interaction Detected!"
        } else if (unsafeIngestions.isNotEmpty()) {
            "Unsafe Interaction Detected!"
        } else if (uncertainIngestions.isNotEmpty()) {
            "Uncertain Interaction Detected"
        } else {
            return
        }
        alertText = dangerousIngestions.map { ingestion ->
            val diffInMs: Long = Date().time - ingestion.time.time
            val howLongAgo = diffInMs.toDuration(DurationUnit.MILLISECONDS)
            "Dangerous Interaction with ${ingestion.substanceName} taken $howLongAgo ago"
        }.joinToString(separator = "\n")
        alertText += unsafeIngestions.map { ingestion ->
            val diffInMs: Long = Date().time - ingestion.time.time
            val howLongAgo = diffInMs.toDuration(DurationUnit.MILLISECONDS)
            "Unsafe Interaction with ${ingestion.substanceName} taken $howLongAgo ago"
        }.joinToString(separator = "\n")
        alertText += uncertainIngestions.map { ingestion ->
            val diffInMs: Long = Date().time - ingestion.time.time
            val howLongAgo = diffInMs.toDuration(DurationUnit.MILLISECONDS)
            "Uncertain Interaction with ${ingestion.substanceName} taken $howLongAgo ago"
        }.joinToString(separator = "\n")
        isShowingAlert = true
    }

    private fun getIngestionsWithInteraction(interactionType: InteractionType): List<Ingestion> {
        val interactions = when (interactionType) {
            InteractionType.DANGEROUS -> dangerousInteractions
            InteractionType.UNSAFE -> unsafeInteractions
            InteractionType.UNCERTAIN -> uncertainInteractions
        }
        return latestIngestions.filter { ingestion ->
            val substance = substanceRepo.getSubstance(ingestion.substanceName)
            val isSubstanceInDangerClass = substance?.psychoactiveClasses?.any { interactions.contains(it) } ?: false
            interactions.contains(ingestion.substanceName) || isSubstanceInDangerClass
        }
    }
}