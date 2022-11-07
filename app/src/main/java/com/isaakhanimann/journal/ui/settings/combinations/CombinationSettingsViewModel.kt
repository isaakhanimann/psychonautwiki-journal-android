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
    comboStorage: CombinationSettingsStorage,
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