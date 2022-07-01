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
    var alertTitle by mutableStateOf("")
    var alertText by mutableStateOf("")

    init {
        substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!
        val substance = substanceRepo.getSubstance(substanceName)!!
        dangerousInteractions = substance.dangerousInteractions
        unsafeInteractions = substance.unsafeInteractions
        uncertainInteractions = substance.uncertainInteractions
        viewModelScope.launch {
            val foundDangerousInteractions = substanceRepo.getAllInteractions(
                type = InteractionType.DANGEROUS,
                substanceName = substanceName,
                originalInteractions = substance.dangerousInteractions,
                interactionsToFilterOut = emptyList(),
                psychoactiveClassNames = substance.psychoactiveClasses
            )
            val foundUnsafeInteractions = substanceRepo.getAllInteractions(
                type = InteractionType.UNSAFE,
                substanceName = substanceName,
                originalInteractions = substance.unsafeInteractions,
                interactionsToFilterOut = dangerousInteractions,
                psychoactiveClassNames = substance.psychoactiveClasses
            )
            val foundUncertainInteractions = substanceRepo.getAllInteractions(
                type = InteractionType.UNCERTAIN,
                substanceName = substanceName,
                originalInteractions = substance.uncertainInteractions,
                interactionsToFilterOut = dangerousInteractions + unsafeInteractions,
                psychoactiveClassNames = substance.psychoactiveClasses
            )
            val twoDaysInMs = 2.toDuration(DurationUnit.DAYS).inWholeMilliseconds
            val twoDaysAgo = Date(System.currentTimeMillis() - twoDaysInMs)
            val latestIngestions = experienceRepo.getIngestionAfterDate(twoDaysAgo)
            val alertContent = checkInteractionsAndMaybeShowAlert(
                latestIngestions = latestIngestions,
                foundDangerousInteractions = foundDangerousInteractions,
                foundUnsafeInteractions = foundUnsafeInteractions,
                foundUncertainInteractions = foundUncertainInteractions
            )
            if (alertContent!=null) {
                alertTitle = alertContent.title
                alertText = alertContent.text
                isShowingAlert = true
            }
            dangerousInteractions = foundDangerousInteractions
            unsafeInteractions = foundUnsafeInteractions
            uncertainInteractions = foundUncertainInteractions
            isSearchingForInteractions = false
        }
    }

    private data class AlertContent(
        val title: String,
        val text: String
    )

    private fun checkInteractionsAndMaybeShowAlert(
        latestIngestions: List<Ingestion>,
        foundDangerousInteractions: List<String>,
        foundUnsafeInteractions: List<String>,
        foundUncertainInteractions: List<String>,
    ): AlertContent? {
        val dangerousIngestions = getIngestionsWithInteraction(
            interactions = foundDangerousInteractions,
            ingestions = latestIngestions
        )
        val unsafeIngestions = getIngestionsWithInteraction(
            interactions = foundUnsafeInteractions,
            ingestions = latestIngestions
        )
        val uncertainIngestions = getIngestionsWithInteraction(
            interactions = foundUncertainInteractions,
            ingestions = latestIngestions
        )
        val alertTitle = if (dangerousIngestions.isNotEmpty()) {
            "Dangerous Interaction Detected!"
        } else if (unsafeIngestions.isNotEmpty()) {
            "Unsafe Interaction Detected!"
        } else if (uncertainIngestions.isNotEmpty()) {
            "Uncertain Interaction Detected"
        } else {
            return null
        }
        val now = Date()
        var alertText = dangerousIngestions.joinToString(separator = "\n") { ingestion ->
            "Dangerous Interaction with ${ingestion.substanceName} (taken ${
                getTimeDifferenceText(
                    fromDate = ingestion.time,
                    toDate = now
                )
            } ago)."
        }
        alertText += unsafeIngestions.joinToString(separator = "\n") { ingestion ->
            "Unsafe Interaction with ${ingestion.substanceName} (taken ${
                getTimeDifferenceText(
                    fromDate = ingestion.time,
                    toDate = now
                )
            } ago)."
        }
        alertText += uncertainIngestions.joinToString(separator = "\n") { ingestion ->
            "Uncertain Interaction with ${ingestion.substanceName} (taken ${
                getTimeDifferenceText(
                    fromDate = ingestion.time,
                    toDate = now
                )
            } ago)."
        }
        return AlertContent(title = alertTitle, text = alertText)
    }

    private fun getIngestionsWithInteraction(
        interactions: List<String>,
        ingestions: List<Ingestion>
    ): List<Ingestion> {
        return ingestions.filter { ingestion ->
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