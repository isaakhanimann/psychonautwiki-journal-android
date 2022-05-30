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
    var dangerousInteractions: List<String>
    var unsafeInteractions: List<String>
    var uncertainInteractions: List<String>

    init {
        val substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!
        substance = substanceRepo.getSubstance(substanceName)!!
        dangerousInteractions = substance.dangerousInteractions
        unsafeInteractions = substance.unsafeInteractions
        uncertainInteractions = substance.uncertainInteractions
        viewModelScope.launch {
            val otherDanger = substanceRepo.getOtherInteractions(
                type = InteractionType.DANGEROUS,
                substanceName = substanceName,
                interactionsToFilterOut = substance.dangerousInteractions,
                psychoactiveClassNames = substance.psychoactiveClasses
            )
            dangerousInteractions = substance.dangerousInteractions + otherDanger
            val otherUnsafe = substanceRepo.getOtherInteractions(
                type = InteractionType.UNSAFE,
                substanceName = substanceName,
                interactionsToFilterOut = dangerousInteractions + substance.unsafeInteractions,
                psychoactiveClassNames = substance.psychoactiveClasses
            )
            unsafeInteractions = substance.unsafeInteractions + otherUnsafe
            val otherUncertain = substanceRepo.getOtherInteractions(
                type = InteractionType.UNCERTAIN,
                substanceName = substanceName,
                interactionsToFilterOut = dangerousInteractions + unsafeInteractions + substance.uncertainInteractions,
                psychoactiveClassNames = substance.psychoactiveClasses
            )
            uncertainInteractions = substance.uncertainInteractions + otherUncertain
            isSearchingForInteractions = false
        }

    }
}