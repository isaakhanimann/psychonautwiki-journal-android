/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.addingestion.interactions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.data.substances.classes.InteractionType
import com.isaakhanimann.journal.data.substances.repositories.SubstanceRepository
import com.isaakhanimann.journal.ui.main.navigation.routers.SUBSTANCE_NAME_KEY
import com.isaakhanimann.journal.ui.settings.combinations.CombinationSettingsStorage
import com.isaakhanimann.journal.ui.utils.getTimeDifferenceText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class CheckInteractionsViewModel @Inject constructor(
    private val substanceRepo: SubstanceRepository,
    private val experienceRepo: ExperienceRepository,
    private val combinationSettingsStorage: CombinationSettingsStorage,
    state: SavedStateHandle,
) : ViewModel() {
    val substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!
    val substance = substanceRepo.getSubstance(substanceName)!!
    val dangerousInteractions = substance.interactions?.dangerous ?: emptyList()
    val unsafeInteractions = substance.interactions?.unsafe ?: emptyList()
    val uncertainInteractions = substance.interactions?.uncertain ?: emptyList()

    var isSearchingForInteractions by mutableStateOf(true)
    var isShowingAlert by mutableStateOf(false)
    var alertInteractionType by mutableStateOf<InteractionType?>(null)
    var alertText by mutableStateOf("")
    private var latestIngestionsOfEverySubstanceSinceTwoDays: List<Ingestion> = emptyList()

    init {
        viewModelScope.launch {
            val foundDangerous = substanceRepo.getAllInteractions(
                type = InteractionType.DANGEROUS,
                substanceName = substanceName,
                originalInteractions = dangerousInteractions,
                interactionsToFilterOut = emptyList(),
                categories = substance.categories
            )
            val foundUnsafe = substanceRepo.getAllInteractions(
                type = InteractionType.UNSAFE,
                substanceName = substanceName,
                originalInteractions = unsafeInteractions,
                interactionsToFilterOut = dangerousInteractions,
                categories = substance.categories
            )
            val foundUncertain = substanceRepo.getAllInteractions(
                type = InteractionType.UNCERTAIN,
                substanceName = substanceName,
                originalInteractions = uncertainInteractions,
                interactionsToFilterOut = dangerousInteractions + unsafeInteractions,
                categories = substance.categories
            )
            val twoDaysAgo = Instant.now().minus(2, ChronoUnit.DAYS)
            latestIngestionsOfEverySubstanceSinceTwoDays =
                experienceRepo.getLatestIngestionOfEverySubstanceSinceDate(twoDaysAgo)
            checkInteractionsAndMaybeShowAlert(
                dangerous = foundDangerous,
                unsafe = foundUnsafe,
                uncertain = foundUncertain
            )
            isSearchingForInteractions = false
        }
    }

    private suspend fun checkInteractionsAndMaybeShowAlert(
        dangerous: List<String>,
        unsafe: List<String>,
        uncertain: List<String>,
    ) {
        val dangerousIngestions = getIngestionsWithInteraction(
            interactions = dangerous
        ).sortedByDescending { it.time }
        val unsafeIngestions = getIngestionsWithInteraction(
            interactions = unsafe
        )
        val uncertainIngestions = getIngestionsWithInteraction(
            interactions = uncertain
        )
        val enabledExtraInteractions =
            combinationSettingsStorage.substanceInteractors.filter { it.flow.first() }
                .map { it.name }
        val dangerousExtras = enabledExtraInteractions.filter { dangerous.contains(it) }
        val unsafeExtras = enabledExtraInteractions.filter { unsafe.contains(it) }
        val uncertainExtras = enabledExtraInteractions.filter { uncertain.contains(it) }
        alertInteractionType =
            if (dangerousIngestions.isNotEmpty() || dangerousExtras.isNotEmpty()) {
                InteractionType.DANGEROUS
            } else if (unsafeIngestions.isNotEmpty() || unsafeExtras.isNotEmpty()) {
                InteractionType.UNSAFE
            } else if (uncertainIngestions.isNotEmpty() || uncertainExtras.isNotEmpty()) {
                InteractionType.UNCERTAIN
            } else {
                return
            }
        val now = Instant.now()
        val messages = dangerousIngestions.map { ingestion ->
            "Dangerous Interaction with ${ingestion.substanceName} (taken ${
                getTimeDifferenceText(
                    fromInstant = ingestion.time,
                    toInstant = now
                )
            } ago)."
        }.toMutableList()
        messages += dangerousExtras.map { extra ->
            "Dangerous Interaction with $extra."
        }
        messages += unsafeIngestions.map { ingestion ->
            "Unsafe Interaction with ${ingestion.substanceName} (taken ${
                getTimeDifferenceText(
                    fromInstant = ingestion.time,
                    toInstant = now
                )
            } ago)."
        }
        messages += unsafeExtras.map { extra ->
            "Unsafe Interaction with $extra."
        }
        messages += uncertainIngestions.map { ingestion ->
            "Uncertain Interaction with ${ingestion.substanceName} (taken ${
                getTimeDifferenceText(
                    fromInstant = ingestion.time,
                    toInstant = now
                )
            } ago)."
        }
        messages += uncertainExtras.map { extra ->
            "Uncertain Interaction with $extra."
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
                substance?.categories?.any { categoryName ->
                    interactions.any { interactionName ->
                        interactionName.contains(categoryName, ignoreCase = true)
                    }
                } ?: false
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
            return@filter interactions.contains(ingestion.substanceName) || isSubstanceInDangerClass || isWildCardMatch
        }
    }
}