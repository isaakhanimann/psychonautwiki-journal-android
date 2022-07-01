package com.example.healthassistant.ui.search.substance

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.substances.InteractionType
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import com.example.healthassistant.ui.main.routers.SUBSTANCE_NAME_KEY
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
            isSearchingForInteractions = false
        }
    }
}