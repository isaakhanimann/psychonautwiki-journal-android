package com.isaakhanimann.healthassistant.ui.search.substance

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.substances.classes.InteractionType
import com.isaakhanimann.healthassistant.data.substances.classes.Substance
import com.isaakhanimann.healthassistant.data.substances.repositories.SubstanceRepository
import com.isaakhanimann.healthassistant.ui.main.routers.SUBSTANCE_NAME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubstanceViewModel @Inject constructor(
    substanceRepo: SubstanceRepository,
    state: SavedStateHandle
) : ViewModel() {
    val substance: Substance
    var isSearchingForInteractions by mutableStateOf(true)
    var dangerousInteractions: List<String> by mutableStateOf(listOf())
    var unsafeInteractions: List<String> by mutableStateOf(listOf())
    var uncertainInteractions: List<String> by mutableStateOf(listOf())

    init {
        val substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!
        substance = substanceRepo.getSubstance(substanceName)!!
        dangerousInteractions = substance.interactions?.dangerous ?: emptyList()
        unsafeInteractions = substance.interactions?.unsafe ?: emptyList()
        uncertainInteractions = substance.interactions?.uncertain ?: emptyList()
        viewModelScope.launch {
            dangerousInteractions = substanceRepo.getAllInteractions(
                type = InteractionType.DANGEROUS,
                substanceName = substanceName,
                originalInteractions = substance.interactions?.dangerous ?: emptyList(),
                interactionsToFilterOut = emptyList(),
                categories = substance.categories
            )
            unsafeInteractions = substanceRepo.getAllInteractions(
                type = InteractionType.UNSAFE,
                substanceName = substanceName,
                originalInteractions = substance.interactions?.unsafe ?: emptyList(),
                interactionsToFilterOut = dangerousInteractions,
                categories = substance.categories
            )
            uncertainInteractions = substanceRepo.getAllInteractions(
                type = InteractionType.UNCERTAIN,
                substanceName = substanceName,
                originalInteractions = substance.interactions?.uncertain ?: emptyList(),
                interactionsToFilterOut = dangerousInteractions + unsafeInteractions,
                categories = substance.categories
            )
            isSearchingForInteractions = false
        }
    }
}