package com.isaakhanimann.journal.ui.settings.combinations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CombinationSettingsViewModel @Inject constructor(
    private val comboStorage: CombinationSettingsStorage,
) : ViewModel() {

    val skipViewModelInteraction = SubstanceViewModelInteraction(
        scope = viewModelScope,
        booleanInteraction = comboStorage.skipInteractor
    )

    val substanceInteraction = comboStorage.substanceInteractors.map {
        SubstanceViewModelInteraction(
            scope = viewModelScope,
            booleanInteraction = it
        )
    }
}

class SubstanceViewModelInteraction(
    private val scope: CoroutineScope,
    private val booleanInteraction: BooleanInteraction
) {
    val name = booleanInteraction.name
    val stateFlow = booleanInteraction.flow.stateIn(
        initialValue = false,
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    fun toggle() {
        scope.launch {
            booleanInteraction.toggle()
        }
    }
}