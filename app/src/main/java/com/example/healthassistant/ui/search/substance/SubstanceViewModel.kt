package com.example.healthassistant.ui.search.substance

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
//    val unsafeInteractions: List<String>
//    val uncertainInteractions: List<String>

    init {
        val substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!
        substance = substanceRepo.getSubstance(substanceName)!!
        dangerousInteractions = substance.dangerousInteractions
        viewModelScope.launch {
            val other = substanceRepo.getOtherDangerousInteractions(
                substanceName = substanceName,
                interactionsToFilterOut = substance.dangerousInteractions,
                psychoactiveClassNames = substance.psychoactiveClasses
            )
            dangerousInteractions = substance.dangerousInteractions + other
            isSearchingForInteractions = false
        }

    }
}