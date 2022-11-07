package com.isaakhanimann.journal.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CombinationSettingsViewModel @Inject constructor(
    private val comboStorage: CombinationSettingsStorage,
) : ViewModel() {

    val skipFlow: StateFlow<Boolean> = comboStorage.skipFlow.stateIn(
        initialValue = false,
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    fun toggle() {
        viewModelScope.launch {
            comboStorage.toggle()
        }
    }
}